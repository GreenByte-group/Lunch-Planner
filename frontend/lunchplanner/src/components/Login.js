import React from "react";
import {doLogin} from "./LoginFunctions";
import {Link, Redirect} from "react-router-dom";

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            redirectToReferrer: false,
            error: "",
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
        doLogin(this.state.username, this.state.password, message => {

            if(message.type === "LOGIN_EMPTY") {
                this.setState({
                    error: message.payload.message,
                }); 
            } else if(message.type === "LOGIN_SUCCESS") {
                this.setState({
                    error: "",
                    redirectToReferrer: true,
                });
            } else if(message.type === "LOGIN_FAILED") {
                this.setState({
                    error: "Wrong username or password"
                });
            }
        });

        event.preventDefault();
    }

    render() {
        const { from } = this.props.location.state || { from: { pathname: "/" } };
        const { redirectToReferrer } = this.state;
        const { error } = this.state;

        if (redirectToReferrer) {
            return <Redirect to={from} />;
        }
        return (
            <div className="container">
                <h3>Login</h3>
                {(error
                        ? <div>{error}</div>
                        : ""
                )}
                <form className="form col-md-12 center-block" onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <input className="form-control input-lg" type="text" name="username"
                               onChange={this.handleInputChange} placeholder="User Name"/>
                    </div>
                    <div className="form-group">
                        <input className="form-control input-lg" type="password" name="password"
                               onChange={this.handleInputChange} placeholder="Password"/>
                    </div>
                    <div className="form-group">
                        <input className="btn btn-primary btn-lg btn-block" type="submit" value="Login"/>
                    </div>
                </form>

                <Link to="/register" >Register</Link>
            </div>
        )
    }
}

export default Login;