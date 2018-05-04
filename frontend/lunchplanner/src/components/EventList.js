import React from "react"
import axios from "axios"
import FloatingActionButton from "./FloatingActionButton"
import {createEvent} from "./CreateEventFunctions";

import {HOST, TOKEN} from "../Config"
import CreateEventScreen from "./CreateEventScreen";
import {Link, withRouter} from "react-router-dom";

class EventList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            open: false,
            events: [],
        }
    }

    componentDidMount() {
        let token = localStorage.getItem(TOKEN);

        this.setState({
            search: this.props.search,
        });

        let url;
        if(this.props.search)
            url = HOST + "/event/search/" + this.props.search;
        else
            url = HOST + "/event";

        console.log("Token: " + token);
        let config = {
            headers: {
                'Authorization': token,
            }
        };

        axios.get(url, config)
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
                <Link to="/event/create"><FloatingActionButton/></Link>

            </div>

        );
    }
}

export default EventList;