import React from "react"
import {compose, withProps} from "recompose"
import {withScriptjs, withGoogleMap, GoogleMap, Marker, DirectionsRenderer, DirectionsRendererProps} from "react-google-maps"
import Dialog from "../Dialog";
import {getEvent} from "../Event/EventFunctions";
import CircularProgress from "@material-ui/core/CircularProgress";



const style = {
    linkSubscribe: {
        "&:hover": {
            textDecoration: 'underline',
        }
    }
};

let ShowMapComponent = compose(
    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyA9g1HmDqPm-H4jF-SUMPAWAEkJRbwnsSw",
        loadingElement: <div style={{ height: `80%` }} />,
        containerElement: <div style={{ height: `100%` }} />,
        mapElement: <div style={{ height: `100%` }} />,
    }),
    withScriptjs,
    withGoogleMap
)((props) => {
    console.log('props in map', props);
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

        console.log("props in showmap", props.location.query);
        this.state = {
            eventId: props.location.query.eventId,
            clicked: false,
            loading: true,
        };
        this.getEventCredentials();

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
        console.log('id', id);
        getEvent(id,(response) => {
            console.log("RES",response);
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

    render() {
        const { classes } = this.props;


        console.log("show states in showmap", this.state);
        let loading = this.state.loading;
        let lat = this.state.lat;
        let lng = this.state.lng;

        if(lat === undefined || lng === undefined){
            this.getEventCredentials()
        }

        let myLat = this.state.myLat;
        let myLng =this.state.myLng;

        if(myLat === null && myLng === null){
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
                {loading ?
                    <CircularProgress  color="secondary"/>
                    :
                    <ShowMapComponent
                        lat={lat}
                        lng={lng}
                        myLng={myLng}
                        myLat={myLat}
                    />
                }
            </Dialog>
        )
    }
}

export default ShowMap;