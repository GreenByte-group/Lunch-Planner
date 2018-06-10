import React from "react"

import {HOST} from "../../Config"
import Event from "./Event";
import List from "@material-ui/core/List";
import {withStyles} from "@material-ui/core/styles/index";
import {Link} from "react-router-dom";
import FloatingActionButton from "../FloatingActionButton";
import {getUsername} from "../authentication/LoginFunctions";
import {getEvents} from "./EventFunctions";
import {needReload} from "./EventContainer";
import moment from "moment";

const styles = {
    root: {
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
    },
    list: {
        padding: 0,
    },
    day:{
        marginLeft: 16,
        marginTop: 10,
        fontSize: 16,
    },
};

const lightBackground = 'transparent';
const darkerBackground = '#03030305';

class EventList extends React.Component {

    constructor(props) {
        super();
        this.state = {
            events: props.events,
            search:props.search,
            sort: props.sort,
        }
        //this.handleToday = this.handleToday.bind(this);
    }

    componentWillReceiveProps(newProps) {
        if(newProps.search !== this.state.search){
            this.setState({
                search: newProps.search,
            });
        }
        if(newProps.events !== this.state.events){
            this.setState({
                events: newProps.events,
            });
        }
    }



    render() {
        const { classes } = this.props;
        let events = this.state.events || [];
        let showLightBackground = true;
        let sortByDate = false;
        let sortByFollowing = false;
        let locations = [];
        events.sort(function(a,b) {return (a.startDate > b.startDate) ? 1 : ((b.startDate > a.startDate) ? -1 : 0);});

        for(let i = 0; i < events.length; i++){
            locations.push(events[i].location);
        }

        let isNotToday = true;
        let isNotTomorrow = true;
        let isNotThisWeek = true;

        return (
            <div className={classes.root}>
                <List className={classes.list}>
                    {events.map(function(listValue){
                        let background;
                        if(showLightBackground)
                            background = lightBackground;
                        else
                            background = darkerBackground;
                        showLightBackground = !showLightBackground;

                        let accepted = false;
                        let invited = false;
                        let username = getUsername();
                        listValue.invitations.forEach((value) => {
                            if(value.userName === username) {
                                invited = true;
                                if(value.answer === 0) {
                                    accepted = true;
                                }
                            }
                        });
                        const event = <Event name={listValue.eventName}
                                             key={'Event' + listValue.eventId}
                                             id={listValue.eventId}
                                             description={listValue.eventDescription}
                                             location={listValue.location}
                                             date={listValue.startDate}
                                             background={background}
                                             accepted={accepted}
                                             invited={invited}
                                             people={listValue.invitations}
                                             token={listValue.shareToken}
                        />;

                        return moment(listValue.startDate).isSame(moment(), 'day') && isNotToday
                            ?
                            <div>
                                <p className={classes.day}>Today</p>
                                {event}
                                {isNotToday = false}
                            </div>:
                            moment(listValue.startDate).isSame(moment(new Date()).add(1,'days'),'day') && isNotTomorrow
                                ?
                                <div>
                                    <p className={classes.day}>Tomorrow</p>
                                    {event}
                                    {isNotTomorrow = false}
                                </div> :
                                (moment(listValue.startDate).isSame(moment(new Date()).add(2,'days'),'day') ||
                                moment(listValue.startDate).isSame(moment(new Date()).add(3,'days'),'day') ||
                                moment(listValue.startDate).isSame(moment(new Date()).add(4,'days'),'day') ||
                                moment(listValue.startDate).isSame(moment(new Date()).add(5,'days'),'day') ||
                                moment(listValue.startDate).isSame(moment(new Date()).add(7,'days'),'day') ||
                                moment(listValue.startDate).isSame(moment(new Date()).add(7,'days'),'day')) && isNotThisWeek
                                    ?
                                    <div>
                                        <p className={classes.day}>This Week</p>
                                        {event}
                                        {isNotThisWeek = false}
                                    </div> : <div>{event}</div>
                    })}
                </List>
                <Link to={{pathname:'/event/create'}}>
                    <FloatingActionButton />
                </Link>
            </div>

        );
    }
}

export default withStyles(styles)(EventList);