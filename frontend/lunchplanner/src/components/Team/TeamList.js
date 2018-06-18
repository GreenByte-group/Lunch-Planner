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
        height: '100%',
        overflowX: 'hidden',
        overflowY: 'auto',
    },
    list: {
        padding: 0,
    },
    progress:{
        marginLeft: '50%',
        marginTop: "50%",
    },
    parentTeamName:{
        marginLeft: 16,
        marginTop: 10,
        fontSize: 14,
    }
};

export let needReload = false;
let funcReload;

export function teamListNeedReload() {
    needReload = true;
    if(funcReload)
        funcReload();
}

class TeamList extends React.Component {

    constructor(props) {
        setAuthenticationHeader();
        super();
        this.state = {
            teams: [],
            search:props.search,
            loading: true,
        };

        funcReload = () => {
            this.setState({loading: true})
        };
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
        if(newProps.search !== this.state.search){
            this.setState({
                search: newProps.search,
            });
            this.loadTeams(newProps.search);
        }
    }

    loadTeams(search){
        getTeams(search,
            (response) => {
                this.setState({
                    teams: response.data,
                    loading: false,
                })
            });
    }

    render() {
        if(needReload) {
            needReload = !needReload;
            this.loadTeams();
        }
        let loading = this.state.loading;

        const { classes } = this.props;
        let teams = this.state.teams;
        let parentTeamName = "";
        return (
            <div className={classes.root}>
                {loading ? <CircularProgress className={classes.progress} color="secondary"/>
                    :
                        <div >
                            <List className={classes.list}>
                                {teams.map((listValue)=>{
                                    return <div>
                                        <p className={classes.parentTeamName}>{teams.forEach((value) =>{
                                            if(listValue.parentTeam === value.teamId){
                                                parentTeamName = value.teamName;
                                            }
                                            return parentTeamName;
                                        })}{parentTeamName}</p>
                                        <Team name={listValue.teamName}
                                              id={listValue.teamId}
                                              member={listValue.invitations}
                                        />
                                    </div>
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