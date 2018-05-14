import axios from "axios";
import {HOST} from "../Config";
import moment from "moment";

export function createEvent(location, date, member, visible, responseFunc, errorFunc) {
    //TODO send visibility
    let momentDate = moment(date);

    let timeEnd = date.getTime();
    let url =  HOST + '/event';
    axios.post(url, {name: location + ", " + momentDate.format('DD MMM, HH:mm'), description: "", location : location, timeStart: date})
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
