import axios from "axios";
import {HOST} from "../../Config";

export function getTeams(search, responseFunc) {
    let url;
    if(search)
        url = HOST + "/team/search/" + search;
    else
        url = HOST + "/team";

    axios.get(url)
        .then(responseFunc)
}

export function getTeam(teamId, responseFunc) {
    let url = HOST + "/team/" + teamId;

    axios.get(url)
        .then(responseFunc);
}

export function createTeam(name, description, member, visible, responseFunc, errorFunc) {
    //TODO send description
    let url =  HOST + '/team';
    axios.post(url, {teamName: name, description: description})
        .then((response) => {
            if(response.status === 201) {
                inviteMember(response.data, member);
            }
            responseFunc(response);
        })
        .catch(errorFunc);
}

// send invitations
export function inviteMember(teamId, member) {
    let string = String(member);
    if(string !== "") {
        (string.split(',')).forEach((oneMember) => {
            let url = HOST + "/team/" + oneMember + "/invite/team/" + teamId;
            axios.post(url)
                .then((response) => {
                })
        });
    }
}

export function replyToTeam(teamId, answer, responseFunc) {
    let config = {
        headers: {
            'Content-Type': 'text/plain',
        }
    };

    let url = HOST + '/team/' + teamId + '/reply';
    axios.put(url, answer, config)
        .then(responseFunc);
}