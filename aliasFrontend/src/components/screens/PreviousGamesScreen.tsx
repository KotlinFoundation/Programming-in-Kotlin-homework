import {GameState} from "../GameScreen";
import {useEffect, useState} from "react";
import {alias} from "common-types";
import Team = alias.JsTeam;
import Leaderboard from "../Leaderboard";
import axios from "axios";

type PreviousGamesScreenProps = {
    gameStateSetter: (gs: GameState) => void
}

type PreviousGamesProps = {
    gameResults: Array<Array<Team>>
    index: number
    currentIndexSetter: (i: number) => void
}

function PreviousGames({gameResults, index, currentIndexSetter}: PreviousGamesProps) {
    if (gameResults.length === 0) {
        return (
            <div className="App-leaderboard-container">
                <p className="App-no-top-margin font-link-base">You have not had completed games yet :(</p>
            </div>
        )
    }

    function getResults(index: number) {
        if (index >= gameResults.length) {
            currentIndexSetter(0)
            return gameResults[0]
        }
        if (index < 0) {
            currentIndexSetter(gameResults.length - 1)
            return gameResults[gameResults.length - 1]
        }
        currentIndexSetter(index)
        return gameResults[index]
    }

    return (
        <div className="App-container">
            <Leaderboard teams={gameResults[index]} />
            <div className="App-buttons-container">
                <div className="App-buttons-container App-teams-buttons-container">
                    <button className="App-button-base App-teams-button-base App-teams-button-back App-margin-right" onClick={
                        () => getResults(index - 1)
                    }></button>
                    <button className="App-button-base App-teams-button-base App-teams-button-next App-margin-left" onClick={
                        () => getResults(index + 1)
                    }></button>
                </div>
            </div>
        </div>
    )
}

export default function PreviousGamesScreen({gameStateSetter}: PreviousGamesScreenProps) {
    let [gameResults, gameResultsSetter] = useState<Array<Array<Team>>>([])
    let [currentIndex, currentIndexSetter] = useState<number>(0)

    // Load initial data
    useEffect(() => {
        axios.get("/results/all").then((response) => {
            gameResults = response.data as Array<Array<Team>>;
            console.log(gameResults)
            gameResultsSetter(gameResults)
        })
    }, []);

    return (
        <div className="App-cards-container">
            <PreviousGames
                gameResults={gameResults}
                index={currentIndex}
                currentIndexSetter={currentIndexSetter} />
            <button className="App-button-base App-teams-button-base App-teams-button-back" onClick={() => gameStateSetter(GameState.START)}>
            </button>
        </div>
    );
}
