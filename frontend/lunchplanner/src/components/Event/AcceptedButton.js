import React from "react"
import {Done} from "@material-ui/icons";
import {withStyles} from "material-ui";

const styles = theme => ({
    root: {
        float: 'right',
        paddingTop: '15px',
    },
    button: {
        border: '2px solid',
        borderRadius: '50%',
        height: '32px',
        width: '32px',
        padding: '3px',
        marginLeft: 'auto',
        marginRight: 'auto',
        display: 'block',
    },
    text: {
        fontFamily: "Work Sans",
        fontSize: '11px',
        lineHeight: '35px',
        color: theme.palette.primary.main,
        marginLeft: 'auto',
        marginRight: 'auto',
    },
});

class AcceptedButton extends React.Component {
    render() {
        const {classes} = this.props;

        return (
            <div className={classes.root}>
                <Done color="primary" className={classes.button} />
                <span className={classes.text} >Accepted</span>
            </div>
        )
    }
}

export default withStyles(styles)(AcceptedButton);