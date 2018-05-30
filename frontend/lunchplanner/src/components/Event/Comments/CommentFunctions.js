import axios from 'axios';
import {HOST} from "../../../Config";

export function loadComments(eventId, responseFunc) {
    let url = HOST + "/event/" + eventId + "/getComments";

    axios.get(url)
        .then(responseFunc)
}

export function sendComment(eventId, comment, responseFunc) {
    let url = HOST + "/event/" + eventId + "/comment";

    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    axios.put(url, comment, config)
        .then(responseFunc);
}