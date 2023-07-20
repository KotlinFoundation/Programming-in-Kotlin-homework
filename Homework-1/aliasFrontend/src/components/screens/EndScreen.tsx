import {GameState} from "../GameScreen";
import {GameTeams} from "../../models/Team";
import axios from "axios";
import Leaderboard from "../Leaderboard";

type EndScreenProps = {
    gameStateSetter: (gs: GameState) => void
    gameTeams: GameTeams
}

export default function EndScreen({gameStateSetter, gameTeams}: EndScreenProps) {
    let sortedTeams = gameTeams.teams.sort((a,b) => b.points - a.points)

    // TODO: can I do it better?
    function makeBody() {
        let body: { id: number; points: number; name: string }[] = [];
        sortedTeams.forEach((t) => {
            body.push({
                "id": t.id,
                "points": t.points,
                "name": t.name
            })
        });
        console.log(body)
        return body
    }

    return (
        <div className="App-cards-container">
            <Leaderboard teams={sortedTeams} />
            <button className="App-button-base App-game-button-bottom-base App-game-button-bottom-finish" onClick={
                () => {
                    axios.post("/results/save", makeBody()).then((response) => {
                        gameStateSetter(GameState.START)
                    })
                }
            } ></button>
        </div>
    );
}
