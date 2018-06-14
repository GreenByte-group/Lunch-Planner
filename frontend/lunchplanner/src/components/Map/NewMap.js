import React from "react"
import { compose, withProps, lifecycle } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker} from "react-google-maps"
import Dialog from '../Dialog';

const MyMapComponent = compose(
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
            defaultZoom={17}
            defaultCenter={{ lat: parseFloat(props.lat), lng: parseFloat(props.lng) }}
        >
            {
                (props.isMarkerShown)
                    ? <Marker
                        clickable={false}
                        position={{lat: parseFloat(props.lat), lng: parseFloat(props.lng)}}
                        onClick={props.onMarkerClick}
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
                lat: props.location.query.lat,
                lng: props.location.query.lng,
            };
        } else {
            this.state = {
                isMarkerShown: false,
                open: true,
                lat: 49.4874592,
                lng: 8.466039499999965,
            }
        }
    }

    handleMarkerClick = () => {
        console.log('Marker click');
    };

    render() {
        let lat = this.state.lat || 49.4874592;
        let lng = this.state.lng || 8.466039499999965;
        let showMarker = this.state.isMarkerShown && this.state.lat && this.state.lng;

        console.log('render newmap');

        return (
            <Dialog

            >
                <MyMapComponent
                    isMarkerShown={showMarker}
                    onMarkerClick={this.handleMarkerClick}
                    lat={lat}
                    lng={lng}
                />
            </Dialog>
        )
    }
}

export default NewMap;