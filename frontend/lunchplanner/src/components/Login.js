import React from "react";
import {doLogin} from "./LoginFunctions";
import {Link, Redirect} from "react-router-dom";
import Modal from 'react-modal';
import {getUser} from "./User/UserFunctions";

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

        this.setState = {
            modalIsOpen: false
        };

        this.openModal = this.openModal.bind(this);
        this.afterOpenModal = this.afterOpenModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
    }



    handleInputChange(event) {
        const target = event.target;
        const name = target.name;

        this.setState({
            [name]: target.value
        });
    }

    handleSubmit(event) {
        getUser(this.state.username, (response) => {
            console.log('getUser', response);
            this.setState({
                username: response.data.userName,
            });
            if(response.status === 200){
                doLogin(response.data.userName, this.state.password, message => {

                    if (message.type === "LOGIN_EMPTY") {
                        this.setState({
                            error: message.payload.message,
                        });
                    } else if (message.type === "LOGIN_SUCCESS") {
                        this.setState({
                            error: "",
                            redirectToReferrer: true,
                        });
                    } else if (message.type === "LOGIN_FAILED") {
                        this.setState({
                            modalIsOpen: true,
                            error: "wrong username or password",
                        });
                    }
                });
            }else{
                this.setState({
                    modalIsOpen: true,
                    error: "wrong username or password",
                });
            }
        });

        event.preventDefault();
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }

    afterOpenModal() {
        // references are now sync'd and can be accessed.
        this.subtitle.style.color = '#f00';
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
                        ? <>{error}</>
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