// App.js
import React from "react";
import logo from "./logo.svg";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import Login from "./components/Login"
import Registration from "./components/Registration";

class App extends React.Component {

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