import React from "react";
import {List, ListItem, TextField, withStyles} from "material-ui";
import {HOST} from "../../Config";
import axios from "axios/index";
import {setAuthenticationHeader} from "../authentication/Authentication";
import Comment from "./Comment";

const styles = {
    list: {
        padding: '0px',
    }
};

class Comments extends React.Component {

    constructor(props) {
        super();

        setAuthenticationHeader();

        this.state = {
            eventId: "",
            comments: [],
            newComment: "",
        }
    }

    componentDidMount() {
        // let eventId = this.props.eventId;
        let eventId = 28;

        this.setState({
            eventId: eventId,
        });

        let url = HOST + "/event/" + eventId + "/getComments";

        axios.get(url)
            .then((response) => {
                this.setState({
                    comments: response.data,
                })
            })
    }

    textFieldChanged = (event) => {
        this.setState({
            newComment: event.target.value,
        })
    };

    onSubmit = (event) => {
      console.log(this.state.newComment);

      let url = HOST + "/event/" + this.state.eventId + "/comment";

      axios.put(url, this.state.newComment)
          .then((response) => {
              console.log(response)
          });

      event.preventDefault();
    };

    render() {
        const {classes} = this.props;
        const comments = this.state.comments;

        return (
            <div>
                <form
                    className={classes.textFieldComment}
                    noValidate autoComplete="on"
                    onSubmit={this.onSubmit}
                >
                    <TextField
                        id="textFieldComment"
                        value={this.state.newComment}
                        onChange={this.textFieldChanged}
                    />
                </form>
                <List className={classes.list}>
                    {comments.map((listValue) => {
                        return (
                            <Comment
                                text={listValue.commentText}
                                date={listValue.date}
                                creater={listValue.userName}
                            />
                        )
                    })}
                </List>
            </div>
        )
    }

}

export default withStyles(styles)(Comments);