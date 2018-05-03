import React from 'react';
import PropTypes from 'prop-types';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import Button from 'material-ui/Button';
import IconButton from 'material-ui/IconButton';
import IconSearch from '@material-ui/icons/Search';
import { withStyles } from 'material-ui/styles';


import SimpleMenu from "./SimpleMenu";

const styles = {
    root: {
        flexGrow: 1,
    },
    flex: {
        flex: 1,
        fontFamily: "Work Sans",
        fontWeight: '600',
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    },
    appbar: {
        position: 'fixed',
        boxShadow: 'none',
    }
};

function ButtonAppBar(props) {
    const { classes } = props;
    return (
        <div className={classes.root}>
            <AppBar className={classes.appbar}>
                <Toolbar>
                    <IconButton className={classes.menuButton} color="inherit" aria-label="Menu">
                        <SimpleMenu />
                    </IconButton>
                    <Typography variant="Events" color="inherit" className={classes.flex}>
                        Title
                    </Typography>
                    <Button color = "inherit">
                        <IconSearch/>
                    </Button>
                </Toolbar>
            </AppBar>
        </div>
    );
}

export default withStyles(styles)(ButtonAppBar);