import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import SwipeableViews from 'react-swipeable-views';
import AppBar from '@material-ui/core/AppBar';
import Tabs, {Tab} from '@material-ui/core/Tabs';
import Typography from '@material-ui/core/Typography';
import Appbar from "../Appbar";
import Teamlist from "../Team/TeamList";
import BottomNavigationBar from "../BottomNavigationBar";
import {setAuthenticationHeader} from "../authentication/LoginFunctions";
import NotificationList from "./NotificationList";
import NotificationSettings from "./NotificationSettings";


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
    },
    tabContainer: {
        height: '100%',
    }
});

class NotificationsScreen extends React.Component {

    constructor(props) {
        super();
        this.state = {
            value: this.getTabValue(props),
        };

        setAuthenticationHeader();
    }

    getTabValue(props) {
        const params = new URLSearchParams(props.location.search);
        let tab = params.get('tab');
        if(tab != null && tab !== undefined) {
            return tab;
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
                <Appbar currentScreen="Notifications"/>
                <AppBar position="relative" color="default">
                    <Tabs
                        value={this.state.value}
                        onChange={this.handleChange}
                        indicatorColor="secondary"
                        textColor="secondary"
                        centered
                        fullWidth
                    >
                        <Tab className={classes.tab} label="HISTORY" />
                        <Tab className={classes.tab} label="SETTINGS" />
                    </Tabs>
                </AppBar>
                <SwipeableViews
                    axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                    index={this.state.value}
                    onChangeIndex={this.handleChangeIndex}
                    className={classes.swipeViews}
                >

                    <TabContainer dir={theme.direction} className={classes.tabContainer}>
                        <NotificationList />
                    </TabContainer>
                    <TabContainer dir={theme.direction}>
                        <NotificationSettings />
                    </TabContainer>
                </SwipeableViews>
                <BottomNavigationBar />
            </div>
        );
    }
}

NotificationsScreen.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(NotificationsScreen);