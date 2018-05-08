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
            people:[
                {userName: "Benjamin", short: "B", food: "Salad"},
                {userName: "Gustav", short: "G", food: "Pizza"},
                {userName: "Donald", short: "D", food: "Döner"},
            ],
            service: "service",
        };
    }

    componentDidMount(){
        //TODO: Load via Request all people who want to use the Service
    }
    handleToggle = value => () => {
        const { checked } = this.state.checked;
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
    handleAdd = (e) => {
        let array = this.state.people;
        array.push({name: "MyName", short: "My", food: this.state.service});
        this.setState({
            people: array
        });
    }

    handleChange = (event) => {
        let target = event.target;
        this.setState({
            service: target.value,
        });
    }

    render(){
        const { classes } = this.props;
        let people = this.state.people;
        return (
            <div className={classes.root}>
                <List>
                    <ListItem
                        role={undefined}
                        dense
                        button
                        className={classes.listItem}>
                        <TextField
                            id="service"
                            label="Service"
                            placeholder="What do you want?"
                            multiline
                            className={classes.textField}
                            onChange={this.handleChange}
                        />
                        <ListItemSecondaryAction>
                            <IconButton onClick={this.handleAdd}>
                                <AddIcon />
                            </IconButton>
                        </ListItemSecondaryAction>
                    </ListItem>
                    {people.map(function(p) {
                        return <ListItem
                            key={p}
                            role={undefined}
                            dense
                            button
                            className={classes.listItem}
                        >
                            <Checkbox
                                tabIndex={-1}
                                disableRipple
                            />
                            <ListItemText primary={p.food}/>
                            <ListItemSecondaryAction>
                                <Avatar>{p.short}</Avatar>
                            </ListItemSecondaryAction>
                        </ListItem>
                    })}
                </List>
                <Button variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                    Save
                </Button>
            </div>
        );
    }
}
export default withStyles(styles, { withTheme: true })(ServiceList);