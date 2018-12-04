import axios from "axios";
import {HOST} from "../../Config";

let config = {
    headers: {
        'Content-Type': 'text/plain',
    }
};

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

export function createTeam(name, description, member, picture, visible, responseFunc, errorFunc) {
    createTeamWithParent(name,description, null, picture, member, visible, responseFunc, errorFunc);
}

export function createTeamWithParent(name, description, parent, picture, member, visible, responseFunc, errorFunc) {
    let url =  HOST + '/team';
    let data;
    if(parent !== null) {
        data = {teamName: name, description: description, visible: visible, parent: parent, picture: picture};
    } else {
        data = {teamName: name, description: description, visible: visible, picture: picture}
    }

    axios.post(url, data)
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
export function changePicture(teamId, picture){
    let url = HOST + "/team" + picture + "/" + teamId;

    axios.post(url)
        .then((response) => {});
}

export function removeUserFromTeam(teamdId, username, responseFunc) {
    let url = HOST + '/team/' + username + "/team/" + teamdId + "/remove";
    axios.delete(url)
        .then(responseFunc);
}

export function replyToTeam(teamId, answer, responseFunc) {
    let url = HOST + '/team/' + teamId + '/reply';
    axios.put(url, answer, config)
        .then(responseFunc);
}

export function changeTeamName(teamId, teamName, responseFunc, errorFunc){
    let url = HOST + '/team/' + teamId + '/name';
    axios.post(url, teamName, config)
        .then(responseFunc)
        .catch(errorFunc);
}

export function changeTeamDescription(teamId, teamDescription, responseFunc, errorFunc){
    let url = HOST + '/team/' + teamId + '/description';
    axios.post(url, teamDescription, config)
        .then(responseFunc)
        .catch(errorFunc);
}

//TODO
export function changeProfilePicture(teamID, profilePicture, responseFunc, errorFunc){

}

export function inviteMemberToTeam(teamId, member, responseFunc, errorFunc) {
    member.forEach((oneMember) => {
        let url = HOST + "/team/" + oneMember + "/invite/team/" + teamId;
        axios.post(url)
            .then((response) => {
                if(response.status === 201)
                    responseFunc(oneMember)
            })
    });
}

export function leaveTeam(teamId, responseFunc, errorFunc){
    let url = HOST + '/team/' + teamId + '/leave';
    axios.delete(url, config)
        .then(responseFunc)
        .catch(errorFunc);
}

export function joinTeam(teamId, responseFunc, errorFunc){
    let url = HOST + '/team/' + teamId + '/join';
    axios.post(url, config)
        .then(responseFunc)
        .catch(errorFunc);
}