import React from 'react';
import PropTypes from 'prop-types';
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
        this.state = {
            currentScreen: props.currentScreen,
            openSearch: false,
            search: props.searchValue,
        };
        this.handleSearch = this.handleSearch.bind(this);
    }

    componentWillReceiveProps(newProps) {
        if(newProps.searchValue !== this.state.search){
            this.setState({
                search: newProps.searchValue,
            });
        }
    }

    showSearch = () => {
        this.setState({openSearch: true});
    };

    cancelSearch = () => {
        this.setState({ openSearch: false });
    };

    handleSearch = (search) => {
        this.props.onHandleSearch(search);
        this.setState({ openSearch: false });
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
                            <IconButton  className={classes.search}>
                                <SearchIcon onClick={this.showSearch}/>
                                {this.state.openSearch ?
                                    <Search open={this.state.openSearch} handleCancel={this.cancelSearch} handleSearch={this.handleSearch} search={this.state.search}/> :
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