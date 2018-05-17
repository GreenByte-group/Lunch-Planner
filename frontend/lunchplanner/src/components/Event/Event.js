import React from "react"
import moment from "moment"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";
import {Schedule, Today} from "@material-ui/icons";
import AcceptedButton from "./AcceptedButton";
import InvitedButton from "./InvitedButton";
import {Link} from "react-router-dom";
import EventScreen from "./EventScreen";
import Avatar from "material-ui/es/Avatar/Avatar";
import Card, { CardActions, CardContent } from 'material-ui/Card';
import Button from 'material-ui/Button';
import Typography from 'material-ui/Typography';

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
        let people = invitations.map(value => value.userName).join(', ');

        this.state = {
            background: props.background,
            accepted: props.accepted || false,
            id: props.id,
            name: props.name,
            description: props.description,
            date: date,
            monthDay: date.format('DD MMM'),
            time: date.format('HH:mm'),
            people: people,
            location: props.location,
        }
    }


    render() {
        const {classes} = this.props;

        const background = this.state.background;
        let accepted= this.state.accepted;

        let name = this.state.name;
        let description = this.state.description;
        let monthDay = this.state.monthDay;
        let time = this.state.time;
        let date = this.state.date;
        let people = this.state.people;
        let location = this.state.location;

        people = people.split(',');
        people = people.map((value) => value.trim());

        return (
            <Link className={classes.link} to={{pathname:`/event/${this.state.id}`, query:{
                    eventName: name,
                    description: description,
                    date: date,
                    people: people,
                    accepted: accepted,
                    location:location
                }}}>

                <ListItem style={{backgroundColor: background}} button className={classes.listItem}>

                    <Card className={classes.card}>
                        <CardContent className={classes.cardContent}>
                            <div className={classes.imageDiv}>

                            </div>
                            <div className={classes.text}>
                                <p className={classes.title}>{name}</p>
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
                                    : <InvitedButton text="Invited" />
                            )}
                        </div>
                    </Card>

                </ListItem>
            </Link>

        );
    }
}

export default withStyles(styles)(Event);