import React from "react";
import {withStyles} from "material-ui/styles/index";
import Dialog from 'material-ui/Dialog';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import IconButton from 'material-ui/IconButton';
import Typography from 'material-ui/Typography';
import CloseIcon from '@material-ui/icons/Close';
import Slide from 'material-ui/transitions/Slide';
import ServiceList from "./ServiceList";
import {Link} from "react-router-dom";

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
};

class ServiceListScreen extends React.Component {

    state = {
        open: true,
        error:"",
    };

    render() {
        const {classes} = this.props;
        const error = this.state.error;
        return (
            <div>
                <Dialog
                    fullScreen
                    open={this.state.open}
                    onClose={this.handleClose}
                    transition={Transition}
                >
                    <AppBar className={classes.appBar} color ="white">
                        <Toolbar>
                            <Link to="/event">
                                <IconButton color="inherit" aria-label="Close">
                                    <CloseIcon />
                                </IconButton>
                            </Link>
                            <Typography variant="title" color="inherit" className={classes.flex}>
                                Service List
                            </Typography>

                        </Toolbar>
                    </AppBar>
                    {(error
                            ? <p className={classes.error}>{error}</p>
                            : ""
                    )}
                    <ServiceList/>

                </Dialog>
            </div>
        );
    }

}
export default withStyles(styles, { withTheme: true })(ServiceListScreen);