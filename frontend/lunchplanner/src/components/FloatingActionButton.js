import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import AddIcon from '@material-ui/icons/Add';
import DoneIcon from '@material-ui/icons/Done'
import Button from '@material-ui/core/Button';

const styles = theme => ({
    whiteSymbol: {
        color: theme.palette.common.white
    },
    floatingButton: {
        position: 'absolute',
        float: 'right',
        bottom: '15px',
        marginLeft: 'calc(100% - 56px - 15px)',
    }
});


class FloatingActionButton extends React.Component {

    constructor(props) {
        super();
        this.state = {
            icon: props.icon,
        };
    }

    componentWillReceiveProps(newProps) {
        if(newProps.icon && newProps.icon !== this.state.icon)
            this.setState({
                icon: newProps.icon,
            })
    }

    render() {
        const { classes } = this.props;

        return (
            <Button variant="fab" color="secondary" className={classNames(classes.button, classes.whiteSymbol, classes.floatingButton)} onClick={this.props.onClick}>
                {(() => {switch (this.state.icon) {
                    case 'add':
                        return <AddIcon />;
                    case 'done':
                        return <DoneIcon />;
                    default:
                        return <AddIcon/>;

                }})()}
            </Button>
        )
    }

}

FloatingActionButton.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(FloatingActionButton);