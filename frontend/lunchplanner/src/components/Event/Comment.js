import React from "react"
import moment from "moment"
import ListItem from "material-ui/List/ListItem";
import {withStyles} from "material-ui";

const styles = {
    root: {
      width: '100%',
    },
    text: {
        width: '100%',
        float: 'none',
    },
    dateText: {
        width: '100%',
        textAlign: 'right',
        float: 'none',
    },
};

class Comment extends React.Component {

    constructor(props) {
        super();

        //http://momentjs.com/docs/
        let date = moment(props.date);

        this.state = {
            text: props.text,
            dateText: date.format("HH:mm"),
            username: props.creater,
        }
    }

    render() {
        const {classes} = this.props;

        return (
            <ListItem button className={classes.listItem}>
                <div className={classes.root}>
                    <p className={classes.text} >
                        {this.state.text}
                    </p>
                    <p className={classes.dateText}>
                        {this.state.username}, {this.state.dateText}
                    </p>
                </div>
            </ListItem>
        );
    }
}

export default withStyles(styles)(Comment);