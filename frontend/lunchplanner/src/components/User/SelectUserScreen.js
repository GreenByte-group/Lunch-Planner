import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from 'material-ui/styles';
import Dialog from 'material-ui/Dialog';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import IconButton from 'material-ui/IconButton';
import Typography from 'material-ui/Typography';
import CloseIcon from '@material-ui/icons/Close';
import Slide from 'material-ui/transitions/Slide';

import {Link} from "react-router-dom";
import axios from "axios/index";
import {HOST} from "../../Config";
import {List} from "material-ui";
import User from "./User";
import FloatingActionButton from "../FloatingActionButton";

const styles = {
    root: {
      position: 'fixed'
    },
    appBar: {
        position: 'fixed',
    },
    flex: {
        flex: 1,
    },
    list: {
        marginTop: '56px',
        padding: 0,
    },

};

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

class SelectUserScreen extends React.Component {

    constructor(props) {
        super();

        let array;
        if(props.location.query && props.location.query.invitedUsers) {
            let string = String(props.location.query.invitedUsers);
            if(string !== "")
                array = string.split(',');
        }

        this.state = {
            open: true,
            users: [],
            selectedUsers: array || [],
        };
    }

    componentDidMount() {
        this.setState({
            search: this.props.search,
        });

        let url;
        if(this.props.search)
            url = HOST + "/user/search/" + this.props.search;
        else
            url = HOST + "/user";

        axios.get(url)
            .then((response) => {
                this.setState({
                    users: response.data,
                })
            });

        // this.setState({
        //     users: [{username: "Martin"}, {username: "Sarah"}, {username: "Test"}, {username: "Test2"}]
        // });
    }

    clickHandler = (username, selected) => {
        let selectedUsers = this.state.selectedUsers;
        if(selected) {
            selectedUsers.push(username);
        } else {
            let index = selectedUsers.indexOf(username);
            selectedUsers.splice(index, 1);
        }

        this.setState({
            selectedUsers: selectedUsers,
        });
    };

    handleSend = () => {
        if(this.props.location.query)
            this.props.history.push(this.props.location.query.source +
                "?invitedUsers=" + this.state.selectedUsers);
    };

    render() {
        const { classes } = this.props;
        let users = this.state.users;
        let countSelected = this.state.selectedUsers.length;
        let textTitle = "Select users";
        if(countSelected !== 0) {
            textTitle = countSelected + " selected";
        }

        return (
            <div className={classes.root}>
                <Dialog
                    fullScreen
                    open={this.state.open}
                    onClose={this.handleClose}
                    transition={Transition}
                >
                    <AppBar className={classes.appBar} color ="white">
                        <Toolbar>
                            <Link to="/event/create">
                                <IconButton color="inherit" aria-label="Close">
                                    <CloseIcon />
                                </IconButton>
                            </Link>
                            <Typography variant="title" color="inherit" className={classes.flex}>
                                {textTitle}
                            </Typography>

                        </Toolbar>
                    </AppBar>
                    <List className={classes.list}>
                        {users.map((listValue) => {
                            return <User selected={this.state.selectedUsers.includes(listValue.userName)} username={listValue.userName} onClick={this.clickHandler}/>;
                        })}
                    </List>
                    <FloatingActionButton onClick={this.handleSend} icon="done"/>
                </Dialog>
            </div>
        );
    }
}

SelectUserScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(SelectUserScreen);