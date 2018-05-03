import axios from "axios";
import {HOST, TOKEN} from "../Config";

export function createEvent(eventname, locationId, date, time, member, visible) {
    console.log("Date" + date);
    //console.log("Date" + date.getTime());
    console.log("Time" + time);
    //console.log("time" + time.getTime());
    let url =  HOST + '/event';
        axios.post(url, {eventname, locationId, date, time, member, visible})
            .then((response) => {
                console.log("Event created " + response.data);
            })
            .catch((err) => {
               console.log(err)
            })

}
