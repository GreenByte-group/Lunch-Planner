import React from "react"
import {compose, withProps} from "recompose"
import {withScriptjs, withGoogleMap, GoogleMap, Marker, InfoWindow} from "react-google-maps"
import { geocodeByPlaceId } from 'react-places-autocomplete'
import { MarkerClusterer } from "react-google-maps/lib/components/addons/MarkerClusterer"
import { AddLocation, LocationOff, Create } from "@material-ui/icons"
import {getUsername} from "../authentication/LoginFunctions";
import {getSubscribedLocations, subscribe, unsubscribe} from "../User/UserFunctions";
import {getHistory} from "../../utils/HistoryUtils";
import {Button} from "@material-ui/core";

const style = {
    linkSubscribe: {
        "&:hover": {
            textDecoration: 'underline',
        }
    }
}

let SubscribeMapComponent = compose(
    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyCOYsTeZ29UyBEHqYG39GXJIN1-rp1KayU",
        loadingElement: <div style={{ height: `80%` }} />,
        containerElement: <div style={{ height: `100%` }} />,
        mapElement: <div style={{ height: `100%` }} />,
    }),
    withScriptjs,
    withGoogleMap
)((props) => {
    console.log('render', props.subscriptions);
    return (
        <div>
            <GoogleMap
                defaultZoom={14}
                defaultCenter={{lat: parseFloat(props.myLat), lng: parseFloat(props.myLng)}}
                onClick={props.onMapClick}
                defaultClickableIcons={true}
            >
                <MarkerClusterer
                    averageCenter
                    enableRetinaIcons
                    gridSize={60}
                >
                    {
                        props.subscriptions.map((value) => {
                            console.log('in render:', value);
                            if(value.isOpen || value.subscribed) {
                                console.log('erstelle marker', value);
                                return (
                                    <Marker
                                        key={value.key}
                                        clickable={true}
                                        onClick={() => props.onMarkerToggle(value.key)}
                                        position={{lat: parseFloat(value.lat), lng: parseFloat(value.lng)}}
                                    >
                                        {
                                            value.isOpen
                                                ?
                                                <InfoWindow onCloseClick={() => props.onMarkerToggle(value.key)}>
                                                    <div>
                                                        {
                                                            (value.subscribed)
                                                                ? <Button
                                                                     onClick={() => props.onMarkerToggleSubscribe(value.key)}><LocationOff/> unsubscribe</Button>
                                                                : <Button
                                                                     onClick={() => props.onMarkerToggleSubscribe(value.key)}><AddLocation/> subscribe</Button>
                                                        }
                                                        <br />
                                                        <Button
                                                           onClick={() => props.onCreateEvent(value.key)}><Create/>Create an event for this location</Button>
                                                    </div>
                                                </InfoWindow>
                                                : ''
                                        }

                                    </Marker>
                                );
                            } else
                                return '';
                        })
                    }
                </MarkerClusterer>

            </GoogleMap>
        </div>
    )
    }
)

export class SubscribeMap extends React.Component {

    /**
     * Subscriptions
     *
     * key
     * lat:
     * lng:
     */

    constructor(props) {
        super();

        this.state = {
            subscriptions: [
                // {key: 'test1', lat: 49.4874592, lng: 8.466039499999965, subscribed: true, isOpen: false},
                // {key: 'test2', lat: 49.4884692, lng: 8.466039499999965, subscribed: true, isOpen: false},
                // {key: 'test3', lat: 49.4894792, lng: 8.466039499999965, subscribed: true, isOpen: false},
                // {key: 'test4', lat: 49.48974892, lng: 8.466039499999965, subscribed: true, isOpen: false},
                ],
            myLat: 49.4874592,
            myLng: 8.466039499999965,
            clicked: false,
        };
    };

    componentWillMount() {
        navigator.geolocation.getCurrentPosition(
            position => {
                this.setState({
                    myLat: position.coords.latitude,
                    myLng: position.coords.longitude
                });
            },
            error => console.log(error)
        );

        this.loadSubscriptions();
    };

    loadSubscriptions = () => {
        getSubscribedLocations(getUsername(), (response) => {
            let array = response.data;

            array.forEach((location) => {
                geocodeByPlaceId(location)
                    .then((result) => {
                        let lat = result[0].geometry.location.lat();
                        let lng = result[0].geometry.location.lng();
                        let subscriptions = this.state.subscriptions;

                        subscriptions.push({
                            key: location,
                            lat: lat,
                            lng: lng,
                            subscribed: true,
                            isOpen: false,
                            location: result[0].formatted_address,
                        });

                        this.setState({
                            subscriptions: subscriptions,
                            clicked: !this.state.clicked,
                        })
                    })
                    .catch(error => console.error('Error', error));
            })
        })
    };

    onCreateEvent = (locationId) => {
        let sub = this.state.subscriptions;

        sub.forEach((value) => {
            if(value.key === locationId) {
                if(value.location)
                    getHistory().push("/app/event/create?location=" + value.location + "&locationId=" + value.key);
                else {
                    geocodeByPlaceId(value.key)
                        .then((result) => {
                            let address = result[0].formatted_address;
                            if(address)
                                getHistory().push("/app/event/create?location=" + address + "&locationId=" + value.key);
                        })
                }
            }
        })
    };

    handleMarkerClick = (marker) => {

        let subscriptions = this.state.subscriptions;
        subscriptions.forEach((value) => {
            if(value.key === marker) {
                value.isOpen = !value.isOpen;
            }
        });

        subscriptions = subscriptions.filter((value) => value.isOpen || value.subscribed);

        this.setState({
            subscriptions: subscriptions,
            clicked: !this.state.clicked,
        })
    };

    handleMarkerSubscribe = (marker) => {
        let subscriptions = this.state.subscriptions;
        subscriptions.forEach((value) => {
            if(value.key === marker) {
                value.subscribed = !value.subscribed;

                if(value.subscribed) {
                    this.subscribe(value.key);
                } else {
                    value.isOpen = false;
                    this.unsubscribe(value.key);
                }
            }
        });

        this.setState({
            subscriptions: subscriptions,
            clicked: !this.state.clicked,
        })
    };

    onMapClick = (event) => {
        if(!event.placeId)
            return ;

        console.log('click', event);

        let subscriptions = this.state.subscriptions;

        subscriptions.push({
            lat: event.latLng.lat(),
            lng: event.latLng.lng(),
            key: event.placeId,
            subscribed: false,
            isOpen: true,
        });

        setTimeout(() =>
        this.setState({
            subscriptions: subscriptions,
            clicked: !this.state.clicked,
        }), 100);
    };

    subscribe = (location) => {
        console.log('subscribe', location);
        subscribe(getUsername(), location, (response) => {

        });
    };

    unsubscribe = (location) => {
        console.log('unsubscribe', location);
        unsubscribe(getUsername(), location, (response) => {

        });
    };

    render() {

        return (
            <SubscribeMapComponent
                onMapClick={this.onMapClick}
                onMarkerToggle={this.handleMarkerClick}
                onMarkerToggleSubscribe={this.handleMarkerSubscribe}
                onCreateEvent={this.onCreateEvent}
                subscriptions={this.state.subscriptions}
                clicked={this.state.clicked}
                myLat={this.state.myLat}
                myLng={this.state.myLng}
            />
        )
    }
}

export default SubscribeMap;