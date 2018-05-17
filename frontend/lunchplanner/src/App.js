import React from "react";
import FirstScreen, {setAuthenticationHeader} from "./components/authentication/Authentication"
import LunchPlanner from "./components/LunchPlanner"
import { BrowserRouter as Router, Route, Redirect } from "react-router-dom";
import {isAuthenticated} from "./components/authentication/LoginFunctions"
import { MuiThemeProvider } from 'material-ui/styles';
import { MuiThemeProvider as OldMuiThemeProvider } from 'material-ui-old/styles'
import { createMuiTheme } from 'material-ui/styles'
import CreateEventScreen from "./components/CreateEventScreen";
import SelectUserScreen from "./components/User/SelectUserScreen";
import {getMuiTheme} from "material-ui-old/styles/index";
import SimpleMap from "./components/GoogleMap/SimpleMap";
import GoogleMapsContainer from "./components/GoogleMap/GoogleMapsContainer";

const oldTheme = getMuiTheme({
    palette: {
        primary1Color: '#75a045',
        accent1Color: '#f29b26',
    },
});

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
            <OldMuiThemeProvider theme={oldTheme}>
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
                            <PrivateRoute path="/event/create/map" component={SimpleMap}/>
                        </div>
                    </Router>
                </MuiThemeProvider>
            </OldMuiThemeProvider>

        );
    }
}

export default App;