import React from   "react"
import { geocodeByAddress, geocodeByPlaceId, getLatLng } from 'react-places-autocomplete'
import PlacesAutocomplete from 'react-places-autocomplete'
import {Input, withStyles} from "@material-ui/core"
import {Link} from "react-router-dom";
import MapIcon from '@material-ui/icons/Map'
import Geocode from "react-geocode";

const MY_API_KEY = "AIzaSyA9g1HmDqPm-H4jF-SUMPAWAEkJRbwnsSw";

const styles = {
    autocomplete: {

    },
    mapIcon:{
        margin:'0 15px',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: '100px',
    },
};

export class GoogleSuggest extends React.Component {

    constructor(props) {
        super();
        this.setLocationChange = this.setLocationChange.bind(this);
        this.state = {
            search: "",
            value: props.value,
            onChange: props.onChange,
        }
    }

    handleChange = (address) => {
        this.setState({ address });
        this.props.onChange(address);
    };

    setLocationChange = (lat, lng, placeId) => {
        if(placeId == undefined || this.state.placeId == null){
        //     this.setState({
        //         address: "Type Address In ..."
        //     })
            const geocode = new Geocode
           .fromLatLng(lat, lng, MY_API_KEY)
               .then((results) =>
               {console.log(results)
                    this.setState({
                        address: results[0].formatted_address,
                    });
                    })
        }else{
            geocodeByAddress();
            geocodeByPlaceId(placeId)
                .then((results) =>
                {
                    this.setState({
                        address: results[0].formatted_address
                    })
                    console.log(this.state.address)})
                .then(this.state.onChange(String(this.state.address)))
        }
    };

    handleSelect = (address) => {
        this.handleChange(address);
        geocodeByAddress(address)
            .then(results => getLatLng(results[0]))
            .then(latLng => {
                this.setState({
                    lat: latLng.lat,
                    lng: latLng.lng,
                })
            })
            .catch(error => console.error('Error', error))
    };

    render() {
        const {classes} = this.props;
        const {search, value} = this.state;

        return (
            <div
                style={{
                    display: 'flex',
                    flexDirection: 'row',
                    width: '100%',
                }}
            >
                <PlacesAutocomplete
                    className={styles.autocomplete}
                    value={this.state.address}
                    onChange={this.handleChange}
                    onSelect={this.handleSelect}
                >
                    {({ getInputProps, suggestions, getSuggestionItemProps }) => (
                        <div style={{width: '100%'}}>
                            <Input
                                style={{
                                    marginTop: '12px',
                                }}
                                {...getInputProps({
                                    placeholder: 'Search Places ...',
                                    className: 'location-search-input'
                                })}
                                fullWidth
                                defaultValue={this.state.value}
                            />
                            <div className="autocomplete-dropdown-container">
                                {suggestions.map(suggestion => {
                                    const className = suggestion.active ? 'suggestion-item--active' : 'suggestion-item';
                                    // inline style for demonstration purpose
                                    const style = suggestion.active
                                        ? { backgroundColor: '#fafafa', cursor: 'pointer' }
                                        : { backgroundColor: '#ffffff', cursor: 'pointer' };
                                    return (
                                        <div {...getSuggestionItemProps(suggestion, { className, style })}>
                                            <span>{suggestion.description}</span>
                                        </div>
                                    )
                                })}
                            </div>
                        </div>
                    )}
                </PlacesAutocomplete>
                <Link className={classes.mapIcon}
                      float="right"
                      to={{pathname: "/app/event/create/map", query: {
                            source: "/app/event/create",
                              lng: this.state.lng,
                              lat: this.state.lat,
                              locationChange: this.setLocationChange,
                          }}}

                >
                    <MapIcon disabled={false} className={classes.mapIcon} />
                    <span>View on Map</span>
                </Link>
            </div>
        )
    }
}
export default withStyles(styles, {withTheme: true}) (GoogleSuggest);