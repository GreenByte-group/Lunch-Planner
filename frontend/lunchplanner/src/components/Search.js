import React from 'react';
import {withStyles,IconButton, TextField,} from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';

const styles =  theme => ({
    icon:{
        float: "right",
        marginRight: 24,
        color: "white"
    },
    textField: {
        width: '90%',
        color: "white",
        marginLeft: 5,
    },
    row:{
        float: 'left',
        display: 'inline-flex',
        justifyContent: 'center',
    }
});
class Search extends React.Component {

    constructor(props){
        super();

        this.state = {
            visible: false,
            open: props.open,
            search: props.search,
            team: "",
            cancel: props.cancel,
        };
    }

    componentWillReceiveProps(newProps) {
        if(newProps.search !== this.state.search){
            this.setState({
                search: newProps.search,
            });
        }
    }

    handleChange = name => event => {
        this.setState({
            [name]: event.target.value,
        });

    };


    handleClickOpen = () => {
        this.setState({ open: true });
    };

    handleCancel = () => {
        this.setState({open: false});
        this.props.handleCancel();
    };

    handleClose = () => {
        this.setState({ open: false });
    };

    searchForEvents = () => {
        this.setState({ open: false });
        this.props.handleSearch(this.state.search);
    };
    render() {
        const { classes } = this.props;
        let open = this.state.open;
        return (
            <div className={classes.row}>
                {open ? <TextField
                    id="Search"
                    defaultValue={this.state.search}
                    className={classes.textField}
                    onChange={this.handleChange('search')}
                    margin="normal"
                    /> : ""}
                <IconButton>
                    <SearchIcon className={classes.icon} onClick={this.searchForEvents}/>
                </IconButton>
            </div>
        );
    }
}

export default withStyles(styles) (Search);