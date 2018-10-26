import React from "react"
import { compose, withProps } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker} from "react-google-maps"
import Dialog from '../Dialog';
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

    handleMarkerClick = () => {
        console.log('Marker click');
    };

    onMapClick = (event) => {

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
        if(this.state.isMarkerShown){
            this.state.onLocationChange(this.state.lat, this.state.lng, this.state.placeId);
            getHistory().push(this.props.location.query.source);



        }else{

        }
       //  console.log(this.state.lat, this.state.lng, this.state.placeId)
       // this.state.onLocationChange(this.state.lat, this.state.lng, this.state.placeId);
       //  getHistory().push(this.props.location.query.source);
    };

    render() {
        let lat = this.state.lat || this.state.defaultLat || 49.474210558898626;
        let lng = this.state.lng || this.state.defaultLng || 8.46496045589447;
        let showMarker = !!(this.state.isMarkerShown && this.state.lat && this.state.lng);


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