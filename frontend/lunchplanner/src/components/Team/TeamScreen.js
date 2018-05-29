import PropTypes from "prop-types";
import {withStyles} from "material-ui/styles/index";
import React from 'react';
import Slide from 'material-ui/transitions/Slide';
import {HOST} from "../../Config";
import axios from "axios/index";
import Dialog from "../Dialog";
import {Button} from "material-ui";
import {getUsername, setAuthenticationHeader} from "../authentication/Authentication";
import InvitationButton from "../Event/InvitationButton";
import {teamListNeedReload} from "./TeamList";
import {getHistory} from "../../utils/HistoryUtils"
import SecretIcon from "@material-ui/icons/es/Https";
import Divider from "material-ui/es/Divider/Divider";
import UserList from "../User/UserList";


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
        marginTop: '50px',
    },
    secretTeam:{
        marginTop: '20px',
        color: '#1EA185',
    },
    secretTeamText:{
        marginLeft: '40px',
        marginTop: '-30px'
    },
    divider:{
        marginTop: '50px',
        width: '100%',
    },
    member:{
        marginLeft: '16px',
        marginTop: '10px',
    },
    overButton: {
        height: '100%',
        marginBottom: '56px',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
    },
};

const buttonStyle = {
    display:"block",
    marginLeft:"auto",
    marginRight:"auto",
};

class TeamScreen extends React.Component {

    constructor(props) {
        super();

        this.state = {
            teamId: 0,
            open: true,
            isAdmin: false,
            name:"",
            description: "",
            people:[],
        };
        setAuthenticationHeader();
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

    loadTeam = (teamId) => {
        if(!teamId)
            teamId = this.state.teamId;

        let url = HOST + "/team/" + teamId;

        axios.get(url)
            .then((response) => {
                console.log(response.data);
                this.setState({
                    teamId: response.data.teamId,
                    name: response.data.teamName,
                    description: response.data.description,
                    people: response.data.invitations,
                })
            });
        console.log("people", this.state.people);
    };

    handleLeave = () => {
        getHistory().push("/social?tab=1");
        let people = this.state.people;
        let index = people.indexOf(getUsername());
        people.splice(index, 1);
        console.log("people", people);
        this.setState({
           people: people,
        });
        this.sendAnswer();
    };

    sendAnswer = () => {
        console.log("leave");
        let url = HOST + '/team/' + this.state.teamId + '/leave';
        axios.delete(url)
            .then(() => {
                this.loadTeam();
                teamListNeedReload();
            })
    };

    render() {
        const { classes } = this.props;
        const error = this.state.error;
        let name = this.state.name;
        let description = this.state.description;
        let people = this.state.people;

        let admin = "";
        let selectedUsers = [];

        people.sort((a, b) => {
            if(a.answer === 0 && b.answer !== 0) {
                return -1;
            } else if(a.answer !== 0 && b.answer === 0) {
                return 1;
            } else {
                return 0;
            }
        });

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
                                    <p className={classes.fontBig}>{name}</p>
                                </div>
                                <div className={classes.description}>
                                    <p className={classes.fontSmall}>Description</p>
                                    <p>{description}</p>
                                </div>
                                <div className={classes.secretTeam}>
                                    <SecretIcon/>
                                    <p className={classes.secretTeamText}>Secret team. Only you can see the activity of this team.</p>
                                </div>
                            </div>
                            <Divider className={classes.divider}/>
                            <div className={classes.member}>
                                <p className={classes.fontBig}> Team Member ({people.length})</p>
                                <UserList
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