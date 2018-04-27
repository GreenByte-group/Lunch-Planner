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
            <form class="form col-md-12 center-block" onSubmit={this.handleSubmit}>
                <div className="form-group">
                    <input class="form-control input-lg" type="text" name="username" onChange={this.handleInputChange} placeholder="Email"/>
                </div>
                <div className="form-group">
                    <input class="form-control input-lg" type="password" name="password" onChange={this.handleInputChange} placeholder="User Name"/>
                </div>
                <div className="form-group">
                    <input class="btn btn-primary btn-lg btn-block" type="submit" value="Login"/>
                </div>
            </form>
        )
    }
}

export default Login;