import axios from "axios";
import {HOST} from "../Config";

export function createEvent(locationId, date, member, visible, responseFunc, errorFunc) {
    let url =  HOST + '/event';
        axios.post(url, {name: "dummy", description: "", locationId : locationId, timeStart: date, timeEnd: date+1})
            .then(responseFunc)
            .catch(errorFunc);
}
