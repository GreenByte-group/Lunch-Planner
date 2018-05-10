import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import BottomNavigation, { BottomNavigationAction } from 'material-ui/BottomNavigation';
import PlaceIcon from '@material-ui/icons/Place';
import EventIcon from '@material-ui/icons/LocalDining';
import SocialIcon from '@material-ui/icons/Group';
import {Link} from "react-router-dom";
import IconButton from "material-ui/es/IconButton/IconButton";
import Button from "material-ui/es/Button/Button";

const styles = {
    root: {
        boxShadow: '0 -2px 4px 0 rgba(51,51,51,0.1), 0 2px 4px 0 rgba(0,0,0,0.5)',
        position: 'fixed',
        bottom: 0,
        width: '100%',
    }
};

class BottomNavigationBar extends React.Component {
    state = {
        value: 1,
    };

    handleChange = (event, value) => {
        this.setState({ value });
    };

    render() {
        const { classes } = this.props;
        const { value } = this.state;

        return (
                <BottomNavigation
                    className={classes.root}
                    value={value}
                    onChange={this.handleChange}
                    showLabels
                >
                    <Link to="/location">
                        <Button>
                            <IconButton
                                label="Places">
                                <PlaceIcon />
                            </IconButton>
                            Places
                        </Button>
                    </Link>
                    <Link to="/event">
                        <Button>
                            <IconButton
                                label="Events">
                                <EventIcon />
                            </IconButton>
                            Events
                        </Button>
                    </Link>
                        <Link to="/social">
                        <Button >
                            <IconButton
                                label="Social">
                                <SocialIcon />
                            </IconButton>
                            Social
                        </Button>

                    </Link>
                </BottomNavigation>
        );
    }
}

BottomNavigationBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

//export default withTheme(MuiThemeProvider) (BottomNavigationBar);
export default withStyles(styles)(BottomNavigationBar);