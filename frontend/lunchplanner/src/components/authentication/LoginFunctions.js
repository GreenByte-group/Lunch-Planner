import axios from "axios";
import {HOST, TOKEN, USERNAME} from "../../Config";
import {sendTokenToServer} from "../notification/NotificationFunctions";

const authentication = {
    isAuthenticated: false,
}

export function isAuthenticated() {
    if(authentication.isAuthenticated)
        return true;
    else {
        if(localStorage.getItem(TOKEN))
            return true;
    }
}

export function getUsername() {
    return localStorage.getItem(USERNAME);
}

export function setAuthenticationHeader() {
    let token = localStorage.getItem(TOKEN);

    axios.interceptors.request.use(function(config) {
        if ( token != null ) {
            config.headers.Authorization = token;
        }

        return config;
    }, function(err) {
        return Promise.reject(err);
    });
}

export function resetAuthenticationHeader() {

    axios.interceptors.request.use(function(config) {
        config.headers.Authorization = null;

        return config;
    }, function(err) {
        return Promise.reject(err);
    });
}

export function register(username, mail, password, responseFunc, errorFunc) {
    let url =  HOST + '/user';
    console.log("PASSWORD: " + password);
    axios.post(url, {userName: username, password: password, mail: mail})
        .then(responseFunc)
        .catch(errorFunc)
}

export function doLogin(username, password, responseFunc) {
    if(username && password) {
        let url =  HOST + '/login?username=' + username +  '&password=' + password;
        axios.post(url)
            .then((response) => {
                localStorage.removeItem(TOKEN);
                localStorage.setItem(TOKEN, response.data.token );
                localStorage.setItem(USERNAME, username);
                setAuthenticationHeader();
                authentication.isAuthenticated = true;
                responseFunc({type: "LOGIN_SUCCESS", payload: response.data})
            })
            .catch((err) => {
                authentication.isAuthenticated = false;
                responseFunc({type: "LOGIN_FAILED", payload: err})
            })
    } else {
        responseFunc({
            type: "LOGIN_EMPTY",
            payload: {
                message: "Empty username or password.",
            }
        })
    }
}

export function doLogout() {
    sendTokenToServer("");
    localStorage.removeItem(TOKEN);
    localStorage.removeItem(USERNAME);
    authentication.isAuthenticated = false;
    resetAuthenticationHeader
    return {
        type: "IS_NOT_AUTHENTICATED",
        payload: ''
    }
}