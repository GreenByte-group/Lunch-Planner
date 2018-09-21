import axios from "axios";
import {HOST} from "../../Config";

export function getNotifications(responseFunc) {
    let url = HOST + "/user/notifications";

    axios.get(url)
        .then(responseFunc);
}

export function setNotificationRead(notificationId, responseFunc) {
    let url = HOST + "/user/notification/" + notificationId;

    axios.post(url)
        .then(responseFunc);
}

export function getNotificationOptions(responseFunc) {
    let url = HOST + "/user/options/notifications";

    axios.get(url)
        .then(responseFunc);
}

export function sendOptions({blockAll, events, teams,
                                subscriptions, blockedForWork,
                                startWorking, stopWorking, blockUntil},
                            responseFunc) {
    let data = {};
    if(blockAll !== undefined && blockAll !== null)
        data.blockAll = blockAll;

    if(events !== undefined && events !== null)
        data.eventsBlocked = !events;

    if(teams !== undefined && teams !== null)
        data.teamsBlocked = !teams;

    if(subscriptions !== undefined && subscriptions !== null)
        data.subscriptionsBlocked = !subscriptions;

    if(blockedForWork !== undefined && blockedForWork !== null)
        data.blockedForWork = !blockedForWork;

    if(startWorking !== undefined && startWorking !== null)
        data.start_working = startWorking;

    if(stopWorking !== undefined && stopWorking !== null)
        data.stop_working = stopWorking;

    if(blockUntil !== undefined && blockUntil !== null)
        data.block_until = blockUntil;

    console.log("start: ", startWorking);

    if(Object.keys(data).length === 0)
        return;

    let url = HOST + "/user/options/notifications/update";

    axios.put(url, data)
        .then(responseFunc)
}

export function sendTokenToServer(tokenToSend) {
    let url = HOST + "/user/fcm";
    console.log("/USER/FCM = FRONTEND");
    axios.post(url, {fcmToken: tokenToSend})
        .then((response) => {
        }).catch((error) => {
    })
}