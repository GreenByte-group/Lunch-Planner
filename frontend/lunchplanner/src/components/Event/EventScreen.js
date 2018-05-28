import PropTypes from "prop-types";
import {withStyles} from "material-ui/styles/index";
import React from 'react';
import Slide from 'material-ui/transitions/Slide';
import {Link} from "react-router-dom";
import {Today, Schedule} from "@material-ui/icons/es/index";
import ServiceIcon from "@material-ui/icons/Toc"
import ListIcon from "@material-ui/icons/Assignment"
import {HOST} from "../../Config";
import axios from "axios/index";
import moment from "moment";
import Dialog from "../Dialog";
import CommentsIcon from '@material-ui/icons/Message';
import UserList from "../User/UserList";
import {Button} from "material-ui";
import ServiceList from "./ServiceList";
import {getUsername} from "../authentication/Authentication";
import InvitationButton from "./InvitationButton";
import {eventListNeedReload} from "./EventList";

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
        fontBig: {
            fontSize: '16px',
            margin: '0px',
        },
        fontSmall: {
            fontSize: '13px',
            margin: '0px',
        },
        icons: {
            height: '13px',
            width: '13px',
        },
        headerText: {
            float: 'left',
        },
        headerComment: {
            float: 'right',
            display: 'flex',
            flexDirection: 'column',
            color: 'white',
        },
        commentIcon: {
            marginTop: '12px',
            marginRight: 'auto',
            marginLeft: 'auto',
        },
        commentText: {
            fontSize: '11px',
            lineHeight: '16px',
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
        serviceListLink: {
            minHeight: '171px',
            height: '100%',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            backgroundColor: '#FAFAFA',
        },
        serviceList: {
            alignSelf: 'center',
            display: 'flex',
            flexDirection: 'column',
            textAlign: 'center',
            color: 'black',
        },
        serviceListIcon: {
            height: '58px',
            width: '41px',
            marginLeft: 'auto',
            marginRight: 'auto',
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

class EventScreen extends React.Component {

    constructor(props) {
        super();

        this.state = {
            eventId: 0,
            open: true,
            isAdmin: false,
            name:"",
            location:"",
            date: "",
            description: "",
            people:[],
            accepted: false,
        };
    }

    componentDidMount() {
        let eventName, description, date, people, accepted, location, eventId;

        eventId = this.props.match.params.eventId;

        if(this.props.location.query) {
            console.log("Query exists");
            if (this.props.location.query.eventName) {
                eventName = String(this.props.location.query.eventName);
            }
            if (this.props.location.query.description) {
                description = String(this.props.location.query.description);
            }
            if (this.props.location.query.date) {
                date = this.props.location.query.date;
            }
            if (this.props.location.query.people) {
                people = this.props.location.query.people;
            }
            if (this.props.location.query.accepted) {
                accepted = Boolean(this.props.location.query.accepted);
            }
            if (this.props.location.query.location) {
                location = String(this.props.location.query.location);
            }

            this.setState({
                eventId: eventId,
                name: eventName,
                description: description,
                date: date,
                people: people,
                accepted: accepted,
                location: location,
            })

            console.log(people);
        } else {
            console.log("Query does not exists");
            this.loadEvent(eventId);
        }
    }

    loadEvent = (eventId) => {
        if(!eventId)
            eventId = this.state.eventId;

        let url = HOST + "/event/" + eventId;

        axios.get(url)
            .then((response) => {
                this.setState({
                    eventId: response.data.eventId,
                    name: response.data.eventName,
                    description: response.data.eventDescription,
                    location: response.data.location,
                    people: response.data.invitations,
                    date: response.data.startDate,
                })
            });
    };

    handleDecline = () => {
        this.sendAnswer('reject');
    };

    handleAccept = () => {
        let username = getUsername();
        let invitationAccepted = false;
        this.state.people.forEach((value) => {
            if(value.userName === username) {
                if(value.answer === 0) {
                    invitationAccepted = true;
                }
            }
        });

        if(invitationAccepted) {
            this.handleDecline();
        } else {
            this.sendAnswer('accept');
        }
    };

    sendAnswer = (answer) => {
        let config = {
            headers: {
                'Content-Type': 'text/plain',
            }
        };

        let url = HOST + '/event/' + this.state.eventId + '/reply';
        axios.put(url, answer, config)
            .then((response) => {
                this.loadEvent();
                eventListNeedReload();
            })
            .catch((error) => {
            })
    };

    render() {
        const { classes } = this.props;
        const error = this.state.error;
        let name = this.state.name;
        let description = this.state.description;
        let date = this.state.date;
        let location = this.state.location;
        let people = this.state.people;
        let eventId = this.state.eventId;

        let momentDate = moment(date);

        let time = momentDate.format('HH:mm');
        let monthDay = momentDate.format('DD MMM');

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

        let username = getUsername();
        let invited = false;
        let accepted = false;
        let buttonText = "Join Event";
        let barTitle = name;

        people.forEach((listValue) => {
            if(listValue.userName === username) {
                if(listValue.answer !== 0) {
                    invited = true;
                    barTitle = "Invitation...";
                } else {
                    accepted = true;
                    buttonText = "Leave Event";
                }
            }

            if(listValue.answer === 0) {
                selectedUsers.push(listValue.userName);
            }

            if(listValue.admin) {
                admin = listValue.userName;
            }
        });

        //TODO anzahl kommentare

        return (
            <div>
                <Dialog
                    title={barTitle}
                    closeUrl="/event"
                    imageUrl="https://greenbyte.group/assets/images/logo.png"
                >
                    <div className={classes.overButton}>
                        <div className={classes.header}>
                            <div className={classes.headerText}>
                                <p className={classes.fontSmall}>Created by {admin}</p>
                                <p className={classes.fontBig}>{name}</p>
                                <p className={classes.fontSmall}><Today viewBox="-5 -5 27 27" className={classes.icons} /> {monthDay} <Schedule viewBox="-5 -5 27 27" className={classes.icons}/> {time}</p>
                            </div>
                            {
                                (invited)
                                    ? ''
                                    :   <Link to={{pathname:`/event/${eventId}/comments`}}>
                                            <div className={classes.headerComment}>
                                            <CommentsIcon className={classes.commentIcon} />
                                                <p className={classes.commentText}>Comments</p>
                                            </div>
                                        </Link>
                            }
                        </div>
                        <div className={classes.invitations}>
                            <p className={classes.invitaionsHeader}>Invited People ({people.length})</p>
                            <UserList selectedUsers={selectedUsers} othersInvited={true} users={people} selectable={false} />
                        </div>
                        {
                            (invited)
                                ? ''
                                : <div>
                                    <ServiceList eventId={eventId} />
                                    <Link className={classes.serviceListLink} to={{pathname:`/event/${eventId}/service`}}>
                                        <div className={classes.serviceList}>
                                            <ListIcon className={classes.serviceListIcon} />
                                            <p>Add a task</p>
                                        </div>
                                    </Link>
                                </div>
                        }
                    </div>
                    {
                        (invited)
                            ? <InvitationButton decline={this.handleDecline} join={this.handleAccept} class={classes.buttonInvitation} />
                            : <Button variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                                {buttonText}
                            </Button>
                    }
                </Dialog>
            </div>
        );
    }
}
EventScreen.propTypes = {
    classes: PropTypes.object.isRequired,
    id: PropTypes.number.isRequired,
    isAdmin: PropTypes.bool.isRequired,
    name:PropTypes.string.isRequired,
    location:PropTypes.string.isRequired,
    monthDay: PropTypes.object.isRequired,
    time: PropTypes.object.isRequired,
    description: PropTypes.string.isRequired,
    people:PropTypes.object.isRequired,
    accepted: PropTypes.bool.isRequired,

};
export default withStyles(styles, { withTheme: true })(EventScreen);