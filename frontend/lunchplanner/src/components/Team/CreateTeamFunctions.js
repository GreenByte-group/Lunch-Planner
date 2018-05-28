import axios from "axios";
import {HOST} from "../../Config";

export function createTeam(name, description, member, visible, responseFunc, errorFunc) {
    //TODO send visibility and description
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
function inviteMember(teamId, member) {
    let string = String(member);
    if(string !== "") {
        (string.split(',')).forEach((oneMember) => {
            let url = HOST + "/team/" + oneMember + "/invite/team/" + teamId;
            console.log(url);
            axios.post(url)
                .then((response) => {
                    console.log(response);
                })
        });
    }
}
