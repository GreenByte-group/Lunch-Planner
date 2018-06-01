import React from 'react';
import {withStyles} from "@material-ui/core/styles/index";
import SwitchSettingsItem from "../settings/SwitchSettingsItem";
import {List} from "@material-ui/core";

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
            newEvent: true,
            eventInvitation: true,
            social: true,
            teamInvitation: true,
        }
    }

    switchClicked = (id, value) => {
        //TODO send to server
        this.setState({
            [id]: value,
        });
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
                    id='newEvent'
                    title='New events'
                    description="New Events created by followed."
                    enabled={!this.state.blockAll}
                    value={this.state.newEvent}
                    onClick={this.switchClicked}
                />
                <SwitchSettingsItem
                    id='eventInvitation'
                    title='Event invitations'
                    description="Invitations for events."
                    enabled={!this.state.blockAll}
                    value={this.state.eventInvitation}
                    onClick={this.switchClicked}
                />
                <SwitchSettingsItem
                    id='teamInvitation'
                    title='Team invitations'
                    description="Invitations for teams."
                    enabled={!this.state.blockAll}
                    value={this.state.teamInvitation}
                    onClick={this.switchClicked}
                />
                <SwitchSettingsItem
                    id='social'
                    title='Social Activity'
                    description={"What your \"folowing\" are doing."}
                    enabled={!this.state.blockAll}
                    value={this.state.social}
                    onClick={this.switchClicked}
                />
            </List>
        )
    }

}

export default withStyles(styles, { withTheme: true })(NotificationSettings);