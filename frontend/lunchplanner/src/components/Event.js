import React from "react";
//import PropTypes from "prop-types";

function Event(props) {
    return (
        <div className="event">
            <span>{props.name}</span>
        </div>
    );
}

// Typüberprüfung
//Event.propTypes = {
//    name: PropTypes.string.isRequired
//};

export default Event;