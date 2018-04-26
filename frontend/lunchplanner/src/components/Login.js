import React from "react";
import { HOST } from "../Config";
import axios from "axios";
import {doLogin} from "./LoginFunctions";

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
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
        console.log("Submit");
        let returnValue = doLogin(this.state.username, this.state.password, message => {
            console.log(message);

            if(message.type) {
                if(message.type === "LOGIN_EMPTY") {
                    console.log("empty")
                }
            } else if(message.type === "LOGIN_SUCCESS") {
                console.log("Success")
            } else if(message.type === "LOGIN_FAILED") {
                console.log("Failed")
            }
        });

        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                User Name : <input type="text" name="username" onChange={this.handleInputChange}/> <br/><br/>
                Password: <input type="password" name="password" onChange={this.handleInputChange}/> <br/><br/>
                <input type="submit" value="Login"/>
            </form>
        )
    }
}

export default Login;