import React from 'react';
import CreateEventScreen from "./CreateEventScreen";
import ReactDOM from 'react-dom';

export function createEvent() {
    console.log("createEvent");
    ReactDOM.render(<CreateEventScreen />);
}