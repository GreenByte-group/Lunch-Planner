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
            <div>
                <Appbar currentScreen="Events"/>
                <EventContainer />
                <BottomNavigationBar />
            </div>
        )
    }

}

export default LunchPlanner;