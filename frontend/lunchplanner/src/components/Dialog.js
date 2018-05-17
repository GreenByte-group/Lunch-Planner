import React from 'react';
import { withStyles } from 'material-ui/styles';
import DialogMaterial from 'material-ui/Dialog';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import IconButton from 'material-ui/IconButton';
import Typography from 'material-ui/Typography';
import CloseIcon from '@material-ui/icons/Close';
import Slide from 'material-ui/transitions/Slide';
import {Link} from "react-router-dom";
import {getHistory} from "../utils/HistoryUtils";

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    }
};

class Dialog extends React.Component {

    constructor(props) {
        super();

        this.state = {
            title: props.title || "",
            onClose: props.onClose,
            open: true,
        }
    }

    onClose = () => {
        if(this.state.onClose) {
            this.state.onClose();
        } else {
            getHistory().goBack();
        }
    };

    render() {
        const { classes } = this.props;

        return (
            <DialogMaterial
                fullScreen
                open={this.state.open}
                transition={Transition}
            >
                <AppBar className={classes.appBar} color ="white">
                    <Toolbar>
                        <IconButton onClick={this.onClose} color="inherit" aria-label="Close" className={classes.closeIcon}>
                            <CloseIcon color='primary' />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={classes.flex}>
                            {this.state.title}
                        </Typography>

                    </Toolbar>
                </AppBar>

                {(this.props.children) ? this.props.children : ""}

            </DialogMaterial>
        )
    }
}

export default withStyles(styles, { withTheme: true })(Dialog);