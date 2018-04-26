import React from "react";
import { HOST } from "../Config";
import axios from "axios";
import {doLogin} from "./LoginFunctions";

class Registration extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            email: "",
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        const name = target.name;

        this.setState({
            [name]: target.value
        });
    }

    handleSubmit(event) {
        if(this.state.username && this.state.password && this.state.email) {
            let url =  'http://localhost:8080/user';
            axios.post(url, {userName: this.state.username, password: this.state.password, mail: this.state.email})
                .then((response) => {
                    console.log("Respone: "+ response);
                })
                .catch((err) => {
                    console.log("Error: " + err);
                })
        } else {
            //TODO
        }

        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                User Name : <input type="text" name="username" onChange={this.handleInputChange}/> <br/><br/>
                Password: <input type="password" name="password" onChange={this.handleInputChange}/> <br/><br/>
                Email: <input type="text" name="email" onChange={this.handleInputChange}/> <br/><br/>
                <input type="submit" value="Login"/>
            </form>
        )
    }
}

export default Registration;