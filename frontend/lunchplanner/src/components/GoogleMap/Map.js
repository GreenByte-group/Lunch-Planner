import React from 'react';
import { WithGoogleMap, GoogleMap, Marker } from "react-google-maps"

class Map extends React.Component {

    render(){

        const markers = this.props.markers || []

        return(

            <div>
                <Map


                />

                <GoogleMaps
                    defaultZoom = {3}
                    defaultCenter={{ lat: -34.397, lng: 150.644 }}>
                    {markers.map((marker,index) => (
                            <Marker {...marker}/>
                        )
                    )}

                </GoogleMaps>




            </div>

        )
    }
}

export default withGoogleMap(Map);