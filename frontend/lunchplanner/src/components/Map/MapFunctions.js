import axios from "axios";
import {HOST} from "../../Config";

export function getCoordinates(eventId, responseFunc) {
    let url = HOST + "/event/" + eventId + "/getCoordinates";

    axios.get(url)
        .then(responseFunc);
}