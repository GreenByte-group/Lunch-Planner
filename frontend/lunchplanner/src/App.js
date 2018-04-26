// App.js
import React from "react";
import logo from "./logo.svg";
import "./App.css";
import axios from "axios";

import EventList from "./components/EventList"

class App extends React.Component {

    // default State object
    state = {
        events: []
    };

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

}

export default App;