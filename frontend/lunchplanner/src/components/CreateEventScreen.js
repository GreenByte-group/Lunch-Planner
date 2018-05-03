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
import TimePicker from 'material-ui-time-picker';
import ExpansionPanel, {ExpansionPanelSummary, ExpansionPanelDetails,} from 'material-ui/ExpansionPanel';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Switch from 'material-ui/Switch';
import Input, { InputLabel, InputAdornment } from 'material-ui/Input';
import AddIcon from '@material-ui/icons/Add';
import {FormLabel, FormControl, FormGroup, FormControlLabel,FormHelperText,} from 'material-ui/Form';
import {createEvent} from "./CreateEventFunctions";

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
    textField: {
        marginTop:1,
        marginBottom:1,
        marginLeft: 20,
        //marginRight: 20,
        width: "90%",
    },
    timePicker:{
        marginLeft: 20,
        //marginRight: 20,
        width: "30%",
    },
    dateField:{
        marginLeft: 20,
        //marginRight: 20,
        width: "30%",
    },
    button:{
        color: "white",
        position: "fixed",
        bottom:0,
        width: "100%"
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
    state = {
        open: false,
        name: "",
        time:"",
        visible: false,
        date: "",
        member:"",
        location:0,
    };

    handleClickOpen = () => {
        console.log("handleClickOpen");
        this.setState({ open: true });
    };

    handleClose = () => {
        console.log("handleClose");
        createEvent(this.state.name, this.state.location, this.state.date, this.state.time, this.state.member, this.state.visible);
    };

    handleChange = name => event => {
        this.setState({[name]: event.target.value,});
    };

    handleTime = name => event =>{
        this.setState({ [name]: event.target.time });
    }

    handleDate =  name => event =>{
        this.setState({ [name]: event.target.date });
    }

    handleVisibility = name => event =>{
        this.setState({ [name]: event.target.checked });
    }

    render() {
        const { classes } = this.props;
        return (
            <div>
                <Button onClick={this.handleClickOpen} style={buttonStyle}>
                    <FloatingActionButton/>
                </Button>
                <Dialog
                    fullScreen
                    open={this.state.open}
                    onClose={this.handleClose}
                    transition={Transition}
                >
                    <AppBar className={classes.appBar} color ="white">
                        <Toolbar>
                            <IconButton color="inherit" onClick={this.handleClose} aria-label="Close">
                                <CloseIcon />
                            </IconButton>
                            <Typography variant="title" color="inherit" className={classes.flex}>
                                Create Event
                            </Typography>

                        </Toolbar>
                    </AppBar>
                    <form className={classes.container} noValidate autoComplete="off" >
                        <TextField
                            id="name"
                            label="Name"
                            className={classes.textField}
                            value={this.state.name}
                            onChange={this.handleChange('name')}
                            margin="normal"
                        />
                        <TextField
                            id="location"
                            label="Location"
                            className={classes.textField}
                            value={this.state.location}

                            margin="normal"
                        />
                        <TextField
                            id="date"
                            label="Date"
                            type="date"
                            defaultValue={this.state.date}
                            className={classes.dateField}
                            onChange={this.handleChange('date')}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                        <TimePicker

                            className={classes.timePicker}
                            mode='24h'
                            value={this.state.time}
                            onChange={this.handleTime}
                        />

                    </form>
                    <ExpansionPanel>
                        <ExpansionPanelSummary expandIcon={<ExpandMoreIcon />}>
                            <Typography className={classes.heading}>Invite & Change Vibility</Typography>
                        </ExpansionPanelSummary>
                        <ExpansionPanelDetails>
                            <FormControl  classname={classes.invitation}>
                                <FormHelperText id="participants-invitation">Participants</FormHelperText>
                                <Input
                                    placeholder="Invite People"
                                    style={{width: 200}}
                                    id="adornment-participants-invitation"
                                    onChange={this.handleChange}
                                    endAdornment={<InputAdornment position="end"
                                    >
                                        <Button>
                                            <AddIcon/>
                                        </Button>
                                    </InputAdornment>}
                                />
                            </FormControl>
                            <FormControl>
                            <FormControlLabel

                                control={
                                    <Switch
                                        color = "primary"
                                        checked={this.state.visible}
                                        onChange={this.handleVisibility("visible")}
                                        value="visible"
                                    />
                                }
                                label="Only visible if invited"
                            />
                            </FormControl>
                        </ExpansionPanelDetails>
                    </ExpansionPanel>
                    <Button variant="raised" color="secondary" onClick={this.handleClose} className={classes.button}>
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