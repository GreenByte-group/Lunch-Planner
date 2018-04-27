// App.js
import React from "react";
import logo from "./logo.svg";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import Login from "./components/Login"
import Registration from "./components/Registration";

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
            <Router>
                <div className="App" class="container">
                    <header className="App-header">
                        <h1 className="App-title">Lunch Planner</h1>
                    </header>

                    <div class="container">
                        <Link to="/login">Login</Link>
                        <br/>
                        <Link to="/register">Registration</Link>

                        <Route path="/login" component={Login} />
                        <Route path="/register" component={Registration} />
                    </div>
                </div>
            </Router>
        );
    }

}

export default App;