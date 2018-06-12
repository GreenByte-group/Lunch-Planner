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

export function inviteExtern(eventId, responseFunc) {
    let url = HOST + "/event/" + eventId +  "/token";
    axios.get(url)
        .then(responseFunc);
}

export function updateEmail(email, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + "/user/options/profile/mail";
    axios.put(url, email, config)
        .then(responseFunc)
        .catch(responseFunc);
}

export function updatePassword(password, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + "/user/options/profile/password";
    axios.put(url, password, config)
        .then(responseFunc)
        .catch(responseFunc);
}

export function updateProfilePicture(file, responseFunc) {
    let config = {
        headers:{
            'Content-Type':'multipart/form-data'
        }
    };

    const formData = new FormData();
    formData.append('file',file);

    let url = HOST + "/user/options/profile/picture/upload";
    axios.post(url, formData, config)
        .then(responseFunc)
        .catch(responseFunc);
}

export function getProfilePicturePath(username, responseFunc) {
    let url = HOST + "/user/getProfilePicture/" + username;
    axios.get(url)
        .then(responseFunc);
}