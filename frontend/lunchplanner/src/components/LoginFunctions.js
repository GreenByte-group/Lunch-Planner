import axios from "axios";
import {HOST, TOKEN} from "../Config";

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

export function doLogin(username, password, responseFunc) {
    if(username && password) {
        let url =  HOST + '/login?username=' + username +  '&password=' + password;
        axios.post(url)
            .then((response) => {
                console.log("Token: " + response.data.token);
                localStorage.removeItem(TOKEN);
                localStorage.setItem(TOKEN, response.data.token );
                axios.defaults.headers.common['Authorization'] = response.data.token;
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
    localStorage.removeItem(TOKEN)
    authentication.isAuthenticated = false;
    return {
        type: "IS_NOT_AUTHENTICATED",
        payload: ''
    }
}