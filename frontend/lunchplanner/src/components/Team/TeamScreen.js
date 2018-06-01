import PropTypes from "prop-types";
import {withStyles} from "@material-ui/core/styles/index";
import React from 'react';
import Dialog from "../Dialog";
import {Button, Slide, Divider} from "@material-ui/core";
import {getUsername, setAuthenticationHeader} from "../authentication/Authentication";
import {getHistory} from "../../utils/HistoryUtils"
import {Https as SecretIcon} from "@material-ui/icons";
import UserList from "../User/UserList";
import {getTeam, replyToTeam, changeTeamDescription,changeTeamName} from "./TeamFunctions";
import {teamListNeedReload} from "./TeamList";
import TextFieldEditing from "../editing/TextFieldEditing";
import axios from "axios";
import {HOST} from "../../Config";

import {Link} from "react-router-dom";
import {Add} from "@material-ui/icons";
import {inviteMemberToTeam} from "./TeamFunctions";


function Transition(props) {
    return <Slide direction="up" {...props} />;
}

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
    textField: {
        marginTop:20,
        marginBottom:30,
        marginLeft: 20,
        width: "90%",
    },
    error: {
        textAlign: 'center',
        color: '#ff7700',
        marginTop: '10px',
        marginBottom: '0px',
    },
    header: {
        backgroundColor: '#1EA185',
        height: '100px',
        color: 'white',
        padding: '16px',
        fontFamily: 'Work Sans',
        fontSize: '16px',
        lineHeight: '24px',
    },
    button:{
        fontSize: '16px',
        fontFamily: 'Work Sans',
        color: "white",
        position: "fixed",
        bottom: 0,
        width: "100%",
        height: '56px',
        zIndex: '10000',
    },
    buttonInvitation: {
        position: "fixed",
        zIndex: '10000',
    },
    fontBig: {
        fontSize: '20px',
        margin: '0px',
    },
    fontSmall: {
        fontSize: '11px',
        margin: '0px',
    },
    icons: {
        height: '13px',
        width: '13px',
    },
    headerText: {
        float: 'left',
    },
    picture:{
        float: 'left',
        border: '1px black solid',
        borderRadius: '50%',
        height: '64px',
        width: '64px',
    },
    content: {
        width: '100%',
    },
    information:{
        height: '160px',
        width: '280px',
        marginLeft: '24px',
        marginTop: '24px',
    },
    teamName: {
        marginLeft: '100px',
    },
    description: {
        paddingTop: '10px',
        marginTop: '15px',
        fontSize: '16px',
        width: '300px',
    },
    secretTeam:{
        marginTop: '20px',
        marginLeft: '20px',
        color: '#1EA185',
    },
    secretTeamText:{
        marginLeft: '40px',
        marginTop: '-30px',
        width: '50%',
    },
    divider:{
        width: '100%',
    },
    invitations: {
        marginLeft: '0px',
        marginTop: '8px',
    },
    invitaionsHeader: {
        marginLeft: '16px',
        marginBottom: '0px',
        fontSize: '16px',
        fontWeight: '500',
        lineHeight: '24px',
    },
    overButton: {
        height: '100%',
        marginBottom: '56px',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
    },
    // ADD NEW PEOPLE
    addNewPeopleRoot: {
        height: '72px',
        padding: '20px 16px',
        backgroundColor: '#f3f3f3',
        "&:hover": {
            cursor: 'pointer',
        },
    },
    newPeopleIcon: {
        height: '32px',
        float: 'left',
    },
    newPeopleText: {
        marginTop: '6px',
        marginLeft: '57px',
    },
};

class TeamScreen extends React.Component {

    constructor(props) {
        super();
        setAuthenticationHeader();
        this.state = {
            teamId: 0,
            open: true,
            name:"",
            description: "",
            people:[],
        };

    }

    componentDidMount() {
        let teamId, teamName, description, people;

        teamId = this.props.match.params.teamId;

        if(this.props.location.query) {
            console.log("Query exists");
            if (this.props.location.query.teamName) {
                teamName = String(this.props.location.query.teamName);
            }
            if (this.props.location.query.description) {
                description = String(this.props.location.query.description);
            }
            if (this.props.location.query.people) {
                people = this.props.location.query.people;
            }


            this.setState({
                teamId: teamId,
                name: teamName,
                description: description,
                people: people,
            })

        } else {
            console.log("Query does not exists");
            this.loadTeam(teamId);
        }
    }

    parseUrl = () => {
        const params = new URLSearchParams(this.props.location.search);
        let invitedUsers = params.get('invitedUsers');
        let invitedTeams = params.get('invitedTeams');
        let teamMember = params.get('teamMember');

        let usersToInvite = [];

        if(invitedUsers) {
            usersToInvite = usersToInvite.concat(invitedUsers.split(','));
        }
        if(teamMember) {
            usersToInvite = usersToInvite.concat(teamMember.split(','));
        }

        if(usersToInvite.length !== 0) {
            //remove doubles and already invited people
            let usersToInviteUnique = usersToInvite.filter((item, pos) => {
                return usersToInvite.indexOf(item) === pos && !this.state.people.some((person) => person.userName === item);
            });

            inviteMemberToTeam(this.state.teamId, usersToInviteUnique, (user) => {
                let allUsers = this.state.people;
                allUsers.push({userName: user, admin: false});
                this.setState({
                    people: allUsers,
                });
            })
        }
    };

    loadTeam = (teamId) => {
        if(!teamId)
            teamId = this.state.teamId;

        getTeam(teamId, (response) => {
            this.setState({
                teamId: response.data.teamId,
                name: response.data.teamName,
                description: response.data.description,
                people: response.data.invitations,

            });
        })
    };

    handleLeave = () => {
        getHistory().push("/social?tab=1");
        let people = this.state.people;
        let index = people.indexOf(getUsername());
        people.splice(index, 1);
        this.setState({
           people: people,
        });
        this.sendAnswer();
    };

    sendAnswer = () => {
        let url = HOST + '/team/' + this.state.teamId + '/leave';
        axios.delete(url)
            .then(() => {
                teamListNeedReload();
            })
    };

    onTitleChanged = (event) => {
        this.setState({
            name: event.target.value,
        });

        //TODO error func
        changeTeamName(this.state.teamId, event.target.value, this.reloadTeamsOnSuccess);
    };

    onDescriptionChanged = (event) => {
        this.setState({
            description: event.target.value,
        });

        //TODO error func
        changeTeamDescription(this.state.teamId, event.target.value, this.reloadTeamsOnSuccess);
    };

    reloadTeamsOnSuccess = (response) => {
        if(response.status === 204) {
            teamListNeedReload();
        }
    };

    render() {
        const { classes } = this.props;
        const error = this.state.error;
        let name = this.state.name;
        let description = this.state.description;
        let people = this.state.people;
        let iAmAdmin = false;
        let userName = getUsername();

        if(people.length !== 0) {
            this.parseUrl();
        }

        people.sort((a, b) => {
            if(a.answer === 0 && b.answer !== 0) {
                return -1;
            } else if(a.answer !== 0 && b.answer === 0) {
                return 1;
            } else {
                return 0;
            }
        });

        people.forEach((listValue) => {
            if(listValue.userName === userName) {
                if(listValue.admin) {
                    iAmAdmin = true;
                }
            }
        });

        let selectedUsers = [];
        let buttonText = "Join Team";
        let username = getUsername();

        people.forEach((listValue) => {
            if(listValue.userName === username) {
                buttonText = "Leave Team";
            }
        });

        return (
            <div>
                <Dialog
                    title={name}
                    closeUrl="/social?tab=1"
                >
                    <div className={classes.overButton}>
                        <div className={classes.content}>
                            <div className={classes.information}>
                                <div className={classes.picture}/>
                                <div className={classes.teamName}>
                                    <p className={classes.fontSmall}>Team Name</p>
                                    <TextFieldEditing onChange={this.onTitleChanged} value={name} editable={iAmAdmin} className={classes.fontBig} />
                                </div>
                                <div className={classes.description}>
                                    <p className={classes.fontSmall}>Description</p>
                                    <TextFieldEditing rowsMax="3" onChange={this.onDescriptionChanged} value={description} editable={iAmAdmin} className={classes.description}  multiline/>
                                </div>
                            </div>
                            <div className={classes.secretTeam}>
                                <SecretIcon/>
                                <p className={classes.secretTeamText}>Secret team. Only you can see the activity of this team.</p>
                            </div>
                            <Divider className={classes.divider} />

                            <div className={classes.invitations}>
                                <p className={classes.invitaionsHeader}> Team Member ({people.length})</p>
                                {
                                    (iAmAdmin)
                                        ? <Link to={{pathname: "/team/create/invite",  query: {
                                                source: "/team/" + this.state.teamId,
                                                invitedUsers: people.map((value) => value.userName).join(','),
                                            }}}>
                                            <div className={classes.addNewPeopleRoot}>
                                                <Add className={classes.newPeopleIcon} />
                                                <p className={classes.newPeopleText}>Add more people...</p>
                                            </div>
                                        </Link>
                                        : ''
                                }
                                <UserList
                                    selectedUsers={selectedUsers}
                                    othersInvited={true}
                                    selectable={false}
                                    users={people}
                                />
                            </div>
                        </div>
                    </div>
                    <Button variant="raised"
                    color="secondary"
                    onClick={this.handleLeave}
                    className={classes.button}>
                    {buttonText}
                </Button>
                </Dialog>
            </div>
        );
    }
}
TeamScreen.propTypes = {
    classes: PropTypes.object.isRequired,
    id: PropTypes.number.isRequired,
    isAdmin: PropTypes.bool.isRequired,
    name:PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    people:PropTypes.object.isRequired,

};
export default withStyles(styles, { withTheme: true })(TeamScreen);