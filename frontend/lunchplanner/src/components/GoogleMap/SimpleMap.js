import React from 'react'
import GoogleMapReact from 'google-map-react';
import Dialog from 'material-ui/Dialog';





const AnyReactComponent = ({ text }) => <div>{text}</div>;



class SimpleMap extends React.Component {

    constructor(props) {
        super();

        this.state = {
            open: true,

        };



    }


    state = {
        showingInfoWindow: false,
        activeMarker: {},
        selectedPlace: {},
    };

    handleOpenModal () {
        this.setState({ showModal: true });
    }

    handleCloseModal () {
        this.setState({ showModal: false });
    }

    onMarkerClick = (props, marker, e) =>
        this.setState({
            selectedPlace: props,
            activeMarker: marker,
            showingInfoWindow: true
        });

    onMapClicked = (props) => {
        if (this.state.showingInfoWindow) {
            this.setState({
                showingInfoWindow: false,
                activeMarker: null
            })
        }
    };


    static defaultProps = {
        center: {
            lat: 49.378835 ,
            lng:  8.675512
        },
        zoom: 11
    };

    render() {


        return (
            <div>
                <Dialog fullScreen
                    open={this.state.open}>
                    <div style={{ height: '50%', width: '70%' }}>
                        <GoogleMapReact
                        bootstrapURLKeys={{ key: "AIzaSyBxwL_2v8uEcq3ItG-gNI21NnSWgekJGNs" }}
                        defaultCenter={this.props.center}
                        defaultZoom={this.props.zoom}>
                            <AnyReactComponent
                                lat={ 49.378835  }
                                lng={ 8.675512 }
                                text={ "nope, there´s no weed!"}
                            />
                        </GoogleMapReact>
                    </div>
                </Dialog>
            </div>
        );
    }
}

export default SimpleMap;
