import axios from "axios";

export function isAuthenticated() {
    return function(dispatch) {
        let url =  'http://localhost:8080/login'
        var config = {
            headers: {'Authorization': + localStorage.getItem( "token" )}
        };

        axios.get(url, config)
            .then((response) => {
                dispatch({type: "IS_AUTHENTICATED", payload: response.data})
            })
            .catch((err) => {
                dispatch({type: "IS_NOT_AUTHENTICATED", payload: ''})
            })
    }
}

export function doLogin(username, password, responseFunc) {
    if(username && password) {
        let url =  'http://localhost:8080/login?username=' + username +  '&password=' + password;
        axios.post(url)
            .then((response) => {
                console.log("Login token: " + response.data.token);
                localStorage.removeItem( "token" );
                localStorage.setItem( "token", response.data.token );
                responseFunc({type: "LOGIN_SUCCESS", payload: response.data})
            })
            .catch((err) => {
                console.log("Error: " + err);
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
    localStorage.removeItem( "token" )
    return {
        type: "IS_NOT_AUTHENTICATED",
        payload: ''
    }
}