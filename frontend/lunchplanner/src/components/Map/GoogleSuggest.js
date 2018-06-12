import React from   "react"
import GoogleMapLoader from "react-google-maps-loader"
import GooglePlacesSuggest from "react-google-places-suggest"

const MY_API_KEY = "AIzaSyCOYsTeZ29UyBEHqYG39GXJIN1-rp1KayU";

const styles = {

}

export class GoogleSuggest extends React.Component {

    constructor(props) {
        super();
        this.state = {
            search: "",
            value: "",
            onChange: props.onChange,
        }
    }

    handleInputChange = e => {
        this.setState({search: e.target.value, value: e.target.value})
    }

    handleSelectSuggest = (geocodedPrediction, originalPrediction) => {
        this.setState({search: "", value: geocodedPrediction.formatted_address})
        this.state.onChange(geocodedPrediction);
    }

    render() {
        const {search, value} = this.state;

        console.log('google succest render');

        return (
            <GoogleMapLoader
                style={{width: '200px'}}
                params={{
                    key: MY_API_KEY,
                    libraries: "places,geocode",
                }}
                render={googleMaps =>
                    googleMaps && (
                        <GooglePlacesSuggest
                            style={{width: '200px'}}
                            googleMaps={googleMaps}
                            autocompletionRequest={{
                                input: search,
                                // Optional options
                                // https://developers.google.com/maps/documentation/javascript/reference?hl=fr#AutocompletionRequest
                            }}
                            // Optional props
                            onSelectSuggest={this.handleSelectSuggest}
                            textNoResults="No result found" // null or "" if you want to disable the no results item
                            customRender={prediction => (
                                <div className="customWrapper">
                                    {prediction
                                        ? prediction.description
                                        : "No result found"}
                                </div>
                            )}
                        >

                            <input
                                type="text"
                                value={value}
                                label="Location"
                                style={{marginTop: 30, marginBottom:30, marginLeft: 20, width: "100%", maxWidth: "400px"}}
                                placeholder="Search a location"
                                onChange={this.handleInputChange}
                            />
                        </GooglePlacesSuggest>
                    )
                }
            />
        )
    }
}
export default GoogleSuggest;