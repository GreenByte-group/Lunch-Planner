import React from 'react';
import {Button, MenuItem, List, Divider, withStyles, FormControlLabel, Switch, Drawer, Avatar} from '@material-ui/core';
import PlaceIcon from '@material-ui/icons/Place';
import EventIcon from '@material-ui/icons/LocalDining';
import SocialIcon from '@material-ui/icons/Group';
import NotificationIcon from '@material-ui/icons/NotificationsNone';
import SignOutIcon from '@material-ui/icons/ExitToApp';
import OptionIcon from '@material-ui/icons/Settings';
import MenuIcon from '@material-ui/icons/Menu';
import {Link} from "react-router-dom";
import {getUsername} from "./authentication/Authentication";
import {getUser} from "./User/UserFunctions";
import {doLogout} from "./LoginFunctions";
import {getHistory} from "../utils/HistoryUtils";

const styles = {
    list: {
        width: 250,
        color:"white",
    },
    profile:{
        fontSize: 10,
        textAlign: "center",
        backgroundColor: "darkGrey",
    },
    avatar:{
        marginTop: 24,
        marginLeft: 24,
        width: 64,
        height: 64,
        marginBottom: 15,

    },
    icon:{
        marginRight: 20,
        color: "#1EA185",
    },
    avatarText:{
        marginLeft: -10,
        marginBottom: -5,
    }

};
class LunchMenu extends React.Component {

    constructor(){
        super();

        this.handleClick = this.handleClick.bind(this);

        this.state = {
            popupVisible: false,
            visible: false,
            username: getUsername(),
            email: "",
        };

    }

    componentDidMount() {
        getUser(getUsername(), (response) => {
            if(response.status === 200) {
                this.setState({
                    email: response.data.eMail,
                })
            }
        })
    }

    signOut = () => {
        doLogout();
        getHistory().push("/login");
    };

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

        let name = getUsername();
        return (

            <div ref={node => { this.node = node; }}>
                <Button
                    onClick={this.handleClick}
                >
                    <MenuIcon style={{color: "white"}}/>
                </Button>
                {this.state.popupVisible && (
                    <Drawer open={this.state.popupVisible}>
                        <div
                            tabIndex={0}
                            role="button"
                        >
                            <div className={classes.list}>
                                <List className={classes.profile}>
                                    <Avatar alt={name} className={classes.avatar} >{name.charAt(0)}</Avatar>
                                    <p className={classes.avatarText}>{this.state.username} ● {this.state.email}</p>
                                </List>
                                <Divider />
                                <List className ={classes.menu}>
                                    <Link to="/location">
                                        <MenuItem>
                                            <PlaceIcon className={classes.icon}/>
                                            Places
                                        </MenuItem>
                                    </Link>
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
                                    <MenuItem onClick={this.signOut}>
                                        <SignOutIcon className={classes.icon}/>
                                        Sign Out
                                    </MenuItem>
                                </List>
                            </div>
                        </div>
                    </Drawer>
                )}
            </div>
        );
    }
}

export default withStyles(styles) (LunchMenu);