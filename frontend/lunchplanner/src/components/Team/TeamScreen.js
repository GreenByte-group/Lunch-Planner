import PropTypes from "prop-types";
import Loadable from "react-loading-overlay";
import {withStyles} from "@material-ui/core/styles/index";
import React from 'react';
import Dialog from "../Dialog";
import {IconButton, Button, Slide, Divider, CircularProgress, Modal} from "@material-ui/core";
import {getUsername, setAuthenticationHeader} from "../authentication/LoginFunctions";
import {getHistory} from "../../utils/HistoryUtils"
import {Https as SecretIcon} from "@material-ui/icons";
import {Public as PublicIcon} from "@material-ui/icons";
import {AddCircleOutlined as AddIcon} from "@material-ui/icons";

import UserList from "../User/UserList";
import {
    getTeam,
    changeTeamDescription,
    changeTeamName,
    removeUserFromTeam,
    inviteMemberToTeam,
    joinTeam,
    leaveTeam, changePicture
} from "./TeamFunctions";
import {teamListNeedReload} from "./TeamList";
import TextFieldEditing from "../editing/TextFieldEditing";
import axios from "axios";
import {HOST} from "../../Config";
import {Link} from "react-router-dom";
import TeamPicsGrid from "./TeamPicsGrid";


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
        bottom: 0,
        width: "100%",
        minHeight: '56px',
        zIndex: '10000',
    },
    buttonInvitation: {
        position: "fixed",
        zIndex: '10000',
    },
    fontBig: {
        fontSize: '20px',
        fontWeight: '900',
        margin: '0px',
        width: '100%',
        backgroundColor: 'rgba(0,0,0,0.4)',
        color: 'white',
        textShadowOffset: { width: '5', height: '5' },
        textShadowRadius: '3',
        textShadowColor: 'white',
    },
    fontSmall: {
        fontSize: '11px',
        fontWeight: '900',
        margin: '0px',
        color: '#ff7700',
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
        width: 'auto',
        marginLeft: '24px',
        marginRight: '24px',
        marginTop: '24px',
    },
    image: {
        height: '160px',
        width: 'auto',
        float: 'right',
        },
    teamName: {
        width: '100%',
    },
    description: {
        paddingTop: '10px',
        marginTop: '15px',
        fontSize: '16px',
        fontWeight: '900',
        width: '100%',
    },
    descriptionText: {
        fontSize: '20px',
        fontWeight: '900',
        margin: '0px',
        width: '100%',
        backgroundColor: 'rgba(0,0,0,0.4)',
        color: 'white',
        textShadowOffset: { width: '5', height: '5' },
        textShadowRadius: '3',
        textShadowColor: 'black',
        backgroundColor: 'rgba(0,0,0,0.4)',
        textShadowOffset: { width: '5', height: '5' },
        textShadowRadius: '3',
        textShadowColor: 'white',
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
    plusIconContainer: {
        float: 'right',
    },
    plusIcon: {
        color: '#ff7700',

    },
    modal:{
        top: '25%',
        left: '10%',
        position: 'absolute',
        width: '80%',
        backgroundColor: 'white',
        boxShadow: '5px',
        padding: '20px',
    }
};
let backgroundImage = "";



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
            loading: true,
            isPublic: false,
            openModal: false,
        };

    }

     rand=()=> {
        return Math.round(Math.random() * 20) - 10;
    }

     getModalStyle=()=> {
        const top = 50 + this.rand();
        const left = 50 + this.rand();

        return {
            top: `${top}%`,
            left: `${left}%`,
            transform: `translate(-${top}%, -${left}%)`,
        };
    };

    componentDidMount() {
        let teamId, teamName, description, people, isPublic, picture;

        teamId = this.props.match.params.teamId;
        console.log('datas in frontend',teamId);
        console.log('datas in frontend',teamName);
        console.log('datas in frontend',description);
        console.log('datas in frontend',isPublic);
        console.log('datas in frontend',people);
        console.log('datas in frontend',picture);

        if(this.props.location.query) {

            if (this.props.location.query.teamName) {
                teamName = String(this.props.location.query.teamName);
            }
            if (this.props.location.query.description) {
                description = String(this.props.location.query.description);
            }
            if (this.props.location.query.people) {
                people = this.props.location.query.people;
            }
            if (this.props.location.query.public) {
                isPublic = this.props.location.query.public;
            }
            if (this.props.location.query.picture){
                picture = String(this.props.location.query.picture);
            }

            this.setState({
                teamId: teamId,
                name: teamName,
                description: description,
                people: people,
                loading: false,
                public: isPublic,
                picture: picture,
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
        this.setState({
            loading: true,
        });
        if(!teamId)
            teamId = this.state.teamId;

        getTeam(teamId, (response) => {
            this.setState({
                teamId: response.data.teamId,
                name: response.data.teamName,
                description: response.data.description,
                people: response.data.invitations,
                public: response.data.public,
                loading: false,
                isPublic: response.data.public,
                picture: response.data.picture,
            });
        })
        this.backgroundImage = this.state.picture;
        console.log('pic', this.backgroundImage);

    };

    handleLeave = () => {
        getHistory().push("/app/team");
        let people = this.state.people;
        let index = people.indexOf(getUsername());
        people.splice(index, 1);
        this.setState({
           people: people,
        });
        this.sendAnswer("leave");
    };

    handleJoin = () => {

        getHistory().push("/app/team");
        let people = this.state.people;
        people.push(getUsername());
        this.setState({
            people: people,
        });
        this.sendAnswer("join");
    };

    sendAnswer = (answer) => {
        console.log(answer)
        if(answer === "leave"){
            leaveTeam(this.state.teamId, () => {
                teamListNeedReload();
            })
        }else if(answer === "join"){
            console.log(answer)
            joinTeam(this.state.teamId, () => {
                this.loadTeam(this.state.teamId);
                teamListNeedReload();
            })
        }
    };

    onTitleChanged = (event) => {
        this.setState({
            name: event.target.value,
        });

        changeTeamName(this.state.teamId, event.target.value, this.reloadTeamsOnSuccess);
    };

    onDescriptionChanged = (event) => {
        this.setState({
            description: event.target.value,
        });

        changeTeamDescription(this.state.teamId, event.target.value, this.reloadTeamsOnSuccess);
    };

    reloadTeamsOnSuccess = (response) => {

        if(response.status === 204 || response.status === 201) {
            teamListNeedReload();
        }
    };

    clickRemove = (username) => {
        let people = this.state.people;
        people = people.filter(listValue => listValue.userName !== username)
        this.setState({
            people: people,
        });

        removeUserFromTeam(this.state.teamId, username, this.reloadTeamsOnSuccess)
    };


    openGrid = () => {
        console.log('openGrid => teamScreen');

        this.setState({
            openModal: true,
        });
    };

    closeGrid = (event) => {
        console.log('closeGrid => teamScreen', event);

        this.setState({
            openModal: false,
        });
        console.log('data for picturechange', this.state.teamId, this.state.picture);
        changePicture(this.state.teamId, this.state.picture);
        getHistory().goBack();
        window.location.reload();

    };



    handleTeamPicGrid = (event) => {
        console.log('handleTeamPicGrid => teamScreen => event', event);
        this.setState({
            picture: event,
        });
    };


    render() {
        const { classes } = this.props;

        let a = this.state.openModal;
        console.log('states => closePicChange => teamScreen', a);
        const error = this.state.error;

        let name = this.state.name;
        let description = this.state.description;
        let people = this.state.people;
        let iAmAdmin = false;
        let userName = getUsername();
        let loading = this.state.loading;
        let isPublic = this.state.isPublic;
        let picture =  String(this.state.picture);


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
        let isInTeam = false;

        people.forEach((listValue) => {
            if(listValue.userName === username) {
                buttonText = "Leave Team";
                isInTeam = true;
            }
        });

        let clickRemove;
        if(iAmAdmin)
            clickRemove = this.clickRemove;

        console.log('picChange', this.state.picChange);
        return (
            <div >
                {loading ?
                        <CircularProgress className={classes.progress} color="secondary"/>
                    :
                <Dialog
                    title={name}
                    closeUrl="/app/team"
                >
                    <div className={classes.overButton}>
                        <div className={classes.content}>
                            <div className={classes.information} style={ {backgroundImage: `url(${picture})` ,
                                backgroundSize: 'cover',
                                backgroundPosition: 'center',

                            } }>
                                <div className={classes.teamName}>
                                    <p className={classes.fontSmall}>Team Name</p>
                                    <TextFieldEditing onChange={this.onTitleChanged} value={name} editable={iAmAdmin} className={classes.fontBig} />
                                </div>
                                <div className={classes.description}>
                                    <p className={classes.fontSmall}>Description</p>
                                    <TextFieldEditing rowsMax="3" onChange={this.onDescriptionChanged} value={description} editable={iAmAdmin} className={classes.descriptionText}  multiline/>
                                </div>
                                <div className={classes.plusIconContainer}>
                                        <IconButton>
                                            <AddIcon className={classes.plusIcon} onClick={this.openGrid}/>
                                            {(this.state.openModal)
                                                ?
                                                <Modal open={this.state.openModal}>
                                                    <div className={classes.modal}>
                                                        <TeamPicsGrid picChange={true} onChange={this.handleTeamPicGrid} handleClose={this.closeGrid}/>
                                                    </div>
                                                </Modal>


                                                : console.log('give me the juice', this.state)
                                            }
                                        </IconButton>
                                </div>
                            </div>
                            <div className={classes.secretTeam}>
                                {isPublic ?
                                    <div>
                                        <PublicIcon/>
                                        <p className={classes.secretTeamText}>Public team. All people can see the activity of this team.</p>
                                    </div>
                                :
                                    <div>
                                        <SecretIcon/>
                                        <p className={classes.secretTeamText}>Secret team. Only you can see the activity of this team.</p>
                                    </div>
                                }
                            </div>
                            <Divider className={classes.divider} />
                            <div className={classes.invitations}>
                                <p className={classes.invitaionsHeader}> Team Member ({people.length})</p>
                                {
                                    (iAmAdmin)
                                        ? <Link to={{pathname: `/app/team/${this.state.teamId}/invite`,  query: {
                                                source: "/app/team/" + this.state.teamId,
                                                invitedUsers: people.map((value) => value.userName).join(','),
                                            }}}>
                                            <div className={classes.addNewPeopleRoot}>
                                                <AddIcon className={classes.newPeopleIcon} />
                                                <p className={classes.newPeopleText}>Add more people...</p>
                                            </div>
                                        </Link>
                                        : ''
                                }
                                <UserList
                                    selectedUsers={people.map(value => value.userName)}
                                    othersInvited={false}
                                    selectable={false}
                                    users={people}
                                    clickRemove={clickRemove}
                                />
                            </div>
                        </div>
                    </div>
                    {isInTeam?
                        <Button variant="raised"
                                        color="secondary"
                                        onClick={this.handleLeave}
                                        className={classes.button}>
                        {buttonText}
                    </Button>
                    :
                        <Button variant="raised"
                                color="secondary"
                                onClick={this.handleJoin}
                                className={classes.button}>
                            {buttonText}
                        </Button>
                    }
                </Dialog>}
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