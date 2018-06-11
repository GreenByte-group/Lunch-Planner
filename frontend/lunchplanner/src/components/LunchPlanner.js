import React from "react";
import Appbar from "./Appbar";
import EventContainer from "./Event/EventContainer";
import BottomNavigationBar from "./BottomNavigationBar";
import {setAuthenticationHeader} from "./authentication/LoginFunctions";

class LunchPlanner extends React.Component {

    constructor() {
        super();
        this.state = {
            search: null,
        };
        this.handleSearch = this.handleSearch.bind(this);
        setAuthenticationHeader();
    }

    handleSearch(search){
        this.setState({
            search: search
        });
    }

    render() {
        return (
            <EventContainer style={{height: '100%'}} search={this.state.search}/>
        )
    }

}

export default LunchPlanner;
