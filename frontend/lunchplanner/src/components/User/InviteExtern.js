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

const styles = {
    linkField:{
        marginLeft: 24,
        width: '300px',

    }
};

class InviteExtern extends React.Component {

    constructor(props) {

        let link = inviteExtern(id);

        super();
        this.state = {
            link: link || "",
            copied: false,
        };
    }

    render(){

        const classes = this.props;

        return(<div >
            <TextField
                className={classes.linkField}
                label={"Copy this link an send it per e-mail"}
                value = {this.state.link}
                style={{marginLeft: 24, width: '70%'}}

            />
            <CopyButton/>
        </div>);
    }
}

InviteExtern.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(InviteExtern);