// App.js
import React from "react";
import logo from "./logo.svg";
import "./App.css";
import axios from "axios";

import EventList from "./components/EventList"

// Tutorial part 3
/*const events = [
    { id: 1, name: "Event1" },
    { id: 2, name: "Event2" },
    { id: 3, name: "Event3" },
    { id: 4, name: "Event4" }
];*/

class App extends React.Component {

    // default State object
    state = {
        events: []
    };

    componentDidMount() {
        axios
            .get("http://localhost:8080/event")
            .then(response => {

                console.log(response.data);

                // create an array of contacts only with relevant data
                const newEvents = response.data.map(e => {
                    return {
                        id: e.id,
                        name: e.name
                    };
                });

                // create a new "State" object without mutating
                // the original State object.
                const newState = Object.assign({}, this.state, {
                    events: newEvents
                });

                // store the new state object in the component's state
                this.setState(newState);
            })
            .catch(error => console.log(error));
    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo" />
                    <h1 className="App-title">Welcome to our Contact Manager</h1>
                </header>

                <EventList events={this.state.events} />
            </div>
        );
    }
    // Tutorial part 2
    /*render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo" />
                    <h1 className="App-title">React Contact Manager</h1>
                </header>

                <EventList events={events} />
            </div>
        );
    }*/

}

export default App;