import React from "react"
import axios from "axios"
import FloatingActionButton from "../FloatingActionButton"

import {HOST, TOKEN} from "../../Config"
import Event from "./Event";
import List from "material-ui/List";

const styles = {
    root: {
        height: '100%',
        overflow: 'hidden',
    },
    list: {
        padding: 0,
    },
};

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
        let events = this.state.events;

        return (
            <div style={styles.root}>
                <List style={styles.list}>
                    {events.map(function(listValue){
                        return <Event name={listValue.eventName}
                                      description={listValue.eventDescription}
                                      date={listValue.startDate}
                        />;
                    })}
                </List>
                <FloatingActionButton />
            </div>

        );
    }
}

export default EventList;