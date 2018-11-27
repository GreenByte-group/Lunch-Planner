import React from "react";
import {checkName, doLogin} from "./LoginFunctions";
import {Redirect} from "react-router-dom";
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import {Input, InputLabel, InputAdornment} from '@material-ui/core';
import { FormControl, } from '@material-ui/core';
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import Button from '@material-ui/core/Button';
import LoginIcon from '@material-ui/icons/ExitToApp';
import Modal from 'react-modal';
import {getUser} from "../User/UserFunctions";


let sushi = '/sushi.jpg';
let kebab = '/kebab.jpg';

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
    left:{
        height: '-webkit-fill-available',
        maxHeight: '100%',
        width: '-webkit-fill-available',
        maxWidth: '25%',
        float: 'left',
        overflow: 'hidden',
        backgroundImage: 'url(' + sushi + ')',
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
        backgroundImage: 'url(' + kebab + ')',
        backgroundSize: 'cover',
        backgroundPosition: 'center center',
        backgroundRepeat: 'no-repeat',
    },
    logo: {
        display: 'flex',
        position: 'relative',
        height: '-webkit-fill-available',
        maxHeight: '200px',
        width: '-webkit-fill-available',
        maxWidth: '200px',
        marginTop: '15px',
        marginLeft: 'auto',
        marginRight: 'auto',

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

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            redirectToReferrer: false,
            error: "",
            modalIsOpen: false,

        };


        this.openModal = this.openModal.bind(this);
        this.afterOpenModal = this.afterOpenModal.bind(this);
        this.closeModal = this.closeModal.bind(this);

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }



    handleInputChange(event) {
        const target = event.target;
        const name = target.id;

        this.setState({
            [name]: target.value
        });
    }
    openModal() {
        this.setState({modalIsOpen: true});
    }

    afterOpenModal() {
        // references are now sync'd and can be accessed.
        this.subtitle.style.color = '#f00';
    }

    closeModal() {
        this.setState({modalIsOpen: false});
    }

    handleSubmit(event) {

         getUser(this.state.username, (response) => {
           console.log('getUser', response);
           this.setState({
               username: response.data.userName,
           });
           if(response.status === 200){
               console.log('res',response.data.userName);
               if(response.data.userName === undefined){
                   this.setState({
                       modalIsOpen: true,
                       error: "wrong username or password",
                       username: "",
                       password: "",
                   });
               }else{
                   doLogin(response.data.userName, this.state.password, message => {

                       if (message.type === "LOGIN_EMPTY") {
                           this.setState({
                               error: message.payload.message,
                               username: "",
                               password: "",
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
                               username: "",
                               password: "",
                           });
                       }
                   })
               }
              ;
           }else{
               this.setState({
                   modalIsOpen: true,
                   error: "wrong username or password",
               });
           }
        });

        if(event)
            event.preventDefault();
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

    keyPress = (e) => {
        if(e.keyCode === 13){
            this.handleSubmit();
        }
    };

    render() {
        const { classes } = this.props;
        const { from } = /*this.props.location.state || */{ from: { pathname: "/" } };
        const { redirectToReferrer } = this.state;
        const { error } = this.state;

        if (redirectToReferrer) {
            return <Redirect to={from} />;
        }

        let disabled = true;
        if(this.state.username && this.state.password)
            disabled = false;

        return (
            <div className={classes.root2}>
                <div className={classes.left}/>
                <div className={classes.root}>
                    <div className={classes.content}>
                        {(error
                                ? <Modal
                                    isOpen={this.state.modalIsOpen}
                                // onAfterOpen={this.afterOpenModal}
                                onRequestClose={this.closeModal}
                                style={customStyles}
                                contentLabel="Example Modal"
                            >{error}</Modal>
                            : ""
                    )}
                    <p className={classes.description}>
                        Welcome back. <br/>
                    </p>
                    <img label="logoLunchplanner" src="/foodastic.png" className={classes.logo}/>
                    <FormControl
                        fullWidth
                        className={classes.margin}
                        aria-describedby="weight-helper-text"
                    >
                        <InputLabel>{<p style={{fontSize: '18px'}}>Username</p>}</InputLabel>
                        <Input
                            style={{fontSize: '20px'}}
                            id="username"
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
                        <InputLabel htmlFor="adornment-password">{<p style={{fontSize: '18px'}}>Password</p>}</InputLabel>
                        <Input
                            id="password"
                            style={{fontSize: '20px'}}
                            type={this.state.showPassword ? 'text' : 'password'}
                            value={this.state.password}
                            onChange={this.handleInputChange}
                            onKeyDown={this.keyPress}
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
                </div>
                <Button disabled={disabled} fullWidth variant="raised" color="secondary" className={classes.button} onClick={this.handleSubmit}>
                    <LoginIcon color={"#FFFFF"}/>LOGIN
                </Button>
            </div>
                <div className={classes.right}/>
            </div>
        );
    }
}

Login.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(Login);
