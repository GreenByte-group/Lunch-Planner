import React from 'react';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import Button from 'material-ui/Button';
import IconButton from 'material-ui/IconButton';
import IconSearch from '@material-ui/icons/Search';
import { withStyles } from 'material-ui/styles';


import LunchMenu from "./LunchMenu";

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
    let titel = props.currentScreen;
    return (
        <div className={classes.root}>
            <AppBar className={classes.appbar}>
                <Toolbar>
                    <IconButton className={classes.menuButton} color="inherit" aria-label="Menu">
                        <LunchMenu open={false}/>
                    </IconButton>
                    <Typography variant="Events" color="inherit" className={classes.flex}>
                        {titel}
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