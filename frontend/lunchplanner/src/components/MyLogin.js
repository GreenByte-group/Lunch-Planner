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
    state = {
        password: '',
        username: '',
        showPassword: false,
    };

    handleChange = prop => event => {
        this.setState({ [prop]: event.target.value });
    };

    handleMouseDownPassword = event => {
        event.preventDefault();
    };

    handleClickShowPassword = () => {
        this.setState({ showPassword: !this.state.showPassword });
    };

    render() {
        const { classes } = this.props;

        return (
            <div className={classes.root}>
                <FormControl
                    fullWidth
                    className={classes.margin}
                    aria-describedby="weight-helper-text"
                >
                    <Input
                        id="username"
                        placeholder="Your Username or email"
                        value={this.state.username}
                        onChange={this.handleChange('username')}
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
                        onChange={this.handleChange('password')}
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
                <Button fullWidth variant="raised" color="secondary" className={classes.button} onClick={console.log("Login")}>
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
