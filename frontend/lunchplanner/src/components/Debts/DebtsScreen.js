import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core';
import SwipeableViews from 'react-swipeable-views';
import AppBar from '@material-ui/core/AppBar';
import {Tabs, Tab, Typography, CircularProgress} from '@material-ui/core';
import DebtsDebtorScreen from "./DebtsDebtorScreen";
import DebtsCreditorScreen from "./DebtsCreditorScreen";
import {getUsername} from "../authentication/LoginFunctions";
import {getAll} from "./DebtsFunctions";



const styles = theme => ({
    root: {
        backgroundColor: theme.palette.background.paper,
        //width: 1500,,
        position: 'relative',
        height: '100%',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
    },
    fab: {
        position: 'absolute',
        bottom: theme.spacing.unit * 2,
        right: theme.spacing.unit * 2,
    },
    whiteSymbol: {
        color: theme.palette.common.white
    },
    tab: {
        fontFamily: "Work Sans",
        fontWeight: '600',
        letterSpacing: '0.65px',
        fontSize: '13px',
    },
    swipeViews: {
        height: '100%',
        overflowY: 'auto',
        maxWidth: '1024px',
        width: '100%',
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    progress:{
        marginLeft: 'auto',
        marginRight: 'auto',
        marginTop: "auto",
        marginBottom: "auto",
        display: "block",
    }
});

TabContainer.propTypes = {
    children: PropTypes.node.isRequired,
    dir: PropTypes.string.isRequired,
};

function TabContainer({ children, dir }) {
    return (
        <Typography component="div" dir={dir} style={{ padding: 0, height: '100%' }}>
            {children}
        </Typography>
    );
}





export let needReload = false;

export function eventListNeedReload() {
    needReload = true;

    if(functionToFire)
        functionToFire();
}

let functionToFire;




class DebtsScreen extends React.Component {
    constructor(props) {
        super();


        this.state = {
            value: 1,
            search: props.search,
            debtsPAY: [],
            debtsCLAIM: [],
            loading: true,
            completed: 0,
        };

        }


    componentDidMount() {
    }

    loadDebts() {

    }

    componentWillReceiveProps(newProps) {
        this.loadDebts()
    }

    handleChange = (event, value) => {
        this.setState({ value });
    };

    handleChangeIndex = index => {
        this.setState({ value: index });
    };

    render() {
        const { classes, theme } = this.props;
        return (
                    <div className={classes.root}>
                        <AppBar position="relative" color="default" >
                            <Tabs
                                value={this.state.value}
                                onChange={this.handleChange}
                                indicatorColor="secondary"
                                textColor="secondary"
                                centered
                                fullWidth
                            >
                                <Tab className={classes.tab} label="LIABILITIES" />
                                <Tab className={classes.tab} label="CLAIMS" />
                            </Tabs>
                        </AppBar>
                        <SwipeableViews
                            axis={theme.direction === 'rtl' ? 'x-reverse' : 'x'}
                            index={this.state.value}
                            onChangeIndex={this.handleChangeIndex}
                            className={classes.swipeViews}
                        >
                            <TabContainer dir={theme.direction}>
                                <DebtsDebtorScreen/>
                            </TabContainer>
                            <TabContainer dir={theme.direction}>
                                <DebtsCreditorScreen/>
                            </TabContainer>
                        </SwipeableViews>
                    </div>
        );
    }
}

DebtsScreen.propTypes = {
    classes: PropTypes.object.isRequired,
    theme: PropTypes.object.isRequired,
};

export default withStyles(styles, { withTheme: true })(DebtsScreen);
