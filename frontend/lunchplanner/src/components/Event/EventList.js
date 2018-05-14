import React from "react"
import axios from "axios"

import {HOST} from "../../Config"
import Event from "./Event";
import List from "material-ui/List";
import {withStyles} from "material-ui/styles/index";
import {Link} from "react-router-dom";
import FloatingActionButton from "../FloatingActionButton";

const styles = {
    root: {
        height: '100%',
        overflow: 'hidden',
    },
    list: {
        padding: 0,
    },
};

export let needReload = false;

export function eventListNeedReload() {
    needReload = true;
}

const lightBackground = 'transparent';
const darkerBackground = '#03030305';

class EventList extends React.Component {

    constructor(props) {
        super();
        this.state = {
            events: [],
            search:null,
        }
    }

    componentDidMount() {
        this.setState({
            search: this.props.search,
        });

        this.loadEvents(this.props.search);

        // this.setState({
        //     events: [{eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}]
        // })
    }

    loadEvents(search) {
        if(search === null || search === undefined)
            search = this.state.search;

        let url;
        if(search)
            url = HOST + "/event/search/" + search;
        else
            url = HOST + "/event";

        axios.get(url)
            .then((response) => {
                this.setState({
                    events: response.data,
                })
            })
    }

    render() {
        const { classes } = this.props;
        let events = this.state.events;
        let showLightBackground = true;

        if(needReload) {
            needReload = !needReload;
            this.loadEvents();
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

                        return <Event name={listValue.eventName}
                                      key={'Event' + listValue.eventId}
                                      id={listValue.eventId}
                                      description={listValue.eventDescription}
                                      date={listValue.startDate}
                                      background={background}
                                      accepted={true}
                                      people={listValue.invitations}
                                      location={listValue.location}
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