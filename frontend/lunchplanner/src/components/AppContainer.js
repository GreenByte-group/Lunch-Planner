import React from 'react';
import classNames from 'classnames';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Search from "./Search";
import SearchIcon from '@material-ui/icons/Search';
import {Button, MenuItem, List, Divider, withStyles, FormControlLabel, Switch, Drawer, Avatar, Hidden} from '@material-ui/core'
import {Place, LocalDining, Group, NotificationsNone, ExitToApp, Settings, Menu} from '@material-ui/icons'

import {Link} from "react-router-dom";
import {getUser} from "./User/UserFunctions";
import {getUsername} from "./authentication/LoginFunctions";
import {getHistory} from "../utils/HistoryUtils";
import {doLogout} from "./LoginFunctions";
import BottomNavigationBar from "./BottomNavigationBar";
import LunchPlanner from "./LunchPlanner";
import SocialScreen from "./SocialScreen";
import LocationScreen from "./LocationScreen";
import NotificationsScreen from "./notification/NotificationsScreen";

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
    avatar:{
        marginTop: 24,
        marginLeft: 24,
        width: 64,
        height: 64,
        marginBottom: 15,

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
    },
    mainContent: {
        height: '100vh',
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
        boxShadow: 'none',
    },
};

class AppContainer extends React.Component {

    constructor(props) {
        super();
        this.state = {
            currentScreen: props.currentScreen,
            openSearch: false,
            search: props.searchValue,

            drawerOpen: false,
            username: getUsername(),
            email: "",
        };
        this.handleSearch = this.handleSearch.bind(this);
    }

    componentDidMount() {
        getUser(getUsername(), (response) => {
            if(response.status === 200) {
                this.setState({
                    email: response.data.eMail,
                })
            }
        })
    }

    signOut = () => {
        doLogout();
        getHistory().push("/login");
    };

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

    render() {
        const { classes } = this.props;

        let component = this.props.match.params.component;
        let children;
        let title;
        switch(component) {
            case 'event':
                children = <LunchPlanner/>;
                title = "Events";
                break;
            case 'team':
                children = <SocialScreen/>;
                title = "Teams";
                break;
            case 'location':
                children = <LocationScreen/>;
                title = "Places";
                break;
            case 'notifications':
                children = <NotificationsScreen/>;
                title = "Notifications";
                break;
        }

        const drawer = (
            <div
                tabIndex={0}
                role="button"
            >
                <div className={classes.list}>
                    <List className={classes.profile}>
                        <Avatar alt={this.state.username} className={classes.avatar} >{this.state.username.charAt(0)}</Avatar>
                        <p className={classes.avatarText}>{this.state.username} ● {this.state.email}</p>
                    </List>
                    <Divider />
                    <List className ={classes.menu}>
                        <Link to="/app/location">
                            <MenuItem>
                                <Place className={classes.icon}/>
                                Places
                            </MenuItem>
                        </Link>
                        <Link to="/app/event">
                            <MenuItem>
                                <LocalDining className={classes.icon}/>
                                Event
                            </MenuItem>
                        </Link>
                        <Link to="/app/team">
                            <MenuItem>
                                <Group className={classes.icon}/>
                                Social
                            </MenuItem>
                        </Link>
                        <Divider />
                        <Link to="/app/notifications">
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
                                        // checked={this.state.visible}
                                        // onChange={this.handleVisibility("visible")}
                                        value="visible"
                                    />
                                }
                                label="No notifications today"
                            />
                        </MenuItem>
                        <Divider />
                        <Link to="/app/options">
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
                    {children}

                    <Hidden mdUp>
                        <BottomNavigationBar />
                    </Hidden>
                </div>
            </div>
        )
    }
}

export default withStyles(styles)(AppContainer);