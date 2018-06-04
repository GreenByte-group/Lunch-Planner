import React from 'react';
import {Button, List, ListItem, Divider, withStyles, Drawer, IconButton, MenuItem, FormControlLabel, Switch, TextField, ListItemSecondaryAction} from '@material-ui/core';
import EventIcon from '@material-ui/icons/LocalDining';
import SocialIcon from '@material-ui/icons/Group';
import NotificationIcon from '@material-ui/icons/NotificationsNone';
import SignOutIcon from '@material-ui/icons/ExitToApp';
import OptionIcon from '@material-ui/icons/Build';
import {Link} from "react-router-dom";
import SearchIcon from '@material-ui/icons/Search';

const styles = {
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
    }
};
class Search extends React.Component {

    constructor(){
        super();

        this.handleClick = this.handleClick.bind(this);

        this.state = {
            popupVisible: false,
            visible: false,
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

    handleVisibility = name => event =>{
        this.setState({
            [name]: event.target.checked,
            popupVisible : false,
        });
    }

    render() {

        const { classes } = this.props;
        return (

            <div ref={node => { this.node = node; }}>
                <Button
                    onClick={this.handleClick}
                >
                    <SearchIcon style={{color: "white"}}/>
                </Button>
                {this.state.popupVisible && (
                    <Drawer anchor="top" open={this.state.popupVisible}>
                        <div
                            tabIndex={0}
                            role="button"
                        >
                            <div className={classes.list}>
                                <List className ={classes.menu}>
                                    <ListItem
                                        role={undefined}
                                        dense
                                        button
                                        className={classes.listItem}>
                                        <TextField
                                            id="service"
                                            label="Service"
                                            placeholder="What do you want?"
                                            multiline
                                            className={classes.textField}
                                            onChange={this.handleChange}
                                        />
                                        <ListItemSecondaryAction>
                                            <IconButton onClick={this.handleSearch}>
                                                <SearchIcon />
                                            </IconButton>
                                        </ListItemSecondaryAction>
                                    </ListItem>
                                    <Link to="/event">
                                        <MenuItem>
                                            <EventIcon className={classes.icon}/>
                                            Event
                                        </MenuItem>
                                    </Link>
                                    <Link to="/social">
                                        <MenuItem>
                                            <SocialIcon className={classes.icon}/>
                                            Social
                                        </MenuItem>
                                    </Link>
                                    <Divider />
                                    <Link to="/notifications">
                                        <MenuItem>
                                            <NotificationIcon className={classes.icon}/>
                                            Notifications
                                        </MenuItem>
                                    </Link>
                                    <MenuItem>
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
                                            label="No notifications today"
                                        />
                                    </MenuItem>
                                    <Divider />
                                    <Link to="/options">
                                        <MenuItem>
                                            <OptionIcon className={classes.icon}/>
                                            Options
                                        </MenuItem>
                                    </Link>
                                    <Link to="/signout">
                                        <MenuItem>
                                            <SignOutIcon className={classes.icon}/>
                                            Sign Out
                                        </MenuItem>
                                    </Link>
                                </List>
                            </div>
                        </div>
                    </Drawer>
                )}
            </div>
        );
    }
}

export default withStyles(styles) (Search);