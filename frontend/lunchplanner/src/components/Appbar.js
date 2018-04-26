import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from 'material-ui/styles';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import Button from 'material-ui/Button';
import IconButton from 'material-ui/IconButton';
import IconSearch from '@material-ui/icons/Search';

import SimpleMenu from "./SimpleMenu";

const styles = {
    root: {
        flexGrow: 1,

    },
    flex: {
        flex: 1,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    },
    selected: {
        color:"#75a045"
    }

};

function ButtonAppBar(props) {
    const { classes } = props;
    return (
        <div className={classes.root} >
            <AppBar position="static" style={{backgroundColor: "#75a045"}}>
                <Toolbar>
                    <IconButton className={classes.menuButton} color="inherit" aria-label="Menu">

                        <SimpleMenu/>
                    </IconButton>
                    <Typography variant="title" color="inherit" className={classes.flex}>
                        aktuelle Seite
                    </Typography>
                    <Button color="inherit">
                        <IconSearch/>
                    </Button>
                </Toolbar>
            </AppBar>
        </div>
    );
}

ButtonAppBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ButtonAppBar);