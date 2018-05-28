import React from "react"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";
import AcceptedButton from "../Event/AcceptedButton";
import Avatar from "material-ui/es/Avatar/Avatar";

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
    },
    member: {
        height: '24px',
        width: '24px',
        fontFamily: "Work Sans",
        fontSize: '13px',
        lineHeight: '20px',
        marginBottom: '0px',
        color: '#A4A4A4',
        marginLeft: '-12px',
    },
    memberAvatar: {
        height: '24px',
        width: '24px',
        border: '1px solid white',
    },
    memberAvatarText: {
        fontSize:'10px',
        marginLeft: '-8px',
    },
    memberAvatarTextLast: {
        marginLeft: '0px',
        fontSize: '10px',
    },
    row: {
        marginLeft: '12px',
        float: 'left',
        display: 'inline-flex',
        justifyContent: 'center',
    },
};

class TeamInvitation extends React.Component {

    constructor(props) {
        super();
        let invitations = props.member;
        let people = invitations.map(value => value.userName).join(', ');
        this.state = {
            selected: props.selected || false,
            teamname: props.teamname,
            teamId: props.teamId,
            people: people,
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
            this.props.onClick(this.state.teamname, !this.state.selected);

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
        let people = this.state.people;
        people = people.split(',');
        people = people.map((value) => value.trim());
        let member = 0;

        return (
            <ListItem button className={listClasses} onClick={this.clickHandler}>
                <div className={classes.content}>
                    <div className={classes.row}>
                    {/*TODO picture*/
                    people.map((person, index) =>{
                        member = index + 1;
                    return(
                        <div className={classes.member}>
                    {(index === people.length - 1)
                        ?
                        <Avatar className={classes.memberAvatar}><span
                            className={classes.memberAvatarTextLast}>{person.charAt(0)}</span></Avatar>
                        :
                        <Avatar className={classes.memberAvatar}><span
                            className={classes.memberAvatarText}>{person.charAt(0)}</span></Avatar>
                    }
                        </div>)

                })
                    }
                    </div>
                    <div className={classes.text}>
                        <span className={classes.teamname}>
                            <p>{teamname} ({member})</p>
                        </span>
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