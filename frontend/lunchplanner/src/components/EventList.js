import React from "react"
import axios from "axios"
import FloatingActionButton from "./FloatingActionButton"
import {createEvent} from "./CreateEventFunctions";

import {HOST, TOKEN} from "../Config"
import CreateEventScreen from "./CreateEventScreen";

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
    handleClickOpen = () => {
        this.setState({ open: true });
        console.log("handleClick");
        <CreateEventScreen/>
    };

    render() {
        let events = this.state.events;

        return (
            <div>
                <ul>
                    {events.map(function(listValue){
                        return <li>{listValue.eventName}</li>;
                    })}
                </ul>
                <CreateEventScreen/>

            </div>

        );
    }
}

export default EventList;