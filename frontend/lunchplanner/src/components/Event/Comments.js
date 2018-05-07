import React from "react";
import {List, ListItem, withStyles} from "material-ui";
import {HOST} from "../../Config";
import axios from "axios/index";
import {setAuthenticationHeader} from "../authentication/Authentication";
import Comment from "./Comment";

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
        let eventId = 28;

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
        const comments = this.state.comments;

        return (
            <List>
                {comments.map((listValue) => {
                    return (
                        <Comment
                            text={listValue.commentText}
                            date={listValue.date}
                        />
                    )
                })}
            </List>
        )
    }

}

export default withStyles(styles)(Comments);