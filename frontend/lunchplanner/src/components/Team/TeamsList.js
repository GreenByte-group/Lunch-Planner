import React from 'react';
import {withStyles} from 'material-ui/styles';
import {List} from "material-ui";
import Team from "./Team";

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

    clickHandler = (teamname, selected) => {
        if(this.state.selectable) {
            let selectedTeams = this.state.selectedTeams;
            if (selected) {
                selectedTeams.push(teamname);
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

        return (
            <List
                className={classes.list}
            >
                {teams.map((listValue) => {
                    return <Team selectable={this.state.selectable} selected={selectedTeams.includes(listValue.teamName)} name={listValue.teamName} onClick={this.clickHandler}/>;
                })}
            </List>
        );
    }
}

export default withStyles(styles, { withTheme: true })(TeamsList);