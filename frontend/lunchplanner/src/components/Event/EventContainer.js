import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import SwipeableViews from 'react-swipeable-views';
import AppBar from '@material-ui/core/AppBar';
import {Tabs, Tab, Typography} from '@material-ui/core';
import {getEvents} from "./EventFunctions";
import EventList from "./EventList";
import LocationList from "./LocationList";
import {getUsername} from "../authentication/LoginFunctions";


function TabContainer({ children, dir }) {
    return (
        <Typography component="div" dir={dir} style={{ padding: 0, height: '100%' }}>
            {children}
        </Typography>
    );
}

TabContainer.propTypes = {
    children: PropTypes.node.isRequired,
    dir: PropTypes.string.isRequired,
};

export let needReload = false;

export function eventListNeedReload() {
    needReload = true;
}

const styles = theme => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        //width: 1500,,
        position: 'relative',
        height: '100%',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
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

class EventContainer extends React.Component {
    constructor(props) {
        super();
        this.state = {
            value: 0,
            search: props.search,
            events: [],
        };
    }

    componentDidMount() {
        this.setState({
            search: this.props.search,
            joinedAndInvitedEvents: [],
        });

        this.loadEvents(this.props.search);
    }

    componentWillReceiveProps(newProps) {
        if(needReload) {
            needReload = !needReload;
            this.loadEvents();
        }
        if(newProps.search !== this.state.search){
            this.setState({
                search: newProps.search,
            });
            this.loadEvents(newProps.search);
        }
    }

    loadEvents(search) {
        if(search === null || search === undefined)
            search = this.state.search;

        getEvents(search, (response) => {
            this.setState({
                events: response.data,
            });
            this.sort();
        });

    }

    sort(){
        let events = this.state.events;
        let joinedAndIvitedEvents = [];
        events.forEach(function(listValue) {
            let accepted = false;
            let invited = false;
            let username = getUsername();
            listValue.invitations.forEach((value) => {
                if (value.userName === username) {
                    invited = true;
                    joinedAndIvitedEvents.push(listValue);
                    if (value.answer === 0) {
                        accepted = true;
                    }
                }
            });
        });
        this.setState({
            joinedAndInvitedEvents: joinedAndIvitedEvents,
        });
    };

    handleChange = (event, value) => {
        this.setState({ value });
    };

    handleChangeIndex = index => {
        this.setState({ value: index });
    };

    render() {
        const { classes, theme } = this.props;
        if(needReload) {
            needReload = !needReload;
            this.loadEvents();
        }
        return (
            <div className={classes.root}>
                <AppBar position="relative" color="default" >
                    <Tabs
                        value={this.state.value}
                        onChange={this.handleChange}
                        indicatorColor="secondary"
                        textColor="secondary"
                        centered
                        fullWidth
                    >
                        <Tab className={classes.tab} label="PERSONAL" />
                        <Tab className={classes.tab} label="PLACES" />
                        <Tab className={classes.tab} label="BY DATE" />
                    </Tabs>
                </AppBar>
                <SwipeableViews
                    axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                    index={this.state.value}
                    onChangeIndex={this.handleChangeIndex}
                    className={classes.swipeViews}
                >

                    <TabContainer dir={theme.direction}>
                        <EventList events={this.state.joinedAndInvitedEvents}/>
                    </TabContainer>
                    <TabContainer dir={theme.direction}>
                        <LocationList events={this.state.events} />
                    </TabContainer>
                    <TabContainer dir={theme.direction}>
                        <EventList events={this.state.events} />
                    </TabContainer>
                </SwipeableViews>
            </div>
        );
    }
}

EventContainer.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(EventContainer);
