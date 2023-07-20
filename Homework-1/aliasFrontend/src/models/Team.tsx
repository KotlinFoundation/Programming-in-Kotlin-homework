import {alias} from "common-types";
import Team = alias.JsTeam;

export class GameTeams {
    teams: Array<Team>
    currentTeamIndex: number

    constructor(teams: Array<Team>) {
        // this.teams = Array.from(Array(capacity).keys()).map(i => new Team(i, 0))
        this.teams = teams
        this.currentTeamIndex = 0
    }

    addPoints(points: number) {
        this.teams[this.currentTeamIndex].points += points
        this.currentTeamIndex += 1
        if (this.currentTeamIndex >= this.teams.length) {
            this.currentTeamIndex = 0
        }
    }
}