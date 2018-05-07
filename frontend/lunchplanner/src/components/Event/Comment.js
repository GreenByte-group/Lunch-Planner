import React from "react"
import moment from "moment"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";

const styles = {

};

class Comment extends React.Component {

    constructor(props) {
        super();

        //http://momentjs.com/docs/
        let date = moment(props.date);

        this.state = {
            text: props.text,
        }
    }

    render() {
        const {classes} = this.props;

        return (
            <ListItem button className={classes.listItem}>
                <p>{this.state.text}</p>
            </ListItem>
        );
    }
}

export default withStyles(styles)(Comment);