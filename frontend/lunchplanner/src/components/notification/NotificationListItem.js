import React from 'react';
import {ListItem, withStyles} from "@material-ui/core";
import {getHistory} from "../../utils/HistoryUtils";
import {setNotificationRead} from "./NotificationFunctions";

const styles = {
    listItem: {
        padding: '14px 16px',
    },
    avatar: {
        height: '32px',
        width: '32px',
        borderRadius: '50%',
        backgroundPosition: 'center',
        backgroundSize: 'cover',
    },
    content: {
        paddingLeft: '16px',
    },
    title: {
        margin: 0,
        fontSize: '13px',
        lineHeight: '20px',
        color: 	'rgba(46,46,46,0.87)',
    },
    description: {
        margin: 0,
        color: '#A4A4A4',
        fontSize: '11px',
        lineHeight: '16px',
    },
    read: {
        backgroundColor: '#F29B26',
        borderRadius: '50%',
        height: '10px',
        width: '10px',
        position: 'absolute',
        right: '16px',
        "&:hover": {
            backgroundColor: '#00b1ff',
        }
    }
};

class NotificationListItem extends React.Component {

    constructor(props) {
        super();

        this.state = {
            title: props.title,
            description: props.description,
            icon: props.icon,
            read: props.read,
            clickUrl: props.clickUrl,
            id: props.id,
        }
    }

    componentWillReceiveProps(newProps) {
        let title, description, icon, read, clickUrl, id;

        if(newProps.title && newProps.title !== this.state.title) {
            title = newProps.title;
        }
        if(newProps.description && newProps.description !== this.state.description) {
            description = newProps.description;
        }
        if(newProps.icon && newProps.icon !== this.state.icon) {
            icon = newProps.icon;
        }
        if(newProps.read !== undefined && newProps.read !== this.state.read) {
            read = newProps.read;
        }
        if(newProps.clickUrl !== undefined && newProps.clickUrl !== this.state.clickUrl) {
            clickUrl = newProps.clickUrl;
        }
        if(newProps.id !== undefined && newProps.id !== this.state.id) {
            id = newProps.id;
        }

        if(title || description || icon || id || clickUrl || read !== undefined) {
            if(read === undefined)
                read = this.state.read;

            this.setState({
                title: title || this.state.title,
                description: description || this.state.description,
                icon: icon || this.state.icon,
                read: read,
                clickUrl: clickUrl || this.state.clickUrl,
                id: id || this.state.id,
            })
        }
    }

    clickHandler = (event) => {
        let title = event.target.title;

        if(title !== 'read') {
            getHistory().push("/app" + this.state.clickUrl);
            setNotificationRead(this.state.id);
        }
    };

    notificationRead = () => {
        setNotificationRead(this.state.id, (response) => {
            if(response.status === 204) {
                this.setState({
                    read: true,
                })
                //TODO update notifications lsit
            }
        })
    };

    render() {
        const {classes} = this.props;

        return (
            <ListItem button className={classes.listItem} onClick={this.clickHandler}>
                <div className={classes.avatar} style={{backgroundImage:"url(" + this.state.icon + ")"}}></div>
                <div className={classes.content}>
                    <p className={classes.title}>{this.state.title}</p>
                    <p className={classes.description}>{this.state.description}</p>
                </div>
                {
                    (!this.state.read)
                        ? <div onClick={this.notificationRead} className={classes.read} title={'read'}></div>
                        : ''
                }
            </ListItem>
        )
    }
}

export default withStyles(styles)(NotificationListItem);