import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import Button from 'material-ui/Button';
import Dialog from 'material-ui/Dialog';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import IconButton from 'material-ui/IconButton';
import Typography from 'material-ui/Typography';
import CloseIcon from '@material-ui/icons/Close';
import Slide from 'material-ui/transitions/Slide';
import FloatingActionButton from "./FloatingActionButton";
import TextField from "material-ui/es/TextField/TextField";
import ExpansionPanel, {ExpansionPanelSummary, ExpansionPanelDetails,} from 'material-ui/ExpansionPanel';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Switch from 'material-ui/Switch';
import AddIcon from '@material-ui/icons/Add';
import {FormGroup, FormControlLabel,FormHelperText,} from 'material-ui/Form';
import {createEvent} from "./CreateEventFunctions";
import DatePicker from 'react-datepicker';
import moment from 'moment';

import 'react-datepicker/dist/react-datepicker-cssmodules.css';
import "../assets/CreateEventScreen.css"
import {Link} from "react-router-dom";

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
    textField: {
        marginTop:20,
        marginBottom:30,
        marginLeft: 20,
        width: "90%",
    },
    button:{
        color: "white",
        position: "fixed",
        bottom:0,
        width: "100%"
    },
    error: {
        textAlign: 'center',
        color: '#ff7700',
        marginTop: '10px',
        marginBottom: '0px',
    }

};
const buttonStyle = {
    float: 'right',
    marginBottom: '15px',
};

function Transition(props) {
    return <Slide direction="up" {...props} />;
}


class CreateEventScreen extends React.Component {
    state = {
        open: false,
        name: "",
        time:null,
        visible: false,
        date: moment(),
        member:"",
        location:0,
        error: "",
    };

    componentDidMount() {
        this.handleClickOpen();
    }

    handleAccept = () => {
        createEvent(this.state.location, this.state.date, this.state.member, this.state.visible,
            (response) => {
                console.log(response);
                if(response.status === 201)
                    this.props.history.push('/event');
                else
                    this.setState({error: response.response.data});
            },
            (error) => {
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

    handleDate =  (date) => {
        this.setState({ date: date });
    }

    handleVisibility = name => event =>{
        this.setState({ [name]: event.target.checked });
    }

    render() {
        const { classes } = this.props;
        const error = this.state.error;

        return (
            <div>
                <Dialog
                    fullScreen
                    open={this.state.open}
                    onClose={this.handleClose}
                    transition={Transition}
                >
                    <AppBar className={classes.appBar} color ="white">
                        <Toolbar>
                            <Link to="/event">
                                <IconButton color="inherit" aria-label="Close">
                                    <CloseIcon />
                                </IconButton>
                            </Link>
                            <Typography variant="title" color="inherit" className={classes.flex}>
                                Create Event
                            </Typography>

                        </Toolbar>
                    </AppBar>
                    {(error
                            ? <p className={classes.error}>{error}</p>
                            : ""
                    )}
                    <form className={classes.container} noValidate autoComplete="on" >
                        <TextField
                            id="location"
                            label="Location"
                            className={classes.textField}
                            placeholder ="Add an Location ..."
                            onChange={this.handleChange}
                            margin="normal"
                        />
                        <DatePicker
                            selected={this.state.date}
                            onChange={this.handleDate}
                            showTimeSelect
                            timeFormat="HH:mm"
                            timeIntervals={15}
                            dateFormat="LLL"
                            timeCaption="time"
                        />
                    </form>
                    <ExpansionPanel>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Invite & Change Vibility</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FormGroup row>
                                <TextField
                                    style={{width: 200}}
                                    id="invitation"
                                    label="Participants"
                                    placeholder ="Invite People"
                                />
                                <Button classname={classes.addButton}>
                                    <AddIcon/>
                                </Button>

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
                    <Button variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                        Create Event
                    </Button>
                </Dialog>
            </div>
        );
    }
}

CreateEventScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(CreateEventScreen);