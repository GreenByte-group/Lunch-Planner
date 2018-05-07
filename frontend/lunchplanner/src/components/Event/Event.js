import React from "react"
import moment from "moment"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";
import {Schedule, Today} from "@material-ui/icons";
import AcceptedButton from "./AcceptedButton";
import {Link} from "react-router-dom";
import EventScreen from "./EventScreen";

const styles = {
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
        fontFamily: "Work Sans",
        fontSize: '14px',
        lineHeight: '20px',
        marginBottom: '0px',
    },
    users: {
        fontFamily: "Work Sans",
        fontSize: '13px',
        lineHeight: '20px',
        marginBottom: '0px',
        color: '#A4A4A4',
    },
    icons: {
        width: '13px',
        height: 'auto',
    },
    text: {
        float: 'left',
        width: '100%',
    }
};

class Event extends React.Component {

    constructor(props) {
        super();

        //http://momentjs.com/docs/
        let date = moment(props.date);

        this.state = {
            background: props.background,
            accepted: props.accepted | false,
            id: props.id,
            name: props.name,
            description: props.description,
            monthDay: date.format('DD MMM'),
            time: date.format('HH:mm'),
            location: props.location,

            //TODO invited people
            people:
                [{userName: "Can", short: "C"},
                  {userName: "Felix", short: "F"},
                  {userName: "Martin", short: "M"},
                ],
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
        let people = this.state.people;
        let location = this.state.location;

        return (
            <Link to={{pathname:`/event/${this.state.id}`, query:{
                    source: "/event",
                    eventName: name,
                    description: description,
                    monthDay: monthDay,
                    time: time,
                    people: people,
                    accepted: accepted,
                    location:location
                }}}>
                <ListItem style={{backgroundColor: background}} button className={classes.listItem}>
                    <div className={classes.text}>
                        <p className={classes.title}>{name}</p>
                        <p className={classes.date}><Today viewBox="-5 -5 27 27" className={classes.icons} /> {monthDay} <Schedule viewBox="-5 -5 27 27" className={classes.icons}/> {time}</p>
                        <p className={classes.users}>People</p>
                    </div>
                    {(accepted
                            ? <AcceptedButton text="Accepted" />
                            : ""
                    )}
                </ListItem>
            </Link>

        );
    }
}

export default withStyles(styles)(Event);