import React from "react"
import {compose, withProps} from "recompose"
import PropTypes from 'prop-types';
import {withStyles} from "@material-ui/core/styles/index";
import {withScriptjs, withGoogleMap, GoogleMap, Marker, DirectionsRenderer, DirectionsRendererProps} from "react-google-maps"
import Dialog from "../Dialog";
import {getEvent} from "../Event/EventFunctions";
import {CircularProgress, Button} from "@material-ui/core";
import {geocodeByPlaceId} from "react-places-autocomplete";



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
        // minHeight: '40px',
        // backgroundColor: '#ff7700',
        // color: 'white',
        // fontSize: 'large',

    },

};

let ShowMapComponent = compose(

    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyA9g1HmDqPm-H4jF-SUMPAWAEkJRbwnsSw",
        loadingElement: <div style={{ height: `100%` }} />,
        containerElement: <div style={{ height: `100%` }} />,
        mapElement: <div style={{
            maxHeight: `100%`,
            height: '80%',
        }} />,


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
                    <Button onClick={props.redirectToGoogleMaps} style={{
                        width: '-webkit-fill-available',
                        height: '-webkit-fill-available',
                        minHeight: '30px',
                        maxHeight: '50px',
                        backgroundColor: '#ff7700',
                        color: 'white',
                        fontSize: 'large',}}
                    >direct me to event...</Button>
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
            adresse: props.location.query.adresse,
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

    render() {
        const { classes } = this.props;
        let loading = this.state.loading;
        let lat = this.state.lat;
        let lng = this.state.lng;
        let adresse = this.state.adresse;

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
                            redirectToGoogleMaps={this.redirectToGoogleMaps}
                            adresse={adresse}
                        />
                }
            </Dialog>
        )
    }
}

export default withStyles(styles, { withTheme: true })(ShowMap);