import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

const styles = {

};

class DebtsScreen extends React.Component {

    constructor(props) {
        super();
    };
    render(){
        return ("ok");
    };

}
DebtsScreen.propTypes = {
    classes: PropTypes.object.isRequired,
};
export default withStyles(styles, { withTheme: true })(DebtsScreen);