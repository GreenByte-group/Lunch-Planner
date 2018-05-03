import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import {withStyles} from 'material-ui/styles';
import AddIcon from '@material-ui/icons/Add';
import Button from 'material-ui/Button';

const styles = theme => ({
    whiteSymbol: {
        color: theme.palette.common.white
    },
    floatingButton: {
        position: 'relative',
        float: 'right',
        bottom: '15px',
        right: '15px',
    }
});


class FloatingActionButton extends React.Component {

    render() {
        const { classes } = this.props;

        return (
            <Button variant="fab" color="secondary" aria-label="add" className={classNames(classes.button, classes.whiteSymbol, classes.floatingButton)}>
                <AddIcon/>
            </Button>
        )
    }

}

FloatingActionButton.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(FloatingActionButton);