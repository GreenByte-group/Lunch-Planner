import React from "react"
import { compose, withProps } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker, InfoWindow} from "react-google-maps"
import Dialog from '../Dialog';
import {getHistory} from "../../utils/HistoryUtils";
import FloatingActionButton from "../FloatingActionButton";
import {geocodeByAddress, geocodeByPlaceId} from "react-places-autocomplete";
import {getEvent} from "../Event/EventFunctions";
import Geocode from 'react-geocode';



let MyMapComponent = compose(
    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyA9g1HmDqPm-H4jF-SUMPAWAEkJRbwnsSw",
        loadingElement: <div  style={{ height: `calc(100% - 56px)` }}/>,
        containerElement: <div  style={{ height: `calc(100% - 56px)` }}/>,
        mapElement: <div  style={{ height: `100%` }}/>
    }),
    withScriptjs,
    withGoogleMap
)((props) =>
    <div>
        <GoogleMap
            defaultZoom={(props.isMarkerShown) ? 17 : 15}
            defaultCenter={{ lat: parseFloat(props.lat), lng: parseFloat(props.lng) }}
            onClick={(e) => props.onMapClick(e)}
        >
            {
                (props.isMarkerShown)
                    ? <Marker
                        clickable={false}
                        position={{lat: parseFloat(props.lat), lng: parseFloat(props.lng)}}
                    />
                    : ''
            }

        </GoogleMap>
    </div>
)

export class NewMap extends React.Component {

    constructor(props) {
        super();
        if(props.location.query.change){
            if(props.location.query.lat && props.location.query.lng) {
                this.state = {
                    isMarkerShown: true,
                    open: true,
                    change:true,
                    eventId: props.location.query.eventId,
                    placeId: props.location.query.locationId,
                    lat: props.location.query.lat,
                    lng: props.location.query.lng,
                    onMapChange: props.location.query.onMapChange,
                    adresse: props.location.query.adresse,
                };
            }else{
                getEvent(props.location.query.eventId, response => {
                    this.state = {
                        eventId: props.location.query.eventId,
                        placeId: response.data.locationId,
                        lat: response.data.lat,
                        lng: response.data.lng,
                        adresse: props.location.query.adresse,
                    }
                })
            };
        }else{
            if(props.location.query && props.location.query.lat && props.location.query.lng) {
                this.state = {
                    isMarkerShown: true,
                    open: true,
                    change:false,
                    eventId: props.location.query.eventId,
                    placeId: props.location.query.locationChange,
                    lat: props.location.query.lat,
                    lng: props.location.query.lng,
                    onLocationChange: props.location.query.locationChange,
                    adresse: props.location.query.adresse,

                };
            }else{
                if(props.location.query.locationId){
                    this.state = {
                        isMarkerShown: true,
                        open: true,
                        change:false,
                        eventId: props.location.query.eventId,
                        placeId: props.location.query.locationId,
                        onLocationChange: props.location.query.locationChange,
                        adresse: props.location.query.adresse,

                    };
                }else {
                    this.state = {
                        isMarkerShown: false,
                        open: true,
                        change:false,
                        eventId: props.location.query.eventId,
                        placeId: null,
                        lat: null,
                        lng: null,
                        onLocationChange: props.location.query.locationChange,
                        adresse: null,

                    };
                }
            };
        };
    }



    handleMarkerClick = () => {
    };

    onMapClick = (event) => {
        this.setState({
            isMarkerShown: true,
            lat: event.latLng.lat(),
            lng: event.latLng.lng(),
            placeId: event.placeId,
            adresse: null,
        });
    };


    componentWillMount() {
           navigator.geolocation.getCurrentPosition(
               position => {
                   this.setState({
                       defaultLat: position.coords.latitude,
                       defaultLng: position.coords.longitude,
                   });
               },
               error => console.log(error)
           );
           this.setState({
              placeId:null
           });

    }

    setLocation = () => {
        if(this.state.isMarkerShown){
            this.state.onLocationChange(this.state.lat, this.state.lng, this.state.placeId);
            getHistory().push(this.props.location.query.source);
        }
    };

    setLocationChange = () => {
        if(this.props.location.query.change === true){
            if(this.state.isMarkerShown){
                this.state.onMapChange(this.state.lat, this.state.lng, this.state.placeId);
                getHistory().push(this.props.location.query.source + "/" + this.state.eventId);
            }
        }else{
            if(this.state.isMarkerShown){
                this.state.onMapChange(this.state.lat, this.state.lng, this.state.placeId);
                getHistory().push(this.props.location.query.source);
            }
        }
    };

    getAdresseAndPlaceIdFromLatLng = () => {
        Geocode.setApiKey("AIzaSyA9g1HmDqPm-H4jF-SUMPAWAEkJRbwnsSw");
        Geocode.fromLatLng(this.state.lat, this.state.lng)
            .then((result) => {
                this.setState({
                    adresse: result.results[0].address_components,
                    placeId: result.results[0].place_id,
                }) ;
            })
            .then(() => {
                let placeId = this.state.placeId;
                window.open("https://www.google.com/maps/dir/?api=1&destination=" + this.state.adresse[1].long_name + "+" + this.state.adresse[0].long_name + "&travelmode=walking", '_blank');
            });

    };

    redirectToGoogleMaps = () => {
        if(this.state.adresse !== null && this.state.adresse !== undefined){
            window.open("https://www.google.com/maps/dir/?api=1&destination=" + this.state.adresse[1].long_name + "+" + this.state.adresse[0].long_name + "&travelmode=walking", '_blank');
        }
        else if(this.state.placeId !== null && this.state.placeId !== undefined){
            window.open("https://www.google.com/maps/dir/?api=1&&destination_place_id=" + this.state.placeId + "&travelmode=walking", '_blank');
        }
        if((this.state.placeId === null || this.state.placeId === undefined) && (this.state.adresse === null || this.state.adresse === undefined)){
            this.getAdresseAndPlaceIdFromLatLng();
        }


    };

    render() {
        let lat = this.state.lat || this.state.defaultLat || 49.474210558898626;
        let lng = this.state.lng || this.state.defaultLng || 8.46496045589447;
        let showMarker = (this.state.isMarkerShown);
        let change = this.state.change;
        if(change === true){
            return (
                <Dialog>
                    <FloatingActionButton onClick={this.redirectToGoogleMaps}  styleRoot={{zIndex: '10000', top: '20px'}}/>
                    <MyMapComponent
                        isMarkerShown={showMarker}
                        onMarkerClick={this.handleMarkerClick}
                        onMapClick={this.onMapClick}
                        lat={lat}
                        lng={lng}/>
                    <FloatingActionButton onClick={this.setLocationChange} icon="done" styleRoot={{marginLeft: 'calc(100% - 56px - 15px - 50px)'}}/>
                </Dialog>
            )
        }else{
            return (
                <Dialog>
                    <MyMapComponent
                        isMarkerShown={showMarker}
                        onMarkerClick={this.handleMarkerClick}
                        onMapClick={this.onMapClick}
                        lat={lat}
                        lng={lng}/>
                    <FloatingActionButton onClick={this.setLocation} icon="done" styleRoot={{marginLeft: 'calc(100% - 56px - 15px - 50px)'}}/>
                </Dialog>
            )
        }

    }
}
export default NewMap;