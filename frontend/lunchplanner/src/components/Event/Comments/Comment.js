import React from "react"
import moment from "moment"
import {ListItem} from "@material-ui/core";
import {withStyles} from "@material-ui/core";

const styles = {
    root: {
      width: '100%',
    },
    text: {
        width: '100%',
        float: 'none',
        fontSize: '13px',
        lineHeight: '20px',
    },
    dateText: {
        width: '100%',
        float: 'none',
        color: '#A4A4A4',
        fontSize: '11px',
        lineHeight: '16px',
    },
    listItem: {
        borderTop: '1px solid #F2F2F2',
    },
};

class Comment extends React.Component {

    constructor(props) {
        super();

        //http://momentjs.com/docs/
        let date = moment(props.date);

        this.state = {
            text: props.text,
            date: date,
            dateText: date.format("HH:mm"),
            username: props.creater,
        }
    }

    render() {
        const {classes} = this.props;

        let date = this.state.date;

        let isCurrentDate = date.isSame(new Date(), "day");
        let dateToShow = "";
        if(isCurrentDate)
            dateToShow = this.state.dateText;
        else
            dateToShow = date.format("DD.MM HH:mm");

        return (
            <ListItem button className={classes.listItem}>
                <div className={classes.root}>
                    <p className={classes.dateText}>
                        {this.state.username} - {dateToShow}
                    </p>
                    <p className={classes.text}>
                        {this.state.text}
                    </p>
                </div>
            </ListItem>
        );
    }
}

export default withStyles(styles)(Comment);