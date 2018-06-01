import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import { withStyles } from '@material-ui/core/styles';
import Search from "./Search";


import LunchMenu from "./LunchMenu";

const styles = {
    root: {
        flexGrow: 1,
    },
    flex: {
        flex: 1,
        fontFamily: "Work Sans",
        fontWeight: '600',
        fontSize: '14px',
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    },
    appbar: {
        position: 'relative',
        boxShadow: 'none',
    }
};

class ButtonAppBar extends React.Component {

    constructor(props) {
        super();

        this.state = {
            currentScreen: props.currentScreen,
        };
    }

    render() {
        const { classes } = this.props;
        let titel = this.state.currentScreen;

        return (
            <div className={classes.root}>
                <AppBar className={classes.appbar}>
                    <Toolbar>
                        <div className={classes.menuButton} color="inherit" aria-label="Menu">
                            <LunchMenu open={false}/>
                        </div>
                        <Typography color="inherit" className={classes.flex}>
                            {titel}
                        </Typography>
                        <div color="inherit">
                            <Search/>
                        </div>
                    </Toolbar>
                </AppBar>
            </div>
        )
    }
}

export default withStyles(styles)(ButtonAppBar);