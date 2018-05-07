import React from 'react';
import {withStyles} from "material-ui/styles/index";
import List, { ListItem, ListItemSecondaryAction, ListItemText } from 'material-ui/List';
import Checkbox from 'material-ui/Checkbox';
import IconButton from 'material-ui/IconButton';
import AddIcon from '@material-ui/icons/Add';
import Button from "material-ui/es/Button/Button";
import TextField from "material-ui/es/TextField/TextField";
import Avatar from "material-ui/es/Avatar/Avatar";

const styles = theme => ({
    root: {
        width: '100%',
        //maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
    },
    button:{
        color: "white",
        position: "fixed",
        bottom:0,
        width: "100%"
    },
    textField: {
        marginLeft: 20,
        width: "90%",
    },
});

class ServiceList extends React.Component {
    constructor(props) {
        super();

        this.state = {
            error: "",
            checked: [0],
        };
    }
    handleToggle = value => () => {
        const { checked } = this.state;
        const currentIndex = checked.indexOf(value);
        const newChecked = [...checked];

        if (currentIndex === -1) {
            newChecked.push(value);
        } else {
            newChecked.splice(currentIndex, 1);
        }

        this.setState({
            checked: newChecked,
        });
    };

    render(){
        const { classes } = this.props;

        return (
            <div className={classes.root}>
                <List>
                    <ListItem
                        role={undefined}
                        dense
                        button
                        className={classes.listItem}>
                        <TextField
                            label="Service"
                            placeholder="What do you want?"
                            multiline
                            className={classes.textField}
                        />
                        <ListItemSecondaryAction>
                            <IconButton>
                                <AddIcon />
                            </IconButton>
                        </ListItemSecondaryAction>
                    </ListItem>
                    {[0, 1, 2, 3, 4, 5, 6].map(value => (
                        <ListItem
                            key={value}
                            role={undefined}
                            dense
                            button
                            className={classes.listItem}
                        >
                            <Checkbox
                                checked={this.state.checked.indexOf(value) !== -1}
                                tabIndex={-1}
                                disableRipple
                            />
                            <ListItemText primary={`Participant ${value + 1}`} />
                            <ListItemSecondaryAction>
                                <Avatar >Test</Avatar>
                            </ListItemSecondaryAction>
                        </ListItem>
                    ))}
                </List>
                <Button variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                    Save
                </Button>
            </div>
        );
    }
}
export default withStyles(styles, { withTheme: true })(ServiceList);