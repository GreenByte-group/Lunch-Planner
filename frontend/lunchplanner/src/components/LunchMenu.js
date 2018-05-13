import React from 'react';
import Button from 'material-ui/Button';
import MenuIcon from '@material-ui/icons/Menu';
import List from "material-ui/es/List/List";
import Divider from "material-ui/es/Divider/Divider";
import PlaceIcon from '@material-ui/icons/Place';
import EventIcon from '@material-ui/icons/LocalDining';
import SocialIcon from '@material-ui/icons/Group';
import NotificationIcon from '@material-ui/icons/NotificationsNone';
import SignOutIcon from '@material-ui/icons/ExitToApp';
import OptionIcon from '@material-ui/icons/Build';
import {withStyles} from "material-ui";
import Drawer from "material-ui/es/Drawer/Drawer";
import Avatar from "material-ui/es/Avatar/Avatar";
import ListItem from "material-ui/es/List/ListItem";
import IconButton from "material-ui/es/IconButton/IconButton";
import {Link} from "react-router-dom";
import Menu from "material-ui/es/Menu/Menu";
import MenuItem from "material-ui/es/Menu/MenuItem";
import FormControlLabel from "material-ui/es/Form/FormControlLabel";
import Switch from "material-ui/es/Switch/Switch";

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
        marginLeft: 15,
        marginBottom: 15,

    },
    icon:{
        marginRight: 20,
    }
};
class LunchMenu extends React.Component {

    constructor(){
        super();

        this.handleClick = this.handleClick.bind(this);
        this.handleOutsideClick = this.handleOutsideClick.bind(this);

        this.state = {
            popupVisible: false,
            visible: false,
        };

    }

    handleClick() {
        if (!this.state.popupVisible) {
            // attach/remove event handler
            document.addEventListener('click', this.handleOutsideClick, false);
        } else {
            document.removeEventListener('click', this.handleOutsideClick, false);
        }
        this.setState(prevState => ({
            popupVisible: !prevState.popupVisible,
        }));
    }

    handleOutsideClick(e) {
        // ignore clicks on the component itself
        if (this.node.contains(e.target)) {
            return;
        }

        this.handleClick();
    }

    handleVisibility = name => event =>{
        this.setState({ [name]: event.target.checked });
    }

    render() {

        const { classes } = this.props;
        return (

            <div ref={node => { this.node = node; }}>
                <Button
                    onClick={this.handleClick}
                >
                    <MenuIcon/>
                </Button>
                {this.state.popupVisible && (
                    <Drawer open={this.state.popupVisible}>
                        <div
                            tabIndex={0}
                            role="button"
                        >
                            <div className={classes.list}>
                                <List className={classes.profile}>
                                    <Avatar alt="Max Mustermann" className={classes.avatar} >MM</Avatar>
                                    <p>Max Mustermann ● max.mustermann@gmail.com</p>
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

export default withStyles(styles) (LunchMenu);