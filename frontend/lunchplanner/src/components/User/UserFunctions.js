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

export function getUser(username, responseFunc) {
    let url = HOST + "/user/" + username;
    axios.get(url)
        .then(responseFunc);
}