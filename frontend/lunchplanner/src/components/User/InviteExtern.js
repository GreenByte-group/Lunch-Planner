import React from 'react';
import PropTypes from "prop-types";
import {withStyles} from "material-ui/styles/index";
import Input from "@material-ui/icons/es/Input";
import InputAdornment from "material-ui/es/Input/InputAdornment";
import IconButton from "material-ui/es/IconButton/IconButton";
import VisibilityOff from "@material-ui/icons/es/VisibilityOff";
import Visibility from "@material-ui/icons/es/Visibility";
import TextField from "material-ui/es/TextField/TextField";
import CopyButton from "@material-ui/icons/es/ContentCopy"
import {inviteExtern} from "./UserFunctions";
import Dialog from "material-ui/es/Dialog/Dialog";
import DialogTitle from "material-ui/es/Dialog/DialogTitle";
import DialogContent from "material-ui/es/Dialog/DialogContent";
import DialogActions from "material-ui/es/Dialog/DialogActions";
import Button from "material-ui/es/Button/Button";
import DialogContentText from "material-ui/es/Dialog/DialogContentText";
import {getHistory} from "../../utils/HistoryUtils";
import {HOST} from "../../Config";

const styles = {
    linkField:{
        marginLeft: 24,
        width: '300px',

    }
};

class InviteExtern extends React.Component {

    constructor(props) {
        super();
        let eventId = props.match.params.eventId;
        let inviteLink = inviteExtern(eventId, (response) =>{
            let link = HOST + "/event/token/" + response.data;
            this.setState({
                link: link,
            });
            console.log(this.state.link, "link");
        })
        this.state = {
            open: true,
            link: "link",
            copied: false,
            eventId: eventId,
        };
    }

    handleClose = () => {
        this.setState({ open: false });
        getHistory().push(this.props.location.query.source);
    };


    render(){

        const classes = this.props;
        let link = this.state.link;

        return(
            <div>
            <Dialog
                open={this.state.open}
                closeUrl={"/event/" + this.state.eventId}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{"Share this event with other people"}</DialogTitle>
                <DialogContent>
                    <DialogContentText >
                        Copy this link an invite other people per e-mail
                        <br/>
                        Link: {link}
                        <CopyButton/>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleClose} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
        </div>);
    }
}

InviteExtern.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(InviteExtern);