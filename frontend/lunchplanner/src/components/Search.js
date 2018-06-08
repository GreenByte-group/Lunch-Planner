import React from 'react';
import {Button, List, ListItem, Divider, withStyles, Drawer, IconButton, MenuItem, FormControlLabel, Switch, TextField, ListItemSecondaryAction} from '@material-ui/core';
import {Link} from "react-router-dom";
import SearchIcon from '@material-ui/icons/Search';
import DialogContent from "@material-ui/core/es/DialogContent/DialogContent";
import DialogActions from "@material-ui/core/es/DialogActions/DialogActions";

const styles =  theme => ({
    root:{
        height: 100,
        width: "max-content",
    },
    list: {
        width: "100%",
        color:"white",
        height: 50,
    },
    listItem: {
        width: '100%',
    },
    icon:{
        float: "right",
        marginRight: 20,
    },
    textField: {
        marginBottom: 30,
        width: 300,
    },
    menu: {
        width: 200,
    },
    input: {
        display: 'none',
    },
    button: {
        margin: theme.spacing.unit,
    },
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
        let teams = this.state.teams;
        return (

            <Drawer
                open={this.state.open}
                onClose={this.handleClose}
                anchor='top'
            >
                <DialogContent >
                    <div className={classes.list}>
                        <List className ={classes.menu}>
                            <ListItem
                                dense
                                className={classes.listItem}>
                                <form noValidate autoComplete="on" >
                                    <TextField
                                        id="Search"
                                        label="Search"
                                        defaultValue={this.state.search}
                                        className={classes.textField}
                                        onChange={this.handleChange('search')}
                                        margin="normal"
                                    />
                                </form>
                                <IconButton>
                                    <SearchIcon className={classes.icon} onClick={this.searchForEvents}/>
                                </IconButton>

                            </ListItem>
                        </List>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button variant="contained" color="secondary" className={classes.button} onClick={this.handleCancel}>
                        Cancel
                    </Button>
                    <Link to={{pathname:'/app/event/create'}}>
                        <Button variant="contained" color="secondary" className={classes.button}>
                            Create Event
                        </Button>
                    </Link>
                </DialogActions>
            </Drawer>
        );
    }
}

export default withStyles(styles) (Search);