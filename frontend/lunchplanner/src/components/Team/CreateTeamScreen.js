import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '../Dialog';
import { Slide, TextField, MenuItem, Select } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import PeopleIcon from '@material-ui/icons/People'
import {getHistory} from "../../utils/HistoryUtils";
import {Link} from "react-router-dom";
import {FormControlLabel, FormHelperText, InputAdornment, Switch, InputLabel, FormControl} from "@material-ui/core";
import {createTeam, createTeamWithParent, getTeams} from "./TeamFunctions";
import {setAuthenticationHeader} from "../authentication/LoginFunctions";
import {teamListNeedReload} from "./TeamList";

const styles = {
    button:{
        fontSize: '16px',
        fontFamily: 'Work Sans',
        color: "white",
        bottom: 0,
        width: "100%",
        minHeight: '56px',
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
        height: '100%',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
    },
    error: {
        color: 'red',
    },
    selectParent: {
        width: '100%',
    },
    formControl: {
        width: '100%',
        marginTop: '15px',
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

        this.state = {
            open: true,
            error: "",
            name: "",
            description: "",
            secret: false,
            invitedUsers: params.get('invitedUsers') || [],
            invitedTeams: params.get('invitedTeams') || [],
            invitedTeamMember: params.get('teamMember') || [],
            withParent: params.get('withParent') === 'true',
            parentTeam: null,
            teams: [],
            loading: true,
        };

        this.getTeams();
    }

    getTeams = () => {
        getTeams("", (response) => {
            this.setState({
                teams: response.data,
                loading: false,
            });
        });
    };

    parseUrl = () => {
        const params = new URLSearchParams(this.props.location.search);
        let invitedUsers = params.get('invitedUsers');
        let invitedTeams = params.get('invitedTeams');
        let teamMember = params.get('teamMember');
        if(invitedUsers != null && invitedUsers !== undefined && invitedUsers !== this.state.invitedUsers) {
            this.setState({
                invitedUsers: params.get('invitedUsers'),
            });
        }
        if(invitedTeams != null && invitedTeams !== undefined && invitedTeams !== this.state.invitedTeams) {
            this.setState({
                invitedTeams: params.get('invitedTeams'),
            });
        }
        if(teamMember != null && teamMember !== undefined && teamMember !== this.state.invitedTeamMember) {
            this.setState({
                invitedTeamMember: params.get('teamMember'),
            });
        }

        let withParent = params.get('withParent') == 'true';

        if(withParent === true && this.state.withParent !== true) {
            console.log('set with parent true');
            this.setState({
                withParent: true,
            })
        }
    };

    handleAccept = () => {
        let invitedUsers = this.state.invitedUsers + "," + this.state.invitedTeamMember;
        createTeamWithParent(this.state.name, this.state.description, this.state.parentTeam,
            invitedUsers, !this.state.secret,
            (response) => {

                if(response.status === 201) {
                    teamListNeedReload();
                    getHistory().push('/app/team');
                    this.setState({
                        loading: false
                    });
                } else {
                    this.setState({
                        error: response.response.data,
                        loading: false
                    });
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

    handleParentChange = (event) => {
        this.setState({ parentTeam: event.target.value });
    };

    render() {
        this.parseUrl();
        const { classes } = this.props;
        const error = this.state.error;

        let invited = this.state.invitedUsers + "," + this.state.invitedTeams;

        let buttonEnabled = false;
        if(this.state.name && (this.state.invitedUsers || this.state.invitedTeams)
            && (!this.state.withParent || this.state.parentTeam))
            buttonEnabled = true;

        let switchEnabled = false;
        if(this.state.invitedUsers) {
            switchEnabled = true;
        }

        return (
            <Dialog
                title="Create Team"closeIconAbsolute
                closeUrl="/app/team?tab=1"
            >
                <div className={classes.overButton}>
                    {(error
                            ? <p className={classes.error}>{error}</p>
                            : ""
                    )}
                    <div className={classes.padding}>
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
                        {
                            (this.state.withParent)
                                ? <form>
                                    <FormControl className={classes.formControl}>
                                        <InputLabel htmlFor="organisation">Choose organisation</InputLabel>
                                        <Select
                                            inputProps={{
                                                name: 'Choose organisation',
                                                id: 'organisation',
                                            }}
                                            value={this.state.parentTeam}
                                            onChange={this.handleParentChange}
                                            className={classes.selectParent}
                                        >
                                            {
                                                this.state.teams.map((value) => <MenuItem value={value.teamId}>{value.teamName}</MenuItem>)
                                            }
                                        </Select>
                                    </FormControl>
                                </form>
                                : ''
                        }
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
                              to={{pathname: "/app/team/create/invite",  query: {
                                source: "/app/team/create",
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
                                value={invited}
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
                            If not secret, the team is visible for all users
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
