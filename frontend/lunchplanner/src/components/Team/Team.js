import React from "react"
import {withStyles, ListItem, Avatar, IconButton} from "@material-ui/core";
import {Link} from "react-router-dom";
import TeamIcon from  "@material-ui/icons/Create";
import {getProfilePicturePath} from "../User/UserFunctions";

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
        let people = invitations.map(value => {
            getProfilePicturePath(value.userName, (response) => {
                let stateId = "pic" + value.userName.replace(/\s/g, '');
                this.setState({
                    [stateId]: response.data,
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


        return (
            <Link to={{pathname:`/app/team/${this.state.id}`}}>
                <ListItem style={{backgroundColor: background}} button className={classes.listItem}>
                    <div className={classes.text}>
                        <div className={classes.row}>
                            {
                                people.map((person, index) =>{
                                    let imageId = "pic" + person.replace(/\s/g, '');
                                    let url = this.state[imageId];
                                    return(
                                        <div className={classes.member}>
                                                <Avatar className={classes.memberAvatar}>
                                                    <img className={classes.memberPicture} src={url} />
                                                </Avatar>
                                        </div>)

                                })
                            }
                       </div>
                        <p className={classes.title}>{name}</p>
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