import React from "react"
import {withStyles, ListItem, IconButton} from "@material-ui/core";
import {Close} from "@material-ui/icons"
import AcceptedButton from "../Event/AcceptedButton";
import {getProfilePicturePath} from "./UserFunctions";

const styles = {
    listItem: {
        padding: '20px 16px',
        '&:hover': {
            backgroundColor: '#0303031a !important',
        },
        transition: 'background 0.5s',
    },
    content: {
        width: '100%',
    },
    selected: {
        backgroundColor: '#bacfa255',
        '&:hover': {
            backgroundColor: '#bacfa288 !important',
        }
    },
    unselectable: {
        backgroundColor: 'white !important',
        '&:hover': {
            backgroundColor: 'white !important',
            cursor: 'default',
        }
    },
    text: {
        marginLeft: '24px',
        marginTop: '6px',
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
        height: '32px',
        width: '32px',
        overflow: 'hidden',
    },
    profilePictureImg: {
        objectFit: 'cover',
        width: '100%',
        height: '100%',
    },
    iconButtonRemove: {
        marginLeft: '7px',
    },
    iconRemove: {

    },
};

class Event extends React.Component {

    constructor(props) {
        super();

        this.state = {
            selected: props.selected || false,
            invited: props.invited || false,
            username: props.username,
            selectable: props.selectable || false,
            clickRemove: props.clickRemove,
            profilePicturePath: '',
        };

        this.getUserImage(props.username);
    }

    componentWillReceiveProps(newProps) {
        if(newProps.username && newProps.username !== this.state.username) {
            this.setState ({
                username: newProps.username,
            });
            this.getUserImage(newProps.username);
        }

        if(newProps.selected !== undefined && newProps.selected !== null && newProps.selected !== this.state.selected) {
            this.setState ({
                selected: newProps.selected,
            });
        }

        if(newProps.invited !== undefined && newProps.invited !== null && newProps.selected !== this.state.invited) {
            this.setState ({
                invited: newProps.invited,
            });
        }
    }

    getUserImage = (username) => {
        getProfilePicturePath(username, (response) => {
            this.setState({
                profilePicturePath: response.data,
            })
        })
    };

    clickHandler = () => {
        if(this.state.selectable) {
            this.props.onClick(this.state.username, !this.state.selected);

            this.setState({
                selected: !this.state.selected,
            });
        }
    };

    clickRemove = () => {
        this.state.clickRemove(this.state.username);
    };

    render() {
        const {classes} = this.props;
        let selected= this.state.selected;
        let invited = this.state.invited;
        let username = this.state.username;

        let listClasses = classes.listItem;
        if(selected && this.state.selectable)
            listClasses += " " + classes.selected;
        else if(!this.state.selectable)
            listClasses += " " + classes.unselectable;

        return (
            <ListItem button className={listClasses} onClick={this.clickHandler}>
                <div className={classes.content}>
                    <div className={classes.profilePicture}>
                        <img className={classes.profilePictureImg} src={this.state.profilePicturePath} />
                    </div>
                    <div className={classes.text}>
                        <span className={classes.username}>{username}</span>
                    </div>
                </div>
                {(selected
                        ? <AcceptedButton />
                        : ""
                )}
                {(!selected && invited
                        ? <AcceptedButton invited={true} />
                        : ""
                )}
                {(this.state.clickRemove)
                        ? <IconButton className={classes.iconButtonRemove} onClick={this.clickRemove}><Close className={classes.iconRemove} /></IconButton>
                        : ""
                }
            </ListItem>
        );
    }
}

export default withStyles(styles)(Event);