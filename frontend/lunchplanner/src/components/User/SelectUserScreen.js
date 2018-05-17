import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from 'material-ui/styles';
import Slide from 'material-ui/transitions/Slide';

import axios from "axios/index";
import {HOST} from "../../Config";
import {List, TextField} from "material-ui";
import User from "./User";
import FloatingActionButton from "../FloatingActionButton";
import {getHistory} from "../../utils/HistoryUtils";
import Dialog from "../Dialog";

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
            search: "",
            selectedUsers: array || [],
        };
    }

    componentDidMount() {
        this.updateUsers(this.props.search);
    }

    updateUsers(search) {
        let url;
        if(search == null || search === undefined || search === "")
            url = HOST + "/user";
        else
            url = HOST + "/user/search/" + search;

        axios.get(url)
            .then((response) => {
                this.setState({
                    search: search,
                    users: response.data,
                })
            });
    }

    searchChanged = (search) => {
        this.updateUsers(search);
    };

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
            getHistory().push(this.props.location.query.source +
                "?invitedUsers=" + this.state.selectedUsers);
    };

    render() {
        const { classes } = this.props;
        let users = this.state.users;

        // Title for appbar
        let countSelected = this.state.selectedUsers.length;
        let textTitle = "Select users";
        if(countSelected !== 0) {
            textTitle = countSelected + " selected";
        }

        return (
            <Dialog
                title={textTitle}
                onSearch={this.searchChanged}
            >
                <List className={classes.list}>
                    {users.map((listValue) => {
                        return <User selected={this.state.selectedUsers.includes(listValue.userName)} username={listValue.userName} onClick={this.clickHandler}/>;
                    })}
                </List>
                <FloatingActionButton onClick={this.handleSend} icon="done"/>
            </Dialog>
        );
    }
}

SelectUserScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(SelectUserScreen);