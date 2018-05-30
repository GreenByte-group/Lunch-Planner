import axios from "axios";
import {HOST} from "../../Config";

export function getEvents(search, responseFunc) {
    let url = HOST + "/event";
    if(search)
        url = HOST + "/event/search/" + search;

    axios.get(url)
        .then(responseFunc);
}

export function getEvent(eventId, responseFunc) {
    let url = HOST + "/event/" + eventId;

    axios.get(url)
        .then(responseFunc);
}

export function replyToEvent(eventId, answer, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/reply';
    axios.put(url, answer, config)
        .then(responseFunc)
}

export function changeEventTitle(eventId, title, responseFunc, errorFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/name';
    axios.put(url, title, config)
        .then(responseFunc)
        .catch(errorFunc)
}

export function changeEventLocation(eventId, location, responseFunc, errorFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/location';
    axios.put(url, location, config)
        .then(responseFunc)
        .catch(errorFunc)
}

export function changeEventTime(eventId, time, responseFunc, errorFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/timestart';
    axios.put(url, time.valueOf(), config)
        .then(responseFunc)
        .catch(errorFunc)
}

export function inviteMemberToEvent(eventId, member, responseFunc, errorFunc) {
    member.forEach((oneMember) => {
        let url = HOST + "/event/" + oneMember + "/invite/event/" + eventId;
        axios.post(url)
            .then((response) => {
                if(response.status === 201)
                    responseFunc(oneMember)
            })
    });
}