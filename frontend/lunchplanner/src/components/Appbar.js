import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import { withStyles } from '@material-ui/core/styles';
import Search from "./Search";
import SearchIcon from '@material-ui/icons/Search';


import LunchMenu from "./LunchMenu";
import {Link} from "react-router-dom";

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
    },
    search:{
        color: 'white',
    }
};

class ButtonAppBar extends React.Component {

    constructor(props) {
        super();
        this.handleOpenSearch = this.handleOpenSearch.bind(this);
        this.state = {
            currentScreen: props.currentScreen,
            openSearch: false,
        };
    }

    handleOpenSearch = () =>{
        console.log("handle open", this.state.openSearch);
        this.setState({
            openSearch : true,
        });
    };

    handleCancel = () => {
        console.log("handle cancel");
        this.setState({openSearch: false});
    };

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
                            <IconButton onClick={this.handleOpenSearch} className={classes.search}>
                                <SearchIcon/>
                                {this.state.openSearch ?
                                    <Search open={this.state.openSearch} clickCancel={this.handleCancel}/> :
                                   ""
                                }
                            </IconButton>
                        </div>
                    </Toolbar>
                </AppBar>
            </div>
        )
    }
}

export default withStyles(styles)(ButtonAppBar);