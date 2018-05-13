import React from 'react';
import Button from 'material-ui/Button';
import Menu, { MenuItem } from 'material-ui/Menu';
import MenuIcon from '@material-ui/icons/Menu';
import List from "material-ui/es/List/List";
import Divider from "material-ui/es/Divider/Divider";
import SwipeableDrawer from "material-ui/es/SwipeableDrawer/SwipeableDrawer";
import {withStyles} from "material-ui";

const styles = {
    list: {
        width: 250,
    },
};
//Only in Chrome not in Firefox
class LunchMenu extends React.Component {

    state = {
        anchorEl: null,
    };

    handleClick = event => {
        this.setState({ anchorEl: event.currentTarget });
    };

    handleClose = () => {
        this.setState({ anchorEl: null });
    };

    render() {
        const { anchorEl } = this.state;

        return (
            <div>
                <Button
                    aria-owns={anchorEl ? 'simple-menu' : null}
                    aria-haspopup="true"
                    onClick={this.handleClick}
                >
                    <MenuIcon/>
                </Button>
                <Menu
                    id="simple-menu"
                    anchorEl={anchorEl}
                    open={Boolean(anchorEl)}
                    onClose={this.handleClose}
                >
                    <MenuItem onClick={this.handleClose}>Profile</MenuItem>
                    <MenuItem onClick={this.handleClose}>My account</MenuItem>
                    <MenuItem onClick={this.handleClose}>Logout</MenuItem>
                </Menu>
            </div>
        );
    }
}

export default withStyles(styles) (LunchMenu);