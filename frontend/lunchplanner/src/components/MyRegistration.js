import React from "react";
import axios from "axios";
import {Link, Redirect} from "react-router-dom";
import IconButton from 'material-ui/IconButton';
import Input, { InputLabel, InputAdornment } from 'material-ui/Input';
import { FormControl, FormHelperText } from 'material-ui/Form';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import Button from 'material-ui/Button';
import LoginIcon from '@material-ui/icons/ExitToApp';
import {withStyles} from "material-ui/styles/index";

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

class MyRegistration extends React.Component {

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
        const { classes } = this.props;

        return (
            <div className={classes.root}>
                <FormControl
                    fullWidth
                    className={classes.margin}
                    aria-describedby="weight-helper-text"
                >
                    <Input
                        id="email"
                        placeholder="E-mail"
                        value={this.state.email}
                        onChange={this.handleChange('email')}
                        inputProps={{
                            'aria-label': 'Email',
                        }}

                    />
                </FormControl>
                    <FormControl
                        fullWidth
                        className={classes.margin}
                        aria-describedby="weight-helper-text"
                    >
                        <Input
                            id="username"
                            placeholder="Username"
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
                <Button fullWidth variant="raised" color="secondary" className={classes.button} onClick={console.log("Register")}>
                    <LoginIcon color={"#FFFFF"}/>REGISTER
                </Button>
            </div>
        );
    }
}

export default withStyles(styles, { withTheme: true })(MyRegistration);