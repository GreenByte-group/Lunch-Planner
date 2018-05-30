import axios from "axios";
import {HOST} from "../../../Config";
import {getHistory} from "../../../utils/HistoryUtils";
import {serviceListNeedReload} from "./ServiceList";

export function getServices(eventId, responseFunc) {
    let url = HOST + "/event/" + eventId + "/service";
    axios.get(url)
        .then(responseFunc)
}

export function acceptService(eventId, serviceId, responseFunc) {
    let url = HOST + "/event/" + this.state.eventId + "/service/" + this.state.serviceId;
    axios.post(url)
        .then(responseFunc)
}

export function createService(eventId, food, description, responseFunc, errorFunc) {
    let url = HOST + "/event/" + this.state.eventId + "/service";
    axios.put(url, {food: this.state.food, description: this.state.description})
        .then(responseFunc)
        .catch(errorFunc)
}