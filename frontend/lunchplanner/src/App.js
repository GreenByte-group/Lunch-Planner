import React from "react";
import FirstScreen from "./components/authentication/Authentication"
import LunchPlanner from "./components/LunchPlanner"
import { Router, Route, Redirect } from "react-router-dom";
import {isAuthenticated} from "./components/authentication/LoginFunctions"
import { MuiThemeProvider } from '@material-ui/core/styles';
import { MuiThemeProvider as OldMuiThemeProvider } from 'material-ui-old/styles'
import { createMuiTheme } from '@material-ui/core/styles'
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
import AppContainer from "./components/AppContainer";

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
            let notificationTitle = message.data.title;
            let notificationOptions = {
                body: message.data.body,
                icon: message.data.icon,
                tag: message.data.tag,
                click_action: message.data.tag,
            };

            let notification = new Notification(notificationTitle, notificationOptions);
            notification.addEventListener('click', (event) => {
                console.log('click', event);
                getHistory().push(event.target.tag);
            })
        });
    }

    render() {
        return (
            <OldMuiThemeProvider theme={oldTheme}>
                <MuiThemeProvider theme={theme}>
                    <Router history={getHistory()}>
                        <div style={{height: '100%'}}>
                            <Route exact path="/login" component={FirstScreen} />
                            <Route path="/public/:securityToken" component={EventScreen} />
                            <Route exact path="/"
                                          render={ () => <Redirect to="/app/event" />}
                            />
                            <PrivateRoute path="/app/:component" component={AppContainer} />
                            {/*<PrivateRoute path="/event" component={LunchPlanner} />*/}
                            {/*<PrivateRoute path="/social" component={SocialScreen} />*/}

                            {/*<PrivateRoute path="/location" component={LocationScreen} />*/}
                            {/*<PrivateRoute path="/notifications" component={NotificationsScreen} />*/}
                            <PrivateRoute path="/app/team/create" component={CreateTeamScreen}/>
                            <PrivateRoute path="/app/team/create/invite" component={SelectUserScreen} />
                            <PrivateRoute path="/app/team/:teamId(\d+)" component={TeamScreen} />
                            <PrivateRoute path="/app/event/create" component={CreateEventScreen} />
                            <PrivateRoute path="/app/event/create/invite" component={SelectUserScreen} />
                            <PrivateRoute path="/app/event/:eventId(\d+)" component={EventScreen} />
                            <PrivateRoute path="/app/event/:eventId(\d+)/comments" component={Comments} />
                            <PrivateRoute path="/app/event/:eventId(\d+)/share" component={InviteExtern} />
                            <PrivateRoute path="/app/event/:eventId(\d+)/service" component={ServiceListScreen} />
                        </div>
                    </Router>
                </MuiThemeProvider>
            </OldMuiThemeProvider>
        );
    }
}

export default App;