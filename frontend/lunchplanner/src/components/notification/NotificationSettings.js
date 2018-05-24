import React from 'react';
import {withStyles} from "material-ui/styles/index";

const styles = {
    root: {
        height: '100%',
    }
};

class NotificationSettings extends React.Component {

    render() {
        const {classes} = this.props;

        return (
            <div className={classes.root}>
                <p>Notfication settings</p>
            </div>
        )
    }

}

export default withStyles(styles, { withTheme: true })(NotificationSettings);