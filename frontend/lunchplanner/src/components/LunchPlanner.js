import React from "react";
import Appbar from "./Appbar";
import FullWidthTabs from "./FullWidthTabs";
import BottomNavigationBar from "./BottomNavigationBar";
import axios from "axios"
import {TOKEN} from "../Config";

class LunchPlanner extends React.Component {

    constructor() {
        super();

        let token = localStorage.getItem(TOKEN);

        axios.interceptors.request.use(function(config) {
            if ( token != null ) {
                config.headers.Authorization = token;
            }

            return config;
        }, function(err) {
            return Promise.reject(err);
        });
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