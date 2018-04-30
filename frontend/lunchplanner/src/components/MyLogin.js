import React from "react";
import {doLogin} from "./LoginFunctions";
import {Link, Redirect} from "react-router-dom";
import classNames from 'classnames';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import IconButton from 'material-ui/IconButton';
import Input, { InputLabel, InputAdornment } from 'material-ui/Input';
import { FormControl, FormHelperText } from 'material-ui/Form';
import TextField from 'material-ui/TextField';
import MenuItem from 'material-ui/Menu/MenuItem';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import Button from 'material-ui/Button';
import LoginIcon from '@material-ui/icons/ExitToApp';
const styles = theme => ({
    root: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    margin: {
        margin: theme.spacing.unit,
    },
    withoutLabel: {
        marginTop: theme.spacing.unit * 3,
    },
    textField: {
        flexBasis: 200,
    },
});

class MyLogin extends React.Component {
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
        console.log("Submit");
        doLogin(this.state.username, this.state.password, message => {
            console.log(message);

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

    /*handleChange = prop => event => {
        this.setState({ [prop]: event.target.value });
    };

    handleMouseDownPassword = event => {
        event.preventDefault();
    };

    handleClickShowPassword = () => {
        this.setState({ showPassword: !this.state.showPassword });
    };*/

    render() {
        const { classes } = this.props;
        const { error } = this.state;

        return (
            <div className={classes.root}>
                {(error
                        ? <div>{error}</div>
                        : ""
                )}

                <FormControl
                    fullWidth
                    className={classes.margin}
                    aria-describedby="weight-helper-text"
                >
                    <Input
                        id="username"
                        placeholder="Username"
                        value={this.state.username}
                        onChange={this.handleInputChange}
                        inputProps={{
                            'aria-label': 'Username',
                        }}

                    />
                </FormControl>
                <FormControl
                    fullWidth
                    className={classes.margin}
                >
                    <InputLabel htmlFor="adornment-password">Password</InputLabel>
                    <Input
                        id="adornment-password"
                        type={this.state.showPassword ? 'text' : 'password'}
                        value={this.state.password}
                        onChange={this.handleInputChange}
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="Toggle password visibility"
                                    onClick={this.handleClickShowPassword}
                                    onMouseDown={this.handleMouseDownPassword}
                                >
                                    {this.state.showPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        }
                    />
                </FormControl>
                <Button fullWidth variant="raised" color="secondary" className={classes.button} onClick={this.handleSubmit("submit")}>
                    <LoginIcon color={"#FFFFF"}/>LOGIN
                </Button>
            </div>
        );
    }
}

MyLogin.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(MyLogin);