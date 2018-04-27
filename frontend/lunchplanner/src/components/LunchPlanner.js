import React from "react";
import Appbar from "./Appbar";
import FullWidthTabs from "./FullWidthTabs";
import BottomNavigationBar from "./BottomNavigationBar";

class LunchPlanner extends React.Component {

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