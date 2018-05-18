import React from "react";
import {List, ListItem, TextField, withStyles} from "material-ui";
import {HOST} from "../../Config";
import axios from "axios/index";
import {getUsername, setAuthenticationHeader} from "../authentication/Authentication";
import Comment from "./Comment";
import Dialog from "../Dialog";

const styles = {
    list: {
        padding: '0px',
        height: '100%',
    },
    textField: {
        width: '100%',
        padding: '16px',
        fontSize: '16px',
        lineHeight: '24px',
    },
    formComment: {
        height: '168px',
        boxShadow: '0 -10px 15px 0 rgba(0,0,0,0.05)',
    },
};

class Comments extends React.Component {

    constructor(props) {
        super();

        setAuthenticationHeader();

        this.state = {
            eventId: props.match.params.eventId,
            comments: [],
            newComment: "",
        }

        this.loadComments();
    }

    componentWillReceiveProps(newProps, newContext) {
        if(this.state.eventId !== newProps.eventId) {
            this.loadComments(newProps.eventId);
            this.setState({
                eventId: newProps.eventId,
            });
        }
    }

    loadComments(eventId) {
        if(eventId == null || eventId === undefined)
            eventId = this.state.eventId;

        let url = HOST + "/event/" + eventId + "/getComments";

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

    keyPress = (e) => {
        if(e.keyCode === 13){
            this.onSubmit();
        }
    };

    textFieldChanged = (event) => {
        this.setState({
            newComment: event.target.value,
        })
    };

    onSubmit = () => {

      let url = HOST + "/event/" + this.state.eventId + "/comment";

      let config = {
          headers: {
              'Content-Type': 'text/plain',
          }
      };

      axios.put(url, this.state.newComment, config)
          .then((response) => {
              this.loadComments();
          });
    };

    render() {
        const {classes} = this.props;
        const comments = this.state.comments;

        return (
            <Dialog
            title={"Comments (" + comments.length + ")"}
            closeUrl={"/event/" + this.state.eventId}
            zIndex={10001}
            >
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
                <form
                    className={classes.formComment}
                    noValidate autoComplete="on"
                >
                    <TextField
                        className={classes.textField}
                        id="textFieldComment"
                        value={this.state.newComment}
                        onChange={this.textFieldChanged}
                        onKeyDown={this.keyPress}
                        placeholder ="Write a comment ..."
                        multiline
                        rows="6"
                    />
                </form>
            </Dialog>
        )
    }

}

export default withStyles(styles)(Comments);