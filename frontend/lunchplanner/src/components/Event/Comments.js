import React from "react";
import {withStyles} from "material-ui";
import {HOST} from "../../Config";
import axios from "axios/index";
import {setAuthenticationHeader} from "../authentication/Authentication";

const styles = {
    para: {
        color: 'green',
    }
};

class Comments extends React.Component {

    constructor(props) {
        super();

        setAuthenticationHeader();

        this.state = {
            comments: [],
        }
    }

    componentDidMount() {
        // let eventId = this.props.eventId;
        let eventId = 5;

        let url = HOST + "/event/" + eventId + "/getComments";

        axios.get(url)
            .then((response) => {
                this.setState({
                    comments: response.data,
                })
            })
    }

    render() {
        const {classes} = this.props;
        console.log(this.state.comments);

        return (
            <p className={classes.para}>Comments</p>
        )
    }

}

export default withStyles(styles)(Comments);