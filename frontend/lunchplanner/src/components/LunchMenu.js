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

const styles = {
    list: {
        width: 250,
    },
};
//Only in Chrome not in Firefox
class LunchMenu extends React.Component {

    constructor(){
        super();

        this.handleClick = this.handleClick.bind(this);
        this.handleOutsideClick = this.handleOutsideClick.bind(this);

        this.state = {
            popupVisible: false
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
                                    <Avatar alt="Remy Sharp" className={classes.avatar} >MM</Avatar>
                                    <p>Name - Email-Adresse</p>
                                </List>
                                <Divider />
                                <List>
                                    <Link to="/location">
                                        <MenuItem>
                                            <PlaceIcon />
                                            Places
                                        </MenuItem>
                                    </Link>
                                    <Link to="/event">
                                        <MenuItem>
                                            <EventIcon />
                                            Event
                                        </MenuItem>
                                    </Link>
                                    <Link to="/social">
                                        <MenuItem>
                                            <SocialIcon />
                                            Social
                                        </MenuItem>
                                    </Link>
                                    <Divider />
                                    <Link to="/notifications">
                                        <MenuItem>
                                            <NotificationIcon />
                                            Notifications
                                        </MenuItem>
                                    </Link>
                                    <Divider />
                                    <Link to="/options">
                                        <MenuItem>
                                            <OptionIcon />
                                            Options
                                        </MenuItem>
                                    </Link>
                                    <Link to="/signout">
                                        <MenuItem>
                                            <SignOutIcon />
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