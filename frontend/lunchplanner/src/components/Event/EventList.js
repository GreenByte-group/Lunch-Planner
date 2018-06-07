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

const styles = {
    root: {
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
    },
    list: {
        padding: 0,
    },
};

const lightBackground = 'transparent';
const darkerBackground = '#03030305';

class EventList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            events: props.events,
            search:props.search,
            sort: props.sort,
        }
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

        if(this.state.sort === "date"){
            events.sort(function(a,b) {return (a.startDate > b.startDate) ? 1 : ((b.startDate > a.startDate) ? -1 : 0);});
        }else if(this.state.sort === "following"){
            events.sort(function(a,b) {return (a.location > b.location) ? 1 : ((b.location > a.location) ? -1 : 0);});
        }

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

                        return <Event name={listValue.eventName}
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