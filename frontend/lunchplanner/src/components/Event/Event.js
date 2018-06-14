import React from "react"
import moment from "moment"
import {Card, CardContent, ListItem, withStyles} from "@material-ui/core";
import {Schedule, Today} from "@material-ui/icons";
import AcceptedButton from "./AcceptedButton";
import InvitedButton from "./InvitedButton";
import {Link} from "react-router-dom";

const styles = {
    card: {
        width: '100%',
        '&:hover': {
            textDecoration: 'none',
        },
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
    },
    date: {
        marginLeft: '16px',
        fontFamily: "Work Sans",
        fontSize: '11px',
        lineHeight: '20px',
        marginBottom: '0px',
        width: 'auto',
        float: 'left',
        color: 'black',
    },
    time : {
        fontFamily: "Work Sans",
        fontSize: '14px',
        lineHeight: '20px',
        marginBottom: '0px',
        marginTop: '5px',
        width: 'auto',
        float: 'left',
    },
    users: {
        fontFamily: "Work Sans",
        fontSize: '13px',
        lineHeight: '20px',
        marginBottom: '0px',
        color: '#A4A4A4',
        float: 'right',
    },
    icons: {
        width: '13px',
        height: 'auto',
    },
    text: {
        width: 'auto',
        color: 'black',
        marginLeft: '72px',
    },
    textSelected: {
        width: 'auto',
        color: '#75A045',
    },
    imageDiv: {
        width: '48px',
        height: '48px',
        borderRadius: '50%',
        border: '1px black solid',
        float: 'left',
    },
    cardContent: {
        width: '100%',
        float: 'none',
    },
    footer: {
        width: '100%',
        float: 'none',
    }
};

class Event extends React.Component {

    constructor(props) {
        super();

        //http://momentjs.com/docs/
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
            token: props.token,
        }
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
                if(value.answer === 0)
                    if(first) {
                        people += value.userName;
                        first = false;
                    } else {
                        people += ', ' + value.userName;
                    }
            });
            this.setState({
                invitations: invitations,
                people: people,
            });
        }
    }

    render() {
        const {classes} = this.props;

        const background = this.state.background;
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
        let token = this.state.token;

        people = people.split(',');
        people = people.map((value) => value.trim());

        let classesText = classes.text;
        if(accepted)
            classesText = classes.textSelected;

        let eventName = name;
        if(name !== location)
            eventName += " @ " + location;

        return (
            <div>
            <Link className={classes.link} to={{pathname:`/app/event/${this.state.id}`, query:{
                    eventName: name,
                    description: description,
                    date: date,
                    people: invitations,
                    accepted: accepted,
                    location:location,
                    token: token,
                }}}>

                    <ListItem style={{backgroundColor: background}} button className={classes.listItem}>

                        <Card className={classes.card}>
                            <CardContent className={classes.cardContent}>
                                <div className={classesText}>
                                    <p className={classes.title}>{eventName}</p>
                                    <p className={classes.time}>
                                        <Schedule viewBox="0 0 22 22" className={classes.icons}/> {time}
                                    </p>
                                    <div className={classes.users}>
                                        {
                                            people.join(', ')
                                        }
                                    </div>
                                </div>
                            </CardContent>
                            <hr style={{marginBottom: '11px'}} />
                            <div className={classes.footer}>
                                <p className={classes.date}>
                                    <Today viewBox="-5 -5 27 27" className={classes.icons} /> {monthDay}
                                </p>
                                {(accepted
                                        ? <AcceptedButton text="Accepted" />
                                        : (invited) ? <InvitedButton text="Invited" /> : ''
                                )}
                            </div>
                        </Card>

                    </ListItem>
                </Link>
            </div>

        );
    }
}

export default withStyles(styles)(Event);
