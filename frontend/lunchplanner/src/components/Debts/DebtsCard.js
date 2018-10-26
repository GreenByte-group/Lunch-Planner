import React from "react"
import moment from "moment"
import {Card, CardContent, ListItem, withStyles} from "@material-ui/core";
import Debts from "./Debts";
import Claims from "./Claims";


const styles = {
    card: {
        width: '100%',
        '&:hover': {
            textDecoration: 'none',
        },
    },
    link: {
        '&:hover': {
            textDecoration: 'none',
        }
    },
    listItem: {
        padding: '7px 16px',
        '&:hover': {
            backgroundColor: '#0303031a !important',
        }
    },
    title: {
        fontFamily: "Work Sans",
        fontSize: '16px',
        lineHeight: '24px',
        marginBottom: '0px',
    },
    date: {
        marginLeft: '16px',
        fontFamily: "Work Sans",
        fontSize: '11px',
        lineHeight: '20px',
        marginBottom: '0px',
        width: 'auto',
        float: 'left',
        color: 'black',
    },
    time : {
        fontFamily: "Work Sans",
        fontSize: '14px',
        lineHeight: '20px',
        marginBottom: '0px',
        marginTop: '5px',
        width: 'auto',
        float: 'left',
    },
    users: {
        fontFamily: "Work Sans",
        fontSize: '13px',
        lineHeight: '20px',
        marginBottom: '0px',
        color: '#A4A4A4',
        float: 'right',
    },
    icons: {
        width: '13px',
        height: 'auto',
    },
    text: {
        width: 'auto',
        color: 'black',
    },
    textSelected: {
        width: 'auto',
        color: '#75A045',
    },
    imageDiv: {
        width: '48px',
        height: '48px',
        borderRadius: '50%',
        border: '1px black solid',
        float: 'left',
    },
    cardContent: {
        width: '100%',
        float: 'none',
    },
};

class DebtsCard extends React.Component {

    constructor(props) {
        super();

        this.state = {
            creditor: props.creditor,
            debtor: props.debtor,
            sum: props.sum,
            artOfCard: props.artOfCard,
            id: props.id,
        }
    }

    componentWillReceiveProps(newProps) {
        if(newProps.creditor !== this.state.creditor){
            this.setState({
                creditor: newProps.creditor,
            });
        }
    }



    render() {
        const {classes} = this.props;


        if(this.state.artOfCard == 'debtor'){
                    return (
                        <div>
                            <ListItem button className={classes.listItem}>
                                <Card className={classes.card}>
                                    <CardContent className={classes.cardContent}>
                                        <div className={classes.text}>
                                            <Debts artOfCard = {"debtor"}
                                                   id = {this.state.id}
                                                   creditor = {this.state.creditor}
                                                   debtor = {this.state.debtor}
                                                   sum = {this.state.sum}/>
                                        </div>
                                    </CardContent>
                                    <hr style={{marginBottom: '11px'}}/>
                                    <div className={classes.footer}>
                                    </div>
                                </Card>
                            </ListItem>
                        </div>
                    )
        }else{
                    return(
                        <div>
                            <ListItem  button className={classes.listItem}>
                                <Card className={classes.card}>
                                    <CardContent className={classes.cardContent}>
                                        <div className={classes.text}>
                                            <Claims artOfCard = {"creditor"}
                                                    id = {this.state.id}
                                                    creditor = {this.state.creditor}
                                                    debtor = {this.state.debtor}
                                                    sum = {this.state.sum}/>
                                        </div>
                                    </CardContent>
                                </Card>
                            </ListItem>
                        </div>
                    )
        }
    }
}
export default withStyles(styles)(DebtsCard);
