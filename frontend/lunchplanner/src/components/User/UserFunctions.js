import axios from "axios";
import {HOST} from "../../Config";
import {doLogout} from "../authentication/LoginFunctions"
import {getHistory} from "../../utils/HistoryUtils";

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

export function deleteAccount(username, responseFunc){
    let url = HOST + "/user/delete/" + username;
    axios.post(url)
        .then(responseFunc)
        .catch(responseFunc);
    doLogout();
    getHistory().push("/login")
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
    console.log("Formdata: " + String(formData))
    console.log("URL: " +String(url))
    console.log("config: "+String(config))
    axios.post(url, formData, config)
        .then(responseFunc)
        .catch(responseFunc);
}

export function getProfilePicturePath(username, responseFunc) {
    let url = HOST + "/user/getProfilePicture/" + username;
    console.log("name: "+String(username)+", host: "+ String(HOST)+"func: "+String(responseFunc));
    axios.get(url)
        .then(responseFunc);
}

export function subscribe(username, location, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + "/user/subscribe/" + username;
    console.log('UsrFunction => subscribe', username, location);
    axios.post(url, location, config)
        .then(responseFunc);
}

export function unsubscribe(username, location, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + "/user/subscribe/delete/" + username;
    console.log('UsrFunction => unsubscribe', username, location);
    axios.post(url, location, config)
        .then(responseFunc);
}

export function getSubscribedLocations(username, responseFunc) {
    console.log('UsrFunction => getSubribedLocation', username);
    let url = HOST + "/user/subscribe/" + username;
    axios.get(url)
        .then(responseFunc);
}