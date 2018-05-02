import React from "react"
import axios from "axios"

import {HOST, TOKEN} from "../../Config"

class Event extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            events: [],
        }
    }

    render() {

        return (
            <div>

            </div>
        );
    }
}

export default Event;