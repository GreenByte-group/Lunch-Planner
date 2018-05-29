import React from 'react';
// import {Map, InfoWindow, Marker, GoogleApiWarapper} from 'google-map-react';
import {Map, GoogleApiWrapper, InfoWindow, Marker} from "google-maps-react";
// import Map from "@material-ui/icons/es/Map";

export class LazyMap extends React.Component{
    render(){
        return(
            <Map google = {this.props.google} zoom={14}>

                <Marker onClick={this.onMarkerClick}
                        name={'Current Location'}/>

                <InfoWindow onClose={this.onInfoWindowClose}>
                    <div>
                        <h1>AMSPMAPSkAPSK</h1>
                    </div>
                </InfoWindow>
            </Map>
        );
    }
}

export default GoogleApiWrapper({
    apiKey:("AIzaSyBxwL_2v8uEcq3ItG-gNI21NnSWgekJGNs")
})(LazyMap)