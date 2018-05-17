import React from "react";
import {withStyles} from "material-ui/styles/index";
import Slide from 'material-ui/transitions/Slide';
import ServiceList from "./ServiceList";
import Dialog from "../Dialog";

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

const styles = {
    appBar: {
        position: 'relative',
    },
    flex: {
        flex: 1,
    },
};

class ServiceListScreen extends React.Component {

    constructor(props) {
        super();
        let eventId = props.match.params.eventId;
        this.state = {
            id:eventId,
            open: true,
            error:"",
        };
    }


    render() {
        const {classes} = this.props;
        const error = this.state.error;
        return (
            <Dialog
                title="Service List"
                closeUrl={"/event/" + this.state.id}
            >
                <ServiceList/>
            </Dialog>
        );
    }

}
export default withStyles(styles, { withTheme: true })(ServiceListScreen);