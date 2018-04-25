import React from "react";

import Event from "./Event"

function EventList(props) {
    return (
        <div>
            {props.events.map(e => <Event key={e.id} name={e.name} />)}
        </div>
    );
}

export default EventList;