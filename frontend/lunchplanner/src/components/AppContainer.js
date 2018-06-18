import React from 'react';
import classNames from 'classnames';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Search from "./Search";
import SearchIcon from '@material-ui/icons/Search';
import {Button, MenuItem, List, Divider, withStyles, FormControlLabel,TextField, Switch, Drawer, Avatar, Hidden} from '@material-ui/core'
import {Place, LocalDining, Group, NotificationsNone, ExitToApp, Settings, Menu} from '@material-ui/icons'

import {Link} from "react-router-dom";
import {getUser} from "./User/UserFunctions";
import {getUsername, doLogout} from "./authentication/LoginFunctions";
import {getHistory} from "../utils/HistoryUtils";
import BottomNavigationBar from "./BottomNavigationBar";
import LunchPlanner from "./LunchPlanner";
import SocialScreen from "./SocialScreen";
import LocationScreen from "./LocationScreen";
import NotificationsScreen from "./notification/NotificationsScreen";
import {getEvents} from "./Event/EventFunctions";
import {getNotificationOptions, sendOptions} from "./notification/NotificationFunctions";
import moment from "moment";
import UserEditScreen from "./User/UserEditScreen";
import {HOST} from "../Config";


const styles = {
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
    search:{
        color: 'white',
    },

    // DRAWER
    list: {
        width: 250,
        color:"white",
    },
    profile:{
        fontSize: 10,
        textAlign: "center",
        backgroundColor: "darkGrey",
    },
    noHover: {
        color: 'black',
        "&:hover": {
            textDecoration: 'none',
        },
    },
    avatar:{
        width: '64px',
        height: '64px',
        marginBottom: 15,
        objectFit: 'cover',
        borderRadius: '50%',
    },
    icon:{
        marginRight: 20,
        color: "#1EA185",
    },
    avatarText:{
        marginLeft: -10,
        marginBottom: -5,
    },

    root: {
        display: 'flex',
        flexGrow: 1,
        position: 'relative',
        height: '100%',
    },
    mainContent: {
        overflow: 'hidden',
        height: '100%',
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
    },
    drawer: {
        position: 'relative',
    },
    drawerPaper: {
        position: 'relative',
    },
    appbar: {
        position: 'relative',
        boxShadow: '0px 2px 4px -1px rgba(0, 0, 0, 0.2),0px 4px 5px 0px rgba(0, 0, 0, 0.14),0px 1px 10px 0px rgba(0, 0, 0, 0.12)',
    },
};

export let needReload = false;

export function userNeedReload() {
    needReload = true;
}

class AppContainer extends React.Component {

    constructor(props) {
        super();
        this.state = {
            currentScreen: props.currentScreen,
            openSearch: false,
            search: null,
            drawerOpen: false,
            username: getUsername(),
            email: "",
            profilePicture: '',

            noNotificationToday: false,
        };
        this.handleSearch = this.handleSearch.bind(this);
    }

    componentDidMount() {
        this.getData();
    }

    getData = () => {
        getUser(getUsername(), (response) => {
            if(response.status === 200) {
                this.setState({
                    email: response.data.eMail,
                    profilePicture: HOST + response.data.profilePictureUrl,
                })
            }
        });

        getNotificationOptions((response) => {
            console.log('not: ', response.data.block_until);
            let blockUntil = response.data.block_until;
            let momentObject = moment(blockUntil);
            if(momentObject.isAfter(moment())) {
                this.setState({
                    noNotificationToday: true,
                })
            }
        })
    };

    signOut = () => {
        doLogout();
        getHistory().push("/login");
    };

    componentWillReceiveProps(newProps) {
        if(needReload) {
            needReload = false;
            this.getData();
        }

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
        this.setState({
            openSearch: false,
            search: "",
        });
    };

    handleSearch = (search) => {
        this.setState({
            openSearch: false,
            search: search,
        });
    };

    handleDrawerClick = () => {
        if (!this.state.drawerOpen) {
            document.addEventListener('click', this.handleDrawerClick, false);
        } else {
            document.removeEventListener('click', this.handleDrawerClick, false);
        }

        this.setState(prevState => ({
            drawerOpen: !prevState.drawerOpen,
        }));
    };

    handleNoNotification = () => {
        let disableNotification = !this.state.noNotificationToday;

        this.setState({
            noNotificationToday: disableNotification,
        });

        if(disableNotification) {
            let dateMoment = moment();
            dateMoment.hour(23);
            dateMoment.minute(59);
            sendOptions({blockUntil: dateMoment.toDate()})
        } else {
            let dateMoment = moment();
            dateMoment.subtract(1, 'minutes');
            sendOptions({blockUntil: dateMoment.toDate()})
        }
    };

    render() {
        const { classes } = this.props;

        let component = this.props.match.params.component;
        let children;
        let title;
        let bottomNavigationValue = -1;

        switch(component) {
            case 'event':
                children = <LunchPlanner location={this.props.location} searchValue={this.state.search}/>;
                title = "Events";
                bottomNavigationValue = 1;
                break;
            case 'team':
                children = <SocialScreen location={this.props.location} searchValue={this.state.search}/>;
                title = "Teams";
                bottomNavigationValue = 2;
                break;
            case 'location':
                children = <LocationScreen location={this.props.location} searchValue={this.state.search}/>;
                title = "Places";
                bottomNavigationValue = 0;
                break;
            case 'notifications':
                children = <NotificationsScreen location={this.props.location} />;
                title = "Notifications";
                break;
            case 'user':
                children = <UserEditScreen location={this.props.location} />;
                title = "Your Profile";
                break;
        }

        const drawer = (
            <div
                tabIndex={0}
                role="button"
            >
                <div className={classes.list}>
                    <Link to="/app/user" className={classes.noHover}>
                        <List className={classes.profile}>
                            <img alt={this.state.username} className={classes.avatar} src={this.state.profilePicture} />
                            <p className={classes.avatarText}>{this.state.username} ● {this.state.email}</p>
                        </List>
                    </Link>
                    <Divider />
                    <List className ={classes.menu}>
                        <Link to="/app/location">
                            <MenuItem>
                                <Place className={classes.icon}/>
                                Places
                            </MenuItem>
                        </Link>
                        <Link  to={{pathname: "/app/event",  query: {
                                search: this.state.search,
                            }}}>
                            <MenuItem>
                                <LocalDining className={classes.icon}/>
                                Events
                            </MenuItem>
                        </Link>
                        <Link  to={{pathname: "/app/team",  query: {
                                search: this.state.search,
                            }}}>
                            <MenuItem>
                                <Group className={classes.icon}/>
                                Teams
                            </MenuItem>
                        </Link>
                        <Divider />
                        <Link to="/app/notifications?tab=0">
                            <MenuItem>
                                <NotificationsNone className={classes.icon}/>
                                Notifications
                            </MenuItem>
                        </Link>
                        <MenuItem>
                            <FormControlLabel
                                control={
                                    <Switch
                                        float ="left"
                                        color = "primary"
                                        checked={this.state.noNotificationToday}
                                        onChange={this.handleNoNotification}
                                        value="visible"
                                    />
                                }
                                label="No notifications today"
                            />
                        </MenuItem>
                        <Divider />
                        <Link to="/app/notifications?tab=1">
                            <MenuItem>
                                <Settings className={classes.icon}/>
                                Options
                            </MenuItem>
                        </Link>
                        <MenuItem onClick={this.signOut}>
                            <ExitToApp className={classes.icon}/>
                            Sign Out
                        </MenuItem>
                    </List>
                </div>
            </div>
        );

        return (
            <div className={classes.root}>
                <Hidden mdUp>
                    <Drawer
                        variant="temporary"
                        open={this.state.drawerOpen}
                        className={classes.drawer} >
                        {drawer}
                    </Drawer>
                </Hidden>
                <Hidden smDown implementation="css">
                    <Drawer
                        variant="permanent"
                        open
                        className={classes.drawer}
                        classes={{
                            paper: classes.drawerPaper,
                        }}
                        ModalProps={{
                            keepMounted: true, // Better open performance on mobile.
                        }}
                    >
                        {drawer}
                    </Drawer>
                </Hidden>
                <div className={classes.mainContent}>
                    <AppBar className={classes.appbar}>
                        <Toolbar>
                            <div className={classes.menuButton} color="inherit" aria-label="Menu">
                                <Hidden mdUp>
                                    <Button
                                        onClick={this.handleDrawerClick}
                                    >
                                        <Menu style={{color: "white"}}/>
                                    </Button>
                                </Hidden>
                            </div>
                            <Typography color="inherit" className={classes.flex}>
                                {title}
                            </Typography>
                            <div color="inherit">
                                {
                                    this.state.openSearch === false ?
                                            <IconButton  className={classes.search}>
                                                <SearchIcon onClick={this.showSearch}/>
                                            </IconButton> : ""
                                }
                                {this.state.openSearch ?
                                        <Search open={this.state.openSearch} handleCancel={this.cancelSearch} handleSearch={this.handleSearch} search={this.state.search}/> :
                                        ""
                                    }

                            </div>
                        </Toolbar>
                    </AppBar>
                    {children}

                    <Hidden mdUp>
                        <BottomNavigationBar value={bottomNavigationValue} search={this.state.search}/>
                    </Hidden>
                </div>
            </div>
        )
    }
}

export default withStyles(styles)(AppContainer);