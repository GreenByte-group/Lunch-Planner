import React from 'react';
import {withStyles} from "@material-ui/core/styles/index";
import {List, ListSubheader} from "@material-ui/core";
import NotificationListItem from "./NotificationListItem";

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
        backgroundColor: 'white',
        padding: '8px 16px',
        lineHeight: '24px',
        fontWeight: 500,
        fontSize: '16px',
        color: 'rgba(46,46,46,0.87)',
        borderTop: '1px solid #D2D2D2;'
    },
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
        let notifications =  [
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: false},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: false},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: true},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: true},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: true},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: true},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: true},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: true},
            {title: 'title', description: 'description', icon: 'https://greenbyte.group/assets/images/logo.png', read: true}
        ];

        let notificationsRead = [];
        let notificationsUnread = [];

        notifications.forEach((item) => {
            if(item.read) {
                notificationsRead.push(item);
            } else {
                notificationsUnread.push(item);
            }
        });

        //TODO Abruf der notifications
        this.setState({
            notifications: notifications,
            notificationsRead: notificationsRead,
            notificationsUnread: notificationsUnread,
        });
    }

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
                                ? <ListSubheader className={classes.subheader}>Uncleared ({notificationsUnread.length})</ListSubheader>
                                : ''
                        }
                        {
                            notificationsUnread.map((listValue) => {
                                return <NotificationListItem
                                    title={listValue.title}
                                    description={listValue.description}
                                    icon={listValue.icon}
                                    read={listValue.read}
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
                                    title={listValue.title}
                                    description={listValue.description}
                                    icon={listValue.icon}
                                    read={listValue.read}
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