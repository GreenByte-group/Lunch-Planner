import PropTypes from "prop-types";
import {withStyles} from "material-ui/styles/index";
import TextField from "material-ui/es/TextField/TextField";
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
import {Today, Schedule} from "@material-ui/icons/es/index";
import Chip from "material-ui/es/Chip/Chip";
import Avatar from "material-ui/es/Avatar/Avatar";
import moment from "moment/moment";
import Button from "material-ui/es/Button/Button";
import ServiceIcon from "@material-ui/icons/Toc"

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
        },

    };

class EventScreen extends React.Component {

    constructor(props) {
        super();
        let eventId = props.match.params.eventId;
        this.state = {
            id:eventId,
            open: true,
            isAdmin: false,
            name:"",
            location:"",
            monthDay: null,
            time: null,
            description: "",
            //TODO invited people
            people:[],
            accepted: true,
        };
    }

    componentDidMount() {
        //TODO wenn die props nicht übergebn wurden ein GET aufrufen, um die Daten zu holen

        let eventName, description, monthDay, time, people, accepted, location;
        if(this.props.location.query && this.props.location.query.eventName ) {
            eventName = String(this.props.location.query.eventName);
            this.setState({
                name: eventName,
            });
        }
        if(this.props.location.query && this.props.location.query.description) {
            description = String(this.props.location.query.description);
            this.setState({
                description: description,
            });
        }
        if(this.props.location.query && this.props.location.query.monthDay) {
            monthDay = this.props.location.query.monthDay;
            this.setState({
                monthDay: monthDay,
            });
        }
        if(this.props.location.query && this.props.location.query.time) {
            time = this.props.location.query.time;
            this.setState({
                time: time,
            });
        }
        if(this.props.location.query && this.props.location.query.people) {
            people = this.props.location.query.people;
            this.setState({
                people: people,
            });
        }
        if(this.props.location.query && this.props.location.query.accepted) {
            accepted = Boolean(this.props.location.query.accepted);
            this.setState({
                accepted: accepted,
            });
        }
        if(this.props.location.query && this.props.location.query.location) {
            location = String(this.props.location.query.location);
            this.setState({
                location: location,
            });
        }
    }


    render() {
        const { classes } = this.props;
        const error = this.state.error;
        let name = this.state.name;
        let description = this.state.description;
        // time and date
        let time = this.state.time;
        let location = this.state.location;
        let people = this.state.people;
        let monthDay = this.state.monthDay;
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
                    <p className={classes.date}><Today viewBox="-5 -5 27 27" className={classes.icons} /> {monthDay} <Schedule viewBox="-5 -5 27 27" className={classes.icons}/> {time}</p>
                        <TextField
                            id="textarea"
                            label="Description"
                            value={description}
                            placeholder="Description"
                            multiline
                            className={classes.textField}
                            margin="normal"
                        />
                    <div style={{marginLeft:20}}>
                        Participants
                        <br />
                        {people.map(function(p){
                            let peopleShortcut = p.short;
                            return <Chip
                                avatar={<Avatar >{peopleShortcut}</Avatar>}
                                label={p.userName}
                                className={classes.chip}
                            />
                        })}

                    </div>
                    <div style={{marginLeft:20}}>Service List</div>
                    <Link to={{pathname:`/event/${this.state.id}/service`}}>
                        <ServiceIcon style={{marginLeft:20}}/>
                    </Link>
                    <IconButton>
                        <AcceptedButton/>
                    </IconButton>

                </Dialog>
            </div>
        );
    }
}
EventScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(EventScreen);

//                        <p className={classes.date}><Today viewBox="-5 -5 27 27" className={classes.icons} /> {monthDay} <Schedule viewBox="-5 -5 27 27" className={classes.icons}/> {time}</p>
