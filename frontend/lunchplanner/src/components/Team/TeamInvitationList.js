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

class TeamInvitationList extends React.Component {

    constructor(props) {
        super();

        this.state = {
            selectedTeams: props.selectedTeams || [],
            teams: props.teams || [],
            selectable: props.selectable || false,
            onSelectionChanged: props.onSelectionChanged,
        };
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

    clickHandler = (teamname, teamMember, selected) => {
        if(this.state.selectable) {
            let selectedTeams = this.state.selectedTeams;
            if (selected) {
                selectedTeams.push({teamname, teamMember});
            } else {
                let index = selectedTeams.indexOf(teamname);
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

        console.log('selected teams: ', selectedTeams);

        return (
            <List
                className={classes.list}
            >
                {teams.map((listValue) => {
                    return <TeamInvitation
                        selectable={this.state.selectable}
                        selected={selectedTeams.some((value) => value.teamname === listValue.teamName)}
                        teamname={listValue.teamName}
                        teamMember={listValue.invitations}
                        onClick={this.clickHandler}

                    />;
                })}
            </List>
        );
    }
}

export default withStyles(styles, { withTheme: true })(TeamInvitationList);