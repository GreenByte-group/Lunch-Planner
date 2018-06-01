import React from "react";
import FirstScreen, {setAuthenticationHeader} from "./components/authentication/Authentication"
import LunchPlanner from "./components/LunchPlanner"
import { Router, Route, Redirect } from "react-router-dom";
import {isAuthenticated} from "./components/authentication/LoginFunctions"
import { MuiThemeProvider } from 'material-ui/styles';
import { MuiThemeProvider as OldMuiThemeProvider } from 'material-ui-old/styles'
import { createMuiTheme } from 'material-ui/styles'
import CreateEventScreen from "./components/Event/CreateEventScreen";
import SelectUserScreen from "./components/User/SelectUserScreen";
import EventScreen from "./components/Event/EventScreen";
import ServiceListScreen from "./components/Event/ServiceList/ServiceListScreen";
import {getMuiTheme} from "material-ui-old/styles/index";
import SocialScreen from "./components/SocialScreen";
import LocationScreen from "./components/LocationScreen";
import Comments from "./components/Event/Comments/Comments";
import { createBrowserHistory as createHistory } from "history";
import {setHistory, getHistory} from "./utils/HistoryUtils";
import CreateTeamScreen from "./components/Team/CreateTeamScreen";
import {init} from './components/notification/Firebase'
import NotificationsScreen from "./components/notification/NotificationsScreen";
import TeamScreen from "./components/Team/TeamScreen";
import InviteExtern from "./components/User/InviteExtern";

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
    typography: {
        fontFamily: "Work Sans",
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

    constructor(props) {
        super();

        setHistory(createHistory(props));

        init(() => {
            console.log('notificaton: success');
        }, (error) => {
            console.log('notificaton: error: ', error);
        }, (message) => {
            console.log('notificaton: message: ', message);
        });
    }

    render() {
        return (
            <OldMuiThemeProvider theme={oldTheme}>
                <MuiThemeProvider theme={theme}>
                    <Router history={getHistory()}>
                        <div style={{height: '100%'}}>
                            <Route exact path="/login" component={FirstScreen} />
                            <Route exact path="/"
                                          render={ () => <Redirect to="/event" />}
                            />

                            <PrivateRoute path="/event" component={LunchPlanner} />
                            <PrivateRoute path="/social" component={SocialScreen} />
                            <PrivateRoute path="/team/create" component={CreateTeamScreen}/>
                            <PrivateRoute path="/team/create/invite" component={SelectUserScreen} />
                            <PrivateRoute path="/team/:teamId(\d+)" component={TeamScreen} />
                            <PrivateRoute path="/location" component={LocationScreen} />
                            <PrivateRoute path="/notifications" component={NotificationsScreen} />
                            <PrivateRoute path="/event/create" component={CreateEventScreen} />
                            <PrivateRoute path="/event/create/invite" component={SelectUserScreen} />
                            <PrivateRoute path="/event/:eventId(\d+)" component={EventScreen} />
                            <PrivateRoute path="/event/:eventId(\d+)/comments" component={Comments} />
                            <PrivateRoute path="/event/:eventId(\d+)/share" component={InviteExtern} />
                            <PrivateRoute path="/event/:eventId(\d+)/service" component={ServiceListScreen} />
                        </div>
                    </Router>
                </MuiThemeProvider>
            </OldMuiThemeProvider>
        );
    }
}

export default App;