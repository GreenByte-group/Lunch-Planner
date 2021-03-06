import React from "react"

import {HOST} from "../../Config"
import Event from "./Event";
import List from "@material-ui/core/List";
import {withStyles} from "@material-ui/core/styles/index";
import FloatingActionButton from "../FloatingActionButton";
import {getUsername} from "../authentication/LoginFunctions";
import moment from "moment";
import {getHistory} from "../../utils/HistoryUtils";

const styles = {
    root: {
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
    },
    list: {
        padding: 0,
        paddingBottom: '75px',
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
            sort: props.sort,
        }
    }

    componentWillReceiveProps(newProps) {
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
        let locations = [];
        events.sort(function(a,b) {return (a.startDate > b.startDate) ? 1 : ((b.startDate > a.startDate) ? -1 : 0);});

        for(let i = 0; i < events.length; i++){
            locations.push(events[i].location);
        }

        let isNotToday = true;
        let isNotTomorrow = true;
        let isNotThisWeek = true;
        let laterHeaderExists = false;

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

                        if(moment(listValue.startDate).isSame(moment(), 'day') && isNotToday) {
                            return (
                                <div>
                                    <p className={classes.day}>Today</p>
                                    {event}
                                    {isNotToday = false}
                                </div>
                            )
                        } else if(moment(listValue.startDate).isSame(moment(new Date()).add(1,'days'),'day') && isNotTomorrow) {
                            return (
                                <div>
                                    <p className={classes.day}>Tomorrow</p>
                                    {event}
                                    {isNotTomorrow = false}
                                </div>
                            )
                        } else if(
                            (moment(listValue.startDate).isSame(moment(new Date()).add(2,'days'),'day') ||
                            moment(listValue.startDate).isSame(moment(new Date()).add(3,'days'),'day') ||
                            moment(listValue.startDate).isSame(moment(new Date()).add(4,'days'),'day') ||
                            moment(listValue.startDate).isSame(moment(new Date()).add(5,'days'),'day') ||
                            moment(listValue.startDate).isSame(moment(new Date()).add(7,'days'),'day') ||
                            moment(listValue.startDate).isSame(moment(new Date()).add(7,'days'),'day')) && isNotThisWeek
                        ) {
                            return (
                                <div>
                                    <p className={classes.day}>This Week</p>
                                    {event}
                                    {isNotThisWeek = false}
                                </div>
                            )
                        } else if(
                            moment(listValue.startDate).isAfter(moment(new Date()).add(7,'days'),'day') && !laterHeaderExists
                        ) {
                            laterHeaderExists = true;
                            return (
                                <div>
                                    <p className={classes.day}>Later</p>
                                    {event}
                                </div>
                            )
                        } else {
                            return (
                                <div>
                                    {event}
                                </div>
                            )
                        }
                    })}
                </List>
                <FloatingActionButton onClick={() => getHistory().push('/app/event/create')} />
            </div>

        );
    }
}

export default withStyles(styles)(EventList);
