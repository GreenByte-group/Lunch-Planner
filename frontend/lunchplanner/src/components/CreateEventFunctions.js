import axios from "axios";
import {HOST} from "../Config";

export function createEvent(locationId, date, member, visible, responseFunc, errorFunc) {
    let timeEnd = date.getTime();
    timeEnd = timeEnd + 1;
    let url =  HOST + '/event';
    axios.post(url, {name: "dummy", description: "", locationId : locationId, timeStart: date, timeEnd: timeEnd})
        .then((response) => {
            if(response.status === 201) {
                inviteMember(response.data, member);
            }
            responseFunc(response);
        })
        .catch(errorFunc);
}

// send invitations
function inviteMember(eventId, member) {
    let string = String(member);
    if(string !== "") {
        (string.split(',')).forEach((oneMember) => {
            let url = HOST + "/event/" + oneMember + "/invite/event/" + eventId;
            console.log(url);
            axios.post(url)
                .then((response) => {
                    console.log(response);
                })
        });
    }
}
