import React from "react"
import { compose, withProps } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker, InfoWindow} from "react-google-maps"
import Dialog from '../Dialog';
import {getHistory} from "../../utils/HistoryUtils";
import FloatingActionButton from "../FloatingActionButton";
import {geocodeByAddress, geocodeByPlaceId} from "react-places-autocomplete";
import {getEvent} from "../Event/EventFunctions";

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
        console.log('NewMap PROPS',props.location.query);

        if(props.location.query.change){
            console.log('change = true');

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
                };
            }else{
                getEvent(props.location.query.eventId, response => {
                    console.log('zweiter Weg', response);
                    this.state = {
                        eventId: props.location.query.eventId,
                        placeId: response.data.locationId,
                        lat: response.data.lat,
                        lng: response.data.lng,
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
                };
            }else{
                if(props.location.query.locationId){
                    console.log('LETZTE VERZWEIGUNG');
                    this.state = {
                        isMarkerShown: true,
                        open: true,
                        change:false,
                        eventId: props.location.query.eventId,
                        placeId: props.location.query.locationId,
                        onLocationChange: props.location.query.locationChange,
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
                    };
                }
            };
        };


    }



    handleMarkerClick = () => {
        console.log('Marker click');
    };

    onMapClick = (event) => {

        console.log(event)
        this.setState({
            isMarkerShown: true,
            lat: event.latLng.lat(),
            lng: event.latLng.lng(),
            placeId: event.placeId,
        })

        this.render();
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
        console.log("NOPE");
        if(this.state.isMarkerShown){
            this.state.onLocationChange(this.state.lat, this.state.lng, this.state.placeId);
            getHistory().push(this.props.location.query.source);
        }else{

        }
    };

    setLocationChange = () => {
        console.log("new",this.props.location.query.source + "/" + this.state.eventId);
        if(this.props.location.query.change === true){
            if(this.state.isMarkerShown){
                this.state.onMapChange(this.state.lat, this.state.lng, this.state.placeId);
                getHistory().push(this.props.location.query.source + "/" + this.state.eventId);
            };
        }else{
            if(this.state.isMarkerShown){
                this.state.onMapChange(this.state.lat, this.state.lng, this.state.placeId);
                getHistory().push(this.props.location.query.source);
            };
        }

    };

    render() {
        let lat = this.state.lat || this.state.defaultLat || 49.474210558898626;
        let lng = this.state.lng || this.state.defaultLng || 8.46496045589447;
        let showMarker = (this.state.isMarkerShown);
        let change = this.state.change;

        console.log("MAP STATE", this.state)

        if(change === true){
            return (
                <Dialog>
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