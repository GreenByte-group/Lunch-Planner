import React from "react"
import moment from "moment"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";
import {Schedule, Today} from "@material-ui/icons";
import AcceptedButton from "../Event/AcceptedButton";

const styles = {
    listItem: {
        padding: '20px 16px',
        '&:hover': {
            backgroundColor: '#0303031a !important',
        }
    },
    content: {
        width: '100%',
    },
    selected: {

    },
    text: {
        marginLeft: '24px',
        float: 'left',
    },
    username: {
        fontFamily: "Work Sans",
        fontSize: '13px',
        lineHeight: '20px',
    },
    placeholder: {
        height: '100%',
        display: 'inline-block',
        verticalAlign: 'middle',
    },
    profilePicture: {
        float: 'left',
        border: '1px black solid',
        borderRadius: '50%',
        height: '24px',
        width: '24px',
    }
};

class Event extends React.Component {

    constructor(props) {
        super();

        this.state = {
            selected: props.accepted | true,
            username: props.username,
        }
    }

    render() {
        const {classes} = this.props;
        let selected= this.state.selected;
        let username = this.state.username;
        let background = 'white'; //TODO change background

        return (
            <ListItem style={{backgroundColor: background}} button className={classes.listItem}>
                <div className={classes.content}>
                    {/*TODO picture*/}
                    <div className={classes.profilePicture}></div>
                    <div className={classes.text}>
                        <span className={classes.username}>{username}</span>
                    </div>
                </div>
                {(selected
                        ? <AcceptedButton />
                        : ""
                )}
            </ListItem>
        );
    }
}

export default withStyles(styles)(Event);