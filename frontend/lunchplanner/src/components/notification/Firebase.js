import * as firebase from "firebase";
import {setAuthenticationHeader} from "../authentication/LoginFunctions";
import {sendTokenToServer} from "./NotificationFunctions";
import {configFirebase, publicKey} from '../../Config';

// Retrieve Firebase Messaging object.
let messaging;

let permissionGranted = false;

/**
 *
 * @param success is called when the initializing process is succesfull finished
 * @param onMessage function is called when a notification is received
 * @param error with error description
 */
export function init(success, error, onMessage) {
    setAuthenticationHeader();

    // Initialize Firebase
    firebase.initializeApp(configFirebase);

    messaging = firebase.messaging();

    // Add the public key generated from the console here.
    messaging.usePublicVapidKey(publicKey);

    // Callback fired if Instance ID token is updated.
    messaging.onTokenRefresh(() => {
        messaging.getToken().then((refreshedToken) => {
            // Send Instance ID token to app server.
            sendTokenToServer(refreshedToken);
        }).catch((err) => {
        });
    });

    requestPermission(() => {
        messaging.onMessage((payload) => {
            onMessage(payload);
        });
        success();
    }, error);
}

function requestPermission(response, error) {
    messaging.requestPermission().then(() => {
        permissionGranted = true;
        getToken(response, error);
    }).catch((err) => {
        permissionGranted = false;
        error('Permission denied');
    });
}

function getToken(response, error) {
    // Get Instance ID token. Initially this makes a network call, once retrieved
    // subsequent calls to getToken will return from cache.
    messaging.getToken().then((currentToken) => {
        if (currentToken) {
            sendTokenToServer(currentToken);
            response(currentToken);
        } else {
            permissionGranted = false;
            error('Permission denied');
        }
    }).catch((err) => {
        console.log(err);
        error('Could not retrieve token from fcm');
    });
}