import React from "react"
import {DateRange} from "@material-ui/icons";
import {withStyles} from "@material-ui/core";

const styles = theme => ({
    root: {
        float: 'right',
    },
    button: {
        color: '#A4A4A4',
        border: 'none',
        borderRadius: '50%',
        height: '32px',
        width: '32px',
        padding: '3px',
        marginLeft: 'auto',
        marginRight: 'auto',
        display: 'block',
        float: 'right',
    },
    text: {
        fontFamily: "Work Sans",
        fontSize: '11px',
        fontWeight: '500',
        lineHeight: '16px',
        color: '#A4A4A4',
        marginLeft: 'auto',
        marginRight: '11px',
        float: 'left',
    },
});

class InvitedButton extends React.Component {
    render() {
        const {classes} = this.props;
        const {text} = this.props;

        let styleButton = "";
        if(text)
            styleButton = {height: '12px', width:'12px', padding: '1px', borderWidth: '1px', marginRight: '16px', marginTop: '3px', marginBottom: '11px'};

        return (
            <div className={classes.root}>
                {(text
                        ? <span className={classes.text} >{text}</span>
                        : ""
                )}
                <DateRange style={styleButton} color="primary" className={classes.button} />
            </div>
        )
    }
}

export default withStyles(styles)(InvitedButton);