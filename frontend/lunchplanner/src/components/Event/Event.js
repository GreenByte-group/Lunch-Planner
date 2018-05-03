import React from "react"
import moment from "moment"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";

let background = 'white';

const styles = {
    title: {
        fontSize: '12px',
        fontWeight: '500',
        marginBottom: '2px',
    },
    date: {
        fontSize: '12px',
        fontWeight: '500',
        marginBottom: '2px',
    },
    users: {
        fontSize: '10px',
        marginBottom: '0px',
    },
};

class Event extends React.Component {

    constructor(props) {
        super();

        //http://momentjs.com/docs/
        let date = moment(props.date);

        this.state = {
            name: props.name,
            description: props.description,
            monthDay: date.format('DD MMM'),
            time: date.format('HH:mm'),
            //TODO invited people
            people: 'Can, Santino, Felix, Martin',

        }
    }

    render() {
        const {classes} = this.props;

        let name = this.state.name;
        let description = this.state.description;
        let monthDay = this.state.monthDay;
        let time = this.state.time;
        let people = this.state.people;

        return (
            <ListItem button className={classes.listItem}>
                <div>
                    <p className={classes.title}>{name}</p>
                    <p className={classes.date}>{monthDay}</p>
                    <p className={classes.users}>{people}</p>
                </div>
            </ListItem>
        );
    }
}

export default withStyles(styles)(Event);