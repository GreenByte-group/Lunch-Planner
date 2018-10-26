import axios from "axios";
import {HOST} from "../../../Config";
import {getHistory} from "../../../utils/HistoryUtils";
import {serviceListNeedReload} from "./ServiceList";

export function getServices(eventId, responseFunc) {
    let url = HOST + "/event/" + eventId + "/service";
    axios.get(url)
        .then(responseFunc)
}

export function acceptService(eventId, serviceId, price, responseFunc) {
    let url = HOST + "/event/" + eventId + "/service/" + serviceId + "/" + price;
    axios.post(url)
        .then(responseFunc)
}

export function createService(eventId, food, description, responseFunc, errorFunc) {
    let url = HOST + "/event/" + eventId + "/service";
    axios.put(url, {food: food, description: description})
        .then(responseFunc)
        .catch(errorFunc)
}