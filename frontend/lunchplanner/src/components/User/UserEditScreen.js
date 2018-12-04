import React from 'react';
import {withStyles} from "@material-ui/core/styles/index";
import {TextField, FormControl, InputLabel, Input, Button} from "@material-ui/core";
import {Edit} from "@material-ui/icons";
import {getUsername} from "../authentication/LoginFunctions";
import {
    deleteAccount,
    getProfilePicturePath,
    getUser,
    updateEmail,
    updatePassword,
    updateProfilePicture
} from "./UserFunctions";
import {HOST} from "../../Config";
import {userNeedReload} from "../AppContainer";
import Modal from 'react-modal';
import Dialog from "../Dialog";
import {getHistory} from "../../utils/HistoryUtils";


const profilpic = getProfilePicturePath(getUsername());
const styles = theme => ({
    root: {
        width: '100%',
        height: '100%',
        overflowY: 'auto',
        maxWidth: '1024px',
        marginLeft: 'auto',
        marginRight: 'auto',
        display: 'flex',
        flexDirection: 'column',
        paddingTop: '20px',
        paddingLeft: '10px',
        paddingRight: '10px',
    },
    pictureName: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
    },
    profilePicture: {
        width: '200px',
        height: '200px',
        border: '1px solid black',
        borderRadius: '50%',
        float: 'left',
        backgroundPosition: 'center',
        backgroundSize: 'cover',
        overflow: 'hidden',
    },
    profilePictureImg: {
        objectFit: 'cover',
        width: '100%',
        height: '100%',
    },
    editIconWrapper: {
        width: '200px',
        height: '200px',
        borderRadius: '50%',
        backgroundColor: 'rgba(0,0,0,0.3)',
        backgroundImage: 'url(' + profilpic + ')',
        transition: 'background-color 0.5s',
        "&:hover": {
            backgroundColor: 'rgba(0,0,0,0.5)',
            cursor: 'pointer',
        }
    },
    editIcon: {
        width: '30px',
        height: '30px',
        marginLeft: '85px',
        marginTop: '85px',
        color: 'white',
    },
    username: {
        marginLeft: '25px',
        fontSize: '20px',
        fontWeight: 600,
    },
    input: {
        display: 'none !important',
    },
    inputLabel: {
        height: '200px',
        width: '200px',
        margin: 0,
        padding: 0,
    },
    edit: {
        marginTop: '20px',
        display: 'flex',
        flexDirection: 'column',
    },
    editArea: {
        display: 'flex',
        flexDirection: 'row',
        marginBottom: '15px',
    },
    text: {
        width: '200px',
    },
    textFieldMail: {
        marginLeft: '5px',
        width: '100%',
        maxWidth: '300px',
    },
    buttonYes: {
        maxWidth: '100px',
        fontSize: '14px',
        marginLeft: '25%',
        float: 'left',
        boxShadow: '0px 1px 5px 0px rgba(0, 0, 0, 0.2),0px 2px 2px 0px rgba(0, 0, 0, 0.14),0px 3px 1px -2px rgba(0, 0, 0, 0.12)',
        backgroundColor: '#75a045',
        color: 'white',
        "&:hover": {
            backgroundColor: '#9cd556',
        },

    },
    buttonNo: {
        fontSize: '14px',
        marginLeft: '25%',
        float: 'right',
        boxShadow: '0px 1px 5px 0px rgba(0, 0, 0, 0.2),0px 2px 2px 0px rgba(0, 0, 0, 0.14),0px 3px 1px -2px rgba(0, 0, 0, 0.12)',
        backgroundColor: '#75a045',
        color: 'white',
        "&:hover": {
            backgroundColor: '#9cd556',
        }
    },
    buttonSubmit: {
        fontSize: '14px',
        justifyContent: 'center',
        float: 'left',
        boxShadow: '0px 1px 5px 0px rgba(0, 0, 0, 0.2),0px 2px 2px 0px rgba(0, 0, 0, 0.14),0px 3px 1px -2px rgba(0, 0, 0, 0.12)',
        backgroundColor: '#75a045',
        color: 'white',
        "&:hover": {
            backgroundColor: '#9cd556',
        }
    },
    error: {
        color: 'red',
    },
    placeGiver: {
        padding: '10px',
    },
    sure: {
        display: 'flex',
        flexDirection: 'row',
        width: '-webkit-fill-available',
        height: '-webkit-fill-available',
        maxHeight: '100px',
        marginBottom:'20px'

    },
    mainContent: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent:'center',
    }
});

class UserEditScreen extends React.Component {


    constructor(props) {
        super();

        this.state = {
            username: "",
            email: '',
            showPassword: false,
            password: '',
            repeat: '',
            emailError: null,
            passwordError: null,
            pathImage: null,
            delete: false,
            pathProfilePicture: "",
            modalIsOpen: false,
            sure: false,
            disabledSubmit: false,
            onChange: props.location.query.onChange,

        };
        this.getUser();
    }

    getUser = () => {
        getUser(getUsername(), (response) => {
            this.setState({
                pathProfilePicture: response.data.profilePictureUrl,
                email: response.data.eMail,
            })
        })
    }

    onChange = (value) => {
        this.setState({
            [value.target.id]: value.target.value,
        })
    };

    onProfile = (event) => {
        let fileList = event.target.files;
        //fileList.item(0);
        this.setState({
            pathImage: fileList.item(0),
        })
    };

    onDeleteYes = () => {
        this.setState({
            sure: true,
            disabledSubmit: true,
        });
    };

    onDeleteNo = () => {
        this.setState({
            sure: false,
            disabledSubmit: true,
        });
    };

    onDeleteClick = () => {
        this.setState({
            delete: true,
        });
    };

    onDeleteSubmit = () => {
      let sure = this.state.sure;
      if(sure){
          deleteAccount(getUsername());
      }else{
          getHistory().push('/app/event');
      }
    };


    onSubmit = () => {

        let change = false;

        if(this.state.pathImage) {
            updateProfilePicture(this.state.pathImage, (response) => {
                userNeedReload();
                change = true;
            })
        }

        if(this.state.email) {
            updateEmail(this.state.email, (response) => {

                if(response.status !== 204) {
                    this.setState({emailError: response.response.data})
                    userNeedReload();
                    change = true;
                } else {
                    this.setState({
                        emailError: null,
                        email: '',
                    })
                }
            });
        }

        if(this.state.password && this.state.password === this.state.repeat) {
            updatePassword(this.state.password, (response) => {
                if(response.status !== 204) {
                    this.setState({passwordError: response.response.data})
                    userNeedReload();
                    change = true;
                } else {
                    this.setState({
                        passwordError: null,
                        password: '',
                        repeat: '',
                    })
                }
            })
        } else if (this.state.password !== this.state.repeat){
            this.setState({passwordError: 'Passwords are different'})
        }
        console.log('states', this.state);
        this.props.location.query.onChange();
    };

    render() {
        const { classes } = this.props;

        let url;
        if(this.state.pathImage) {
            url = URL.createObjectURL(this.state.pathImage)
        } else {
            url = HOST + this.state.pathProfilePicture;
        }

        return (
            <div className={classes.root}>
                <div  className={classes.pictureName}>
                    <div
                        ref="image-pane"
                        className={classes.profilePicture}
                    >
                        <label htmlFor="file" className={classes.inputLabel}>
                            <div className={classes.editIconWrapper} >
                                <Edit className={classes.editIcon} />
                            </div>

                            <img src={url} className={classes.profilePictureImg} />
                        </label>
                        <input className={classes.input} type="file" accept="image/*" id="file" onChange={this.onProfile} />
                    </div>
                    <div className={classes.username}>
                        {getUsername()}
                    </div>
                </div>
                <div className={classes.edit}>
                    {
                        (this.state.emailError)
                            ? <p className={classes.error}>{this.state.emailError}</p>
                            : ''
                    }
                    {
                        (this.state.passwordError)
                            ? <p className={classes.error}>{this.state.passwordError}</p>
                            : ''
                    }
                    <div className={classes.editArea}>
                        <TextField className={classes.textFieldMail}
                                   label="E-Mail"
                                   id="email"
                                   value={this.state.email}
                                   onChange={this.onChange}
                        />
                    </div>
                    { (this.state.showPassword) ?
                        <div>
                            <div className={classes.editArea}>
                                <TextField className={classes.textFieldMail}
                                           type="password"
                                           label="New password"
                                           id="password"
                                           value={this.state.password}
                                           onChange={this.onChange}
                                />
                            </div>
                            <div className={classes.editArea}>
                                <TextField className={classes.textFieldMail}
                                           label="Repeat password"
                                           type="password"
                                           id="repeat"
                                           value={this.state.repeat}
                                           onChange={this.onChange}
                                />
                            </div>
                        </div>
                        :
                        <div className={classes.editArea}>
                            <Button
                                className={classes.button}
                                onClick={() => this.setState({showPassword: true})}
                                variant="contained"
                                color="primary"
                            >
                                Change password
                            </Button>
                            <p className={classes.placeGiver}/>
                            <Button
                                className={classes.button}
                                onClick={this.onDeleteClick}
                                variant="contained"
                                color="primary"
                            >
                                Delete my account
                            </Button>
                            { (this.state.delete) ?

                                <Dialog
                                    title={'Delete User'}
                                >

                                    <div className={classes.mainContent}>
                                        <p style={{
                                            position: 'sticky',
                                            margin: 'auto',
                                            marginTop: '20px',
                                            marginBottom: '20px',}}
                                        >
                                            Are you sure??
                                        </p>
                                    <div className={classes.sure}>
                                        <Button
                                            className={classes.buttonYes}
                                            onClick={this.onDeleteYes}
                                            variant="contained"
                                            color="primary"
                                        >
                                            <p>Yes</p>
                                        </Button>
                                        <Button
                                            className={classes.buttonNo}
                                            onClick={this.onDeleteNo}
                                            variant="contained"
                                            color="primary"
                                        >
                                            <p>No</p>
                                        </Button>

                                    </div>
                                    </div>
                                    <Button
                                        disabled={!this.state.disabledSubmit}
                                        className={classes.buttonSubmit}
                                        onClick={this.onDeleteSubmit}
                                        variant="contained"
                                        color="primary"
                                    >
                                        <p>Submit</p>
                                    </Button>
                                </Dialog>

                                :""
                            }
                        </div>
                    }
                    <div className={classes.editArea}>
                        <Button
                            className={classes.button}
                            onClick={this.onSubmit}
                            variant="contained"
                            color="primary"
                        >
                            Save
                        </Button>
                    </div>
                </div>
            </div>
        )
    }
}

export default withStyles(styles, { withTheme: true })(UserEditScreen);