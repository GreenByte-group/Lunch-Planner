import React from 'react';
import {withStyles} from "material-ui/styles/index";
import PropTypes from 'prop-types';
import List, { ListItem, ListItemSecondaryAction, ListItemText } from 'material-ui/List';
import Checkbox from 'material-ui/Checkbox';
import IconButton from 'material-ui/IconButton';
import CommentIcon from '@material-ui/icons/Comment';

const styles = theme => ({
    root: {
        width: '100%',
        maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
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
                    {[0, 1, 2, 3].map(value => (
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
                            <ListItemText primary={`Line item ${value + 1}`} />
                            <ListItemSecondaryAction>
                                <IconButton aria-label="Comments">
                                    <CommentIcon />
                                </IconButton>
                            </ListItemSecondaryAction>
                        </ListItem>
                    ))}
                </List>
            </div>
        );
    }
}
export default withStyles(styles, { withTheme: true })(ServiceList);