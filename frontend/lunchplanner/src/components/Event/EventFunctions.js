import axios from "axios";
import {HOST} from "../../Config";
import moment from "moment/moment";
import {getUsername} from "../authentication/LoginFunctions";

/**
 * {
 *  eventId: int,
 *  startDate: date,
 *  eventName: string,
 *  eventDescription: string (unused),
 *  location: string,
 *  shareToken: string or null,
 *  comments: [],
 *  bringServices: [],
 *  invitations: {
 *      userName: string,
 *      answer: 0, 1 ,2,
 *      admin: boolean
 *  },
 *  public: boolean,
 * }
 *
 * @type {Array}
 */
let events = [];

function findIndexOfEvent(eventId) {
    let indexEvent = null;
    events.forEach((value, index) => {
        if(value.eventId === eventId)
            indexEvent = index;
    });

    return indexEvent;
}

function findIndexOfInvitation(event, username) {
    let indexUser = null;
    let invitations = event.invitations;
    if(invitations) {
        invitations.forEach((invitation, index) => {
            if(invitation.userName === username) {
                indexUser = index;
            }
        })
    }

    return indexUser;
}

/**
 * Event enthält:
 * {
 *  eventId: int,
 *  startDate: date,
 *  eventName: string,
 *  eventDescription: string (unused),
 *  location: string,
 *  shareToken: string or null,
 *  invitations: {
 *      userName: string,
 *      answer: 0, 1 ,2,
 *      admin: boolean
 *  },
 *  public: boolean,
 * }
 */
function updateEvent(event) {
    let indexInEvents = findIndexOfEvent(event.eventId);
    if(indexInEvents !== null) {
        events[indexInEvents].eventId = event.eventId;
        events[indexInEvents].startDate = event.startDate;
        events[indexInEvents].eventName = event.eventName;
        events[indexInEvents].eventDescription = event.eventDescription;
        events[indexInEvents].location = event.location;
        events[indexInEvents].shareToken = event.shareToken;
        events[indexInEvents].invitations = event.invitations;
        events[indexInEvents].public = event.public;
    } else {
        events.push(event);
    }
}

function unauthorized() {
    //TODO unauthoriziert
}

/**
 * Liefert:
 * {
 *  eventId: int,
 *  startDate: date,
 *  eventName: string,
 *  eventDescription: string (unused),
 *  location: string,
 *  shareToken: string or null,
 *  invitations: {
 *      userName: string,
 *      answer: 0, 1 ,2,
 *      admin: boolean
 *  },
 *  public: boolean,
 * }
 *
 * @param search
 * @param responseFunc
 */
export function getEvents(search, responseFunc) {
    let url = HOST + "/event";
    if(search)
        url = HOST + "/event/search/" + search;

    axios.get(url)
        .then((response) => {
            if(response.status === 200) {
                let arrayEvent = response.data;
                arrayEvent.forEach(updateEvent);
            } else if(response.status === 401) {
                unauthorized();
            }

            //TODO irgendwann entfernen
            responseFunc(response);
        });
}

export function getEvent(eventId, responseFunc) {
    let url = HOST + "/event/" + eventId;

    axios.get(url)
        .then((response) => {
            if(response.status === 200) {
                let arrayEvent = response.data;
                arrayEvent.forEach(updateEvent);
            } else if(response.status === 401) {
                unauthorized();
            }

            //TODO irgendwann entfernen
            responseFunc(response);
        });
}

export function replyToEvent(eventId, answer, responseFunc) {
    // Write changes into events

    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/reply';
    axios.put(url, answer, config)
        .then((response) => {
            if(response.status === 204) {
                let indexInEvents = findIndexOfEvent(eventId);
                if(indexInEvents !== null) {
                    let indexUser = findIndexOfInvitation(events[indexInEvents], getUsername());
                    if (answer === 'reject') {
                        events[indexInEvents].invitations[indexUser].answer = 1;
                    } else if (answer === 'accept') {
                        events[indexInEvents].invitations[indexUser].answer = 0;
                    }
                }
            } else if(response.status === 401) {
                unauthorized();
            }

            //TODO irgendwann entfernen
            responseFunc(response);
        })
}

export function changeEventTitle(eventId, title, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/name';
    axios.put(url, title, config)
        .then((response) => {
            if(response.status === 204) {
                let indexInEvents = findIndexOfEvent(eventId);
                if(indexInEvents !== null) {
                    events[indexInEvents].eventName = title;
                }
            } else if(response.status === 401) {
                unauthorized();
            }

            //TODO irgendwann entfernen
            responseFunc(response);
        });
}

export function changeEventLocation(eventId, location, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/location';
    axios.put(url, location, config)
        .then((response) => {
            if(response.status === 204) {
                let indexInEvents = findIndexOfEvent(eventId);
                if(indexInEvents !== null) {
                    events[indexInEvents].location = location;
                }
            } else if(response.status === 401) {
                unauthorized();
            }

            //TODO irgendwann entfernen
            responseFunc(response);
        });
}

export function changeEventTime(eventId, time, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/event/' + eventId + '/timestart';
    axios.put(url, time.valueOf(), config)
        .then((response) => {
            if(response.status === 204) {
                let indexInEvents = findIndexOfEvent(eventId);
                if(indexInEvents !== null) {
                    events[indexInEvents].startDate = time;
                }
            } else if(response.status === 401) {
                unauthorized();
            }

            //TODO irgendwann entfernen
            responseFunc(response);
        });
}

export function createEvent(location, date, member, visible, responseFunc, errorFunc) {
    //TODO description
    let momentDate = moment(date);

    let timeEnd = date.getTime();
    let url =  HOST + '/event';
    axios.post(url, {name: location, description: "", location : location, timeStart: date, visible: visible})
        .then((response) => {
            if(response.status === 201) {
                let eventId = response.data;
                events.push({
                    eventId: eventId,
                    startDate: date,
                    eventName: location,
                    eventDescription: "",
                    location: location,
                    shareToken: null,
                    comments: [],
                    bringServices: [],
                    invitations: [{
                        //TODO invitations
                    }],
                    public: visible,
                });

                inviteMemberToEvent(response.data, member);
            }
            responseFunc(response);
        })
        .catch(errorFunc);
}

export function inviteMemberToEvent(eventId, member, responseFunc) {
    if(member && member instanceof Array)

    member.forEach((oneMember) => {
        let url = HOST + "/event/" + oneMember + "/invite/event/" + eventId;
        axios.post(url)
            .then((response) => {
                if(response.status === 201)
                    if(responseFunc)
                        responseFunc(oneMember)
            })
    });
}

export function getEventExtern(token,responseFunc) {
    let url = HOST + "/event/token/" + token;
    axios.get(url)
        .then(responseFunc)
}