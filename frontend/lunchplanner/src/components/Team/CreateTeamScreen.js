import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import Button from 'material-ui/Button';
import Dialog from '../Dialog';
import IconButton from 'material-ui/IconButton';
import Slide from 'material-ui/transitions/Slide';
import TextField from "material-ui/es/TextField/TextField";
import AddIcon from '@material-ui/icons/Add';
import PeopleIcon from '@material-ui/icons/People'

import {Link} from "react-router-dom";
import moment from "moment";

import {getHistory} from "../../utils/HistoryUtils";
import {FormControlLabel, FormHelperText, InputAdornment, Switch} from "material-ui";
import {eventListNeedReload} from "../Event/EventList";
import {createEvent} from "../CreateEventFunctions";
import {createTeam} from "./CreateTeamFunctions";
import {setAuthenticationHeader} from "../authentication/Authentication";

const styles = {
    button:{
        fontSize: '16px',
        fontFamily: 'Work Sans',
        color: "white",
        position: "fixed",
        bottom: 0,
        width: "100%",
        height: '56px',
        zIndex: '10000',
    },
    padding: {
        paddingTop: '16px',
        paddingLeft: '24px',
        paddingRight: '24px',
    },
    teamPicture: {
        height: '48px',
        width: '48px',
        border: '1px solid black',
        borderRadius: '50%',
        float: 'left',
    },
    textFieldDescription: {
        marginTop: '15px',
        width: '100%',
    },
    textFieldName: {
        width: '100%',
        float: 'right',
        padding: '0px',
        margin: '0px',
        marginLeft: '24px',
    },
    overButton: {
        height: 'calc(100% - 112px)',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
    },
    error: {
        color: 'red',
    }
};
const buttonStyle = {
    float: 'right',
    marginBottom: '15px',
};

function Transition(props) {
    return <Slide direction="up" {...props} />;
}


class CreateTeamScreen extends React.Component {

    constructor(props) {
        super();

        setAuthenticationHeader();

        const params = new URLSearchParams(props.location.search);

        let defaultDate = moment().add(30, 'm').toDate();

        this.state = {
            open: true,
            // location: params.get('location') || "",
            error: "",
            name: "",
            description: "",
            secret: false,
        };
    }

    parseUrl = () => {
        const params = new URLSearchParams(this.props.location.search);
        let invitedUsers = params.get('invitedUsers');
        if(invitedUsers != null && invitedUsers !== undefined && invitedUsers !== this.state.invitedUsers) {
            this.setState({
                invitedUsers: params.get('invitedUsers'),
            });
        }
    };

    handleAccept = () => {
        createTeam(this.state.name, this.state.description, this.state.invitedUsers, this.state.secret,
            (response) => {
                if(response.status === 201) {
                    eventListNeedReload();
                    getHistory().push('/social?tab=1');
                } else {
                    this.setState({error: response.response.data});
                }
            },
            (error) => {
                if(error)
                    this.setState({error: error.response.data});
            });
    };

    handleVisibility = name => event =>{
        this.setState({ [name]: event.target.checked });
    };

    handleChange = (event) => {
        let target = event.target;
        this.setState({
            [target.id]: target.value,
        });
    };

    render() {
        this.parseUrl();
        const { classes } = this.props;
        const error = this.state.error;

        let buttonEnabled = false;
        if(this.state.name && this.state.invitedUsers)
            buttonEnabled = true;

        let switchEnabled = false;
        if(this.state.invitedUsers) {
            switchEnabled = true;
        }

        return (
            <Dialog
                title="Create Team"closeIconAbsolute
                closeUrl="/social?tab=1"
            >
                <div className={classes.overButton}>
                    {(error
                            ? <p className={classes.error}>{error}</p>
                            : ""
                    )}
                    <div className={classes.padding}>
                        <div className={classes.teamPicture}>

                        </div>
                        <TextField
                            InputLabelProps={{
                                shrink: true,
                            }}
                            id="name"
                            label="Team Name"
                            value={this.state.name}
                            className={classes.textFieldName}
                            placeholder ="Your team's name"
                            onChange={this.handleChange}
                        />
                        <TextField
                            InputLabelProps={{
                                shrink: true,
                            }}
                            className={classes.textFieldDescription}
                            id="description"
                            value={this.state.description}
                            onChange={this.handleChange}
                            label="Description"
                            placeholder ="Say something to describe your team"
                            multiline
                            rows="6"
                            fullWidth
                        />

                        <Link className={classes.inviteTextField}
                              style={{width: '100%'}}
                              to={{pathname: "/team/create/invite",  query: {
                                source: "/team/create",
                                invitedUsers: this.state.invitedUsers,
                            }}}>
                            <TextField
                                style={{marginTop: '24px'}}
                                InputLabelProps={{
                                    shrink: true,
                                }}
                                id="invitation"
                                label="Team members"
                                placeholder ="Invite People"
                                value={this.state.invitedUsers}
                                fullWidth
                                InputProps={{
                                    startAdornment: <InputAdornment position="start"><PeopleIcon/></InputAdornment>,
                                    endAdornment: <InputAdornment position="end">
                                            <AddIcon />
                                    </InputAdornment>
                                }}
                            />
                        </Link>

                        <FormControlLabel
                            style={{marginBottom: 0}}
                            control={
                                <Switch
                                    disabled={!switchEnabled}
                                    float ="left"
                                    color = "primary"
                                    checked={this.state.secret}
                                    onChange={this.handleVisibility("secret")}
                                />
                            }
                            label="Secret team"
                        />
                        <FormHelperText
                            style={{margin: 0}}
                        >
                            Only visible to invited members.
                        </FormHelperText>
                    </div>
                </div>
                <Button disabled={!buttonEnabled} variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                    Create Team
                </Button>
            </Dialog>
        );
    }
}

CreateTeamScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(CreateTeamScreen);