import React from 'react';
import PropTypes from "prop-types";
import {withStyles} from "@material-ui/core/styles/index";
// import CopyButton from "@material-ui/icons/"
import {inviteExtern} from "./UserFunctions";
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Input from '@material-ui/core/Input';
import {getHistory} from "../../utils/HistoryUtils";
import {FRONTEND_HOST} from "../../Config";
import IconButton from '@material-ui/core/IconButton';
import {CopyToClipboard} from 'react-copy-to-clipboard';

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
            let link = FRONTEND_HOST + "/public/" + response.data;
            this.setState({
                link: link,
            });
        });
        this.state = {
            open: true,
            link: "",
            copied: false,
            eventId: eventId,
        };
    }

    handleClose = () => {
        this.setState({ open: false });
        getHistory().push(this.props.location.query.source);
    };

    handleCopy = () =>{
        this.setState({
            copied: true,
        })
    };


    render(){

        const classes = this.props;
        let link = this.state.link;

        return(
            <div>
            <Dialog
                open={this.state.open}
                closeUrl={"/app/event/" + this.state.eventId}
            >
                <DialogTitle id="alert-dialog-title">{"Share this event with other people"}</DialogTitle>
                <DialogContent>
                    <DialogContentText >
                        Copy this link an invite other people per e-mail
                        <br/>
                        Link: <Input value={link}/>
                        <CopyToClipboard text={link}
                                         onCopy={this.handleCopy}>
                            <IconButton>
                                <p>COPY</p>
                            </IconButton>
                        </CopyToClipboard>


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