import React from "react"
import moment from "moment"
import {Card, CardContent, ListItem, withStyles} from "@material-ui/core";
import {Avatar, Button, Divider, IconButton, Icon} from "@material-ui/core";
import {deleteClaims} from "./DebtsFunctions";
import {AddCircleOutline, MaximizeRounded} from '@material-ui/icons'

import Arrow from 'react-arrow'
import User from "../User/User";
import UserList from "../User/UserList";
import {getUser} from "../User/UserFunctions";
import DeleteIcon from '@material-ui/icons/Delete';
import {HOST} from "../../Config";
import Dialog from "../Dialog";
import {getHistory} from "../../utils/HistoryUtils";

const styles = {
    card: {
        width: '100%',
        '&:hover': {
            textDecoration: 'none',
        },
        display: 'flex',
        flexDirection:'row',

    },
    icons: {
        width: '13px',
        height: 'auto',
    },
    textCreditor: {
        width: 'auto',
        color: '#75A045',
        fontFamily: "Work Sans",
        fontSize: '15px',

    },
    textDebtor: {
        width: 'auto',
        color: 'red',
        fontFamily: "Work Sans",
        fontSize: '15px',

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
    cardRow: {
        height: '100%',
        width: '80%',
        display: 'flex',
        flexDirection: 'row',
    },
    cardContent: {
        width: '100%',
        float: 'none',
        flexDirection: 'row',
    },
    delete: {
        backgroundColor: '#75A045',
        color: 'white',
        fontFamily: "Work Sans",
        fontSize: '11px',
        marginTop: '10px',
        marginLeft:'20%',
        float: 'right',
        display: 'flex',
    },
    memberAvatar:{
        marginRight: 5,
        width: 16,
        height: 16,
    },
    memberPicture: {
        height: '100%',
        width: '100%',
        objectFit: 'cover',
    },
    debtor: {
        height: '100%',
        width: '10%',
        marginTop: '15px',
    },
    creditor: {
        height: '100%',
        width: '10%',
        marginTop: '15px',
        marginLeft:'10px',
        marginRight:'20px',
    },
    sum: {
        height: '100%',
        width: '20%',
        marginTop: '15px',
        marginLeft: '50%',
        color: 'black',
        fontFamily: "Work Sans",
        fontSize: '13px',
    },
    sumText: {
        height: '100%',
        width: '20%',
        marginTop: '15px',
        marginLeft: '48%',
        color: 'black',
        fontFamily: "Work Sans",
        fontSize: '13px',
    },
    arrowSum: {
        width: '100%',
        display: 'flex',

    },
    arrowsumcontext: {
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
    },
    profilePictureDebtor: {
        float: 'left',
        // border: '1px black solid',
        borderRadius: '50%',
        height: '32px',
        width: '32px',
        overflow: 'hidden',
        backgroundColor: 'red',
    },
    profilePictureCreditor: {
        float: 'left',
        // border: '1px black solid',
        borderRadius: '50%',
        height: '32px',
        width: '32px',
        overflow: 'hidden',
        backgroundColor: '#75A045',
    },
    profilePictureImg: {
        objectFit: 'cover',
        width: '100%',
        height: '100%',
    },
    addCircleIcon: {
        marginRight: 20,
        marginTop: 15,
        backgroundColor: 'orange',

    },

};



class Claims extends React.Component {

    constructor(props){
        super();
        this.state = {
            creditor: props.creditor,
            debtor: props.debtor,
            sum: props.sum,
            artOfCard: props.artOfCard,
            id: props.id,
        };

    }

    deleteDebt = () => {
        let returnId = this.state.id;
        deleteClaims(returnId);
        this.setState({
           loading: true,
        });
        getHistory().push("/app/event");
    }

    getUserprofilPic(){
           getUser(this.state.debtor, (response) => {
               this.setState({
                  picPathDebtor: HOST + response.data.profilePictureUrl
               })
           });
           getUser(this.state.creditor, (response) => {
               this.setState({
                   picPathCreditor: HOST + response.data.profilePictureUrl
               })
           });
    }

    componentWillMount(){
        this.getUserprofilPic();

    }


    render(){
        const {classes} = this.props;
        let picPathDebtor = this.state.picPathDebtor;
        let picPathCreditor = this.state.picPathCreditor;
        console.log('Debts in Claims', this.state)
        let debtId = this.state.id;

        return (
            <div className = {classes.card}>
                <div className={classes.cardRow}>
                    <div className={classes.debtor}>
                        <div className={classes.profilePictureDebtor}>
                            <img className={classes.profilePictureImg} src={picPathDebtor} />
                        </div>
                        <div>
                            <p className={classes.textDebtor}>{this.state.debtor}</p>
                        </div>
                    </div>
                    <div className={classes.arrowsumcontext}>
                        <p className={classes.sumText}>have to pay to</p>
                        <Divider/>
                        <p className={classes.sum}>{this.state.sum + ' €'}</p>
                    </div>
                    <div className={classes.creditor}>
                        <div className={classes.profilePictureCreditor}>
                            <img className={classes.profilePictureImg} src={picPathCreditor} />
                        </div>
                        <div>
                            <p className={classes.textCreditor}>{this.state.creditor}</p>
                        </div>
                    </div>
                </div>
                <IconButton className={classes.delete} onClick={this.deleteDebt}>
                    <DeleteIcon />
                </IconButton>
            </div>
        )
    }
};

export default withStyles(styles)(Claims);
