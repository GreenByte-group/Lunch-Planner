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

const styles =  theme => ({
    root:{
      height: 320,
    },
    list: {
        width: "auto",
        color:"white",
        height: 200,
    },
    profile:{
        fontSize: 10,
        textAlign: "center",
        backgroundColor: "darkGrey",
    },
    avatar:{
        marginLeft: 15,
        marginBottom: 15,

    },
    icon:{
        marginRight: 20,
    },
    textField:{
        float: 'left',
        width: '90%',
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
const citys = [
    {value: "Mannheim"},
    {value: "Heidelberg"},
    {value: "Ladenburg"},
];
const teams = [
    {value: "Mannheim"},
    {value: "Heidelberg"},
    {value: "Ladenburg"},
];
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
            team: "",
            clickCancel: props.clickCancel,
        };
    }

    handleClick() {
        if (!this.state.popupVisible) {
            // attach/remove event handler
            document.addEventListener('click', this.handleClick, false);
        } else {
            document.removeEventListener('click', this.handleClick, false);
        }
        this.setState(prevState => ({
            popupVisible: !prevState.popupVisible,
        }));
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
        this.state.clickCancel;
        this.setState({ open: false });
    };
    render() {

        const { classes } = this.props;
        return (

            <Drawer
                open={this.state.open}
                onClose={this.handleClose}
                anchor='top'
            >
                <DialogContent>
                    <div className={classes.list}>
                        <List className ={classes.menu}>
                            <ListItem
                                dense
                                button
                                className={classes.listItem}>
                                <TextField
                                    id="Search"
                                    label="Search"
                                    placeholder="Search for ..."
                                    multiline
                                    className={classes.textField}
                                    onChange={this.handleChange('search')}

                                />
                                <ListItemSecondaryAction>
                                    <IconButton onClick={this.handleSearch}>
                                        <SearchIcon />
                                    </IconButton>
                                </ListItemSecondaryAction>
                            </ListItem>
                            <TextField
                                id="select-city"
                                select
                                label="City"
                                className={classes.textField}
                                value={this.state.city}
                                onChange={this.handleChange('city')}
                                SelectProps={{
                                    MenuProps: {
                                        className: classes.menu,
                                    },
                                }}
                                margin="normal">
                                {citys.map(option => (
                                    <MenuItem key={option.value} value={option.value}>
                                        {option.value}
                                    </MenuItem>
                                ))}
                            </TextField>
                            <TextField
                                id="select-team"
                                select
                                label="Team"
                                className={classes.textField}
                                value={this.state.team}
                                onChange={this.handleChange('team')}
                                SelectProps={{
                                    MenuProps: {
                                        className: classes.menu,
                                    },
                                }}
                                margin="normal">
                                {teams.map(option => (
                                    <MenuItem key={option.value} value={option.value}>
                                        {option.value}
                                    </MenuItem>
                                ))}
                            </TextField>
                        </List>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button variant="contained" color="secondary" className={classes.button} onClick={this.handleClose}>
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