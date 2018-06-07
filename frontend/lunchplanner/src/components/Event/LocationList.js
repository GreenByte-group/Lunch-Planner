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
import Divider from "@material-ui/core/es/Divider/Divider";
import GPSIcon from "@material-ui/icons/GpsFixed";
import ListItem from "@material-ui/core/es/ListItem/ListItem";

const styles = {
    root: {
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
    },
    list: {
        padding: 0,
    },
    row: {
        float: "left",
        marginLeft: "auto"
    },
    locationText:{
        marginTop: 20,
        marginLeft: 24,
        fontFamily: "Work Sans",
        fontSize: '16px',
    },
    icon:{
        float: "right",
        marginRight: 24,
        color: "#1EA185",
        marginTop: -30,
    },
    divider:{

    }
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
        let isRow = false;
        // Sort events with location name
        events.sort(function(a,b) {return (a.location > b.location) ? 1 : ((b.location > a.location) ? -1 : 0);});
        console.log(events);
        for(let i = 0; i < events.length; i++){
            locations.push(events[i].location);
        }
        let locationsUnique = [];
        if(locations.length !== 0) {
            //remove doubles and already invited people
            locationsUnique = locations.filter((item, pos) => {
                return locations.indexOf(item) === pos && !locations.some((person) => person.location === item);
            });
        }

        let counter = 0;
        let titelNotSet = true;
        return (
            <div className={classes.root}>
                <List className={classes.list}>
                    {locationsUnique.map((value) => {
                        console.log(value)
                        return(
                            <div>
                                <ListItem>
                                    <div className={classes.location}>
                                        <p className={classes.locationText}>{value}</p>
                                        <GPSIcon className={classes.icon}/>
                                    </div>
                                    {events.map(function(listValue){
                                        /*let locationname = "";
                                        if(counter === 0 || locations[counter-1] !== locations[counter]){
                                            locationname = locations[counter];
                                            if(locations[counter] === locations[counter +1]){
                                                isRow = true;
                                            }else{
                                                isRow = false;
                                            }
                                            counter++;
                                        }else{
                                            counter++;
                                            if(locations[counter] === locations[counter +1]){
                                                isRow = true;
                                            }else{
                                                isRow = false;
                                            }
                                            counter++;
                                        }*/

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
                                                <div>
                                                    {isRow ?
                                                        (<div>
                                                            {titelNotSet ?  (<div className={classes.location}>

                                                                <p className={classes.locationText}>{locationname}</p>
                                                                <GPSIcon className={classes.icon}/>

                                                            </div> ) : ""}
                                                            {titelNotSet = false}
                                                            <div className={classes.row}>
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
                                                            </div>
                                                        </div>)
                                                        :
                                                        (<div>
                                                            <div className={classes.location}>
                                                                <p className={classes.locationText}>{locationname}</p>
                                                                <GPSIcon className={classes.icon}/>
                                                            </div>
                                                            {titelNotSet = true}
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
                                                        </div>)
                                                    }
                                                </div>
                                            </div>
                                        );
                                    })}

                                </ListItem>
                                <Divider className={classes.divider}/>
                            </div>);
                    })
                    }
                </List>
                <Link to={{pathname:'/event/create'}}>
                    <FloatingActionButton />
                </Link>
            </div>);
    }
}

export default withStyles(styles)(LocationList);