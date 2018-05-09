import React from "react";
import Appbar from "./Appbar";
import FullWidthTabs from "./EventContainer";
import BottomNavigationBar from "./BottomNavigationBar";
import axios from "axios"
import {TOKEN} from "../Config";
import {setAuthenticationHeader} from "./authentication/Authentication";

class LunchPlanner extends React.Component {

    constructor() {
        super();

        setAuthenticationHeader();
    }

    render() {
        return (
            <div>
                <Appbar />
                <FullWidthTabs />
                <BottomNavigationBar />
            </div>
        )
    }

}

export default LunchPlanner;