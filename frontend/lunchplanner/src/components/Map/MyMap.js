import React from 'react';
import GoogleMapReact from 'google-map-react';
import Dialog from '../Dialog';
import {Marker,GoogleMap} from "react-google-maps";
import IconButton from '@material-ui/core/IconButton';

const styles = theme => ({
    button: {
        margin: theme.spacing.unit,
    },
    leftIcon: {
        marginRight: theme.spacing.unit,
    },
    rightIcon: {
        marginLeft: theme.spacing.unit,
    },
    iconSmall: {
        fontSize: 20,
    },
});

const MapComponent = ({ text }) => <div>{text}</div>;


export class MyMap extends React.Component {

    constructor (props){
        super();

        this.state = {
         open:true
         }
    }

    static defaultProps = {
        center: {
            lat: 59.95,
            lng: 30.33
        },
        zoom: 11
    };




    render() {
        return (
               <Dialog>
                // Important! Always set the container height explicitly
                <div style={{ height: '100%', width: '100%' }}>
                    <GoogleMapReact
                        bootstrapURLKeys={{ key: "AIzaSyBxwL_2v8uEcq3ItG-gNI21NnSWgekJGNs" }}
                        defaultCenter={this.props.center}
                        defaultZoom={this.props.zoom}

                    >
                        {/*<MapComponent*/}
                            {/*lat={59.955413}*/}
                            {/*lng={30.337844}*/}
                            {/*text={'Kreyser Avrora'}/>*/}
                        {/*<IconButton className="material-icons">*/}

                        {/*</IconButton>*/}
                        <GoogleMap
                            defaultZoom={8}
                            defaultCenter={{ lat: -34.397, lng: 150.644 }}
                        >
                            <Marker
                                position={{ lat: -34.397, lng: 150.644 }}
                            />
                        </GoogleMap>



                    </GoogleMapReact>
                </div>
               </Dialog>
            );

    }
}

export default MyMap;