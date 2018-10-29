import React from "react"
import {withStyles, ListItem, Avatar, IconButton} from "@material-ui/core";
import {Link} from "react-router-dom";
import TeamIcon from  "@material-ui/icons/Create";
import {getProfilePicturePath} from "../User/UserFunctions";
import {HOST} from "../../Config";

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
    },
    titleHeader: {
        fontFamily: "Work Sans",
        color: 'rgba(46,46,46,0.5)',
        fontSize: '8px',
        marginBottom: '0',

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
        // border: '1px solid white',
    },
    memberPicture: {
        height: '100%',
        width: '100%',
        objectFit: 'cover',
    },
    icons: {
        width: '13px',
        height: 'auto',
    },
    text: {
        display: 'flex',
        flexDirection: 'row',
        float: 'left',
        width: '100%',
        color: 'black',
    },
    row: {
        marginLeft: '12px',
        float: 'left',
        display: 'inline-flex',
        justifyContent: 'center',
        marginTop: '5px',
    },
    column: {
        display: 'flex',
        flexDirection: 'column',
        marginLeft: '30px'
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
        let people = invitations.map(value => {
            let stateId = "pic" + value.userName.replace(/\s/g, '');
            getProfilePicturePath(value.userName, (response) => {
                this.setState({
                    [stateId]: HOST + response.data,
                })
            });
            return value.userName
        }).join(', ');

        this.state = {
            name:  props.name,
            id: props.id,
            people: people,
            current: true,
            selected: props.selected || false,
            selectable: props.selectable || false,
        };
    }

    componentWillReceiveProps(newProps) {
        if(newProps.name && newProps.name !== this.state.name) {
            this.setState ({
                name: newProps.name,
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
            this.props.onClick(this.state.name, !this.state.selected);

            this.setState({
                selected: !this.state.selected,
            });
        }
    };


    render() {
        const {classes} = this.props;

        const background = this.state.background;
        let accepted= this.state.accepted;

        let selected= this.state.selected;
        let username = this.state.username;

        let name = this.state.name;
        let member = this.state.member;
        let people = this.state.people;
        people = people.split(',');
        people = people.map((value) => value.trim());
        let memberCounter = 0;

        return (
            <Link to={{pathname:`/app/team/${this.state.id}`}}>
                <ListItem style={{backgroundColor: background}} button className={classes.listItem}>
                    <div className={classes.text}>
                        <div className={classes.row}>
                            {people.map((person)=>{
                                memberCounter++;
                                let imageId = "pic" + String(person).replace(/\s/g, '');
                                let url = this.state[imageId];
                                if(person !== "" && memberCounter <= 4) {
                                    return(
                                        <div className={classes.member}>
                                        <Avatar className={classes.memberAvatar}>
                                            <img className={classes.memberPicture} src={url}/>
                                        </Avatar>
                                        </div>
                                    )
                                }
                            })}
                            {people.length > 4 ?
                                <div className={classes.member}>
                                    <Avatar className={classes.memberAvatar}>
                                        +{people.length - 4}
                                    </Avatar>
                                </div> : ""
                            }
                       </div>
                        <div className={classes.column}>
                            <p className={classes.titleHeader}>Team</p>
                            <p className={classes.title}>{name}</p>
                        </div>
                    </div>
                    {/*<IconButton>*/}
                        {/*<TeamIcon className={classes.icon} color="primary" />*/}
                    {/*</IconButton>*/}
                </ListItem>
            </Link>

        );
    }
}

export default withStyles(styles, {withTheme: true })(Team);