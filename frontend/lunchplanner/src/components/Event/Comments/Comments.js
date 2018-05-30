import React from "react";
import {List, ListItem, TextField, withStyles} from "material-ui";
import {HOST} from "../../../Config";
import {getUsername, setAuthenticationHeader} from "../../authentication/Authentication";
import Comment from "./Comment";
import Dialog from "../../Dialog";
import {loadComments, sendComment} from "./CommentFunctions";

const styles = {
    list: {
        padding: '0px',
        overflowY: 'auto',
        height: '100%',
    },
    textField: {
        zIndex: '10003',
        width: '100%',
        padding: '16px',
        fontSize: '16px',
        lineHeight: '24px',
    },
    formComment: {
        backgroundColor: 'white',
        zIndex: '10003',
        position: 'fixed',
        bottom: 0,
        width: '100%',
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

        loadComments(eventId, (response) => {
            this.setState({
                comments: response.data,
            })
        });
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

        sendComment(this.state.eventId, this.state.newComment,
            (response) => {
                this.loadComments();
        });
    };

    render() {
        const {classes} = this.props;
        const comments = this.state.comments;

        return (
            <div>
                <Dialog
                    title={"Comments (" + comments.length + ")"}
                    closeUrl={"/event/" + this.state.eventId}
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
            </div>
        )
    }

}

export default withStyles(styles)(Comments);