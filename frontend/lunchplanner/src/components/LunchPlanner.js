import React from "react";
import Appbar from "./Appbar";
import EventContainer from "./EventContainer";
import BottomNavigationBar from "./BottomNavigationBar";
import {setAuthenticationHeader} from "./authentication/Authentication";

class LunchPlanner extends React.Component {

    constructor() {
        super();

        setAuthenticationHeader();
    }

    render() {
        return (
            <div style={{height: '100%', display: 'flex', flexDirection: 'column'}}>
                <Appbar currentScreen = "Events" />
                <EventContainer style={{height: '100%'}} />
                <BottomNavigationBar />
            </div>
        )
    }

}

export default LunchPlanner;