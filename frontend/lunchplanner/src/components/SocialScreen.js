import React from 'react';
import PropTypes from 'prop-types';
import SwipeableViews from 'react-swipeable-views';
import {withStyles, AppBar, Tabs, Tab, Typography} from '@material-ui/core';
import Teamlist from "./Team/TeamList";
import BottomNavigationBar from "./BottomNavigationBar";
import {setAuthenticationHeader} from "./authentication/LoginFunctions";


function TabContainer({ children, dir }) {
    return (
        <Typography component="div" dir={dir} style={{ padding: 0 }}>
            {children}
        </Typography>
    );
}

TabContainer.propTypes = {
    children: PropTypes.node.isRequired,
    dir: PropTypes.string.isRequired,
};

const styles = theme => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        //width: 1500,,
        position: 'relative',
        height: '100%',
        display: 'flex',
        flexDirection: 'column'
    },
    fab: {
        position: 'absolute',
        bottom: theme.spacing.unit * 2,
        right: theme.spacing.unit * 2,
    },
    whiteSymbol: {
        color: theme.palette.common.white
    },
    tab: {
        fontFamily: "Work Sans",
        fontWeight: '600',
        letterSpacing: '0.65px',
        fontSize: '13px',
    },
    swipeViews: {
        height: '100%',
        overflowY: 'auto',
        maxWidth: '1024px',
        width: '100%',
        marginLeft: 'auto',
        marginRight: 'auto',
    },
});

class SocialScreen extends React.Component {

    constructor(props) {
        super();
        this.state = {
            value: this.getTabValue(props),
        };

        setAuthenticationHeader();
    }

    getTabValue(props) {
        if(props.location) {
            const params = new URLSearchParams(props.location.search);
            let tab = params.get('tab');
            if (tab != null && tab !== undefined) {
                return tab;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    parseUrl = (props) => {
        const params = new URLSearchParams(props.location.search);
        let tab = params.get('tab');
        if(tab != null && tab !== undefined && tab !== this.state.value) {
            this.setState({
                value: tab,
            });
        }
    };

    handleChange = (event, value) => {
        this.setState({ value: value });
    };

    handleChangeIndex = index => {
        this.setState({ value: index });
    };

    render() {
        const { classes, theme } = this.props;

        return (
            <div className={classes.root}>
                <Teamlist/>
            </div>
        );
    }
}

SocialScreen.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(SocialScreen);