import React from "react"
import moment from "moment"
import {Card, CardContent, ListItem, withStyles, Avatar, List, Button, CardActions} from "@material-ui/core";
import {Link} from "react-router-dom";
import {eventListNeedReload} from "./EventContainer";
import {replyToEvent} from "./EventFunctions";
import {getProfilePicturePath} from "../User/UserFunctions";
import {HOST} from "../../Config";

const styles = {
    card: {
        width: '122px',
        '&:hover': {
            textDecoration: 'none',
        },
    },
    cardContent: {
        display: 'flex',
        marginLeft: 'auto',
        marginRight: 'auto',
        flexDirection: 'column',
        paddingBottom: '5px !important',
    },
    link: {
        '&:hover': {
            textDecoration: 'none',
        }
    },
    listItem: {
        padding: '7px 16px',
        '&:hover': {
            backgroundColor: '#0303031a !important',
        }
    },
    title: {
        fontFamily: "Work Sans",
        fontSize: '16px',
        lineHeight: '24px',
        marginBottom: '0px',
        display: 'table',
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    text:{
        marginBottom: '0px',
        display: 'table',
        marginLeft: 'auto',
        marginRight: 'auto',
        color: "black",
    },
    textSelected: {
        marginBottom: '0px',
        display: 'table',
        marginLeft: 'auto',
        marginRight: 'auto',
        color: '#75A045',
    },
    goingPeople:{
        fontFamily: "Work Sans",
        fontSize: '11px',
        lineHeight: '11px',
        display: 'table',
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    users: {
        fontFamily: "Work Sans",
        fontSize: '13px',
        lineHeight: '20px',
        marginBottom: '0px',
        color: '#A4A4A4',
        float: 'left',
    },
    icons: {
        width: '13px',
        height: 'auto',
    },
    memberAvatar:{
        marginRight: 5,
        width: 16,
        height: 16,
    },
    row: {
        display: 'flex',
        justifyContent: 'center',
    },
    joinButton: {
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    memberPicture: {
        height: '100%',
        width: '100%',
        objectFit: 'cover',
    },
};

class EventLocation extends React.Component {

    constructor(props) {
        super();

        let date = moment(props.date);

        let invitations = props.people;
        let people = "";

        let first = true;
        invitations.forEach(value => {
            if(value.answer === 0)
                if(first) {
                    people += value.userName;
                    first = false;
                } else {
                    people += ', ' + value.userName;
                }
        });

        this.state = {
            background: props.background,
            accepted: props.accepted || false,
            invited: props.invited || false,
            id: props.id,
            name: props.name,
            description: props.description,
            date: date,
            monthDay: date.format('DD MMM'),
            time: date.format('HH:mm'),
            invitations: invitations,
            people: people,
            location: props.location,
            locationId: props.locationId,
            lat: props.lat,
            lng: props.lng,
            token: props.token,
            weekday:props.weekDay,
        }
        console.log('PROPS EVENTLOCATION',props)
    }

    componentWillReceiveProps(newProps) {
        if(newProps.name !== undefined && newProps.name !== this.state.name) {
            this.setState({
                name: newProps.name,
            });
        }

        if(newProps.description !== undefined && newProps.description !== this.state.description) {
            this.setState({
                description: newProps.description,
            });
        }

        if(newProps.location !== undefined && newProps.location !== this.state.location) {
            this.setState({
                location: newProps.location,
            });
        }

        if(newProps.date !== undefined && newProps.date !== this.state.date) {
            let date = moment(newProps.date);

            this.setState({
                date: date,
                monthDay: date.format('DD MMM'),
                time: date.format('HH:mm'),
            });
        }

        if(newProps.invited !== undefined && newProps.invited !== this.state.invited) {
            this.setState({
                invited: newProps.invited,
            });
        }

        if(newProps.accepted !== undefined && newProps.accepted !== this.state.accepted) {
            this.setState({
                accepted: newProps.accepted,
            });
        }
        if(newProps.token !== undefined && newProps.token !== this.state.token) {
            this.setState({
                token: newProps.token,
            });
        }

        if(newProps.people !== undefined && newProps.people !== this.state.people) {
            let invitations = newProps.people;
            let people = "";

            let first = true;
            invitations.forEach(value => {
                if(value.answer === 0) {
                    if (first) {
                        people += value.userName;
                        first = false;
                    } else {
                        people += ', ' + value.userName;
                    }

                    let stateId = "pic" + value.userName.replace(/\s/g, '');
                    if(!this.state[stateId]) {
                        getProfilePicturePath(value.userName, (response) => {
                            this.setState({
                                [stateId]:  HOST + response.data,
                            })
                        });
                    }
                }
            });

            this.setState({
                invitations: invitations,
                people: people,
            });
        }
    }

    onJoinLeaveClick = () => {
        if(this.state.accepted) {
            replyToEvent(this.state.id, 'reject', () => {
                eventListNeedReload();
                this.setState({
                    accepted: false,
                })
            });
        } else {
            replyToEvent(this.state.id, 'accept', () => {
                eventListNeedReload();
                this.setState({
                    accepted: true,
                })
            });
        }
    };

    // onDelete = (event) => {
    //     console.log("DADAAAAAAAAAAA 2")
    //     this.props.onDelete(event);
    // };

    render() {
        const {classes} = this.props;

        let accepted= this.state.accepted;
        let invited = this.state.invited;

        let name = this.state.name;
        let description = this.state.description;
        let monthDay = this.state.monthDay;
        let time = this.state.time;
        let date = this.state.date;
        let people = this.state.people;
        let invitations = this.state.invitations;
        let location = this.state.location;
        let locationId = this.state.locationId;
        let lat = this.state.lat;
        let lng= this.state.lng;
        let token = this.state.token;
        let classesText = classes.text;
        if(accepted)
            classesText = classes.textSelected;

        people = people.split(',');
        people = people.map((value) => value.trim());

        let textJoin = 'join';
        if(accepted)
            textJoin = 'leave';

        let memberCounter = 0;

        let weekDay = getDayOfWeek(date);
        function getDayOfWeek(date) {
            var dayOfWeek = new Date(date).getDay();
            return isNaN(dayOfWeek) ? null : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][dayOfWeek];
        }

        return (
            <div>
                <div className={classes.listItem}>
                    <Card className={classes.card}>
                        <Link className={classes.link} to={{pathname:`/app/event/${this.state.id}`, query:{
                                eventName: name,
                                description: description,
                                date: date,
                                people: invitations,
                                accepted: accepted,
                                location:location,
                                locationId: locationId,
                                lat: lat,
                                lng: lng,
                                token: token,
                            }}}>
                            <CardContent className={classes.cardContent}>
                                <div className={classesText}>
                                    <p className={classes.title}>{monthDay}</p>
                                    <p className={classes.title}>{weekDay}</p>
                                    <p className={classes.title}>{time}</p>
                                    <p className={classes.goingPeople}> {
                                        (people[0] !== "") ?
                                            people.length : 0
                                    } going</p>
                                    <List className={classes.users}>
                                        <div className={classes.row}>
                                            {people.map((person)=>{
                                                memberCounter++;
                                                let imageId = "pic" + String(person).replace(/\s/g, '');
                                                let url = this.state[imageId];
                                                if(person !== "" && memberCounter <= 3) {
                                                    return(
                                                        <Avatar className={classes.memberAvatar}>
                                                            <img className={classes.memberPicture} src={url}/>
                                                        </Avatar>
                                                    )
                                                }
                                            })}
                                            {people.length > 3 ?
                                                <div className={classes.member}>
                                                    <Avatar className={classes.memberAvatar}>
                                                        +{people.length - 3}
                                                    </Avatar>
                                                </div> : ""
                                            }
                                        </div>
                                    </List>
                                </div>
                            </CardContent>
                        </Link>
                        <CardActions className={classes.joinButtonContainer}>
                            <Button className={classes.joinButton} onClick={this.onJoinLeaveClick}>{textJoin}</Button>
                        </CardActions>
                    </Card>

                </div>
            </div>
        );
    }
}

export default withStyles(styles)(EventLocation);