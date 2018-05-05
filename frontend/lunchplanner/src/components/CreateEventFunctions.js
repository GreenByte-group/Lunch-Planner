import axios from "axios";
import {HOST} from "../Config";

export function createEvent(locationId, date, member, visible, responseFunc, errorFunc) {
    let timeEnd = date.getTime();
    timeEnd = timeEnd + 1;
    let url =  HOST + '/event';
        axios.post(url, {name: "dummy", description: "", locationId : locationId, timeStart: date, timeEnd: timeEnd})
            .then(responseFunc)
            .catch(errorFunc);
}
