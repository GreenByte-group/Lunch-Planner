import React from "react"
import axios from "axios"
import moment from "moment"

import {HOST, TOKEN} from "../../Config"
import ListItem from "material-ui/List/ListItem";
import {ListItemText} from "material-ui";

let background = 'white';

const styles = {
    listItem: {
      backgroundColor: background,
    },
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

        background = props.background;

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
        let name = this.state.name;
        let description = this.state.description;
        let monthDay = this.state.monthDay;
        let time = this.state.time;
        let people = this.state.people;

        return (
            <ListItem button style={styles.listItem}>
                <div>
                    <p style={styles.title}>{name}</p>
                    <p style={styles.date}>{monthDay}</p>
                    <p style={styles.users}>{people}</p>
                </div>
            </ListItem>
        );
    }
}

export default Event;