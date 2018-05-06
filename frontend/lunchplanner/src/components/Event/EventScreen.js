import PropTypes from "prop-types";
import axios from "axios"
import {withStyles} from "material-ui/styles/index";
import TextField from "material-ui/es/TextField/TextField";
import DatePicker from 'react-datepicker';
import AcceptedButton from "./AcceptedButton";
import React from 'react';
import Slide from 'material-ui/transitions/Slide';
import Dialog from 'material-ui/Dialog';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import {Link} from "react-router-dom";
import Typography from 'material-ui/Typography';
import IconButton from 'material-ui/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import {HOST} from "../../Config";

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

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

class EventScreen extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            open: true,
            isAdmin: false,
            name:"",
            locationId:0,
            timeStart: 0,
            timeEnd: 1,
            description: "",
            //TODO invited people
            people:'Can, Santino, Felix, Martin',
        };
    }

    componentDidMount() {
        let url = HOST + "event/" + this.props.eventId;

        axios.get(url)
            .then((response) =>{
                this.setState({
                    name: response.data.name,
                    // Müsste hier nicht besser die Location zurückkommen?
                    locationId: response.data.locationId,
                    timeStart: response.data.timeStart,
                    timeEnd: response.data.timeEnd,
                    description: response.data.description
                })
            })
    }


    render() {

        const { classes } = this.props;
        const error = this.state.error;
        let name = this.state.name;
        let description = this.state.description;
        // time and date
        let timeStart = this.state.timeStart;
        let location = this.state.locationId;
        let people = this.state.people;

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
                                {name}
                            </Typography>

                        </Toolbar>
                    </AppBar>
                    {(error
                            ? <p className={classes.error}>{error}</p>
                            : ""
                    )}
                        <TextField
                            id="location"
                            label="Location"
                            value={location}
                            className={classes.textField}
                            placeholder ="Add an Location ..."
                            onChange={this.handleChange}
                            margin="normal"
                        />
                        <DatePicker
                            selected={this.state.date}
                            onChange={this.handleDate}
                            value={timeStart}
                            showTimeSelect
                            timeFormat="HH:mm"
                            timeIntervals={15}
                            dateFormat="LLL"
                            timeCaption="time"
                        />
                        <TextField
                            id="textarea"
                            label="Description"
                            value={description}
                            placeholder="Description"
                            multiline
                            className={classes.textField}
                            margin="normal"
                        />
                    <div>{people}</div>
                    <div>Service</div>
                    <AcceptedButton/>
                </Dialog>
            </div>
        );
    }
}
EventScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(EventScreen);
