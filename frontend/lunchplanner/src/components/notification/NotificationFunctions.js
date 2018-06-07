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