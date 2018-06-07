import React from "react"
import { compose, withProps, lifecycle } from "recompose"
import { withScriptjs, withGoogleMap, GoogleMap, Marker, Searchbox, FusionTablesLayer, Geocoder } from "react-google-maps"
import Dialog from '../Dialog';
import GoogleSuggest from '../Map/GoogleSuggest'



// const MyMapComponent = compose(
//     withProps({
//         googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyBxwL_2v8uEcq3ItG-gNI21NnSWgekJGNs",
//         loadingElement: <div style={{ height: `80%` }} />,
//         containerElement: <div style={{ height: `600px` }} />,
//         mapElement: <div style={{ height: `100%` }} />,
//     }),
//     withScriptjs,
//     withGoogleMap
// )((props) =>
//     <div>
//         <GoogleMap
//             defaultZoom={2}
//             defaultCenter={{ lat: 40.7127753, lng: -74.0059728 }}
//         >
//
//             <FusionTablesLayer
//                 url="http://googlemaps.github.io/js-v2-samples/ggeoxml/cta.kml"
//                 options={{
//                     query: {
//                         select: `Geocodable address`,
//                         from: `1mZ53Z70NsChnBMm-qEYmSDOvLXgrreLTkQUvvg`
//                     }
//                 }}
//             />
//             {props.isMarkerShown && <Marker setPosition={{lat: props.location.lat, lng: props.location.lng}}  onClick={props.onMarkerClick} />}
//         </GoogleMap>
//         {console.log("RATATAT: "+props.instanceOf)}
//     </div>
// )
export class NewMap extends React.Component {

    state = {
        isMarkerShown: false,
        open: true,
        lat: -34.397,
        lng: 150.644,
        locationId: 0,
        open: true,
        name:"",
        description: "",
        place:{
            placeId: null,
            location:""
        }
    }

    constructor(props) {
        super();
        console.log(props.location.query.location.geometry)

        this.state = {
            isMarkerShown:false,
            open:true,
            locationId:props.location.query.location.place_id,
            name:props.location.query.location.formatted_address,
            description:props.location.query.location.types[0]+
                        props.location.query.location.types[1],
            place: {
                placeId:  props.location.query.location.place_id,
                location:  props.location.query.location.geometry.location,
                lat: props.location.query.location.geometry.location.lat,
                lng: props.location.query.location.geometry.location.lng

            }
        }
        console.log("description: "+this.state.description)
        console.log("LocationId: "+this.state.locationId)
        console.log("name: "+this.state.name)
        console.log("place: "+this.state.place.location)
        console.log("lat: "+this.state.place.lat)


    }


    componentDidMount() {
        this.delayedShowMarker()
        console.log("Cop: "+ this.state.place.location)
        var myLatlng = {lat: this.state.place.lat,
                        lng: this.state.place.lng};

        let MyMapComponent = compose(
            withProps({
                googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyBxwL_2v8uEcq3ItG-gNI21NnSWgekJGNs",
                loadingElement: <div style={{ height: `80%` }} />,
                containerElement: <div style={{ height: `600px` }} />,
                mapElement: <div style={{ height: `100%` }} />,
            }),
            withScriptjs,
            withGoogleMap
        )((props) =>
            <div>
                <GoogleMap
                    defaultZoom={2}
                    defaultCenter={{ lat: 40.7127753, lng: -74.0059728 }}
                >

                    <FusionTablesLayer
                        url="http://googlemaps.github.io/js-v2-samples/ggeoxml/cta.kml"
                        options={{
                            query: {
                                select: `Geocodable address`,
                                from: `1mZ53Z70NsChnBMm-qEYmSDOvLXgrreLTkQUvvg`
                            }
                        }}
                    />
                    {props.isMarkerShown && <Marker setPosition={this.props.location.query. onClick={props.onMarkerClick} />}
                </GoogleMap>
                {console.log("RATATAT: "+props.instanceOf)}
            </div>
        )


        return (
            <Dialog>
                <MyMapComponent
                    isMarkerShown={this.state.isMarkerShown}
                    onMarkerClick={this.handleMarkerClick}
                    location_id={this.state.locationId}
                    location={this.state.place.location}
                />
            </Dialog>
        )

    }

    delayedShowMarker = () => {
        setTimeout(() => {
            this.setState({ isMarkerShown: true })
        }, 3000)
    }

    handleMarkerClick = () => {
        this.setState({ isMarkerShown: false })
        this.delayedShowMarker()
    }

    getCoordinates = () => {
        let lng = this.state.place.lng
        console.log(lng)
    }
    render() {
        console.log("sdjskdhskd")
        return (
          this.componentDidMount()
        )
    }
}

export default NewMap;