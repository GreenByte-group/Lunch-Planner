import React from   "react"
import { geocodeByAddress, geocodeByPlaceId, getLatLng } from 'react-places-autocomplete'
import PlacesAutocomplete from 'react-places-autocomplete'
import {Input, withStyles} from "@material-ui/core"
import {Link} from "react-router-dom";
import MapIcon from '@material-ui/icons/Map'

const MY_API_KEY = "AIzaSyCOYsTeZ29UyBEHqYG39GXJIN1-rp1KayU";

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
        this.state = {
            search: "",
            value: props.value,
            onChange: props.onChange,
        };
    }

    componentDidMount() {
        this.handleSelect(this.props.value);
    }

    handleChange = (address) => {
        this.setState({ address });
        this.props.onChange(address);
    };

    handleSelect = (address) => {
        this.handleChange(address);
        geocodeByAddress(address)
            .then(results => {
                console.log('result: ', results);
                return getLatLng(results[0])
            })
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
                                lng: this.state.lng,
                                lat: this.state.lat,
                          }}}
                      location={ this.state.location}
                >
                    <MapIcon disabled={false} className={classes.mapIcon} />
                    <span>View on Map</span>
                </Link>
            </div>
        )
    }
}
export default withStyles(styles, {withTheme: true}) (GoogleSuggest);