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
        containerElement: <div style={{ height: `100vh` }} />,
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

        console.log('query', props.location.query);
        if(props.location.query && props.location.query.lat && props.location.query.lng) {
            this.state = {
                isMarkerShown: true,
                open: true,
                lat: props.location.query.lat,
                lng: props.location.query.lng,
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
                       defaultLat: position.coords.latitude,
                       defaultLng: position.coords.longitude
                   });
               },
               error => console.log(error)
           );

    }


    render() {
        let lat = this.state.lat || this.state.defaultLat || 49.4874592;
        let lng = this.state.lng || this.state.defaultLng || 8.466039499999965;
        let showMarker = !!(this.state.isMarkerShown && this.state.lat && this.state.lng);

        console.log('render newmap show:', showMarker);

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