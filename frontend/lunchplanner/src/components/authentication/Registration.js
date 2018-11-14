import React from "react";
import IconButton from '@material-ui/core/IconButton';
import {register, doLogin, setAuthenticationHeader} from './LoginFunctions';
import {Input, InputLabel, InputAdornment, FormControlLabel, Checkbox } from '@material-ui/core';
import { FormControl } from '@material-ui/core';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import Button from '@material-ui/core/Button';
import Done from '@material-ui/icons/Done'
import {withStyles} from "@material-ui/core/styles/index";
import {getHistory} from "../../utils/HistoryUtils";
import {Link} from "react-router-dom";
import Modal from 'react-modal';
import TextField from '@material-ui/core/TextField';




let pizza = '/pizza.jpg';
let banana = '/banana.jpg';

const styles = theme => ({
    root: {
        display: 'flex',
        flexDirection: 'column',
        height: '-webkit-fill-available',
        maxHeight: '100%',
        width: '-webkit-fill-available',
        maxWidth: '100%',

    },
    root2: {
        display: 'flex',
        flexDirection: 'row',
        height: '-webkit-fill-available',
        maxHeight: '100%',
        width: '-webkit-fill-available',
        maxWidth: '100%',
    },
    description: {
        fontSize: '25px',
        lineHeight: '24px',
        textAlign: 'center',
        margin: '20px',
    },
    content: {
        justifyContent: 'space-between',
        flexGrow: 1,
        padding: '0 5px',
        height: '-webkit-fill-available',
        maxHeight: '100%',
        width: '-webkit-fill-available',
        maxWidth: '100%',
    },
    button: {
        height: '-webkit-fill-available',
        maxHeight: '50px',
        width: '-webkit-fill-available',
        maxWidth: '100%',

        color: 'white',
        fontSize: '15px',


    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
    },
    reSize: {
        fontSize: 20,
    },
    margin: {
        margin: '20px 0px',
        width: '-webkit-fill-available',
        maxWidth: '100%',
        marginLeft: '10%',
        marginRight: '10%',
    },
    withoutLabel: {
        marginTop: theme.spacing.unit * 3,
    },
    textField: {
        flexBasis: 200,
    },
    logo: {
        display: 'flex',
        position: 'sticky',
        width: '-webkit-fill-available',
        maxWidth: '200px',
        height: '-webkit-fill-available',
        maxHeight: '200px',

        margin: 'auto',
        textAlign: 'center',


    },
    left:{
        height: '-webkit-fill-available',
        maxHeight: '100%',
        width: '-webkit-fill-available',
        maxWidth: '25%',
        float: 'left',
        overflow: 'hidden',
        backgroundImage: 'url(' + pizza + ')',
        backgroundSize: 'cover',
        backgroundPosition: 'center center',
        backgroundRepeat: 'no-repeat',

    },
    right: {
        height: '-webkit-fill-available',
        maxHeight: '100%',
        width: '-webkit-fill-available',
        maxWidth: '25%',
        float: 'right',
        overflowX: 'hidden',
        overflowY: 'hidden' ,
        backgroundImage: 'url(' + banana + ')',
        backgroundSize: 'cover',
        backgroundPosition: 'center center',
        backgroundRepeat: 'no-repeat',
    },
    textSize: {
        fontSize: '18px',
    },
});


const customStyles = {
    content : {
        top                   : '50%',
        left                  : '50%',
        right                 : 'auto',
        bottom                : 'auto',
        marginRight           : '-50%',
        transform             : 'translate(-50%, -50%)'
    }
};

class Registration extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '',
            username: '',
            email: '',
            showPassword: false,
            checkbox: false,
            modalIsOpen: false,

        };

        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    openModal() {
        this.setState({modalIsOpen: true});
    }
    closeModal() {
        this.setState({modalIsOpen: false});
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
                            setAuthenticationHeader();
                            getHistory().push("/app/event");
                        }})
                    } else {
                        this.openModal();
                        this.setState({
                            error: response.data,
                        })
                    }
                },
                (err) => {
                    this.openModal();
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
            <div className={classes.root2}>
            <div className={classes.left}/>
            <div className={classes.root}>
                <div className={classes.content}>
                    {(error
                            ? <Modal
                                isOpen={this.state.modalIsOpen}
                                onRequestClose={this.closeModal}
                                style={customStyles}
                                contentLabel="Example Modal"
                            >{error}</Modal>
                            : ""
                    )}
                    <p className={classes.description}>
                      Set up your account<br/>
                    </p>
                    <img label="logoLunchplanner" src="/foodastic.png" className={classes.logo}/>
                    <FormControl
                        className={classes.margin}
                        aria-describedby="weight-helper-text"
                    >
                        <TextField
                            id="email"
                            label={<p style={{fontSize: '18px'}}>Email</p>}
                            value={this.state.email}
                            onChange={this.handleInputChange}
                            autoComplete={"email"}
                            type="email"
                            variant="filled"
                            InputProps={{
                                classes: {
                                    input: classes.textSize,
                                },
                            }}

                        />
                    </FormControl>
                    <FormControl
                        fullWidth
                        className={classes.margin}
                        aria-describedby="weight-helper-text"
                    >
                        <TextField
                            id="username"
                            label={<p style={{fontSize: '18px'}}>Name</p>}
                            value={this.state.username}
                            onChange={this.handleInputChange}
                            variant="filled"
                            InputProps={{
                                classes: {
                                    input: classes.textSize,
                                },
                            }}

                        />
                    </FormControl>
                    <FormControl
                        fullWidth
                        className={classes.margin}
                    >
                    <InputLabel htmlFor="adornment-password">{<p style={{fontSize: '18px'}}>Password</p>}</InputLabel>
                    <Input
                        id="password"
                        style={{fontSize: '18px'}}
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
                        label={<p style={{fontSize: '10px'}}>I accept the privacy policy.</p>}
                    />
                    <Link to="/privacyPolicy">read more</Link>
                </div>
                <Button disabled={disabled} fullWidth variant="raised" color="secondary" className={classes.button} onClick={this.handleSubmit}>
                    <Done color={"#FFFFF"}/>REGISTER
                </Button>
            </div>
        <div className={classes.right}/>
        </div>
        );
    }
}

export default withStyles(styles, { withTheme: true })(Registration);