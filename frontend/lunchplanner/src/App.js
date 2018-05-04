import React from "react";
import FirstScreen, {setAuthenticationHeader} from "./components/authentication/Authentication"
import LunchPlanner from "./components/LunchPlanner"
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import {isAuthenticated} from "./components/authentication/LoginFunctions"
import { MuiThemeProvider, createMuiTheme } from 'material-ui/styles';
import CreateEventScreen from "./components/CreateEventScreen";
import SelectUserScreen from "./components/User/SelectUserScreen";

const theme = createMuiTheme({
    palette: {
        primary: {main: "#75a045"},
        secondary: {main: '#f29b26'},
    },

});

function isAuth() {
    return isAuthenticated();
}

export const PrivateRoute = ({ component: Component, ...rest }) => (
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
                        <Route exact path="/login" component={FirstScreen} />
                        <Route exact path="/"
                                      render={ () => <Redirect to="/event" />}
                        />

                        <PrivateRoute path="/event" component={LunchPlanner} />
                        <PrivateRoute path="/event/create" component={CreateEventScreen} />
                        <PrivateRoute path="/event/create/invite" component={SelectUserScreen} />
                    </div>
                </Router>
            </MuiThemeProvider>
        );
    }
}

export default App;