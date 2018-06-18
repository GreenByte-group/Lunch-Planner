import React from 'react';
import PropTypes from 'prop-types';
import SwipeableViews from 'react-swipeable-views';
import {withStyles, AppBar, Tabs, Tab, Typography} from '@material-ui/core';
import Teamlist from "./Team/TeamList";
import BottomNavigationBar from "./BottomNavigationBar";
import {setAuthenticationHeader} from "./authentication/LoginFunctions";
import {needReload} from "./AppContainer";


function TabContainer({ children, dir }) {
    return (
        <Typography component="div" dir={dir} style={{ padding: 0 }}>
            {children}
        </Typography>
    );
}
TabContainer.propTypes = {
    children: PropTypes.node.isRequired,
    dir: PropTypes.string.isRequired,
};

const styles = theme => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        //width: 1500,,
        position: 'relative',
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        maxWidth: '1024px',
        width: '100%',
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    fab: {
        position: 'absolute',
        bottom: theme.spacing.unit * 2,
        right: theme.spacing.unit * 2,
    },
    whiteSymbol: {
        color: theme.palette.common.white
    },
});

class SocialScreen extends React.Component {

    constructor(props) {
        super();
        this.state= {
            search: props.location.state,
        };

        setAuthenticationHeader();
    }

    componentDidMount(){
        this.setState({
            search: this.props.location.state,
        })
    }

    componentWillReceiveProps(newProps){
        if(newProps.search !== this.state.search){
            console.log(newProps.search)
            this.setState({
                search: newProps.search,
            });
        }
    }
    render() {
        const { classes, theme } = this.props;
        return (
            <div className={classes.root}>
                <Teamlist search={this.state.search}/>
            </div>
        );
    }
}

SocialScreen.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(SocialScreen);