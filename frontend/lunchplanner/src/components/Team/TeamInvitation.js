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
    text: {
        marginLeft: '24px',
        marginTop: '6px',
        float: 'left',
    },
    teamname: {
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
    }
};

class TeamInvitation extends React.Component {

    constructor(props) {
        super();

        this.state = {
            selected: props.selected || false,
            teamname: props.teamname,
            selectable: props.selectable || false,
        };
    }

    componentWillReceiveProps(newProps) {
        if(newProps.teamname && newProps.teamname !== this.state.teamname) {
            console.log('teamname: ' + newProps.teamname);
            this.setState ({
                teamname: newProps.teamname,
            });
        }

        if(newProps.selected !== undefined && newProps.selected !== null && newProps.selected !== this.state.selected) {
            this.setState ({
                selected: newProps.selected,
            });
        }
    }

    clickHandler = () => {
        if(this.state.selectable) {
            this.props.onClick(this.state.username, !this.state.selected);

            this.setState({
                selected: !this.state.selected,
            });
        }
    };

    render() {
        const {classes} = this.props;
        let selected= this.state.selected;
        let teamname = this.state.teamname;

        let listClasses = classes.listItem;
        if(selected && this.state.selectable)
            listClasses += " " + classes.selected;

        return (
            <ListItem button className={listClasses} onClick={this.clickHandler}>
                <div className={classes.content}>
                    {/*TODO picture*/}
                    <div className={classes.profilePicture}></div>
                    <div className={classes.text}>
                        <span className={classes.teamname}>{teamname}</span>
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

export default withStyles(styles)(TeamInvitation);