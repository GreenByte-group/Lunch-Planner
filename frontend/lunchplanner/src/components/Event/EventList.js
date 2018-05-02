import React from "react"
import axios from "axios"
import FloatingActionButton from "../FloatingActionButton"

import {HOST, TOKEN} from "../../Config"

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
    }

    render() {
        let events = this.state.events;

        return (
            <div>
                <ul>
                    {events.map(function(listValue){
                        return <li>{listValue.eventName}</li>;
                    })}
                </ul>
                <FloatingActionButton />
            </div>

        );
    }
}

export default EventList;