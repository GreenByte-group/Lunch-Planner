import React from 'react';
import { withStyles, Slide, TextField } from '@material-ui/core';
import {isWidthDown} from '@material-ui/core/withWidth'
import DialogMaterial from '@material-ui/core/Dialog';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import SearchIcon from '@material-ui/icons/Search';
import {getHistory} from "../utils/HistoryUtils";

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

const styles = {
    appBar: {
        position: 'relative',
        padding: '0px',
    },
    typograph: {
        flex: 1,
        textAlign: 'center',
        marginRight: '52px',
    },
    closeIconAbsolute: {
        position: 'absolute',
        top: '12px',
        left: '12px',
    },
    closeIcon: {
        float: 'left',
        marginLeft: '8px',
        marginRight: '8px',
    },
    noPadding: {
        padding: '0px',
    },
    dialog: {
        height: '100%',
    },
    paddingBottom: {
        paddingBottom: '56px',
    },
    paper: {
        maxWidth: '1024px',
        maxHeight: '100vh',
        width: '600px',
        display: 'flex',
        flexDirection: 'column',
        flex: '0 1 auto',
        position: 'relative',
        overflowY: 'auto', // Fix IE11 issue, to remove at some point.
        // We disable the focus ring for mouse, touch and keyboard users.
        outline: 'none',
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
            imageUrl: props.imageUrl,
            zIndex: props.zIndex || 1300,
            paddingBottom: props.paddingBottom || '0px',
        }
    }

    updateDimensions = () => {
        var w = window,
            d = document,
            documentElement = d.documentElement,
            body = d.getElementsByTagName('body')[0],
            width = w.innerWidth || documentElement.clientWidth || body.clientWidth,
            height = w.innerHeight|| documentElement.clientHeight|| body.clientHeight;

        this.setState({
            width: width,
            height: height
        });
    };

    componentWillMount() {
        this.updateDimensions();
    };

    componentDidMount() {
        window.addEventListener("resize", this.updateDimensions);
    }

    componentWillUnmount() {
        window.removeEventListener("resize", this.updateDimensions);
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
            this.state.onClose();
        } else if(this.state.closeUrl) {
           getHistory().push(this.state.closeUrl);
        } else {
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

        let classesCloseButton = classes.closeIcon;

        let fullScreen = true;

        let style;

        if(this.state.width <= 600)
            style = {height: '100vh'};
        else
            style= {maxHeight: '90vh',};

        return (
                <DialogMaterial
                    style={{zIndex: this.state.zIndex}}
                    fullScreen
                    open={this.state.open}
                    transition={Transition}
                    className={classes.dialog}
                    PaperProps={{className: classes.paper, style: style}}
                >
                    <AppBar className={classes.appBar} color ="white">
                        <Toolbar className={classes.noPadding}>
                            <IconButton onClick={this.onClose} color="inherit" aria-label="Close" className={classesCloseButton}>
                                <CloseIcon color='primary' />
                            </IconButton>

                            <Typography variant="title" color="inherit" className={classes.typograph}>
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