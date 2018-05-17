import PropTypes from "prop-types";
import {withStyles} from "material-ui/styles/index";
import TextField from "material-ui/es/TextField/TextField";
import AcceptedButton from "./AcceptedButton";
import React from 'react';
import Slide from 'material-ui/transitions/Slide';
import {Link} from "react-router-dom";
import IconButton from 'material-ui/IconButton';
import {Today, Schedule} from "@material-ui/icons/es/index";
import Chip from "material-ui/es/Chip/Chip";
import Avatar from "material-ui/es/Avatar/Avatar";
import ServiceIcon from "@material-ui/icons/Toc"
import Comments from "./Comments";
import {HOST} from "../../Config";
import axios from "axios/index";
import moment from "moment";
import Dialog from "../Dialog";
import CommentsIcon from '@material-ui/icons/Message';

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
            marginLeft: '16px',
            marginTop: '8px',
        },
        invitaionsHeader: {
            fontSize: '16px',
            fontWeight: '500',
            lineHeight: '24px',
        },
    };

const buttonStyle = {
    display:"block",
    marginLeft:"auto",
    marginRight:"auto",
}

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
            accepted: true,
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
        } else {
            console.log("Query does not exists");
            let url = HOST + "/event/" + eventId;

            axios.get(url)
                .then((response) => {
                    this.setState({
                        eventId: response.data.eventId,
                        name: response.data.eventName,
                        description: response.data.eventDescription,
                        location: response.data.location,
                        people: response.data.invitations.map((value) => value.userName),
                        date: response.data.startDate,
                    })
                });
        }
    }


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

        console.log("State");
        console.log(this.state);

        return (
            <Dialog
                title={name}
                closeUrl="/event"
                imageUrl="https://greenbyte.group/assets/images/logo.png"
            >
                <div className={classes.header}>
                    <div className={classes.headerText}>
                        <p className={classes.fontSmall}>Created by ...</p>
                        <p className={classes.fontBig}>{name}</p>
                        <p className={classes.fontSmall}><Today viewBox="-5 -5 27 27" className={classes.icons} /> {monthDay} <Schedule viewBox="-5 -5 27 27" className={classes.icons}/> {time}</p>
                    </div>
                    <div className={classes.headerComment}>
                        <CommentsIcon className={classes.commentIcon} />
                        <p className={classes.commentText}>2 Comments</p>
                    </div>
                </div>
                {(error
                        ? <p className={classes.error}>{error}</p>
                        : ""
                )}
                <div className={classes.invitations}>
                    <p className={classes.invitaionsHeader}>Invited People ({people.length})</p>
                    <br />
                    {people.map((person) => {
                        let peopleShortcut = person.charAt(0);
                        return <Chip
                            avatar={<Avatar >{peopleShortcut}</Avatar>}
                            label={person}
                            className={classes.chip}
                        />
                    })}

                </div>
                <div style={{marginLeft:20}}>
                 <Link to={{pathname:`/event/${eventId}/service`}}>
                    <ServiceIcon />
                 </Link>
                    Service List
                </div>
                <IconButton style={buttonStyle}>
                    <AcceptedButton/>
                </IconButton>

            </Dialog>
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