import React from 'react';
import {withStyles} from "@material-ui/core/styles/index";
import {List, ListSubheader} from "@material-ui/core";
import NotificationListItem from "./NotificationListItem";
import {getNotifications, setNotificationRead} from "./NotificationFunctions";
import {getUsername} from "../authentication/LoginFunctions";

const styles = {
    list: {
        height: '100%',
        padding: '0px',
        margin: '0px',
    },
    ul: {
        backgroundColor: 'white',
        padding: 0,
    },
    subheader: {
        padding: '8px 16px',
        lineHeight: '24px',
        fontWeight: 500,
        fontSize: '16px',
        color: 'rgba(46,46,46,0.87)',
        boxShadow: '0px 2px 5px -4px black',
        backgroundColor: '#f8f8f8',
    },
    clearAll: {
        float: 'right',
        "&:hover": {
            cursor: 'pointer',
            textDecoration: 'underline',
        }
    }
};

class NotificationList extends React.Component {

    constructor(props) {
        super();

        this.state = {
            notfications: [],
            notificationsRead: [],
            notificationsUnread: [],
        }
    }

    componentDidMount() {
        this.loadNotifications();
    }

    loadNotifications() {
        getNotifications((response) => {
            if(response.status === 200) {
                let notifications = response.data;
                let notificationsRead = [];
                let notificationsUnread = [];
                notifications.forEach((value) => {
                    if (value.read)
                        notificationsRead.push(value);
                    else
                        notificationsUnread.push(value);
                });

                this.setState({
                    notifications: notifications,
                    notificationsRead: notificationsRead,
                    notificationsUnread: notificationsUnread,
                })
            }
        });
    }

    clearAll = () => {
        this.state.notificationsUnread.forEach((value) => {
            setNotificationRead(value.notificationId);
        });

        this.setState({
            notificationsUnread: [],
            notificationsRead: this.state.notificationsRead.concat(this.state.notificationsUnread),
        })
    };

    render() {
        const {classes} = this.props;
        const notificationsRead = this.state.notificationsRead;
        const notificationsUnread = this.state.notificationsUnread;

        return (
            <List className={classes.list} subheader={<li />}>
                <li key='section-uncleared'>
                    <ul className={classes.ul}>
                        {
                            (notificationsUnread)
                                ? <ListSubheader className={classes.subheader}>Uncleared ({notificationsUnread.length}) <span onClick={this.clearAll} className={classes.clearAll}>clear all</span></ListSubheader>
                                : ''
                        }
                        {
                            notificationsUnread.map((listValue) => {
                                return <NotificationListItem
                                    title={listValue.titel}
                                    description={listValue.message}
                                    icon={listValue.picture}
                                    read={listValue.read}
                                    clickUrl={listValue.link}
                                    id={listValue.notificationId}
                                />
                            })
                        }
                    </ul>
                </li>
                <li key='section-cleared'>
                    <ul className={classes.ul}>
                        {
                            (notificationsRead)
                                ? <ListSubheader className={classes.subheader}>Cleared</ListSubheader>
                                : ''
                        }
                        {
                            notificationsRead.map((listValue) => {
                                return <NotificationListItem
                                    title={listValue.titel}
                                    description={listValue.message}
                                    icon={listValue.picture}
                                    read={listValue.read}
                                    clickUrl={listValue.link}
                                    id={listValue.notificationId}
                                />
                            })
                        }
                    </ul>
                </li>

            </List>
        )
    }

}

export default withStyles(styles, { withTheme: true })(NotificationList);