import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import { BottomNavigation, BottomNavigationAction } from '@material-ui/core';
import PlaceIcon from '@material-ui/icons/Place';
import EventIcon from '@material-ui/icons/LocalDining';
import SocialIcon from '@material-ui/icons/Group';
import {Link} from "react-router-dom";
import {getHistory} from "../utils/HistoryUtils";

const styles = {
    root: {
        boxShadow: '0 -2px 4px 0 rgba(51,51,51,0.1), 0 2px 4px 0 rgba(0,0,0,0.5)',
        position: 'relative',
        bottom: 0,
        width: '100%',
    }
};

class BottomNavigationBar extends React.Component {

    constructor(props) {
        super();

        let value = props.value;

        this.state = {
            value: value,
        };
    }

    componentWillReceiveProps(newProps) {
        if(newProps.value !== undefined && newProps.value !== null) {
            this.setState({
                value: newProps.value,
            })
        }
    }

    handleChange = (event, value) => {
        this.setState({ value });

        switch(value) {
            case 0:
                getHistory().push("/app/location");
                break;
            case 1:
                getHistory().push("/app/event");
                break;
            case 2:
                getHistory().push("/app/team");
                break;
        }
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
                    <BottomNavigationAction label="Places" icon={<PlaceIcon />} />
                    <BottomNavigationAction label="Events" icon={<EventIcon />} />
                    <BottomNavigationAction label="Teams" icon={<SocialIcon />}/>
                </BottomNavigation>
        );
    }
}

BottomNavigationBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

//export default withTheme(MuiThemeProvider) (BottomNavigationBar);
export default withStyles(styles)(BottomNavigationBar);