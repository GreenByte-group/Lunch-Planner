import React from "react";
import Appbar from "./Appbar";
import EventContainer from "./EventContainer";
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
            <div style={{height: '100%', display: 'flex', flexDirection: 'column'}}>
                <Appbar currentScreen = "Events" onHandleSearch={this.handleSearch} searchValue={this.state.search}/>
                <EventContainer style={{height: '100%'}} search={this.state.search}/>
                <BottomNavigationBar />
            </div>
        )
    }

}

export default LunchPlanner;