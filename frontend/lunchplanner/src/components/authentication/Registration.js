import React from "react";
import IconButton from '@material-ui/core/IconButton';
import {register, doLogin} from './LoginFunctions';
import {Input, InputLabel, InputAdornment } from '@material-ui/core';
import { FormControl } from '@material-ui/core';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import Button from '@material-ui/core/Button';
import LoginIcon from '@material-ui/icons/ExitToApp';
import {withStyles} from "@material-ui/core/styles/index";
import {getHistory} from "../../utils/HistoryUtils";

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

class Registration extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '',
            username: '',
            email: '',
            showPassword: false,
        };

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

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
        const name = target.id;

        this.setState({
            [name]: target.value
        });
    }

    handleSubmit(event) {
        if(this.state.username && this.state.password && this.state.email) {
            register(this.state.username, this.state.email, this.state.password,
                (response) => {
                    if(response.status === 201) {
                        doLogin(this.state.username, this.state.password, message => {
                        if(message.type === "LOGIN_SUCCESS") {
                            getHistory().push("/app/event");
                        }})
                    } else {
                        this.setState({
                            error: response.data,
                        })
                    }
                },
                (err) => {
                    this.setState({
                        error: err.response.data,
                    })
                });
        } else {
            this.setState({
                error: "All fields are required"
            })
        }

        event.preventDefault();
    }

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
                        id="email"
                        placeholder="E-mail"
                        value={this.state.email}
                        onChange={this.handleInputChange}
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
                        id="password"
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
                <Button fullWidth variant="raised" color="secondary" className={classes.button} onClick={this.handleSubmit}>
                    <LoginIcon color={"#FFFFF"}/>REGISTER
                </Button>
            </div>
        );
    }
}

export default withStyles(styles, { withTheme: true })(Registration);