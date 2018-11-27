import React from 'react';
import classNames from 'classnames';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Search from "./Search";
import SearchIcon from '@material-ui/icons/Search';
import {Button, MenuItem, List, Divider, withStyles, FormControlLabel,TextField, Switch, Drawer, Avatar, Hidden} from '@material-ui/core'
import {EuroSymbol, Place, LocalDining, Group, NotificationsNone, ExitToApp, Settings, Menu} from '@material-ui/icons'
import InfiniteCalendar from 'react-infinite-calendar';
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';


import {Link} from "react-router-dom";
import {getProfilePicturePath, getUser} from "./User/UserFunctions";
import {getUsername, doLogout} from "./authentication/LoginFunctions";
import {getHistory} from "../utils/HistoryUtils";
import BottomNavigationBar from "./BottomNavigationBar";
import LunchPlanner from "./LunchPlanner";
import SocialScreen from "./SocialScreen";
import LocationScreen from "./LocationScreen";
import NotificationsScreen from "./notification/NotificationsScreen";
import DebtsScreen from "./Debts/DebtsScreen"
import {getEvents} from "./Event/EventFunctions";
import {getNotificationOptions, sendOptions} from "./notification/NotificationFunctions";
import moment from "moment";
import UserEditScreen from "./User/UserEditScreen";
import {HOST} from "../Config";

import 'react-day-picker/lib/style.css';
import {Scrollbars} from "react-custom-scrollbars";
import {CircularProgress} from "material-ui";
const styles = {
    menu: {
        fontSize: '14px',
    },
    flex: {
        flex: 1,
        fontFamily: "Work Sans",
        fontWeight: '600',
        fontSize: '14px',
        color: 'white',
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
        position: 'relative',
    },
    profile:{
        fontSize: 10,
        textAlign: "center",
        backgroundColor: "#75a045",
        height: '115px',
        paddingTop:'8px',
        color: 'white'

    },
    noHover: {
        color: 'black',
        "&:hover": {
            textDecoration: 'none',
        },

    },
    scrolls: {
        backgroundColor: '#FAFAFA',
    },
    avatar:{
        width: '64px',
        height: '64px',
        marginBottom: 15,
        objectFit: 'cover',
        borderRadius: '50%',
    },
    icons:{
        marginRight: 20,
        color: "#1EA185",
        height: '40px',
        width: '40px',

    },
    avatarText:{
        marginLeft: -10,
        marginBottom: -5,
        fontSize: '15px',
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
    navCalendar: {
        marginTop: '5%',
        position: 'relative',
        width: '-webkit-fill-available',
        maxWidth: '250px',


    },
    logo: {
        position: 'relative',
        height: '-webkit-fill-available',
        maxHeight: '38px',
        width: '-webkit-fill-available',
        maxWidth: '100px',
        backgroundColor: 'white',
    },
    progress:{
        marginLeft: 'auto',
        marginRight: 'auto',
        marginTop: "auto",
        marginBottom: "auto",
        display: "block",
    },
    menuItem: {
        marginTop: '10px',
    },

};

export let needReload = false;

export function userNeedReload() {
    console.log("in der function", );
    needReload = true;
}

const selectedStyle = `.DayPicker-Day--highlighted {
  backgroundColor: "#75a045",
  color: 'white',
}`;

const modifiers = {

    highlighted: new Date(2018, 10, 19),
};



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
            eventDates: [],
            selectedDay: [],
            noNotificationToday: false,
            loading: true,
        };
       // this.handleSearch = this.handleSearch.bind(this);
    }

    componentDidMount() {
        this.getData();
        this.getEventsDays();

    }


    getEventsDays = () => {

        let eventDays = [];
        getEvents(null, (response) => {
            console.log("mal schauen", response);

            let date = new Date();
            let locationDay = response.data;
           locationDay.map((tile) => {

               let day = parseInt(tile.startDate.substring(8, 10));
               let month = parseInt(tile.startDate.substring(5, 7)) - 1;
               let year = parseInt(tile.startDate.substring(0, 4));
               console.log('DATE', date);
               eventDays.push(new Date(year,month,day));
           });
        });
            this.setState({
               eventDates: eventDays,
            });

        this.setState({
            selectedDay: eventDays,
            loading: false,
        });

    };
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
        console.log('search: ', search);
        if(search === 'play tetris') {
            console.log('open tetris');
            getHistory().push("/app/tetris");
        }

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

    loadUserStates= () => {
            getUser(getUsername(), (response) => {
                if (response.status === 200) {
                    this.setState({
                        email: response.data.eMail,
                        profilePicture: HOST + response.data.profilePictureUrl,
                        changeData: true,
                    })
                } else {
                    console.log('NOPE BIATCH')
                }
            });
        getHistory().push("");
    };


    render() {
        const { classes } = this.props;

        console.log("zeig ich dich", this.state);

        if(needReload) {
            needReload = !needReload;
            this.loadUserStates();
        }
        if(this.state.changeData){
            this.setState({
                changeData: false,
            })
        }
        let loading = this.state.loading;
        let component = this.props.match.params.component;
        let picPath = this.state.profilePicturePath;
        let children;
        let title;
        let bottomNavigationValue = -1;

        switch(component) {
            case 'event':
                children = <LunchPlanner location={this.props.location} search={this.state.search}/>;
                title = "Events";
                bottomNavigationValue = 1;
                break;
            case 'team':
                children = <SocialScreen location={this.props.location} search={this.state.search}/>;
                title = "Teams";
                bottomNavigationValue = 2;
                break;
            case 'location':
                children = <LocationScreen location={this.props.location}/>;
                title = "Places";
                bottomNavigationValue = 0;
                break;
            case 'debts':
                children = <DebtsScreen location={this.props.location} />;
                title = "Debts";
                bottomNavigationValue = 3;
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
                    <Link to={{pathname: "/app/user", query: {onChange: this.loadUserStates}}} className={classes.noHover}>
                        <List className={classes.profile}>
                            <img alt={this.state.username} className={classes.avatar} src={this.state.profilePicture}/>
                            <p className={classes.avatarText}>{this.state.username} ● {this.state.email}</p>
                        </List>
                    </Link>
                    <Divider />
                    <List className ={classes.menu}>
                        <Link to="/app/location">
                            <MenuItem className={classes.menuItem}>
                                <Place className={classes.icons}/>
                                <p style={{fontSize: '20px',}}>Places</p>
                            </MenuItem>
                        </Link>
                        <Link  to={{pathname: "/app/event",  query: {
                                search: this.state.search,
                                events: this.getEvents,
                            }}}>
                            <MenuItem className={classes.menuItem}>
                                <LocalDining className={classes.icons}/>
                                <p style={{fontSize: '20px',}}>Events</p>
                            </MenuItem>
                        </Link>
                        <Link  to={{pathname: "/app/team",  query: {
                                search: this.state.search,
                            }}}>
                            <MenuItem className={classes.menuItem}>
                                <Group className={classes.icons}/>
                                <p style={{fontSize: '20px',}}>Teams</p>
                            </MenuItem>
                        </Link>
                        <Link to={{pathname: "/app/debts", query: {
                            search: this.state.search,
                            }}}>
                            <MenuItem className={classes.menuItem}>
                                <EuroSymbol className={classes.icons}/>
                                    <p style={{fontSize: '20px',}}>Debts</p>
                            </MenuItem>
                        </Link>
                        <Divider className={classes.menuItem}/>
                        <Link to="/app/notifications?tab=0">
                            <MenuItem className={classes.menuItem}>
                                <NotificationsNone className={classes.icons}/>
                                <p style={{fontSize: '20px',}}>Notifications</p>
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
                        <Divider className={classes.menuItem}/>
                        {/*<Link to="/app/notifications?tab=1">*/}
                            {/*<MenuItem>*/}
                                {/*<Settings className={classes.icons}/>*/}
                                {/*Options*/}
                            {/*</MenuItem>*/}
                        {/*</Link>*/}
                        <MenuItem onClick={this.signOut} className={classes.menuItem}>
                            <ExitToApp className={classes.icons}/>
                            <p style={{fontSize: '20px',}}>Sign Out</p>
                        </MenuItem>
                    </List>
                </div>
                <Divider className={classes.menuItem}/>
                {loading ?
                    (this.state.stateDates === false) ?
                        <CircularProgress className={classes.progress} color="secondary"/>
                        : <CircularProgress style={{position: 'absolute', zIndex: 10000, top: 'calc(50% - 20px)', left: 'calc(50% - 20px)'}} className={classes.progress} color="secondary"/>
                    :  <div>
                        <style>{selectedStyle}</style>
                        <DayPicker
                            className={classes.navCalendar}
                            selectedDays={this.state.selectedDay}
                        />
                    </div>
                }
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
                                {<p style={{fontSize: '20px'}}>{title}</p>}
                            </Typography>
                            <img src="/logo.png"  className={classes.logo}/>
                            {/*<div color="inherit">*/}
                                {/*{*/}
                                    {/*this.state.openSearch === false ?*/}
                                            {/*<IconButton  className={classes.search}>*/}
                                                {/*<SearchIcon onClick={this.showSearch}/>*/}
                                            {/*</IconButton> : ""*/}
                                {/*}*/}
                                {/*{this.state.openSearch ?*/}
                                        {/*<Search open={this.state.openSearch} handleCancel={this.cancelSearch} handleSearch={this.handleSearch} search={this.state.search}/> :*/}
                                        {/*""*/}
                                    {/*}*/}
                            {/*</div>*/}
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