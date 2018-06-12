import React from "react";
import {withStyles} from "@material-ui/core/styles/index";
import {Card, CardContent, ListItem} from "@material-ui/core";
import {Done} from "@material-ui/icons";
import {getUsername} from "../../authentication/LoginFunctions";
import {acceptService} from "./ServiceFunctions";

const styles = {
    listItem: {
        width: '100%',
        backgroundColor: 'transparent !important',
        '&:hover': {
            cursor: 'default',
        }
    },
    listItemAccepted: {
        width: '100%',
        backgroundColor: 'transparent !important',
        opacity: '0.5',
        '&:hover': {
            cursor: 'default',
        }
    },
    card: {
      width: '100%',
    },
    accepted: {
        height: '24px',
        width: '24px',
        border: '1px solid #739D44',
        borderRadius: '50%',
        float: 'left',
        '&:hover': {
            cursor: 'pointer',
        }
    },
    doneIcon: {
        height: '18px',
        width: '18px',
        margin: '2px',
    },
    text: {
        marginLeft: '40px',
    },
    footer: {
        textAlign: 'right',
        marginRight: '16px',
        marginBottom: '16px',
        marginLeft: '16px',
    }
};

class ServiceListItem extends React.Component {

    constructor(props) {
        super();
        this.state = {
            error: "",
            eventId: props.eventId,
            serviceId: props.serviceId,
            food: props.food,
            description: props.description,
            creator: props.creator,
            accepter: props.accepter,
        };
    }

    acceptedClicked = () => {
        if(!this.state.accepter) {
            acceptService(this.state.eventId, this.state.serviceId, (response) => {
                if (response.status !== 204) {
                    this.setState({
                        error: response.response.data,
                    })
                } else {
                    this.setState({
                        accepter: getUsername(),
                    })
                }
            });
        }
    };

    render() {
        const {classes} = this.props;
        let food = this.state.food;
        let description = this.state.description;
        let creator = this.state.creator;
        let accepter = this.state.accepter;

        let error = this.state.error;

        let classesListItem = classes.listItem;
        if(accepter)
            classesListItem = classes.listItemAccepted;

        return (
            <ListItem button className={classesListItem}>
                {(error)
                ? <p className={classes.error}>{error}</p>
                : ''}

                <Card className={classes.card}>
                    <CardContent className={classes.cardContent}>
                        <div className={classes.accepted} onClick={this.acceptedClicked}>
                            {
                                (accepter) ?
                                    <Done color="primary" className={classes.doneIcon} />
                                    : ''
                            }
                        </div>
                        <div className={classes.text}>
                            <p>{food}</p>
                            <p>{description}</p>
                        </div>
                    </CardContent>
                    <div className={classes.footer}>
                       <p>Created by {creator}</p>
                    </div>
                </Card>

            </ListItem>
        );
    }

}
export default withStyles(styles, { withTheme: true })(ServiceListItem);