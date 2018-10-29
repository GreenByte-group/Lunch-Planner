import React from 'react';
import {ListItem, Switch, withStyles} from "@material-ui/core";
import {TimePicker} from "material-ui-old";

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
    timeContainer: {
        width: '46px',
        overflow: 'hidden',
    },
    timePicker: {

    },
};

class TimeSettingsItem extends React.Component {

    constructor(props) {
        super();

        this.state = {
            id: props.id,
            title: props.title,
            description: props.description,
            enabled: props.enabled,
            value: props.value,
            onChange: props.onChange,
        }
    }

    componentWillReceiveProps(newProps) {
        let id, title, description, enabled, value, onChange;

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
        if(newProps.onChange && newProps.onChange !== this.state.onChange) {
            onChange = newProps.onChange;
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
                onChange: onChange || this.state.onChange,
            })
        }
    }

    onChange = (event, date) => {
        this.state.onChange(this.state.id, date);
    };

    render() {
        const {classes} = this.props;

        return (
            <ListItem button className={classes.listItem}>
                <div className={classes.content}>
                    <p className={classes.title}>{this.state.title}</p>
                    <p className={classes.description}>{this.state.description}</p>
                </div>
                <div className={classes.timeContainer}>
                    <TimePicker
                        disabled={!this.state.enabled}
                        className={classes.timePicker}
                        placeholder={'00:00'}
                        value={this.state.value}
                        format='24hr'
                        minutesStep={5}
                        onChange={this.onChange}
                    />
                </div>
            </ListItem>
        )
    }
}

export default withStyles(styles)(TimeSettingsItem);