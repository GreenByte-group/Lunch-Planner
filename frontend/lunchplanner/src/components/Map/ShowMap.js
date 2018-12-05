import React from "react"
import {compose, withProps} from "recompose"
import PropTypes from 'prop-types';
import {withStyles} from "@material-ui/core/styles/index";
import {withScriptjs, withGoogleMap, GoogleMap, Marker, DirectionsRenderer, DirectionsRendererProps} from "react-google-maps"
import Dialog from "../Dialog";
import {getEvent} from "../Event/EventFunctions";
import {CircularProgress, Button} from "@material-ui/core";
import {geocodeByPlaceId} from "react-places-autocomplete";
import {getHistory} from "../../utils/HistoryUtils";



const styles = {
    linkSubscribe: {
        "&:hover": {
            textDecoration: 'underline',
        }
    },
    map:{
        width: '-webkit-fill-available',
        height: '-webkit-fill-available',
        maxHeight: '100%',
    },

};

const ShowMapComponent = compose(

    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyA9g1HmDqPm-H4jF-SUMPAWAEkJRbwnsSw",
        loadingElement: <div  style={{ height: `calc(100% - 56px)` }}/>,
        containerElement: <div  style={{ height: `calc(100% - 56px)` }}/>,
        mapElement: <div  style={{ height: `100%` }}/>
}),
    withScriptjs,
    withGoogleMap
)((props) => {
        return (
            <div>
                <GoogleMap
                    defaultZoom={16}
                    defaultCenter={{lat: props.lat, lng: props.lng}}
                    defaultClickableIcons={true}
                >
                        <Marker
                            clickable={false}
                            position={{lat: props.lat, lng: props.lng}}
                        />
                        <Marker
                            clickable={false}
                            position={{lat: props.myLat, lng: props.myLng}}
                        />

                </GoogleMap>

            </div>
        )
    }
);


export class ShowMap extends React.Component {

    constructor(props) {
        super();

        console.log("props in showmap", props);
        if(!props.history.location.query){

            this.state = {
                cancel: true,
            }
        }else{
            this.state = {
                eventId: props.location.query.eventId || null,
                clicked: false,
                loading: true,
                adresse: props.location.query.adresse || [],
                cancel: false,
            };
            this.getEventCredentials();
        }

    };

    componentDidMount() {
        navigator.geolocation.getCurrentPosition(
            position => {
                this.setState({
                    myLat: position.coords.latitude,
                    myLng: position.coords.longitude
                });
            },
            error => console.log(error)
        );
        this.getEventCredentials();
    };

    getEventCredentials(){
        let id = this.state.eventId;
        getEvent(id,(response) => {
            this.setState({
                lat: parseFloat(response.data.lat),
                lng: parseFloat(response.data.lng),
                placeId: response.data.locationId,
                loading: false,
            })
        });
    };
    getDirections(){
    };



    redirectToGoogleMaps = () => {
        if(this.state.adresse !== null && this.state.adresse !== undefined){
            window.open("https://www.google.com/maps/dir/?api=1&destination=" + this.state.adresse[1].long_name + "+" + this.state.adresse[0].long_name + "&travelmode=walking", '_blank');
        }else{
            window.open("https://www.google.com/maps/search/?api=1&query_place_id=" + this.state.placeId, '_blank');
        }
    };

    showAllEvents = () => {
        getHistory().push("/app/event");
    };

    render() {
        const { classes } = this.props;
        if(this.state.cancel === false) {
            let loading = this.state.loading;
            let lat = this.state.lat;
            let lng = this.state.lng;
            let adresse = this.state.adresse;

            if (lat === undefined || lng === undefined) {
                this.getEventCredentials()
            }

            let myLat = this.state.myLat;
            let myLng = this.state.myLng;

            if (myLat === null && myLng === null) {
                navigator.geolocation.getCurrentPosition(
                    position => {
                        this.setState({
                            myLat: position.coords.latitude,
                            myLng: position.coords.longitude,
                            loading: false,
                        });
                    },
                    error => console.log(error)
                );
                myLat = this.state.myLat;
                myLng = this.state.myLng;
                loading = this.state.loading;
            }

            return (
                <Dialog>
                    <Button onClick={this.redirectToGoogleMaps}
                            style={{
                                zIndex: '10000',
                                color: 'white',
                                backgroundColor: '#ff7700',
                                fontSize: 'initial',
                            }}>direct me to event...</Button>
                    <ShowMapComponent
                        lat={lat}
                        lng={lng}
                        myLng={myLng}
                        myLat={myLat}
                        redirectToGoogleMaps={this.redirectToGoogleMaps}
                        adresse={adresse}/>
                </Dialog>
            )
        }else{
            let loading = false;
            let myLat = this.state.myLat;
            let myLng = this.state.myLng;
            let lat = this.state.myLat;
            let lng = this.state.myLng;
            let adresse = "Rheinparkstraße 2, Mannheim, Deutschland";
            return(
                <Dialog>
                    <Button onClick={this.showAllEvents}
                            style={{
                                zIndex: '10000',
                                color: 'white',
                                backgroundColor: '#ff7700',
                                fontSize: 'initial',
                            }}>show me all events</Button>
                    <ShowMapComponent
                        lat={myLat}
                        lng={myLng}
                        myLng={myLng}
                        myLat={myLat}
                        />
                </Dialog>
            )
        }
    }
}

export default withStyles(styles, { withTheme: true })(ShowMap);