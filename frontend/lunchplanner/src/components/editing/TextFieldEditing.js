import React from 'react';
import {withStyles} from "@material-ui/core/styles/index";
import {Input, TextField} from "@material-ui/core";

const styles = {
    textFieldEditable: {
        // "&::before": {
        //     height: '0px !important',
        // },
    },
};

class TextFieldEditing extends React.Component {

    constructor(props) {
        super();

        this.state = {
            editable: props.editable,
            value: props.value,
        }
    }

    componentWillReceiveProps(newprops) {
        if(newprops.editable !== null && newprops.editable !== undefined && newprops.editable !== this.state.editable) {
            this.setState({
                editable: newprops.editable,
            })
        }
    }

    render() {
        const {classes} = this.props;

        let classTextField = this.props.className + " " + classes.textFieldEditable;

        return (
            <div>
                {(this.state.editable)
                    ? <Input {...this.props} className={classTextField}/>
                    : <p {...this.props}>{this.props.value}</p>}
            </div>
        )
    }

}

export default withStyles(styles, { withTheme: true })(TextFieldEditing);