import React from "react"
import {Done} from "@material-ui/icons";
import {withStyles} from "material-ui";

const styles = theme => ({
    root: {
        float: 'right',
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
        lineHeight: '25px',
        color: theme.palette.primary.main,
        marginLeft: 'auto',
        marginRight: 'auto',
    },
});

class AcceptedButton extends React.Component {
    render() {
        const {classes} = this.props;
        const {text} = this.props;

        return (
            <div className={classes.root}>
                <Done color="primary" className={classes.button} />
                {(text
                    ? <span className={classes.text} >{text}</span>
                    : ""
                )}
            </div>
        )
    }
}

export default withStyles(styles)(AcceptedButton);