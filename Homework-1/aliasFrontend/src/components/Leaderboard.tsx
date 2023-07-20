import {alias} from "common-types";
import Team = alias.JsTeam;

type LeaderboardProps = {
    teams: Array<Team>
}

export default function Leaderboard({teams}: LeaderboardProps) {
    return (<div className="App-container">
        <h3 className="App-game-stat font-link-base">
            Game statistics
        </h3>
        <div className="App-container App-container-min-height">
            {
                teams.map((team, index) => (
                    <p key={team.id + 1} className="App-leaderboard-p font-link-base">
                        {team.name} ({team.points} points)
                    </p>
                ))
            }
        </div>
    </div>)
}