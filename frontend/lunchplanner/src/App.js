// App.js
import React from "react";
import Login from "./components/Login"
import Registration from "./components/Registration"
import LunchPlanner from "./components/LunchPlanner"
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import {isAuthenticated} from "./components/LoginFunctions"

function isAuth() {
    return isAuthenticated();
}

const PrivateRoute = ({ component: Component, ...rest }) => (
    <Route{...rest} render={props =>
            isAuth() ? (<Component {...props} />)
                : (<Redirect to={{
                        pathname: "/login",
                        state: { from: props.location }}}/>)}/>
);

class App extends React.Component {

    render() {
        return (
            <Router>
                <div>
                    <PrivateRoute exact path="/" component={LunchPlanner} />
                    <Route path="/login" component={Login} />
                    <Route path="/register" component={Registration} />
                </div>
            </Router>
        );
    }

}

export default App;