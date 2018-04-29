// App.js
import React from "react";
import Login from "./components/MyLogin"
import Registration from "./components/Registration"
import FirstScreen from "./components/FirstScreen"
import LunchPlanner from "./components/LunchPlanner"
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import {isAuthenticated} from "./components/LoginFunctions"
import { MuiThemeProvider, createMuiTheme } from 'material-ui/styles';

/*
TODO:
- Logik Registration und MyRegistration verbinden
- Logik Login und MyLogin verbinden
- Login/Register Button unten fixieren, Schrift weiß
- Events Button rechte Seite
- Events Login/Register nicht anzeigen
 */


const theme = createMuiTheme({
    palette: {
        primary: {main: "#75a045"},
        secondary: {main: '#f29b26'},
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
                <FirstScreen/>
                <Router>
                    <div>
                        <PrivateRoute exact path="/" component={LunchPlanner} />

                    </div>
                </Router>
            </MuiThemeProvider>
        );
    }
    /*
    <Route path="/login" component={Login} />
     <Route path="/register" component={Registration} />
     */

}

export default App;