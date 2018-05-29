import React from 'react';
import {ListItem, withStyles} from "material-ui";

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
        height: '8px',
        width: '8px',
        position: 'absolute',
        right: '16px',
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
        }
    }

    componentWillReceiveProps(newProps) {
        let title, description, icon, read;

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

        if(title || description || icon || read !== undefined) {
            if(read === undefined)
                read = this.state.read;

            this.setState({
                title: title || this.state.title,
                description: description || this.state.description,
                icon: icon || this.state.icon,
                read: read,
            })
        }
    }

    clickHandler = (event) => {
        //TODO notification click
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
                        ? <div className={classes.read}></div>
                        : ''
                }
            </ListItem>
        )
    }
}

export default withStyles(styles)(NotificationListItem);