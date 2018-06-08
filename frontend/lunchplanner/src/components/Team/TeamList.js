import React from "react"

import Team from "./Team";
import List from "@material-ui/core/List";
import {People, TagFaces} from "@material-ui/icons";
import {withStyles} from "@material-ui/core/styles/index";
import FloatingActionButton from "../FloatingActionButton";
import {setAuthenticationHeader} from "../authentication/LoginFunctions";
import {getTeams} from "./TeamFunctions";
import {getHistory} from "../../utils/HistoryUtils";

const styles = {
    root: {
        height: '100%',
        overflow: 'hidden',
    },
    list: {
        padding: 0,
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
        }
    }

    componentDidMount() {
        this.setState({
            search: this.props.search,
        });

        getTeams(this.props.search,
            (response) => {
                this.setState({
                    teams: response.data,
                })
            });
    }

    render() {
        const { classes } = this.props;
        let teams = this.state.teams;
        return (
            <div className={classes.root}>
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
            </div>

        );
    }
}

export default withStyles(styles)(TeamList);