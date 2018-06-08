import React from "react";
import {withStyles} from "@material-ui/core/styles/index";
import ServiceList, {serviceListNeedReload} from "./ServiceList";
import Dialog from "../../Dialog";
import {Button, TextField, Slide} from "@material-ui/core";
import {getHistory} from "../../../utils/HistoryUtils";
import {createService} from "./ServiceFunctions";

function Transition(props) {
    return <Slide direction="up" {...props} />;
}

const styles = {
    overButton: {
        height: '100%',
        marginBottom: '56px',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
        paddingLeft: '24px',
        paddingRight: '24px',
        paddingTop: '16px',
    },
    button:{
        fontSize: '16px',
        fontFamily: 'Work Sans',
        color: "white",
        position: "fixed",
        bottom: 0,
        width: "100%",
        height: '56px',
        zIndex: '10000',
    },
    textFieldDescription: {
        marginTop: '46px',
    },
    error: {
        color: 'red',
    }
};

class ServiceListScreen extends React.Component {

    constructor(props) {
        super();
        let eventId = props.match.params.eventId;
        this.state = {
            eventId:eventId,
            error:"",
            food: "",
            description: "",
        };
    }

    handleAccept = () => {
        createService(this.state.eventId, this.state.food, this.state.description,
            (response) => {
                if(response.status === 201) {
                    serviceListNeedReload();
                    getHistory().push("/app/event/" + this.state.eventId);
                }
            },
            (error) => {
                this.setState({
                    error: error.message,
                })
            });
    };

    handleChange = (event) => {
        let target = event.target;
        this.setState({
            [target.id]: target.value,
        });
    };

    render() {
        const {classes} = this.props;
        const error = this.state.error;

        return (
            <Dialog
                title="Add Checklist..."
                closeUrl={"/app/event/" + this.state.eventId}
            >
                <div className={classes.overButton}>
                    {
                        (error)
                            ? <p className={classes.error}>{error}</p>
                            : ''
                    }
                    <TextField
                        InputLabelProps={{
                            shrink: true,
                        }}
                        id="food"
                        label="Name"
                        value={this.state.name}
                        className={classes.textField}
                        placeholder ="Describe what's to do..."
                        onChange={this.handleChange}
                    />
                    <TextField
                        InputLabelProps={{
                            shrink: true,
                        }}
                        className={classes.textFieldDescription}
                        id="description"
                        label="Description"
                        value={this.state.description}
                        onChange={this.handleChange}
                        placeholder ="Remark for your coworkers etc. (optional)"
                        multiline
                        rows="6"
                    />
                </div>
                <Button variant="raised" color="secondary" onClick={this.handleAccept} className={classes.button}>
                    Add Task
                </Button>
            </Dialog>
        );
    }

}
export default withStyles(styles, { withTheme: true })(ServiceListScreen);