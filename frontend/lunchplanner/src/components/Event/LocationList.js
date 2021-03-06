import React from "react"

import Event from "./EventLocation";
import List from "@material-ui/core/List";
import {withStyles, Button} from "@material-ui/core";
import {Link} from "react-router-dom";
import FloatingActionButton from "../FloatingActionButton";
import {getUsername} from "../authentication/LoginFunctions";
import { Divider, ListItem} from "@material-ui/core";
import GPSIcon from "@material-ui/icons/GpsFixed";
import {getHistory} from "../../utils/HistoryUtils";

const styles = {
    root: {
        height: '100%',
        fontSize: 16,
        overflowX: 'auto',
    },
    list: {
        padding: 0,
    },
    row: {
        float: "left",
        marginLeft: "auto",
    },
    locationText:{
        fontSize: 16,
        fontFamily: "Work Sans",
        marginTop: '5px',
        whiteSpace: 'nowrap',
        marginLeft: '5px',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
    },
    locationAction: {
        fontSize: 16,
        fontFamily: "Work Sans",
        color: '#f29b26',
        width: '130px',
        padding: 0,
        marginTop: '-5px',
    },
    icon:{
        color: "#1EA185",
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
        events.sort((a,b) => {
            let aLocation = a.location.toUpperCase();
            let bLocation = b.location.toUpperCase();
            let atime = a.startDate;
            let btime = b.startDate;

            if (aLocation > bLocation)
                return 1;
            else if (aLocation < bLocation)
                return -1;
            else {
                if (atime > btime)
                    return 1;
                else if (atime < btime)
                    return -1;
                else
                    return 0;
            }
        });

        for(let i = 0; i < events.length; i++){
            locations.push(events[i].location);
        }
        let locationsUnique = [];

        if(locations.length !== 0) {
            //remove doubles
            locationsUnique = locations.filter((item, pos) => {
                return locations.indexOf(item) === pos && !locations.some((person) => person.location === item);
            });
        }
        let isSameLocation = false;
        return (
            <div className={classes.root}>
                <List className={classes.list}>
                    {locationsUnique.map((value) => {

                        let createEvent = () => {
                            getHistory().push("/app/event/create?location=" + value);
                        };

                        let counter = 0;
                        return(
                            <div>
                                <div
                                    style={{
                                        display: 'flex',
                                        flexDirection: 'row',
                                        marginLeft: '18px',
                                        marginTop: '18px',
                                        marginRight: '18px',
                                        justifyContent: 'space-between',
                                    }}
                                >
                                    <div
                                        style={{
                                            width: 'calc(100% - 130px)',
                                            display: 'flex',
                                            overflow: 'hidden',
                                        }}
                                    >
                                        <GPSIcon className={classes.icon}/>
                                        <p className={classes.locationText}>{value}</p>
                                    </div>
                                    <Button
                                        size="small"
                                        className={classes.locationAction}
                                        onClick={createEvent}
                                    >
                                        add event
                                    </Button>
                                </div>
                                <ListItem
                                    style={{overflowX: 'auto'}}
                                >
                                    {events.map(function(listValue){
                                        if(value === locations[counter]){
                                            isSameLocation = true;
                                        }else{
                                            isSameLocation = false;
                                        }
                                        counter++;
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

                                        if(isSameLocation)
                                            return (
                                                <div>
                                                    <div className={classes.row}>
                                                        <div>
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
                                                        </div>
                                                    </div>
                                                </div>
                                            );

                                        return '';
                                    })}

                                </ListItem>
                                <Divider />
                            </div>);
                    })
                    }
                </List>
                <Link to={{pathname:'/app/event/create'}}>
                    <FloatingActionButton />
                </Link>
            </div>);
    }
}

export default withStyles(styles)(LocationList);