// App.js
import React from "react";
import Login from "./components/Login"
import Registration from "./components/Registration"
import LunchPlanner from "./components/LunchPlanner"
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import {isAuthenticated} from "./components/LoginFunctions"
import { MuiThemeProvider, createMuiTheme } from 'material-ui/styles';



const theme = createMuiTheme({
    palette: {
        primary: { main: "#75a045"}, // Purple and green play nicely together.
        secondary: { main: '#f29b26' }, // This is just green.A700 as hex.
    },
});

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
            <MuiThemeProvider theme={theme}>
                <Router>
                    <div>
                        <PrivateRoute exact path="/" component={LunchPlanner} />
                        <Route path="/login" component={Login} />
                        <Route path="/register" component={Registration} />
                    </div>
                </Router>
            </MuiThemeProvider>
        );
    }

}

export default App;