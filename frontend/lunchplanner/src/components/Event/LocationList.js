import React from "react"

import {HOST} from "../../Config"
import Event from "./EventLocation";
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

class LocationList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            events: props.events,
            search:props.search,
            locations:[],
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
        let locations = [];
        // Sort events with location name
        events.sort(function(a,b) {return (a.location > b.location) ? 1 : ((b.location > a.location) ? -1 : 0);});
        console.log(events);
        for(let i = 0; i < events.length; i++){
            locations.push(events[i].location);
        }
        /*let locationsUnique = locations.filter((item, pos) => {
            return locationsUnique.indexOf(item) === pos && !locations.some((lo) => lo === item);
        });*/
        console.log(locations);
        let counter = 0;
        return (
            <div className={classes.root}>
                <List className={classes.list}>
                    {events.map(function(listValue){
                        let locationname = "";
                        if(counter === 0 || locations[counter-1] !== locations[counter]){
                            locationname = locations[counter];
                            counter++;
                            console.log(locationname);
                        }else{
                            counter++;
                        }

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

                        return (
                            <div>
                                <p>{locationname}</p>
                                <Event name={listValue.eventName}
                                       key={'Event' + listValue.eventId}
                                       id={listValue.eventId}
                                       description={listValue.eventDescription}
                                       location={listValue.location}
                                       date={listValue.startDate}
                                       accepted={accepted}
                                       invited={invited}
                                       people={listValue.invitations}
                                       token={listValue.shareToken}
                                />
                            </div>);
                    })}
                </List>
                <Link to={{pathname:'/event/create'}}>
                    <FloatingActionButton />
                </Link>
            </div>

        );
    }
}

export default withStyles(styles)(LocationList);