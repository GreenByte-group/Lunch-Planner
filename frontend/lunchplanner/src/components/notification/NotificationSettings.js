import React from 'react';
import {withStyles} from "@material-ui/core/styles/index";
import SwitchSettingsItem from "../settings/SwitchSettingsItem";
import {List} from "@material-ui/core";
import {getNotificationOptions, sendOptions} from "./NotificationFunctions";
import TimeSettingsItem from "../settings/TimeSettingsItem";

const styles = {
    list: {
        height: '100%',
        padding: '0px',
        margin: '0px',
    },
    hr: {
        margin: '0px 0',
    }
};

class NotificationSettings extends React.Component {

    constructor(props) {
        super();

        this.state = {
            blockAll: false,
            events: true,
            teams: true,
            subscriptions: true,
            blockedForWork: false,
            startWorking: undefined,
            stopWorking: undefined,
            blockUntil: undefined,
        };

        this.getSettings();
    }

    getSettings() {
        getNotificationOptions((response) => {
            if(response.status === 200) {
                this.setState({
                    blockAll: response.data.blockAll,
                    events: !response.data.eventsBlocked,
                    teams: !response.data.teamsBlocked,
                    subscriptions: !response.data.subscriptionsBlocked,
                    blockedForWork: !response.data.blockedForWork,
                    startWorking: (response.data.start_working) ?new Date(response.data.start_working) : undefined,
                    stopWorking: (response.data.stop_working) ? new Date(response.data.stop_working) : undefined,
                    blockUntil: (response.data.blockUntil) ?new Date(response.data.blockUntil) : undefined,
                })
            }
        })
    }

    switchClicked = (id, value) => {
        this.setState({
            [id]: value,
        });

        console.log('send options changed');

        sendOptions({[id]: value});
    };

    render() {
        const {classes} = this.props;

        return (
            <List className={classes.list}>
                <SwitchSettingsItem
                    id='blockAll'
                    title='Block all'
                    description="Don't send me any notifications at all."
                    enabled={true}
                    value={this.state.blockAll}
                    onClick={this.switchClicked}
                />
                <hr className={classes.hr} />
                <SwitchSettingsItem
                    id='events'
                    title='Events'
                    description="Show Notifications for events."
                    enabled={!this.state.blockAll}
                    value={this.state.events}
                    onClick={this.switchClicked}
                />
                <SwitchSettingsItem
                    id='teams'
                    title='Teams'
                    description="Show Notifications for teams."
                    enabled={!this.state.blockAll}
                    value={this.state.teams}
                    onClick={this.switchClicked}
                />
                <SwitchSettingsItem
                    id='subscriptions'
                    title='Subscriptions'
                    description="Get Notifications from subscribed locations"
                    enabled={!this.state.blockAll}
                    value={this.state.subscriptions}
                    onClick={this.switchClicked}
                />
                <hr className={classes.hr} />
                <SwitchSettingsItem
                    id='blockedForWork'
                    title='Enable working time'
                    description="Only receive notifications in your working time"
                    enabled={!this.state.blockAll}
                    value={this.state.blockedForWork}
                    onClick={this.switchClicked}
                />
                <TimeSettingsItem
                    id='startWorking'
                    title='Begin of working time'
                    // description="Only receive notifications in your working time"
                    enabled={this.state.blockedForWork}
                    value={this.state.startWorking}
                    onChange={this.switchClicked}
                />
                <TimeSettingsItem
                    id='stopWorking'
                    title='End of working time'
                    // description="Only receive notifications in your working time"
                    enabled={this.state.blockedForWork}
                    value={this.state.stopWorking}
                    onChange={this.switchClicked}
                />
                <hr className={classes.hr} />
            </List>
        )
    }

}

export default withStyles(styles, { withTheme: true })(NotificationSettings);