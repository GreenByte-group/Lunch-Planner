import React from "react"
import { compose, withProps, lifecycle } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker} from "react-google-maps"
import Dialog from '../Dialog';
import {getLatLng} from "react-places-autocomplete";
import {geolocated} from 'react-geolocated';
import DoneIcon from '@material-ui/icons/Done';
import Button from "@material-ui/core/es/Button/Button";
import {Link} from "react-router-dom";
import {getHistory} from "../../utils/HistoryUtils";
import FloatingActionButton from "../FloatingActionButton";

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
                placeId: props.location.query.locationChange,
                lat: props.location.query.lat,
                lng: props.location.query.lng,
                onLocationChange: props.location.query.locationChange,
            };
        } else {
            this.state = {
                isMarkerShown: false,
                open: true,
                placeId: null,
                lat: null,
                lng: null,
                onLocationChange: props.location.query.locationChange,
            }
        }
    }

    //just to insure for working correctly--
    handleMarkerClick = () => {
        console.log('Marker click');
    };

    //giving a eventHandler with this param
    onMapClick = (event) => {
        console.log(event)
        this.setState({
            isMarkerShown: true,
            lat: event.latLng.lat(),
            lng: event.latLng.lng(),
            placeId: event.placeId,
        })
        console.log("new Lat: "+this.state.lat+"\n"+"new Lng: "+this.state.lng);
        console.log("event: ",this.state.place_id)
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
        if(this.state.isMarkerShown){
            console.log(this.state.lat, this.state.lng, this.state.placeId)
            this.state.onLocationChange(this.state.lat, this.state.lng, this.state.placeId);
            getHistory().push(this.props.location.query.source);
        }else{
            console.log("AAAAAA")
        }
       //  console.log(this.state.lat, this.state.lng, this.state.placeId)
       // this.state.onLocationChange(this.state.lat, this.state.lng, this.state.placeId);
       //  getHistory().push(this.props.location.query.source);
    };

    render() {
        //default coordinates are the position of VSF Experts GmbH in Mannheim
        let lat = this.state.lat || this.state.defaultLat || 49.4874592;
        let lng = this.state.lng || this.state.defaultLng || 8.466039499999965;
        let showMarker = !!(this.state.isMarkerShown && this.state.lat && this.state.lng);

        console.log('render newmap show:', showMarker);

        return (
            <Dialog>
                <MyMapComponent
                    isMarkerShown={showMarker}
                    onMarkerClick={this.handleMarkerClick}
                    onMapClick={this.onMapClick}
                    lat={lat}
                    lng={lng}
                />
                <FloatingActionButton onClick={this.setLocation} icon="done" styleRoot={{marginLeft: 'calc(100% - 56px - 15px - 50px)'}}/>
            </Dialog>
        )
    }
}
export default NewMap;