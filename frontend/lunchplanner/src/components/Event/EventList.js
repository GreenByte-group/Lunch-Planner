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

const lightBackground = 'transparent';
const darkerBackground = '#03030305';

class EventList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            events: [],
        }
    }

    componentDidMount() {
        this.setState({
            search: this.props.search,
        });

        let url;
        if(this.props.search)
            url = HOST + "/event/search/" + this.props.search;
        else
            url = HOST + "/event";

        axios.get(url)
            .then((response) => {
                this.setState({
                    events: response.data,
                })
            })

        // this.setState({
        //     events: [{eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}, {eventName: 'event'}]
        // })
    }

    render() {
        const { classes } = this.props;
        let events = this.state.events;
        let showLightBackground = true;

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
                                      id={listValue.eventId}
                                      description={listValue.eventDescription}
                                      date={listValue.startDate}
                                      background={background}
                                      accepted={true}
                                      location={listValue.location}
                        />;
                    })}
                </List>
                <Link to="/event/create"><FloatingActionButton /></Link>
            </div>

        );
    }
}

export default withStyles(styles)(EventList);