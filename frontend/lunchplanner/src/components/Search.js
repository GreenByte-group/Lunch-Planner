import React from 'react';
import {withStyles,IconButton, Input,InputAdornment,FormControl, TextField} from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';
import DeleteIcon from '@material-ui/icons/Delete';
import classNames from 'classnames';
import {Link} from "react-router-dom";

const styles =  theme => ({
    icon:{
        marginTop: -8,
        float: "right",
        color: "white"
    },
    margin: {
        margin: theme.spacing.unit,
    },
    textField: {
        color: "white",
        flexBasis:200,
        width: 100,
        fontSize: 20,
    },
    root:{
        position: 'relative',
        display: 'flex',
        flexDirection: 'row',
        marginTop: 10,
    },
    deleteIcon:{
        color: "white",
        height: 15,
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
    componentDidMount() {
        this.setState({
            search: this.props.search,
        });
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
        this.setState({
            open: false,
        });
        this.props.handleSearch(this.state.search);
    };
    render() {
        const { classes } = this.props;
        let open = this.state.open;
        return (
            <div className={classes.root}>
                {open ?
                    <FormControl className={classNames(classes.margin ,classes.textField)}>
                        <Input
                            id="adornment-password"
                            value={this.state.search}
                            onChange={this.handleChange('search')}
                            endAdornment={
                                <InputAdornment position="end">
                                        <DeleteIcon className={classes.deleteIcon} onClick={this.handleCancel}/>
                                </InputAdornment>
                            }
                        />
                    </FormControl>
                : <p>{this.state.search}</p>}
                <IconButton>
                    {this.state.search === "tetris" ? <Link to={{pathname: "/app/tetris"}}>
                            <SearchIcon className={classes.icon} />
                        </Link>
                        :
                    <SearchIcon className={classes.icon} onClick={this.searchForEvents}/>
                    }
                </IconButton>
            </div>
        );
    }
}

export default withStyles(styles) (Search);