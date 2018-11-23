import React from "react"

import Event from "./EventLocation";
import List from "@material-ui/core/List";
import {withStyles, Button} from "@material-ui/core";
import {Link} from "react-router-dom";
import FloatingActionButton from "../FloatingActionButton";
import {getUsername} from "../authentication/LoginFunctions";
import { Divider, ListItem} from "@material-ui/core";
import {FastfoodRounded} from "@material-ui/icons";
import {getHistory} from "../../utils/HistoryUtils";
import SpeedSelectGrid from "./SpeedSelectGrid";
import {createEvent, getEvents} from "./EventFunctions";
import {eventListNeedReload} from "./EventContainer";
import moment from "moment/moment";

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
    grid:{
        margin :'0% 10px',
        maxHeight:  '-webkit-fill-available'
        // boxShadow: '0px 2px 4px -1px rgba(0, 0, 0, 0.2),0px 4px 5px 0px rgba(0, 0, 0, 0.14),0px 1px 10px 0px rgba(0, 0, 0, 0.12)',
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
        console.log('Props locatiobList', props)
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
    };
    speedSelect = (event, lat, lng, placeId) => {
            this.setState({
                locationText: event,
                lat: lat,
                lng: lng,
                placeId: placeId,
            });

        console.log('Event: ', event);

        let defaultDate = moment().add(30, 'm').toDate();

        createEvent(event,"", defaultDate, [], true, placeId, lat, lng,
            (response) => {
                console.log('all states of new event', this.state);
                if(response.status === 201) {
                    eventListNeedReload();
                    getHistory().push('/app/event');
                } else {
                    this.setState({error: response.response.data});
                }
            },
            (error) => {
                if(error && error.response)
                    this.setState({error: error.response.data});
                else
                    console.log(error);
            });


        // getHistory().push("/app/event");
    };

    onDelete = (event) => {
        console.log("DADAAAAAAAAAAA")
    };
    updateSite(){
        this.forceUpdate();
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

        // let oldCount = 0;
        // getEvents("", response => {
        //     oldCount = response.data.length;
        // });
        // if(oldCount !== events.length){
        //     this.updateSite();
        // }

        for(let i = 0; i < events.length; i++){
            locations.push(events[i].location);
        }
        let locationsUnique = [];

        if(locations.length !== 0) {
            locationsUnique = locations.filter((item, pos) => {
                return locations.indexOf(item) === pos && !locations.some((person) => person.location === item);
            });
        }
        let isSameLocation = false;
        return (
            <div className={classes.root}>
                <div className={classes.grid}>
                    <SpeedSelectGrid create={true} onChange={this.speedSelect}/>
                </div>
                <Divider style={{marginTop: '15px' }}/>
                <div style={{marginTop: '15px'}}/>
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
                                        <FastfoodRounded className={classes.icon}/>
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
                                                                       locationId = {listValue.locationId}
                                                                       lat={listValue.lat}
                                                                       lng = {listValue.lng}
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