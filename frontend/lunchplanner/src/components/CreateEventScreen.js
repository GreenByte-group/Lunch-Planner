import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import Button from 'material-ui/Button';
import Dialog from './Dialog';
import IconButton from 'material-ui/IconButton';
import Typography from 'material-ui/Typography';
import Slide from 'material-ui/transitions/Slide';
import TextField from "material-ui/es/TextField/TextField";
import ExpansionPanel, {ExpansionPanelSummary, ExpansionPanelDetails,} from 'material-ui/ExpansionPanel';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Switch from 'material-ui/Switch';
import AddIcon from '@material-ui/icons/Add';
import {FormGroup, FormControlLabel,} from 'material-ui/Form';
import {createEvent} from "./CreateEventFunctions";
import {DatePicker, TimePicker} from 'material-ui-old';

import 'react-datepicker/dist/react-datepicker-cssmodules.css';
import "../assets/CreateEventScreen.css"
import {Link} from "react-router-dom";
import {Today, Schedule} from "@material-ui/icons";
import {eventListNeedReload, needReload} from "./Event/EventList";
import moment from "moment";

import {getHistory} from "../utils/HistoryUtils";

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
        width: "90%",
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
            location: params.get('location') || "",
            error: "",
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
        let created = this.state.created;

        createEvent(this.state.location, this.state.date, this.state.invitedUsers, this.state.visible,
            (response) => {
                if(response.status === 201) {
                    eventListNeedReload();
                    getHistory().push('/event');
                } else {
                    this.setState({error: response.response.data});
                }
            },
            (error) => {
                if(error)
                    this.setState({error: error.response.data});
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

    render() {
        this.parseUrl();
        const { classes } = this.props;
        const error = this.state.error;

        let buttonEnabled = false;
        if(this.state.location)
            buttonEnabled = true;

        return (
            <div>
                <Dialog
                    title="Create Event"closeIconAbsolute
                    closeUrl="/event"
                >
                    {(error
                            ? <p className={classes.error}>{error}</p>
                            : ""
                    )}
                    <form className={classes.container} noValidate autoComplete="on" >
                        <TextField
                            id="location"
                            label="Location"
                            value={this.state.location}
                            className={classes.textField}
                            placeholder ="Add an Location ..."
                            onChange={this.handleChange}
                            margin="normal"
                        />

                        {/*<TextField*/}
                            {/*id="description"*/}
                            {/*label="Description"*/}
                            {/*placeholder="Description"*/}
                            {/*multiline*/}
                            {/*className={classes.textField}*/}
                            {/*style={{marginTop:-100}}*/}
                        {/*/>*/}
                    </form>
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
                            <Typography className={classes.heading}>Invite & Change Vibility</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FormGroup row>
                                <TextField
                                    style={{width: 200}}
                                    id="invitation"
                                    label="Participants"
                                    placeholder ="Invite People"
                                    value={this.state.invitedUsers}
                                />
                                <Link to={{pathname: "/event/create/invite",  query: {
                                        source: "/event/create",
                                        invitedUsers: this.state.invitedUsers,
                                    }}}>

                                    <IconButton>
                                        <AddIcon />
                                    </IconButton>
                                </Link>

                                <FormControlLabel
                                    control={
                                        <Switch
                                            float ="left"
                                            color = "primary"
                                            checked={this.state.visible}
                                            onChange={this.handleVisibility("visible")}
                                            value="visible"
                                        />
                                    }
                                    label="Only visible if invited"
                                />
                            </FormGroup>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                </Dialog>
                <Button disabled={!buttonEnabled} variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                    Create Event
                </Button>
            </div>
        );
    }
}

CreateEventScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(CreateEventScreen);