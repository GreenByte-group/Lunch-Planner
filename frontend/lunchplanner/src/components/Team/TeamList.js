import React from "react"

import Team from "./Team";
import {List, CircularProgress} from "@material-ui/core/";
import {People, TagFaces} from "@material-ui/icons";
import {withStyles} from "@material-ui/core/styles/index";
import FloatingActionButton from "../FloatingActionButton";
import {setAuthenticationHeader} from "../authentication/LoginFunctions";
import {getTeams} from "./TeamFunctions";
import {getHistory} from "../../utils/HistoryUtils";

const styles = {
    root: {
        //width: 1500,,
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
       // position: 'absolute',
        //display: 'flex',
    },
    list: {
        padding: 0,
    },
    progress:{
        marginLeft: '50%',
        marginTop: "50%",
    },
};

export let needReload = false;

export function teamListNeedReload() {
    needReload = true;
}

class TeamList extends React.Component {

    constructor(props) {
        setAuthenticationHeader();
        super(props);
        this.state = {
            teams: [],
            search:null,
            loading: true,
        }
        teamListNeedReload();
    }

    componentDidMount() {
        this.setState({
            search: this.props.search,
        });

        this.loadTeams(this.props.search);
}

    componentWillReceiveProps(newProps) {
        if(needReload) {
            needReload = !needReload;
            this.loadTeams();
        }
    }

    loadTeams(search){
        getTeams(this.props.search,
            (response) => {
                this.setState({
                    teams: response.data,
                    loading: false,
                })
            });
    }

    render() {
        if(needReload) {
            console.log("needReload")
            needReload = !needReload;
            this.loadTeams();
        }
        let loading = this.state.loading;

        const { classes } = this.props;
        let teams = this.state.teams;
        console.log(teams);
        return (
            <div className={classes.root}>
                {loading ? <CircularProgress className={classes.progress} color="secondary"/>
                    :
                        <div >
                            <List className={classes.list}>
                                {teams.map((listValue)=>{
                                    return <Team name={listValue.teamName}
                                                 id={listValue.teamId}
                                                 member={listValue.invitations}
                                    />;
                                })}
                            </List>
                            <FloatingActionButton
                                actions={
                                    [
                                        {icon: <People />, text: 'Work team', onClick: () => getHistory().push("/app/team/create?withParent=true") },
                                        {icon: <TagFaces />, text: 'Social group', onClick: () => getHistory().push("/app/team/create?withParent=false") }
                                    ]
                                }
                            />
                        </div>}
                    </div>

        );
    }
}

export default withStyles(styles)(TeamList);