import React from "react"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";
import {Link} from "react-router-dom";
import IconButton from "material-ui/es/IconButton/IconButton";
import TeamIcon from  "@material-ui/icons/Create";
import Avatar from "material-ui/es/Avatar/Avatar";
import Chip from "material-ui/es/Chip/Chip";

const styles = {
    listItem: {
        padding: '7px 16px',
        height: '64px',
        '&:hover': {
            backgroundColor: '#0303031a !important',
        }
    },
    title: {
        fontFamily: "Work Sans",
        color: 'rgba(46,46,46,0.87)',
        fontSize: '13px',
        lineHeight: '20px',
        marginBottom: '0px',
        marginLeft: '88px',
    },
    date: {
        fontFamily: "Work Sans",
        fontSize: '14px',
        lineHeight: '20px',
        marginBottom: '0px',
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
    icons: {
        width: '13px',
        height: 'auto',
    },
    text: {
        float: 'left',
        width: '100%',
        color: 'black',
    },
    row: {
        marginLeft: '12px',
        float: 'left',
        display: 'inline-flex',
        justifyContent: 'center',
    },
    icon: {
        height: '24px',
        width: '24px',
    }
};

class Team extends React.Component {

    constructor(props) {
        super();
        let invitations = props.member;
        let people = invitations.map(value => value.userName).join(', ');
        this.state = {
            name:  props.name,
            id: props.id,
            people: people,
            current: true,
        }
    }


    render() {
        const {classes} = this.props;

        const background = this.state.background;
        let accepted= this.state.accepted;

        let name = this.state.name;
        let member = this.state.member;
        let people = this.state.people;
        people = people.split(',');
        people = people.map((value) => value.trim());

        return (
            <Link to={{pathname:`/team/${this.state.id}`}}>
                <ListItem style={{backgroundColor: background}} button className={classes.listItem}>
                    <div className={classes.text}>
                        <div className={classes.row}>
                            {
                                people.map((person, index) =>{
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
                        <p className={classes.title}>{name}</p>
                    </div>
                    <IconButton>
                        <TeamIcon className={classes.icon} color="primary" />
                    </IconButton>
                </ListItem>
            </Link>

        );
    }
}

export default withStyles(styles, {withTheme: true })(Team);