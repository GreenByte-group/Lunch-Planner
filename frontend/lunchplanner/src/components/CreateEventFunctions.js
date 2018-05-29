import axios from "axios";
import {HOST} from "../Config";
import moment from "moment";

export function createEvent(location, date, member, visible, responseFunc, errorFunc) {
    //TODO send visibility and description
    let momentDate = moment(date);

    let timeEnd = date.getTime();
    let url =  HOST + '/event';
    axios.post(url, {name: location, description: "", location : location, timeStart: date, visible: visible})
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
            if(oneMember) {
                let url = HOST + "/event/" + oneMember + "/invite/event/" + eventId;
                axios.post(url)
                    .then((response) => {
                    })
            }
        });
    }
}
