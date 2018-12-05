import PropTypes from "prop-types";
import {withStyles} from "@material-ui/core/styles/index";
import React from 'react';
import {Slide,CircularProgress, IconButton} from '@material-ui/core';
import {Link} from "react-router-dom";
import {Today, Schedule, MyLocation, Add} from "@material-ui/icons/";
import { Scrollbars } from 'react-custom-scrollbars';

import ListIcon from "@material-ui/icons/Assignment"
import moment from "moment";
import Dialog from "../Dialog";
import CommentsIcon from '@material-ui/icons/Message';
import UserList from "../User/UserList";
import {Button} from "@material-ui/core";
import ServiceList from "./ServiceList/ServiceList";
import {getUsername} from "../authentication/LoginFunctions";
import InvitationButton from "./InvitationButton";
import {eventListNeedReload} from "./EventContainer";
import {getHistory} from "../../utils/HistoryUtils";
import TextFieldEditing from "../editing/TextFieldEditing";
import {DatePicker, TimePicker} from "material-ui";
import {
    changeEventLocationCoordinates,
    changeEventLocation,
    changeEventTime,
    changeEventTitle, getEvent, getEventExtern, getReplyToEvent,
    inviteMemberToEvent,
    replyToEvent,
    deleteEvent
} from "./EventFunctions";
import ShareIcon from "@material-ui/icons/Share"
import {loadComments} from "./Comments/CommentFunctions";
import {geocodeByPlaceId} from "react-places-autocomplete";
import NewMap from "../Map/NewMap";

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

    const styles = {
        appBar: {
            position: 'relative',
        },
        flex: {
            flex: 1,
        },
        textField: {
            marginTop:20,
            marginBottom:30,
            marginLeft: 20,
            width: "90%",
        },
        error: {
            textAlign: 'center',
            color: '#ff7700',
            marginTop: '10px',
            marginBottom: '0px',
        },
        header: {
            backgroundColor: '#1EA185',
            color: 'white',
            padding: '16px',
            fontFamily: 'Work Sans',
            fontSize: '16px',
            lineHeight: '24px',
            // display: 'flex',
            flexDirection: 'row',
        },
        fontBig: {
            fontSize: '16px',
            margin: '0px',
            color: 'white !important',
            overflow: 'hidden',
        },
        fontBigHeader: {
            fontSize: '16px',
            margin: '0px',
            color: 'white !important',
            overflow: 'hidden',
        },
        fontSmall: {
            fontSize: '13px',
            margin: '0px',
        },
        icons: {
            height: '13px',
            width: '13px',
        },
        headerText: {
            float: 'left',
            width: 'calc(100% - 110px)',
        },
        headerComment: {
            float: 'right',
            display: 'flex',
            flexDirection: 'column',
            color: 'white',
        },
        commentIcon: {
            marginTop: '12px',
            marginRight: 'auto',
            marginLeft: 'auto',
        },
        commentText: {
            fontSize: '11px',
            lineHeight: '16px',
        },
        actionContainer: {
            width: '100px',
            float: 'left',
        },
        headerShare: {
            float: 'right',
            display: 'flex',
            flexDirection: 'column',
            color: 'white',
            marginRight: '10px',
        },
        shareIcon: {
            marginTop: '12px',
            marginRight: 'auto',
            marginLeft: 'auto',
        },
        shareText: {
            fontSize: '11px',
            lineHeight: '16px',
        },
        invitations: {
            marginLeft: '0px',
            marginTop: '8px',
        },
        invitaionsHeader: {
            marginLeft: '16px',
            marginBottom: '0px',
            fontSize: '16px',
            fontWeight: '500',
            lineHeight: '24px',
        },
        buttonInvitation: {
            zIndex: '10000',
        },
        serviceListLink: {
            height: '100px',
            width: '140px',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
        },
        taskAndDescription: {
            display: 'flex',
            flexDirection: 'row',
            height: '205px',
            width: '-webkit-fill-available',
            maxHeight: '205px',
            maxWidth: '100%',
            marginLeft: '24px',
            marginRight: '24px',
            marginTop: '24px',

        },
        serviceList: {
            alignSelf: 'center',
            display: 'flex',
            flexDirection: 'column',
            textAlign: 'center',
            color: 'black',

        },
        serviceListIcon: {
            height: '58px',
            width: '41px',
            marginLeft: 'auto',
            marginRight: 'auto',
        },
        descriptionHeader: {
            fontSize: '11px',
            margin: '0px',

        },
        description: {
            display: 'flex',
            paddingTop: '10px',
            marginTop: '15px',
            fontSize: '16px',
            width: '100%',

        },
        descriptionContainer: {
            flexDirection: 'column',
            height: '200px',
            width: '-webkit-fill-available',
            maxHeight: '205px',
            maxWidth: '100%',
            backgroundColor: '#FAFAFA',

        },
        overButton: {
            height: '100%',
            overflowY: 'auto',
            display: 'flex',
            flexDirection: 'column',
        },
        locationIcon: {
            float: 'left',
            marginRight: '5px',
            width: '30px',
            "&:hover": {
                cursor: 'pointer',
            },
        },
        image: {
            minHeight: '152px',
            width: '100%',
            backgroundPosition: 'center',
            backgroundSize: 'cover',
        },

        // TIME AND DATE
        pickerWithIcon: {
            width: '120px',
            float: 'left',
            marginTop: '10px'
        },
        datePicker: {
            width: 'calc(100% - 25px) !important',
            overflow: 'hidden',
            float: 'left',
        },
        timePicker: {
            width: 'calc(100% - 25px) !important',
            overflow: 'hidden',
            float: 'left',
        },
        pickerTextField: {
            fontSize: '14px !important',
            height: '35px !important',
            width: 'auto',
            lineHeight: '34px',
            color: 'white !important',
            marginTop: '-5px',
        },
        input: {
            color: 'white',
            fontSize: '14px',
        },
        iconsPicker: {
            width: '20px',
            height: 'auto',
            float: 'left',
            marginRight: '2px',
        },

        // ADD NEW PEOPLE
        addNewPeopleRoot: {
            height: '72px',
            padding: '20px 16px',
            backgroundColor: '#f3f3f3',
            "&:hover": {
                cursor: 'pointer',
            },
        },
        newPeopleIcon: {
            height: '32px',
            float: 'left',
        },
        newPeopleText: {
            marginTop: '6px',
            marginLeft: '57px',
        },
        myMapContent: {
            display: 'flex',
            flexDirection: 'row',
        },
        buttonContainer: {
            display: 'flex',
            flexDirection: 'row',
            bottom: 0,
            overflowY: 'hidden',
        },
        link:{
            bottom: 0,
            width: "100%",
            minHeight: '56px',
            zIndex: '10000',
            margin: '10px'
        },
        buttonDelete: {
            fontSize: '16px',
            fontFamily: 'Work Sans',
            color: "white",
            bottom: 0,
            minHeight: '56px',
            // maxHeight: '60px',
            width: '100%',
            zIndex: '10000',
        },
        buttonDeleteAccept: {
            fontSize: '16px',
            fontFamily: 'Work Sans',
            color: "white",
            bottom: 0,
            width: "100%",
            minHeight: '56px',
            zIndex: '10000',
            margin: '10px'
        },
        button:{
            fontSize: '16px',
            fontFamily: 'Work Sans',
            color: "white",
            bottom: 0,
            width: "100%",
            minHeight: '56px',
            zIndex: '10000',
        },
    };


class EventScreen extends React.Component {

    constructor(props) {
        super();

        this.state = {
            eventId: 0,
            open: true,
            isAdmin: false,
            name:"",
            location:"",
            locationId: props.locationId || "",
            lat: props.lat ,
            lng: props.lng ,
            date: "",
            description: "",
            people:[],
            accepted: false,
            isShared : false,
            token: null,
            comments: [],
            loading: true,
            acceptedUser: [],
        };

    }

    componentDidMount() {
        let eventName, description, date, people, accepted, location, locationId, lat, lng, eventId, token;

        token = this.props.match.params.securityToken;


        if(token) {
            getEventExtern(token, (response) => {
                this.setState({
                    eventId: response.data.eventId,
                    name: response.data.eventName,
                    location: response.data.location,
                    locationId: response.data.locationId,
                    lat: response.data.lat,
                    lng: response.data.lng,
                    date: new Date(response.data.startDate),
                    description: response.data.eventDescription,
                    people: response.data.invitations,
                    token: response.data.shareToken,
                    isShared: true,
                    loading: false,
                });
            });
        } else {

            eventId = this.props.match.params.eventId;
            if (this.props.location.query) {
                if (this.props.location.query.eventName) {
                    eventName = String(this.props.location.query.eventName);
                }
                if (this.props.location.query.description) {
                    description = String(this.props.location.query.description);
                }
                if (this.props.location.query.date) {
                    date = this.props.location.query.date;
                }
                if (this.props.location.query.people) {
                    people = this.props.location.query.people;
                }
                if (this.props.location.query.accepted) {
                    accepted = Boolean(this.props.location.query.accepted);
                }
                if (this.props.location.query.location) {
                    location = String(this.props.location.query.location);
                }
                if (this.props.location.query.token) {
                    token = String(this.props.location.query.token);
                }
                if (this.props.location.query.locationId) {
                    locationId = String(this.props.location.query.locationId);
                }

                this.setState({
                    eventId: eventId,
                    name: eventName,
                    description: description,
                    date: new Date(date),
                    people: people,
                    accepted: accepted,
                    location: location,
                    locationId: locationId,
                    lat: lat,
                    lng: lng,
                    token: token,
                    loading: false,
                });
                if (this.state.token !== null) {
                    this.setState({
                        isShared: true,
                    });
                }
            } else {
                this.loadEvent(eventId);
            }
        }

        loadComments(eventId, (response) => {
            if(response.status === 200) {
                this.setState({
                    comments: response.data,
                })
            }
        });

        getReplyToEvent(eventId, (response) => {
                this.setState({
                    acceptedUser: response.data,
                })
        });

        this.getLocationFromPlaceId(locationId);
    }

    parseUrl = () => {
        const params = new URLSearchParams(this.props.location.search);
        let invitedUsers = params.get('invitedUsers');
        let invitedTeams = params.get('invitedTeams');
        let teamMember = params.get('teamMember');

        let usersToInvite = [];

        if(invitedUsers) {
            usersToInvite = usersToInvite.concat(invitedUsers.split(','));
        }
        if(teamMember) {
            usersToInvite = usersToInvite.concat(teamMember.split(','));
        }

        if(usersToInvite.length !== 0) {
            //remove doubles and already invited people
            let usersToInviteUnique = usersToInvite.filter((item, pos) => {
                return usersToInvite.indexOf(item) === pos && !this.state.people.some((person) => person.userName === item);
            });

            inviteMemberToEvent(this.state.eventId, usersToInviteUnique, (user) => {
                let allUsers = this.state.people;
                allUsers.push({userName: user, admin: false});
                this.setState({
                    people: allUsers,
                });
            })
        }
    };

    reloadEventsOnSuccess = (response) => {
        if(response.status === 204) {
            eventListNeedReload();
        }
    };

    onTitleChanged = (event) => {
        this.setState({
            name: event.target.value,
        });

        changeEventTitle(this.state.eventId, event.target.value, this.reloadEventsOnSuccess);
    };

    onLocationChanged = (event) => {
        this.setState({
            location: event.target.value,
        });

        changeEventLocation(this.state.eventId, event.target.value, this.reloadEventsOnSuccess);
    };

    onMapChanged = (lat, lng, placeId) => {
        console.log('new Dataset for Googlemaps', lat, lng, placeId);
        changeEventLocationCoordinates(this.state.eventId, lat, lng, placeId, ((response) => {
            this.setState({
                lat: lat,
                lng: lng,
                placeId: placeId,
            });
    }))};

    handleDate = (event, date) => {
        let newDate = moment(date);
        let dateBefore = moment(this.state.date);
        newDate.hour(dateBefore.hour());
        newDate.minute(dateBefore.minute());

        this.setState({ date: newDate.toDate() });

        changeEventTime(this.state.eventId, newDate.toDate(), this.reloadEventsOnSuccess);
    };

    handleTime = (event, date) => {
        this.setState({ date: date });

        changeEventTime(this.state.eventId, date, this.reloadEventsOnSuccess);
    };

    loadEvent = (eventId) => {
        this.setState({
            loading: true,
        });
        if(!eventId)
            eventId = this.state.eventId;


        getEvent(eventId, (response) => {
            this.setState({
                eventId: response.data.eventId,
                name: response.data.eventName,
                description: response.data.eventDescription,
                location: response.data.location,
                people: response.data.invitations,
                date: new Date(response.data.startDate),
                token: response.data.shareToken,
                loading: false,
            })
        });
        if(this.state.token !== null){
            this.setState({
                isShared: true,
            });
        }
    };

    handleDelete = () => {
        deleteEvent(this.state.eventId, response => {
            if(response.status === 204) {
                eventListNeedReload();
            }
        });

    };

    handleDecline = () => {
        this.sendAnswer('reject', () => {
            this.forceUpdate();
            getHistory().push("/app/event/");
        });
    };

    handleAccept = () => {
        let username = getUsername();
        let invitationAccepted = false;

        this.state.people.forEach((value) => {
            if(value.userName === username) {
                if(value.answer === 0) {
                    invitationAccepted = true;
                }
            }
        });

        if(invitationAccepted) {
            let isEmpty = false;
            let id = this.state.eventId;
            let participant = this.state.people;

            this.handleDecline();
        } else {
            this.sendAnswer('accept');
        }

    };

    sendAnswer = (answer, then) => {
        replyToEvent(this.state.eventId, answer, (response) => {
            this.loadEvent();
            eventListNeedReload();
            if(then)
                then();
        });
    };

    getLocationFromPlaceId = (placeId) => {
        let placeIdR = placeId;
        geocodeByPlaceId(placeIdR)
            .then(result => result[0].geometry.location.lat())
            .then(result => this.setState({
                lat: result,
            }))
            .catch(error => console.error('Error', error));


        geocodeByPlaceId(placeIdR)
            .then(result => result[0].geometry.location.lng())
            .then(result => this.setState({
                lng: result,
            }))
            .catch(error => console.error('Error', error));


    };

    getLocationFromPlaceIdTemp (placeId){
        geocodeByPlaceId(placeId)
            .then(result => result[0].address_components)
            .then(result => this.setState({
                adresse: result,
            }))
            .catch(error => console.error('Error', error));
    };

    getCredentialsFromEvent(){
        let id = this.state.eventId;
        let adresse = "";
        getEvent(id,(response) => {
            this.setState({
                lat: parseFloat(response.data.lat),
                lng: parseFloat(response.data.lng),
                locationId: response.data.locationId,
            });
            adresse = this.getLocationFromPlaceIdTemp(response.data.locationId);
        });
    };


    render() {
        const { classes } = this.props;
        const error = this.state.error;
        let name = this.state.name;
        let description = this.state.description;
        let adresse = this.state.adresse;
        let date = this.state.date;
        let location = this.state.location;
        let locationId = this.state.locationId;
        let lat = this.state.lat;
        let lng = this.state.lng;
        let people = this.state.people;
        let eventId = this.state.eventId;
        let isShared = this.state.isShared;
        let loading =  this.state.loading;


        if(lat === undefined || lng === undefined){
            this.getCredentialsFromEvent(this.state.eventId);
        }

        if(people.length !== 0) {
            this.parseUrl();
        }

        let momentDate = moment(date);

        let time = momentDate.format('HH:mm');
        let monthDay = momentDate.format('DD MMM');

        let admin = "";
        let selectedUsers = [];

        people.sort((a, b) => {
            if(a.answer === 0 && b.answer !== 0) {
                return -1;
            } else if(a.answer !== 0 && b.answer === 0) {
                return 1;
            } else {
                return 0;
            }
        });

        let username = getUsername();
        let invited = false;
        let accepted = false;
        let buttonText = "Join Event";
        let deleteText = "Delete Event";
        let barTitle = name;



        let iAmAdmin = false;

        people.forEach((listValue) => {
            if(listValue.userName === username) {
                if(listValue.answer !== 0) {
                    invited = true;
                    barTitle = "Invitation...";
                } else {
                    accepted = true;
                    buttonText = "Leave Event";
                }

                if(listValue.admin) {
                    iAmAdmin = true;
                }
            }

            if(listValue.answer === 0) {
                selectedUsers.push(listValue.userName);
            }

            if(listValue.admin) {
                admin = listValue.userName;
            }
        });

        let countComments = this.state.comments.length;

        return (
            <div>
                        {loading ?
                            <CircularProgress className={classes.progress} color="secondary"/>
                            :
                <Dialog
                    title={barTitle}
                    closeUrl="/app/event"
                >
                    <div className={classes.overButton}>
                        <div className={classes.header}>
                            <div className={classes.headerText}>
                                <p className={classes.fontSmall}>Created by {admin}</p>
                                <div>
                                    {(!iAmAdmin) ?
                                        <Link className={classes.mapIcon}
                                              float="right"
                                              to={{pathname: "/app/event/showMap", query: {
                                                      source: "/app/event",
                                                      locationId: this.state.locationId,
                                                      lat: lat,
                                                      lng: lng,
                                                      eventId: parseInt(eventId),
                                                      adresse: adresse,
                                                  }}}

                                        >
                                            <MyLocation  disabled={true} className={classes.locationIcon}/>
                                        </Link>
                                        :

                                        <Link className={classes.mapIcon}
                                              float="right"
                                              to={{pathname: "/app/event/create/map", query: {
                                                      source: "/app/event",
                                                      change: true,
                                                      locationId: this.state.locationId,
                                                      lat: lat,
                                                      lng: lng,
                                                      eventId: parseInt(eventId),
                                                      onMapChange: this.onMapChanged
                                                  }}}
                                        >
                                            <MyLocation  disabled={false} className={classes.locationIcon}/>
                                         </Link>
                                    }

                                    <TextFieldEditing onChange={this.onTitleChanged} value={name} editable={iAmAdmin} className={classes.fontBigHeader} />
                                </div>
                                {
                                    (iAmAdmin || name !== location)
                                        ?   <TextFieldEditing onChange={this.onLocationChanged} value={location} editable={iAmAdmin} className={classes.fontBig} />
                                        : ''
                                }
                                {
                                    (iAmAdmin)
                                        ? <div>
                                            <div className={classes.pickerWithIcon}>
                                                <Today viewBox="-2 -4 26 26" className={classes.iconsPicker} />
                                                <DatePicker
                                                    className={classes.datePicker}
                                                    onChange={this.handleDate}
                                                    value={this.state.date}
                                                    textFieldStyle={styles.pickerTextField}
                                                    inputStyle={styles.input}
                                                />
                                            </div>
                                            <div className={classes.pickerWithIcon}>
                                                <Schedule viewBox="-2 -4 26 26" className={classes.iconsPicker}/>
                                                <TimePicker
                                                    className={classes.timePicker}
                                                    onChange={this.handleTime}
                                                    value={this.state.date}
                                                    format="24hr"
                                                    textFieldStyle={styles.pickerTextField}
                                                    inputStyle={styles.input}
                                                />
                                            </div>
                                        </div>
                                        : <p className={classes.fontSmall}><Today viewBox="-5 -5 27 27" className={classes.icons} /> {monthDay} <Schedule viewBox="-5 -5 27 27" className={classes.icons}/> {time}</p>
                                }
                            </div>
                            <div className={classes.actionContainer}>
                                <Link to={{pathname:`/app/event/${eventId}/comments`}}>
                                    <div className={classes.headerComment}>
                                    <CommentsIcon className={classes.commentIcon} />
                                        <p className={classes.commentText}>Comments ({countComments})</p>
                                    </div>
                                </Link>
                                {(iAmAdmin || isShared)
                                    ?
                                    <Link to={{pathname:`/app/event/${eventId}/share`, query: {
                                            source: "/app/event/" + this.state.eventId}}}>
                                        <div className={classes.headerShare}>
                                            <ShareIcon className={classes.shareIcon}/>
                                            <p className={classes.shareText}>Share</p>
                                        </div>
                                    </Link>
                                    : ''
                                }
                            </div>

                        </div>
                        <div className={classes.invitations}>
                            <p className={classes.invitaionsHeader}>Invited People ({people.length})</p>
                            {
                                (iAmAdmin)
                                    ? <Link to={{pathname: `/app/event/${eventId}/invite`,  query: {
                                                    source: "/app/event/" + this.state.eventId,
                                                    invitedUsers: people.map((value) => value.userName).join(','),
                                                }}}>
                                        <div className={classes.addNewPeopleRoot}>
                                            <Add className={classes.newPeopleIcon} />
                                            <p className={classes.newPeopleText}>Add more people...</p>
                                        </div>
                                    </Link>
                                    : ''
                            }
                            <UserList selectedUsers={selectedUsers}  othersInvited={true} users={people} selectable={false} />
                        </div>

                        <div className={classes.taskAndDescription}>
                        {
                            (accepted)
                                ? <div>
                                    <ServiceList eventId={eventId} />
                                    <Link className={classes.serviceListLink} to={{pathname:`/app/event/${eventId}/service`}}>
                                        <div className={classes.serviceList}>
                                            <ListIcon className={classes.serviceListIcon} />
                                            <p>Add a task</p>
                                        </div>
                                    </Link>
                                </div>
                                : ''
                        }
                            <div className={classes.descriptionContainer}>
                                <p className={classes.descriptionHeader}>Description </p>
                                <Scrollbars className={classes.scrollbar}>
                                    <TextFieldEditing  rowsMax="3" value={description} className={classes.description} />
                                </Scrollbars>
                            </div>
                        </div>
                    </div>
                    {
                        (invited)
                            ?   <InvitationButton decline={this.handleDecline} join={this.handleAccept} class={classes.buttonInvitation} />
                            :   (iAmAdmin)
                                    ?   <div className={classes.buttonContainer}>
                                            <Link
                                                className={classes.link}
                                                to={{
                                                    pathname: "/app/event", query: {
                                                        source: "/app/event/",
                                                        search: "search",
                                                    }
                                                }}
                                            >
                                                <Button variant="raised" color="secondary" onClick={this.handleDelete} className={classes.buttonDelete}>{deleteText}</Button>
                                            </Link>
                                            <Button variant="raised" color="secondary" onClick={this.handleAccept} className={classes.buttonDeleteAccept}>{buttonText}</Button>
                                        </div>
                                    : <Button variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>{buttonText}</Button>
                    }
                </Dialog>}
            </div>
        );
    }
}
EventScreen.propTypes = {
    classes: PropTypes.object.isRequired,
    id: PropTypes.number.isRequired,
    isAdmin: PropTypes.bool.isRequired,
    name:PropTypes.string.isRequired,
    location:PropTypes.string.isRequired,
    monthDay: PropTypes.object.isRequired,
    time: PropTypes.object.isRequired,
    description: PropTypes.string.isRequired,
    people:PropTypes.object.isRequired,
    accepted: PropTypes.bool.isRequired,

};
export default withStyles(styles, { withTheme: true })(EventScreen);