import firebase from "firebase";

const config = {
    apiKey: "AIzaSyDyuySWwkXgZDrLnO0gX9bmGpR7XAHnngE",
    authDomain: "lunch-planner-ac676.firebaseapp.com",
    databaseURL: "https://lunch-planner-ac676.firebaseio.com",
    projectId: "lunch-planner-ac676",
    storageBucket: "lunch-planner-ac676.appspot.com",
    messagingSenderId: "573276863547"
};

// Retrieve Firebase Messaging object.
let messaging;

let token;

let permissionGranted = false;

/**
 *
 * @param success is called when the initializing process is succesfull finished
 * @param onMessage function is called when a notification is received
 * @param error with error description
 */
export function init(success, error, onMessage) {
    // Initialize Firebase
    firebase.initializeApp(config);

    messaging = firebase.messaging();

    // Add the public key generated from the console here.
    messaging.usePublicVapidKey("BFnHe7da5hcIYsv_Eno8pY6ws4wBWh7iqJAFTHAvWe4gZ8Qs59JBH9tL0YRI_IZOjzhaND0UY3DhAu5yeaWxn4c");

    // Callback fired if Instance ID token is updated.
    messaging.onTokenRefresh(() => {
        messaging.getToken().then((refreshedToken) => {
            console.log('Token refreshed.');
            // Send Instance ID token to app server.
            sendTokenToServer(refreshedToken);
        }).catch((err) => {
            console.log('Unable to retrieve refreshed token ', err);
        });
    });

    requestPermission(() => {
        messaging.onMessage((payload) => {
            console.log('Message received. ', payload);
            onMessage(payload);
        });
        success();
    }, error);
}

function requestPermission(response, error) {
    messaging.requestPermission().then(() => {
        console.log('Notification permission granted.');
        permissionGranted = true;
        getToken(response, error);
    }).catch((err) => {
        console.log('Unable to get permission to notify.', err);
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
            console.log('Token: ', currentToken);
            response(currentToken);
        } else {
            console.log('No Instance ID token available. Request permission to generate one.');
            token = false;
            permissionGranted = false;
            error('Permission denied');
        }
    }).catch((err) => {
        console.log('An error occurred while retrieving token. ', err);
        token = false;
        error('Could not retrieve token from fcm');
    });
}

function sendTokenToServer(tokenToSend) {
    token = false;
    //TODO send token to server

    token = tokenToSend;
}