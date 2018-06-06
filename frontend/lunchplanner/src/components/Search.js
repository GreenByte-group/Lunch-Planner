import React from 'react';
import {Button, List, ListItem, Divider, withStyles, Drawer, IconButton, MenuItem, FormControlLabel, Switch, TextField, ListItemSecondaryAction} from '@material-ui/core';
import EventIcon from '@material-ui/icons/LocalDining';
import SocialIcon from '@material-ui/icons/Group';
import NotificationIcon from '@material-ui/icons/NotificationsNone';
import SignOutIcon from '@material-ui/icons/ExitToApp';
import OptionIcon from '@material-ui/icons/Build';
import {Link} from "react-router-dom";
import SearchIcon from '@material-ui/icons/Search';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from "@material-ui/core/es/DialogTitle/DialogTitle";
import DialogContent from "@material-ui/core/es/DialogContent/DialogContent";
import DialogActions from "@material-ui/core/es/DialogActions/DialogActions";
import {getTeams} from "./Team/TeamFunctions";
import {getEvents} from "./Event/EventFunctions";

const styles =  theme => ({
    root:{
        height: 100,
        width: "max-content",
    },
    list: {
        width: "100%",
        color:"white",
        height: 50,
    },
    listItem: {
        width: '100%',
    },
    icon:{
        float: "right",
        marginRight: 20,
    },
    textField: {
        marginBottom: 30,
        width: 300,
    },
    menu: {
        width: 200,
    },
    input: {
        display: 'none',
    },
    button: {
        margin: theme.spacing.unit,
    },
});
class Search extends React.Component {

    constructor(props){
        super();

        //this.handleClick = this.handleClick.bind(this);

        this.state = {
            popupVisible: false,
            visible: false,
            open: props.open,
            search: "",
            city: "",
            teams: [],
            team: "",
            cancel: props.cancel,
        };
        this.setState()
    }

    handleChange = name => event => {
        this.setState({
            [name]: event.target.value,
        });
    };


    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleClose = () => {
        console.log(this.state.open, "open");
        this.setState({ open: false });
    };

    handleSearch = () => {
        console.log("handle Search")
        getEvents(this.state.search);
    }
    render() {

        const { classes } = this.props;
        let teams = this.state.teams;
        return (

            <Drawer
                open={this.state.open}
                onClose={this.handleClose}
                anchor='top'
            >
                <DialogContent >
                    <div className={classes.list}>
                        <List className ={classes.menu}>
                            <ListItem
                                dense
                                className={classes.listItem}>
                                <form noValidate autoComplete="on" >
                                    <TextField
                                        id="Search"
                                        label="Search"
                                        placeholder="Search for ..."
                                        multiline
                                        className={classes.textField}
                                        onChange={this.handleChange('search')}
                                        margin="normal"
                                    />
                                </form>
                                <IconButton>
                                    <SearchIcon className={classes.icon} onClick={this.handleSearch}/>
                                </IconButton>

                            </ListItem>
                        </List>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button variant="contained" color="secondary" className={classes.button} onClick={this.state.cancel}>
                        Cancel
                    </Button>
                    <Link to={{pathname:'/event/create'}}>
                        <Button variant="contained" color="secondary" className={classes.button}>
                            Create Event
                        </Button>
                    </Link>
                </DialogActions>
            </Drawer>
        );
    }
}

export default withStyles(styles) (Search);