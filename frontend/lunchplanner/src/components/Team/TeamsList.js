import React from 'react';
import {withStyles} from 'material-ui/styles';
import {List} from "material-ui";
import TeamInvitation from "./TeamInvitation";

const styles = {
    list: {
        padding: 0,
        marginTop: '0px !important',
    },
};

class TeamsList extends React.Component {

    constructor(props) {
        super();

        this.state = {
            selectedTeams: props.selectedTeams || [],
            teams: props.teams || [],
            selectable: props.selectable || false,
            onSelectionChanged: props.onSelectionChanged,
        };

        console.log("const")
        console.log(props.selectedTeams);
        console.log("Teams" + this.state.teams);
    }

    componentWillReceiveProps(newProps) {
        let selectedTeams, teams;

        if(newProps.selectedTeams && newProps.selectedTeams !== this.state.selectedTeams) {
            selectedTeams = newProps.selectedTeams;
        }

        if(newProps.teams && newProps.teams !== this.state.teams) {
            teams = newProps.teams;
        }

        if(teams) {
            this.setState({
                teams: teams || this.state.teams,
                selectedTeams: selectedTeams || this.state.selectedTeams,
            });
        }
    }

    clickHandler = (teamId, selected) => {
        if(this.state.selectable) {
            let selectedTeams = this.state.selectedTeams;
            if (selected) {
                selectedTeams.push(teamId);
            } else {
                let index = selectedTeams.indexOf(teamId);
                selectedTeams.splice(index, 1);
            }

            this.setState({
                selectedTeams: selectedTeams,
            });

            this.state.onSelectionChanged(selectedTeams);
        }
    };

    render() {
        const { classes } = this.props;
        let teams = this.state.teams;
        let selectedTeams = this.state.selectedTeams;

        return (
            <List
                className={classes.list}
            >
                {teams.map((listValue) => {
                    return <TeamInvitation
                        selectable={this.state.selectable}
                        selected={selectedTeams.includes(listValue.teamId)}
                        teamname={listValue.teamName}
                        teamId={listValue.teamId} onClick={this.clickHandler}
                        member={listValue.invitations}

                    />;
                })}
            </List>
        );
    }
}

export default withStyles(styles, { withTheme: true })(TeamsList);