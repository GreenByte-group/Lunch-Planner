import React from "react";
import axios from "axios";
import {Link, Redirect} from "react-router-dom";
import Modal from 'react-modal';





class Registration extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            email: "",
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this)


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
            let url =  'http://localhost:8090/user';
            axios.post(url, {userName: this.state.username, password: this.state.password, mail: this.state.email})
                .then((response) => {
                    if(response.status === 201) {
                        this.setState({
                            redirectToReferrer: true,
                        })
                    } else {
                        this.setState({
                            error: response.data,
                        })
                    }
                })
                .catch((err) => {
                    this.setState({
                        error: err.response.data,
                    })
                })
        } else {
            this.setState({
                error: "All fields are requiered"
            })
        }

        event.preventDefault();
    }

    render() {
        const from = "/login"
        const { redirectToReferrer } = this.state;
        const { error } = this.state;

        if (redirectToReferrer) {
            return <Redirect to={from} />;
        }

        return (
            <div class="container">
                <h3>Register</h3>
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
                        <input className="form-control input-lg" type="text" name="email"
                               onChange={this.handleInputChange} placeholder="E-Mail"/>
                    </div>
                    <div className="form-group">
                        <input className="form-control input-lg" type="password" name="password"
                               onChange={this.handleInputChange} placeholder="Password"/>
                    </div>
                    <div className="form-group">
                        <input className="btn btn-primary btn-lg btn-block" type="submit" value="Register"/>
                    </div>
                </form>

                <Link to="/login" >Already registerd? Log in</Link>
            </div>
        )
    }
}

export default Registration;