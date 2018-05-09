import React from "react";
import {List, ListItem, TextField, withStyles} from "material-ui";
import {HOST} from "../../Config";
import axios from "axios/index";
import {getUsername, setAuthenticationHeader} from "../authentication/Authentication";
import Comment from "./Comment";

const styles = {
    list: {
        padding: '0px',
    },
    textField: {
        width: '100%',
        padding: '16px',
    }
};

class Comments extends React.Component {

    constructor(props) {
        super();

        setAuthenticationHeader();

        this.state = {
            eventId: props.eventId,
            comments: [],
            newComment: "",
        }
    }

    componentDidMount() {
        let url = HOST + "/event/" + this.state.eventId + "/getComments";

        axios.get(url)
            .then((response) => {
                this.setState({
                    comments: response.data,
                })
            })
      
        // this.setState({
        //     comments: [{userName: "Martin", date: new Date(), commentText: "Text 1 a bilt longer than normal"},
        //         {userName: "Felix", date: new Date(), commentText: "Text 1 a bilt longer than normal"},
        //         {userName: "Can", date: new Date(), commentText: "Text 1 a bilt longer than normal"},
        //         {userName: "Martin", date: new Date(), commentText: "Text 1 a bilt longer than normal"}]
        // })
    }

    textFieldChanged = (event) => {
        this.setState({
            newComment: event.target.value,
        })
    };

    onSubmit = (event) => {
      console.log(this.state.newComment);

      let url = HOST + "/event/" + this.state.eventId + "/comment";

      let config = {
          headers: {
              'Content-Type': 'text/plain',
          }
      };

      axios.put(url, this.state.newComment, config)
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
                        className={classes.textField}
                        id="textFieldComment"
                        value={this.state.newComment}
                        onChange={this.textFieldChanged}
                        placeholder ="Write a comment ..."

                    />
                </form>
                <List className={classes.list}>
                    {comments.map((listValue) => {
                        let rightAlign = false;
                        if(listValue.userName === getUsername())
                            rightAlign = true;

                        return (
                            <Comment
                                text={listValue.commentText}
                                date={listValue.date}
                                creater={listValue.userName}
                                rightAlign={rightAlign}
                            />
                        )
                    })}
                </List>
            </div>
        )
    }

}

export default withStyles(styles)(Comments);