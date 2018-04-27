import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import BottomNavigation, { BottomNavigationAction } from 'material-ui/BottomNavigation';
import PlaceIcon from '@material-ui/icons/Place';
import EventIcon from '@material-ui/icons/LocalDining';
import SocialIcon from '@material-ui/icons/Group';
import withTheme from "material-ui/es/styles/withTheme";
import MuiThemeProvider from "material-ui/es/styles/MuiThemeProvider";

const styles = {

    root: {
        //width: 500,
        color:"#75a045"

    },
    selected: {

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
                value={value}
                onChange={this.handleChange}
                showLabels
                className={classes.root}
            >
                <BottomNavigationAction style={{active : "#75a045"}}label="Places" icon={<PlaceIcon />} />
                <BottomNavigationAction label="Events" icon={<EventIcon />} />
                <BottomNavigationAction label="Social" icon={<SocialIcon />} />
            </BottomNavigation>
        );
    }
}

BottomNavigationBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

//export default withTheme(MuiThemeProvider) (BottomNavigationBar);
export default withStyles(styles)(BottomNavigationBar);