import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Dialog from '../Dialog';
import {Switch, Typography, TextField, ExpansionPanel, ExpansionPanelSummary, ExpansionPanelDetails} from '@material-ui/core';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import AddIcon from '@material-ui/icons/Add';
import MapIcon from '@material-ui/icons/Map'
import VisibilityOffIcon from '@material-ui/icons/VisibilityOff';
import VisibilityIcon from '@material-ui/icons/Visibility';
import {FormGroup, FormControlLabel, Slide} from '@material-ui/core';
import {DatePicker, TimePicker} from 'material-ui-old';
import PeopleIcon from '@material-ui/icons/People'

import 'react-datepicker/dist/react-datepicker-cssmodules.css';
import "../../assets/CreateEventScreen.css"
import {Link} from "react-router-dom";
import {Today, Schedule} from "@material-ui/icons";
import {eventListNeedReload} from "./EventList";
import moment from "moment";

import {getHistory} from "../../utils/HistoryUtils";
import {InputAdornment} from "@material-ui/core";
import {createEvent} from "./EventFunctions";
import GoogleSuggest from "../Map/GoogleSuggest";
import Icon from "@material-ui/core/es/Icon/Icon";

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
    textField: {
        marginBottom:30,
        marginLeft: 20,
        width: "60%",
    },
    searchfield: {
        marginBottom:30,
        marginLeft:20,
        width:"100%",
        float: "left"
    },
    searchboxField: {
      marginTop:30,
        width:"55%",
        float: "left"

    },
    mapIcon:{
        marginLeft:'15px',
        height:'10px',
        width:'50%'
    },
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
    error: {
        textAlign: 'center',
        color: '#ff7700',
        marginTop: '10px',
        marginBottom: '0px',
    },
    pickerWithIcon: {
        width: '50%',
        float: 'left',
    },
    mapIcon: {
        width: '60% !important',
        overflow: 'hidden',
        float: 'right'
    },
    visibilityIcon:{
        color:'#D3D3D3',
    },
    datePicker: {
        width: '60% !important',
        overflow: 'hidden',
        float: 'left',
    },
    timePicker: {
        width: '60% !important',
        overflow: 'hidden',
        float: 'left',
    },
    pickerTextField: {
        fontSize: '14px !important',
        height: '35px !important',
        widht: 'auto',
        lineHeight: '34px',
    },
    dateHeader: {
        paddingLeft: '20px',
        marginBottom: '0px',
        float: 'left',
        width: '50%',
        fontSize: '11px',
        color: '#A4A4A4',
    },
    timeHeader: {
        float: 'left',
        width: '50%',
        marginBottom: '0px',
        fontSize: '11px',
        color: '#A4A4A4',
    },
    icons: {
        marginTop: '4px',
        marginRight: '5px',
        width: '20px',
        height: 'auto',
        float: 'left',
        color: '#A4A4A4',
    },

    inviteTextField: {
        width: '100%',
    },
    overButton: {
        height: '100%',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
    },
};
const buttonStyle = {
    float: 'right',
    marginBottom: '15px',
};

function Transition(props) {
    return <Slide direction="up" {...props} />;
}


class CreateEventScreen extends React.Component {

    constructor(props) {
        super();
        const params = new URLSearchParams(props.location.search);

        let defaultDate = moment().add(30, 'm').toDate();

        this.state = {
            open: true,
            name: params.get('name') || "",
            visible: params.get('visible') || false,
            date: params.get('date') || defaultDate,
            invitedUsers: params.get('invitedUsers') || [],
            invitedTeams: params.get('invitedTeams') || [],
            invitedTeamMember: params.get('teamMember') || [],
            location: params.get('location') || "",
            error: "",
        };
    }

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
    };

    handleAccept = () => {
        let created = this.state.created;

        let invitedUsers = "";
        if(this.state.invitedUsers)
            invitedUsers += this.state.invitedUsers;

        if(this.state.invitedTeamMember) {
            if(invitedUsers)
                invitedUsers += ",";
            invitedUsers += this.state.invitedTeamMember;
        }

        let invitedUsersArray = [];
        if(invitedUsers)
            invitedUsersArray = invitedUsers.split(",")

        createEvent(this.state.location, this.state.date, invitedUsersArray, this.state.visible,
            (response) => {
                if(response.status === 201) {
                    eventListNeedReload();
                    getHistory().push('/event');
                } else {
                    this.setState({error: response.response.data});
                }
            },
            (error) => {
                if(error && error.response)
                    this.setState({error: error.response.data});
                else
                    console.log(error);
            });
    };

    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleChange = (event) => {
        let target = event.target;
        this.setState({
            [target.id]: target.value,
        });
    }

    handleDate = (event, date) => {
        let newDate = moment(date);
        let dateBefore = moment(this.state.date);
        newDate.hour(dateBefore.hour());
        newDate.minute(dateBefore.minute());

        this.setState({ date: newDate.toDate() });
    }

    handleTime = (event, date) => {
        this.setState({ date: date });
    }

    handleVisibility = name => event =>{
        this.setState({ [name]: event.target.checked });
    }
    checkVisibility = () => {
        if(this.state.visible){
            return true;
        }
    }
    handleLocationChange= (location) => {
        this.setState({
            location: location,
        });
        console.log("Location: ", location);
        console.info("längengrad: "+location.l)
    }

    render() {
        this.parseUrl();
        const { classes } = this.props;
        const error = this.state.error;
        let invited = this.state.invitedUsers + "," + this.state.invitedTeams;
        let buttonEnabled = false;
        if(this.state.location)
            buttonEnabled = true;

        return (
            <Dialog
                title="Create Event"closeIconAbsolute
                closeUrl="/event"
                paddingBottom={'48px'}
            >
                <div className={classes.overButton}>
                    {(error
                            ? <p className={classes.error}>{error}</p>
                            : ""
                    )}
                    {/*<form noValidate autoComplete="on" >*/}
                       <FormGroup row>

                           <GoogleSuggest
                               className={classes.searchboxField}
                               id="location"
                               label="Location"
                               value={this.state.location}
                               placeholder="Add an Location ..."
                               float="left"
                               onChange={this.handleLocationChange}
                           />

                        <Link className={classes.mapIcon}
                              float="right"
                              to={{pathname: "/event/create/map", query: {
                                  location: this.state.location,}}}
                              location={ this.state.location}
                        >   <MapIcon disabled={false} className={classes.mapIcon} />
                        </Link>
                       </FormGroup>
                    {/*</form>*/}
                    <div>
                        <p className={classes.dateHeader}>Date</p><p className={classes.timeHeader}>Time</p>
                        <div className={classes.pickerWithIcon}>
                            <Today viewBox="-2 -4 26 26" className={classes.icons} style={{marginLeft: '18px'}} />
                            <DatePicker
                                className={classes.datePicker}
                                onChange={this.handleDate}
                                value={this.state.date}
                                textFieldStyle={styles.pickerTextField}
                            />
                        </div>
                        <div className={classes.pickerWithIcon}>
                            <Schedule viewBox="-2 -4 26 26" className={classes.icons}/>
                            <TimePicker
                                className={classes.timePicker}
                                onChange={this.handleTime}
                                value={this.state.date}
                                format="24hr"
                                textFieldStyle={styles.pickerTextField}
                            />
                        </div>
                    </div>
                    <ExpansionPanel>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon color='primary' />}>
                            <Typography className={classes.heading}>Invite Somebody & Incognito Mode </Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FormGroup row>
                                <Link className={classes.inviteTextField}
                                      to={{pathname: "/event/create/invite",  query: {
                                        source: "/event/create",
                                        invitedUsers: this.state.invitedUsers,
                                    }}}>
                                    <TextField
                                        InputLabelProps={{
                                            shrink: true,
                                        }}
                                        id="invitation"
                                        label="Participants"
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
                                    control={
                                        <Switch
                                            float ="left"
                                            color = "primary"
                                            checked={this.state.visible}
                                            onChange={this.handleVisibility("visible")}
                                            value="visible"
                                            icon={<VisibilityIcon className={classes.visibilityIcon}/>}
                                            checkedIcon={<VisibilityOffIcon/>}
                                        />
                                    }
                                    label={"Make Event Secret"}

                                />
                            </FormGroup>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                </div>
                <Button disabled={!buttonEnabled} variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                    Create Event
                </Button>
            </Dialog>
        );
    }
}

CreateEventScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(CreateEventScreen);