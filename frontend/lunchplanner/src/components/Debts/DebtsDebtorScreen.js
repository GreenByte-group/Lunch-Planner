import React from "react"

import {HOST} from "../../Config"
import DebtsCard from "./DebtsCard"
import List from "@material-ui/core/List";
import {withStyles} from "@material-ui/core/styles/index";
import FloatingActionButton from "../FloatingActionButton";
import {getUsername} from "../authentication/LoginFunctions";
import moment from "moment";
import {getHistory} from "../../utils/HistoryUtils";
import {getAll, getAllLiab} from "./DebtsFunctions";

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

class DebtsDebtorScreen extends React.Component {

    constructor(props) {
        super();

        this.state = {
            cardStyle: 'debtor',
            debts: [],
        };
    }

    componentWillReceiveProps(newProps) {
        if(newProps.debts !== this.state.events){
            this.setState({
                debts: newProps.debts,
            });
        }
    }

    loadDebts() {
        getAllLiab(getUsername(), (response) => {
            this.setState({
                debts: response.data,
            });
        });
    }

    componentDidMount(){
        this.setState({
            loading: true,
        });
        this.loadDebts()
    }



    render() {
        const { classes } = this.props;
        let stateProps = this.state.debts;
        console.log('Debts in DebtorScreen', stateProps);

        return (
            <div className={classes.root}>
                <List className={classes.list}>
                    {   stateProps.map((response) => {

                              return    <div>
                                            <DebtsCard  artOfCard = {"debtor"}
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
export default withStyles(styles)(DebtsDebtorScreen);
