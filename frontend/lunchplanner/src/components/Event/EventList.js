import React from "react"

import {HOST} from "../../Config"
import Event from "./Event";
import List from "@material-ui/core/List";
import {withStyles} from "@material-ui/core/styles/index";
import {Link} from "react-router-dom";
import FloatingActionButton from "../FloatingActionButton";
import {getUsername} from "../authentication/LoginFunctions";
import {getEvents} from "./EventFunctions";
import {getHistory} from "../../utils/HistoryUtils";

const styles = {
    root: {
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
    },
    list: {
        padding: 0,
        paddingBottom: '75px',
    },
};

export let needReload = false;

export function eventListNeedReload() {
    needReload = true;
}

const lightBackground = 'transparent';
const darkerBackground = '#03030305';

class EventList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            events: [],
            search:props.search,
        }
    }

    componentDidMount() {
        this.setState({
            search: this.props.search,
        });

        this.loadEvents(this.props.search);
    }

    componentWillReceiveProps(newProps) {
        if(needReload) {
            needReload = !needReload;
            this.loadEvents();
        }
        if(newProps.search !== this.state.search){
            this.setState({
                search: newProps.search,
            });
            this.loadEvents(newProps.search);
        }
    }

    loadEvents(search) {
        if(search === null || search === undefined)
            search = this.state.search;

        getEvents(search, (response) => {
            this.setState({
                events: response.data,
            })
        });
    }

    render() {
        const { classes } = this.props;
        let events = this.state.events;
        let showLightBackground = true;

        if(needReload) {
            needReload = !needReload;
            this.loadEvents();
        }
        return (
            <div className={classes.root}>
                <List className={classes.list}>
                    {events.map(function(listValue){
                        let background;
                        if(showLightBackground)
                            background = lightBackground;
                        else
                            background = darkerBackground;
                        showLightBackground = !showLightBackground;

                        let accepted = false;
                        let invited = false;
                        let username = getUsername();
                        listValue.invitations.forEach((value) => {
                            if(value.userName === username) {
                                invited = true;
                                if(value.answer === 0) {
                                    accepted = true;
                                }
                            }
                        });

                        return <Event name={listValue.eventName}
                                      key={'Event' + listValue.eventId}
                                      id={listValue.eventId}
                                      description={listValue.eventDescription}
                                      location={listValue.location}
                                      date={listValue.startDate}
                                      background={background}
                                      accepted={accepted}
                                      invited={invited}
                                      people={listValue.invitations}
                                      token={listValue.shareToken}
                        />;
                    })}
                </List>
                <FloatingActionButton onClick={() => getHistory().push('/app/event/create')} />
            </div>

        );
    }
}

export default withStyles(styles)(EventList);