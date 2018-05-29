import React from 'react';
import {Button} from "material-ui";
import {withStyles} from "material-ui/styles/index";

const styles = {
    root: {
        height: '56px',
        width: '100%',
        backgroundColor: 'white',
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-around',
        position: 'fixed',
        zIndex: '10000',
        bottom: '0px',
    },
    buttonDecline: {
        marginLeft: '24px',
        marginRight: '5%',
        marginTop: '8px',
        marginBottom: '8px',
        border: '1px solid #F29B26',
        backgroundColor: 'white',
        borderRadius: '4px',
        color: '#F29B26',
        flexGrow: 1,
        height: '40px',
    },
    buttonJoin: {
        marginLeft: '5%',
        marginRight: '24px',
        marginTop: '8px',
        marginBottom: '8px',
        border: '1px solid #F29B26',
        borderRadius: '4px',
        color: '#FFFFFF',
        flexGrow: 1,
        height: '40px',
    },
};

class InvitationButton extends React.Component {

    constructor(props) {
        super();

        this.state = {
            decline: props.decline,
            join: props.join,
        }
    }

    render() {
        const {classes} = this.props;

        return(
            <div className={classes.root}>
                <Button variant="raised" color="secondary" onClick={this.state.decline} className={classes.buttonDecline}>
                    Decline
                </Button>
                <Button variant="raised" color="secondary" onClick={this.state.join} className={classes.buttonJoin}>
                    Join
                </Button>
            </div>
        )
    }

}

export default withStyles(styles, { withTheme: true })(InvitationButton);