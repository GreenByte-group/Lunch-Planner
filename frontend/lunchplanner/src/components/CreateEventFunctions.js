import axios from "axios";
import {HOST} from "../Config";

export function createEvent(locationId, date) {
    console.log("Date in createEvent" + date);
    let url =  HOST + '/event';
        axios.post(url, {name: "dummy",locationId : locationId, timeStart: date})
            .then((response) => {
                console.log("Event created " + response.data);
            })
            .catch((err) => {
               console.log(err)
            })

}
