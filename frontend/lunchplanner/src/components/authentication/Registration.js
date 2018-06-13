import React from "react";
import IconButton from '@material-ui/core/IconButton';
import {register, doLogin} from './LoginFunctions';
import {Input, InputLabel, InputAdornment, FormControlLabel, Checkbox } from '@material-ui/core';
import { FormControl } from '@material-ui/core';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import Button from '@material-ui/core/Button';
import Done from '@material-ui/icons/Done'
import {withStyles} from "@material-ui/core/styles/index";
import {getHistory} from "../../utils/HistoryUtils";
import {Link} from "react-router-dom";

const styles = theme => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        maxWidth: '600px',
        marginLeft: 'auto',
        marginRight: 'auto',
        flex: '0 1 auto',
    },
    description: {
        fontSize: '16px',
        lineHeight: '24px',
        textAlign: 'center',
        margin: '20px',
    },
    content: {
        justifyContent: 'space-between',
        flexGrow: 1,
        padding: '0 5px',
    },
    button: {
        height: '56px',
        width: '100%',
        color: 'white',
        fontSize: '15px',
    },
    margin: {
        margin: '10px 0px',
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
            checkbox: false,
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

    keyPress = (e) => {
        if(e.keyCode === 13){
            this.handleSubmit();
        }
    };

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

        if(event)
            event.preventDefault();
    }

    render() {
        const { classes } = this.props;
        const { error } = this.state;

        let disabled = true;
        if(this.state.username && this.state.password && this.state.email && this.state.checkbox) {
            disabled = false;
        }

        return (
            <div className={classes.root}>
                <div className={classes.content}>
                    {(error
                            ? <div>{error}</div>
                            : ""
                    )}
                    <p className={classes.description}>
                        Nice to meet you. <br/> Please set up your account.
                    </p>
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
                    <FormControlLabel
                        control={
                            <Checkbox
                                checked={this.state.checkbox}
                                onChange={(event) => this.setState({checkbox: event.target.checked})}
                                color="primary"
                            />
                        }
                        label="I accept the general terms and conditions"
                    />
                    <Link to="/terms">read more</Link>
                </div>
                <Button disabled={disabled} fullWidth variant="raised" color="secondary" className={classes.button} onClick={this.handleSubmit}>
                    <Done color={"#FFFFF"}/>REGISTER
                </Button>
            </div>
        );
    }
}

export default withStyles(styles, { withTheme: true })(Registration);