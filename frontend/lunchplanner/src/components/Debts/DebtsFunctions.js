import axios from "axios";
import {HOST} from "../../Config";
import moment from "moment/moment";

export function getAll(username, responseFunc, errorFunc) {
    let url = HOST + "/user/debts/getAll/" + username;

    axios.get(url)
        .then((responseFunc) => {
            console.log('response', responseFunc);
        })
        .catch((errorFunc) => {
                console.log('error', errorFunc)
            });
}

export function get(debtId, responseFunc, errorFunc) {
    let url = HOST + "/user/debts/get/" + debtId;

    axios.get(url)
        .then(responseFunc)
        .catch(errorFunc);

}


export function setDebts(username, debtor, sum, responseFunc, errorFunc) {
    let url = HOST + '/user/debts/set/' + username;
    let data = {debtor: debtor, sum: sum};

    axios.post(url, data)
        .then(responseFunc)
        .catch(errorFunc);

}

export function getAllLiab(username, responseFunc, errorFunc) {
    let url = HOST + "/user/debts/getAllLiab/" + username;

    axios.get(url)
        .then(responseFunc)
        .catch(errorFunc);
}

export function deleteClaims(debtsId, responseFunc, errorFunc) {
    let url = HOST + '/user/debts/delete/' + debtsId;

    axios.post(url)
        .then(responseFunc)
        .catch(errorFunc);
}