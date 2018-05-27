import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from 'material-ui/styles';
import Slide from 'material-ui/transitions/Slide';
import axios from "axios/index";
import {HOST} from "../../Config";
import FloatingActionButton from "../FloatingActionButton";
import {getHistory} from "../../utils/HistoryUtils";
import Dialog from "../Dialog";
import UserList from "./UserList";
import Tab from "material-ui/es/Tabs/Tab";
import Tabs from "material-ui/es/Tabs/Tabs";
import SwipeableViews from 'react-swipeable-views';
import Typography from 'material-ui/Typography';
import TeamsList from "../Team/TeamsList";

const styles = theme =>({
    root: {
      position: 'fixed',
        backgroundColor: theme.palette.background.paper,
    },
    appBar: {
        position: 'fixed',
    },
    flex: {
        flex: 1,
    },
});

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

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

class SelectUserScreen extends React.Component {

    constructor(props) {
        super();

        let arrayUsers, arrayTeams;
        if(props.location.query && props.location.query.invitedUsers) {
            let string = String(props.location.query.invitedUsers);
            if(string !== "")
                arrayUsers = string.split(',');
        }
        if(props.location.query && props.location.query.invitedTeams) {
            let string = String(props.location.query.invitedTeams);
            if(string !== "")
                arrayTeams = string.split(',');
        }

        this.state = {
            open: true,
            users: [],
            search: "",
            selectedUsers: arrayUsers || [],
            value: 0,
            teams:[],
            selectedTeams: arrayTeams || [],
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

    updateTeams(search) {
        let url;
        if(search == null || search === undefined || search === "")
            url = HOST + "/team";
        else
            url = HOST + "/team/search/" + search;

        axios.get(url)
            .then((response) => {
                this.setState({
                    search: search,
                    teams: response.data,
                })
            });
    }

    searchChanged = (search) => {
        this.updateUsers(search);
        this.updateTeams(search);
    };

    selectionChanged = (selectedUsers) => {
        this.setState({
            selectedUsers: selectedUsers,
        });
    };

    handleSend = () => {
        if(this.props.location.query)
            getHistory().push(this.props.location.query.source +
                "?invitedUsers=" + this.state.selectedUsers);
    };

    // Methods for handling tabs
    handleChange = (event, value) => {
        this.setState({ value });
    };

    handleChangeIndex = index => {
        this.setState({ value: index });
    };

    render() {
        const { classes, theme} = this.props;
        let users = this.state.users;

        // Title for appbar
        let countSelected = this.state.selectedUsers.length;
        let textTitle = "Select users";
        if(countSelected !== 0) {
            textTitle = countSelected + " selected";
        }

        return (
            <Dialog
                zIndex={10001}
                title={textTitle}
                onSearch={this.searchChanged}
            >
                <Tabs
                    value={this.state.value}
                    onChange={this.handleChange}
                    indicatorColor="secondary"
                    textColor="secondary"
                    centered
                    fullWidth
                >
                    <Tab className={classes.tab} label="ALL" />
                    <Tab className={classes.tab} label="PEOPLE" />
                    <Tab className={classes.tab} label="TEAMS" />
                </Tabs>
                <SwipeableViews
                    axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                    index={this.state.value}
                    onChangeIndex={this.handleChangeIndex}
                >

                    <TabContainer dir={theme.direction}>
                    </TabContainer>
                    <TabContainer dir={theme.direction}>
                        <UserList
                            selectedUsers={this.state.selectedUsers}
                            users={this.state.users}
                            selectable={true}
                            onSelectionChanged={this.selectionChanged}
                        />
                    </TabContainer>
                    <TabContainer dir={theme.direction}>
                        <TeamsList
                            selectedTeams ={this.state.selectedTeams}
                            teams={this.state.teams}
                            selectable={true}
                            onSelectionChanged={this.selectionChanged}
                        />
                    </TabContainer>
                </SwipeableViews>

                <FloatingActionButton onClick={this.handleSend} icon="done"/>
            </Dialog>
        );
    }
}

SelectUserScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(SelectUserScreen);