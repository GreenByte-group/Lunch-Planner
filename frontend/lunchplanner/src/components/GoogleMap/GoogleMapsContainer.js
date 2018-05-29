import React from 'react';
import shouldPureComponentUpdate from 'react-pure-render/function';

import GoogleMap from 'google-map-react';
// import MyGreatPlace from './my_great_place.jsx';
import * as PropTypes from "@google/maps/lib/internal/validate";


function createMapOptions(maps){
    return {
        zoomControlOptions:{
            position:maps.ControlPosition.RIGHT_CENTER,
            style:maps.ZoomControlStyle.SMALL
        },
        mapTypeControlOptions:{
            position:maps.ControlPosition.TOP_RIGHT
        },
        mapTypeControl:true
    };
}

export default class GoogleMapsContainer extends React.Component {
    static propTypes = {
        center: PropTypes.array,
        zoom: PropTypes.number,
        greatePlaceCoords: PropTypes.any
    };
    static defaultProps = {
        center: [59.9, 30.3],
        zoom: 9,
        greatePlaceCoords: {lat: 59.91, lng: 30.31}
    };

    shouldComponentUpdate = shouldPureComponentUpdate;

    constructor(props) {
        super(props)
    }

    render() {
        return(
            <GoogleMap
                apiKEY={"AIzaSyBxwL_2v8uEcq3ItG-gNI21NnSWgekJGNs"}
                center={this.props.center}
                zoom={this.props.zoom}
                options={createMapOptions}>
                {/*<MyGreatPlace lat={59.955} lng={30.33} text={'A'}/>*/}
                {/*<MyGreatPlace lat={59.955} lng={30.35} text={'B'}/>*/}
            </GoogleMap>
        );
    }
}
 // KEY = "AIzaSyBxwL_2v8uEcq3ItG-gNI21NnSWgekJGNs";
