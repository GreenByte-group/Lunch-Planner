import React from 'react';
import { withStyles } from 'material-ui/styles';
import DialogMaterial from 'material-ui/Dialog';
import AppBar from 'material-ui/AppBar';
import Toolbar from 'material-ui/Toolbar';
import IconButton from 'material-ui/IconButton';
import Typography from 'material-ui/Typography';
import CloseIcon from '@material-ui/icons/Close';
import SearchIcon from '@material-ui/icons/Search';
import Slide from 'material-ui/transitions/Slide';
import {Link} from "react-router-dom";
import {getHistory} from "../utils/HistoryUtils";
import {TextField} from "material-ui";

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
            closeUrl: props.closeUrl,
            open: true,
            onSearch: props.onSearch,
            search: "",
        }
    }

    componentWillReceiveProps(newProps) {
        if(newProps.title && newProps.title !== this.props.title) {
            this.setState({
                title: newProps.title,
            })
        }
    }

    onClose = () => {
        if(this.state.onClose) {
            console.log("close function");
            this.state.onClose();
        } else if(this.state.closeUrl) {
            console.log("closeurl");
           getHistory().push(this.state.closeUrl);
        } else {
            console.log("close go back");
            getHistory().goBack();
        }
    };

    searchChanged = (event) => {
        this.setState({
            search: event.target.value,
        });

        this.state.onSearch(event.target.value);
    };

    render() {
        const { classes } = this.props;
        const search = !!this.state.onSearch;

        console.log("Search: " + search);

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

                        {(search) ?
                                <div>
                                    <IconButton color="primary">
                                        <SearchIcon />
                                    </IconButton>
                                    <TextField
                                        value={this.state.search}
                                        onChange={this.searchChanged}
                                    />
                                </div>
                            : ""}

                    </Toolbar>
                </AppBar>

                {(this.props.children) ? this.props.children : ""}

            </DialogMaterial>
        )
    }
}

export default withStyles(styles, { withTheme: true })(Dialog);