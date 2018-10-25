import axios from "axios";
import {HOST} from "../../Config";
import moment from "moment/moment";

export function getAll(username, responseFunc) {
    let url = HOST + "/user/debts/getAll/" + username;
    console.log('ksmdsds')
    axios.get(url)
        .then(responseFunc)
}

export function get(debtId, responseFunc) {
    let url = HOST + "/user/debts/get/" + debtId;

    axios.get(url)
        .then(responseFunc);
}


export function setDebts(username, debtor, sum, responseFunc) {
    let url = HOST + '/user/debts/set/' + username;
    let data = {debtor: debtor, sum: sum};

    axios.post(url, data)
        .then(responseFunc)

}

export function getAllLiab(username, responseFunc) {
    let url = HOST + "/user/debts/getAllLiab/" + username;
    axios.get(url)
        .then(responseFunc)
}

export function deleteClaims(debtsId, responseFunc) {
    let url = HOST + '/user/debts/delete/' + debtsId;
    console.log('debts/delete');
    axios.post(url)
        .then(responseFunc)
}