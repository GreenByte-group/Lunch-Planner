import React from "react"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";
import {Link} from "react-router-dom";
import IconButton from "material-ui/es/IconButton/IconButton";
import TeamIcon from  "@material-ui/icons/Create";

const styles = {
    listItem: {
        padding: '7px 16px',
        '&:hover': {
            backgroundColor: '#0303031a !important',
        }
    },
    title: {
        fontFamily: "Work Sans",
        fontSize: '16px',
        lineHeight: '24px',
        marginBottom: '0px',
    },
    date: {
        fontFamily: "Work Sans",
        fontSize: '14px',
        lineHeight: '20px',
        marginBottom: '0px',
    },
    member: {
        fontFamily: "Work Sans",
        fontSize: '13px',
        lineHeight: '20px',
        marginBottom: '0px',
        color: '#A4A4A4',
    },
    icons: {
        width: '13px',
        height: 'auto',
    },
    text: {
        float: 'left',
        width: '100%',
        color: 'black',
    }
};

class Team extends React.Component {

    constructor(props) {
        super();
        this.state = {
            name:  props.name,
            teamId: props.id,
            member: props.member,
        }
    }


    render() {
        const {classes} = this.props;

        const background = this.state.background;
        let accepted= this.state.accepted;

        let name = this.state.name;

        return (
            <Link to={{pathname:"/team/:teamId"}}>

                <ListItem style={{backgroundColor: background}} button className={classes.listItem}>
                    <div className={classes.text}>
                        <div className={classes.member}></div>
                        <p className={classes.title}>{name}</p>
                        <IconButton>
                            <TeamIcon/>
                        </IconButton>
                    </div>
                </ListItem>
            </Link>

        );
    }
}

export default withStyles(styles)(Team);