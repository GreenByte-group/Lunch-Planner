import React from 'react';
import {withStyles} from '@material-ui/core/styles';
import {List} from "@material-ui/core";
import User from "./User";
import {getReplyToEvent} from "../Event/EventFunctions";

const styles = {
    list: {
        padding: 0,
        marginTop: '0px !important',
    },
};

class UserList extends React.Component {

    constructor(props) {
        super();

        this.state = {
            selectedUsers: props.selectedUsers || [],
            users: props.users || [],
            selectable: props.selectable || false,
            onSelectionChanged: props.onSelectionChanged,
            othersInvited: props.othersInvited || false,
            clickRemove: props.clickRemove,
            acceptedUser: props.acceptedUser,
        };
    }

    componentWillReceiveProps(newProps) {
        let selectedUsers, users;

        if(newProps.selectedUsers && newProps.selectedUsers !== this.state.selectedUsers) {
            selectedUsers = newProps.selectedUsers;
            this.setState({
                selectedUsers: selectedUsers,
            });
        }

        if(newProps.users && newProps.users !== this.state.users) {
            users = newProps.users;
            this.setState({
                users: users,
            });
        }

        if(newProps.clickRemove && newProps.clickRemove !== this.state.clickRemove) {
            this.setState({
                clickRemove: newProps.clickRemove,
            });
        }
    }

    clickHandler = (username, selected) => {
        if(this.state.selectable) {
            let selectedUsers = this.state.selectedUsers;
            if (selected) {
                selectedUsers.push(username);
            } else {
                let index = selectedUsers.indexOf(username);
                selectedUsers.splice(index, 1);
            }

            this.setState({
                selectedUsers: selectedUsers,
            });

            this.state.onSelectionChanged(selectedUsers);
        }
    };

    getReplyToEvents(eventId){
        getReplyToEvent(eventId, (response) => {
            console.log('response data', response.data);
            if(response.status === 200){
                this.setState({
                    replyList: response.data,
                })
            }
        });
    };

    render() {
        const { classes } = this.props;
        let users = this.state.users;
        let selectedUsers = this.state.selectedUsers;
        let othersInvited = this.state.othersInvited;

        return (
            <List
                className={classes.list}
            >
                {users.map((listValue) => {
                    return <User selectable={this.state.selectable}
                                 selected={selectedUsers.includes(listValue.userName)}
                                 invited={othersInvited}
                                 username={listValue.userName}
                                 onClick={this.clickHandler}
                                 clickRemove={this.state.clickRemove}
                    />;

                })}
            </List>
        );
    }
}

export default withStyles(styles, { withTheme: true })(UserList);