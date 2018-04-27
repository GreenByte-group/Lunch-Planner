import axios from "axios";

export const authentication = {
    isAuthenticated: true, //TODO change
}

export function doLogin(username, password, responseFunc) {
    if(username && password) {
        let url =  'http://localhost:8080/login?username=' + username +  '&password=' + password;
        axios.post(url)
            .then((response) => {
                localStorage.removeItem( "token" );
                localStorage.setItem( "token", response.data.token );
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
    localStorage.removeItem( "token" )
    authentication.isAuthenticated = false;
    return {
        type: "IS_NOT_AUTHENTICATED",
        payload: ''
    }
}