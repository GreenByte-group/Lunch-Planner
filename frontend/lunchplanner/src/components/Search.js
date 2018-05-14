import React from 'react';
import Button from 'material-ui/Button';
import MenuIcon from '@material-ui/icons/Menu';
import List from "material-ui/es/List/List";
import {withStyles} from "material-ui";
import Drawer from "material-ui/es/Drawer/Drawer";
import ListItem from "material-ui/es/List/ListItem";
import IconButton from "material-ui/es/IconButton/IconButton";
import IconSearch from '@material-ui/icons/Search';
import ListItemSecondaryAction from "material-ui/es/List/ListItemSecondaryAction";
import TextField from "material-ui/es/TextField/TextField";

const styles = {
    list: {
        width: 'auto',
        color:"white",
    },
    icon:{
        marginRight: 20,
    }
};
class LunchMenu extends React.Component {

    constructor(){
        super();

        this.handleClick = this.handleClick.bind(this);

        this.state = {
            popupVisible: false,
            visible: false,
        };

    }

    handleClick() {
        if (!this.state.popupVisible) {
            // attach/remove event handler
            document.addEventListener('click', this.handleClick, false);
        } else {
            document.removeEventListener('click', this.handleClick, false);
        }
        this.setState(prevState => ({
            popupVisible: !prevState.popupVisible,
        }));
    }

    handleVisibility = name => event =>{
        this.setState({
            [name]: event.target.checked,
            popupVisible : false,
        });
    }

    render() {

        const { classes } = this.props;
        return (

            <div ref={node => { this.node = node; }}>
                <Button
                    onClick={this.handleClick}
                >
                    <IconSearch style={{color: "white"}}/>
                </Button>
                {this.state.popupVisible && (
                    <Drawer anchor="top" open={this.state.popupVisible}>
                        <div
                            tabIndex={0}
                            role="button"
                        >
                            <List className={classes.list}>
                                <ListItem
                                    role={undefined}
                                    dense
                                    button
                                    className={classes.listItem}>
                                    <TextField
                                        id="search"
                                        label="Searching for..."
                                        placeholder="What do you want?"
                                        multiline
                                        className={classes.textField}
                                        onChange={this.handleChange}
                                    />
                                    <ListItemSecondaryAction>
                                        <IconButton onClick={this.handleAdd}>
                                            <IconSearch />
                                        </IconButton>
                                    </ListItemSecondaryAction>
                                </ListItem>
                            </List>
                        </div>
                    </Drawer>
                )}
            </div>
        );
    }
}

export default withStyles(styles) (LunchMenu);