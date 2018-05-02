import React from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import {withStyles} from 'material-ui/styles';
import AddIcon from '@material-ui/icons/Add';
import Button from 'material-ui/Button';

const floatingButtonClass = 'floatingButton';

const buttonStyle = {
    float: 'right',
    marginBottom: '15px',
};

const styles = theme => ({
    whiteSymbol: {
        color: theme.palette.common.white
    },
});


class FloatingActionButton extends React.Component {

    render() {
        const { classes, theme } = this.props;

        return (
            <Button style= {theme.fab}variant="fab" color="secondary" style={buttonStyle} aria-label="add" className={classNames(classes.button, classes.whiteSymbol)}>
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