import React from "react"

import {HOST} from "../../Config"
import List from "@material-ui/core/List";
import {withStyles} from "@material-ui/core/styles/index";
import FloatingActionButton from "../FloatingActionButton";
import {getUsername} from "../authentication/LoginFunctions";
import moment from "moment";
import {getHistory} from "../../utils/HistoryUtils";
import DebtsCard from "./DebtsCard";
import {CircularProgress} from "material-ui";
import {getAll} from "./DebtsFunctions";

export let needReload = false;
export function eventListNeedReload() {
    needReload = true;

    if(functionToFire)
        functionToFire();
}

let functionToFire;


const styles = {
    root: {
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
    },
    list: {
        padding: 0,
        paddingBottom: '75px',
    },
    day:{
        marginLeft: 16,
        marginTop: 10,
        fontSize: 16,
    },
};


const lightBackground = 'transparent';
const darkerBackground = '#03030305';

class DebtsCreditorScreen extends React.Component {

    constructor(props) {
        super();
        this.state = {
            cardStyle: 'creditor',
            debts: [],
        };

    }

    componentDidMount(){
      this.setState({
         loading: true,
      });
        this.loadDebts()
    }


    componentWillReceiveProps(newProps) {
        if(newProps.debts !== this.state.events){
            this.setState({
                debts: newProps.debts,
            });
        }

    }

    loadDebts() {
        getAll(getUsername(), (response) => {
            this.setState({
                debts: response.data,
            });
        });
    }

    render() {
        const { classes } = this.props;
        let stateProps = this.state.debts;
        console.log('Debts in CreditorScreen', stateProps);

        return (
            <div className={classes.root}>
                <List className={classes.list}>
                    {   stateProps.map((response) => {

                               return   <div>
                                            <DebtsCard  artOfCard = {"creditor"}
                                                        id = {response.debtsId}
                                                        creditor = {response.creditor}
                                                        debtor = {response.debtor}
                                                        sum = {response.sum}
                                            />
                                        </div>
                        })
                    }
                </List>
            </div>

        );
    }
}
export default withStyles(styles)(DebtsCreditorScreen);
