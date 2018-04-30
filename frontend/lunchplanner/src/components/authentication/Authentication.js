import React from 'react';
import PropTypes from "prop-types";
import Login from "./Login"
import Register from "./Registration"
import Tabs, { Tab } from 'material-ui/Tabs';
import SwipeableViews from 'react-swipeable-views';
import AppBar from 'material-ui/AppBar';
import Typography from 'material-ui/Typography';
import { withStyles } from 'material-ui/styles';
import axios from "axios";

function TabContainer({ children, dir }) {
    return (
        <Typography component="div" dir={dir} style={{ padding: 8 * 3 }}>
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
        //width: 500,
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
            let url =  'http://localhost:8080/user';
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
                    axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                    index={this.state.value}
                    onChangeIndex={this.handleChangeIndex}
                >
                    <TabContainer dir={theme.direction}>
                        <Register/>
                    </TabContainer>
                    <TabContainer dir={theme.direction}>
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