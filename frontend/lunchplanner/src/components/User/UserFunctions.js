import axios from "axios";
import {HOST} from "../../Config";

export function getUsers(search, responseFunc) {
    let url;
    if(search == null || search === undefined || search === "")
        url = HOST + "/user";
    else
        url = HOST + "/user/search/" + search;

    axios.get(url)
        .then(responseFunc);
}

export function inviteExtern(eventId, responseFunc) {
    console.log("inviteExtern")
    let url = HOST + "/event/" + eventId +  "/token";
    axios.get(url)
        .then(responseFunc);
}