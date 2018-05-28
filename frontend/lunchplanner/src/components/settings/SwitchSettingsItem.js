import React from 'react';
import {ListItem, Switch, withStyles} from "material-ui";

const styles = {
    listItem: {
        padding: '14px 16px',
    },
    content: {
        width: 'calc(100% - 46px - 14px - 14px)',
    },
    title: {
        margin: 0,
        padding: 0,
        fontSize: '16px',
        lineHeight: '24px',
        color: 'rgba(46,46,46,0.87)',
    },
    description: {
        margin: 0,
        padding: 0,
        fontSize: '11px',
        lineHeight: '16px',
        color: '#A4A4A4',
    },
    switchContainer: {
        width: '46px'
    },
    switch: {

    },
};

class SwitchSettingsItem extends React.Component {

    constructor(props) {
        super();

        this.state = {
            id: props.id,
            title: props.title,
            description: props.description,
            enabled: props.enabled,
            value: props.value,
            onClick: props.onClick,
        }
    }

    componentWillReceiveProps(newProps) {
        let id, title, description, enabled, value, onClick;

        if(newProps.id && newProps.id !== this.state.id) {
            id = newProps.id;
        }
        if(newProps.title && newProps.title !== this.state.title) {
            title = newProps.title;
        }
        if(newProps.description && newProps.description !== this.state.description) {
            description = newProps.description;
        }
        if(newProps.enabled !== undefined && newProps.enabled !== this.state.enabled) {
            enabled = newProps.enabled;
        }
        if(newProps.value !== undefined && newProps.value !== this.state.value) {
            value = newProps.value;
        }
        if(newProps.onClick && newProps.onClick !== this.state.onClick) {
            onClick = newProps.onClick;
        }

        if(id || title || description || enabled !== undefined || value !== undefined) {

            if(enabled === undefined)
                enabled = this.state.enabled;
            if(value === undefined)
                value = this.state.value;

            this.setState({
                id: id || this.state.id,
                title: title || this.state.title,
                description: description || this.state.description,
                enabled: enabled,
                value: value,
                onClick: onClick || this.state.onClick,
            })
        }
    }

    clickHandler = () => {
        this.state.onClick(this.state.id, !this.state.value);
    };

    render() {
        const {classes} = this.props;

        return (
            <ListItem button className={classes.listItem} onClick={this.clickHandler}>
                <div className={classes.content}>
                    <p className={classes.title}>{this.state.title}</p>
                    <p className={classes.description}>{this.state.description}</p>
                </div>
                <div className={classes.switchContainer}>
                    <Switch
                        disabled={!this.state.enabled}
                        className={classes.switch}
                        checked={this.state.value}
                    />
                </div>
            </ListItem>
        )
    }
}

export default withStyles(styles)(SwitchSettingsItem);