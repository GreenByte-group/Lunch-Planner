import React from "react"
import { compose, withProps, lifecycle } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker} from "react-google-maps"
import Dialog from '../Dialog';
import {getLatLng} from "react-places-autocomplete";
import {geolocated} from 'react-geolocated';

let MyMapComponent = compose(
    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyCOYsTeZ29UyBEHqYG39GXJIN1-rp1KayU",
        loadingElement: <div style={{ height: `80%` }} />,
        containerElement: <div style={{ height: `100%` }} />,
        mapElement: <div style={{ height: `100%` }} />,
    }),
    withScriptjs,
    withGoogleMap
)((props) =>
    <div>
        <GoogleMap
            defaultZoom={(props.isMarkerShown) ? 17 : 14}
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

        if(props.location.query && props.location.query.location && props.location.query.location.geometry) {
            let location = props.location.query.location;

            console.log('lat: ', location.geometry.location.lat());
            console.log('lat: ', location.geometry.location.lng());

            this.state = {
                isMarkerShown: true,
                open: true,
                lat: location.geometry.location.lat(),
                lng: location.geometry.location.lng(),
            };
        } else {
            this.state = {
                isMarkerShown: false,
                open: true,
                lat: null,
                lng: null,
            }
        }
    }

    handleMarkerClick = () => {
        console.log('Marker click');
    };
    onMapClick = (event) => {
        this.setState({
            isMarkerShown: true,
            lat: event.latLng.lat(),
            lng: event.latLng.lng(),
        })
        console.log("new Lat: "+this.state.lat+"\n"+"new Lng: "+this.state.lng);
        console.log("event: ",event)
        this.render();
    };

    componentWillMount() {
           navigator.geolocation.getCurrentPosition(
               position => {
                   this.setState({
                       lat: position.coords.latitude,
                       lng: position.coords.longitude
                   });
               },
               error => console.log(error)
           );

    }


    render() {
        let lat = this.state.lat || 49.4874592;
        let lng = this.state.lng || 8.466039499999965;
        let showMarker = this.state.isMarkerShown && this.state.lat && this.state.lng;

        console.log('render newmap:');

        return (
            <Dialog
            >
                <MyMapComponent
                    isMarkerShown={showMarker}
                    onMarkerClick={this.handleMarkerClick}
                    onMapClick={this.onMapClick}
                    lat={lat}
                    lng={lng}
                />
            </Dialog>
        )
    }
}

export default NewMap;