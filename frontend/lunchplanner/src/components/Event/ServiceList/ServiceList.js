import React from 'react';
import {withStyles} from "material-ui/styles/index";
import List from 'material-ui/List';
import ServiceListItem from "./ServiceListItem";
import {getServices} from "./ServiceFunctions";

const styles = theme => ({
    root: {
        width: '100%',
        backgroundColor: '#FAFAFA',
    },
});

export let serviceNeedReload = false;

export function serviceListNeedReload() {
    serviceNeedReload = true;
}

class ServiceList extends React.Component {
    constructor(props) {
        super();

        this.state = {
            eventId: props.eventId,
            items: [
                // {food:'Döner', description:'Bitte ohne Zwiebeln', creator:'Martin', accepter:'Felix'},
                // {food:'Döner2', description:'Bitte ohne Tomaten', creator:'Can'},
                // {food:'Döner3', description:'Bitte ohne Rotkraut', creator:'Sergej', accepter:'Felix'},
                // {food:'Döner4', description:'Bitte ohne Fleisch, WHAT???!!! Spaß', creator:'Martin'}
                ],
        };

        this.getServiceList(props.eventId);
    }

    componentWillReceiveProps(newProps) {
        if(newProps.eventId !== null && newProps.eventId !== undefined && newProps.eventId !== this.state.eventId) {
            this.setState({
                eventId: newProps.eventId,
            });
            this.getServiceList(newProps.eventId);
        }
    }

    getServiceList = (eventId) => {
        if(!eventId)
            eventId = this.state.eventId;

        getServices(eventId, (response) => {
            this.setState({
                items: response.data,
            })
        });
    };

    render(){
        const { classes } = this.props;
        let people = this.state.people;

        if(serviceNeedReload) {
            serviceNeedReload = !serviceNeedReload;
            this.getServiceList(this.state.eventId);
        }

        return (
            <List className={classes.root}>
                {
                    this.state.items.map((listValue) => {
                        return (
                            <ServiceListItem
                                eventId={this.state.eventId}
                                serviceId={listValue.serviceId}
                                food={listValue.food}
                                description={listValue.description}
                                creator={listValue.createrName}
                                accepter={listValue.accepter}
                            />
                        )
                    })
                }
            </List>
        );
    }
}
export default withStyles(styles, { withTheme: true })(ServiceList);