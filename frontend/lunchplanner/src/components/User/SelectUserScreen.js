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

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
    list: {
        padding: 0,
    },

};

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

class SelectUserScreen extends React.Component {

    constructor(props) {
        super();
        this.state = {
            open: true,
            users: [],
        }
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
                    // users: response.data, TODO change to real data
                    users: [{username: "Martin"}, {username: "Sarah"}, {username: "Test"}, {username: "Test2"}]
                })
            })

        this.setState({
            users: [{username: "Martin"}, {username: "Sarah"}, {username: "Test"}, {username: "Test2"}]
        });
    }

    render() {
        const { classes } = this.props;
        let users = this.state.users;

        return (
            <div>
                <Dialog
                    fullScreen
                    open={this.state.open}
                    onClose={this.handleClose}
                    transition={Transition}
                >
                    <AppBar className={classes.appBar} color ="white">
                        <Toolbar>
                            <Link to="/event">
                                <IconButton color="inherit" aria-label="Close">
                                    <CloseIcon />
                                </IconButton>
                            </Link>
                            <Typography variant="title" color="inherit" className={classes.flex}>
                                Select user
                            </Typography>

                        </Toolbar>
                    </AppBar>
                    <List className={classes.list}>
                        {users.map(function(listValue){
                            return <User username={listValue.username}/>;
                        })}
                    </List>
                </Dialog>
            </div>
        );
    }
}

SelectUserScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(SelectUserScreen);