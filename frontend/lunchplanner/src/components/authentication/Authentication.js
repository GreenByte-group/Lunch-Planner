import React from 'react';
import PropTypes from "prop-types";
import Login from "./Login"
import Register from "./Registration"
import {Tabs,  Tab } from '@material-ui/core';
import SwipeableViews from 'react-swipeable-views';
import AppBar from '@material-ui/core/AppBar';
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
import axios from "axios";
import {HOST, TOKEN, USERNAME} from "../../Config";

function TabContainer({ children, dir }) {
    return (
        <Typography component="div" dir={dir} style={{ padding: 0, height: '100%' }}>
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
        display: 'flex',
        flexDirection: 'column',
        height: '100%',
        backgroundColor: theme.palette.background.paper,
        //width: 500,
    },
    fullHeight: {
        height: '100%',
    },
    swipeableViews: {
        height: '100%',
    },
    tabContainer: {
        height: '100%',
    },
});

class Authentication extends React.Component {
    state = {
        value: 0,
    };

    handleChange = (event, value) => {
        this.setState({ value });
    };

    handleChangeIndex = index => {
        this.setState({ value: index });
    };
    handleSubmit(event) {
        if(this.state.username && this.state.password && this.state.email) {
            let url =  HOST + '/user';
            axios.post(url, {userName: this.state.username, password: this.state.password, mail: this.state.email})
                .then((response) => {
                    if(response.status === 201) {
                        this.setState({
                            redirectToReferrer: true,
                        })
                    } else {
                        this.setState({
                            error: response.data,
                        })
                    }
                })
                .catch((err) => {
                    this.setState({
                        error: err.response.data,
                    })
                })
        } else {
            this.setState({
                error: "All fields are requiered"
            })
        }

        event.preventDefault();
    }

    render() {
        const { classes, theme } = this.props;

        return (
            <div className={classes.root}>
                <AppBar position="static" color="default">
                    <Tabs
                        value={this.state.value}
                        onChange={this.handleChange}
                        indicatorColor="secondary"
                        textColor="secondary"
                        fullWidth
                        centered
                    >
                        <Tab label="SIGN UP" />
                        <Tab label="LOGIN" />
                    </Tabs>
                </AppBar>
                <SwipeableViews
                    className={classes.swipeableViews}
                    axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                    index={this.state.value}
                    onChangeIndex={this.handleChangeIndex}
                >
                    <TabContainer className={classes.tabContainer} dir={theme.direction}>
                        <Register className={classes.fullHeight}/>
                    </TabContainer>
                    <TabContainer className={classes.tabContainer} dir={theme.direction}>
                        <Login/>
                    </TabContainer>
                </SwipeableViews>
            </div>
        );
    }
}

Authentication.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(Authentication);