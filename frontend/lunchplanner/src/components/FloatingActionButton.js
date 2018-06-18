import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import AddIcon from '@material-ui/icons/Add';
import DoneIcon from '@material-ui/icons/Done'
import Button from '@material-ui/core/Button';
import {Dialog} from '@material-ui/core';

const styles = theme => ({
    root: {
        position: 'absolute',
        float: 'right',
        bottom: '15px',
        marginLeft: 'calc(100% - 56px - 15px)',
        width: 0,
    },
    whiteSymbol: {
        color: theme.palette.common.white
    },
    floatingButton: {

    },
    allActions: {
        display: 'flex',
        flexDirection: 'column',
        marginRight: '-57px',
    },
    actionContainer: {
        display: 'flex',
        flexDirection: 'row',
        marginBottom: '24px',
        justifyContent: 'flex-end',
        cursor: 'pointer',
    },
    actionText: {
        flexShrink: '0',
        fontSize: '13px',
        fontWeight: '500',
        lineHeight: '20px',
        color: 'white',
        backgroundColor: '#484848',
        borderRadius: '3px',
        height: '24px',
        padding: '2px 8px',
        marginRight: '16px',
        marginTop: '7px',
        boxShadow: 'black 2px 2px 5px -3px',
    },
    actionIcon: {
        backgroundColor: 'white',
        borderRadius: '50%',
        boxShadow: 'black 2px 2px 5px -3px',
        marginRight: '15px',
        width: '36px',
        height: '36px',
        padding: '6px',
    },
});


class FloatingActionButton extends React.Component {

    constructor(props) {
        super();
        this.state = {
            icon: props.icon,
            actions: props.actions,
            showActions: false,
        };
    }

    componentWillReceiveProps(newProps) {
        if(newProps.icon && newProps.icon !== this.state.icon)
            this.setState({
                icon: newProps.icon,
            })

        if(newProps.actions && newProps.actions !== this.state.actions) {
            this.setState({
                actions: newProps.actions,
            })
        }
    }

    onClick = () => {
        if(this.state.actions) {
            this.setState({
                showActions: !this.state.showActions,
            });
        } else {
            if(this.props.onClick)
                this.props.onClick();
        }
    };

    render() {
        const { classes } = this.props;

        return (
            <div className={classes.root} style={this.props.styleRoot}>
                {
                    (this.state.showActions)
                        ?   <div className={classes.allActions}>{
                                this.state.actions.map(
                                    (action) => {
                                        return <div onClick={action.onClick} className={classes.actionContainer}>
                                            <div className={classes.actionText}>{action.text}</div>
                                            <div className={classes.actionIcon}>{action.icon}</div>
                                        </div>
                                    })}
                            </div>
                        : ''
                }

                <Button variant="fab" color="secondary" className={classNames(classes.button, classes.whiteSymbol, classes.floatingButton)} onClick={this.onClick}>
                    {(() => {switch (this.state.icon) {
                        case 'add':
                            return <AddIcon />;
                        case 'done':
                            return <DoneIcon />;
                        default:
                            return <AddIcon/>;

                    }})()}
                </Button>
            </div>
        )
    }

}

FloatingActionButton.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(FloatingActionButton);