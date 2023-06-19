import StartScreen from "./screens/StartScreen";
import GameRoundScreen from "./screens/GameRoundScreen";
import EndScreen from "./screens/EndScreen";
import TeamsScreen from "./screens/TeamsScreen";
import {GameTeams} from "../models/Team";
import {useState} from "react";
import PreviousGamesScreen from "./screens/PreviousGamesScreen";

export enum GameState {
    START,
    TEAMS,
    GAME,
    END,
    STAT,
}

export type GameScreenProps = {
    state: GameState,
    gameStateSetter: (gs: GameState) => void,
}

export default function GameScreen({state, gameStateSetter}: GameScreenProps) {
    let [gameTeams, gameTeamsSetter] = useState<GameTeams>(new GameTeams([]))
    switch (state) {
        case GameState.START: {
            return <StartScreen gameStateSetter={gameStateSetter}/>
        }
        case GameState.TEAMS: {
            return <TeamsScreen gameStateSetter={gameStateSetter} gameTeamsSetter={gameTeamsSetter}/>
        }
        case GameState.GAME: {
            return <GameRoundScreen gameStateSetter={gameStateSetter}
                                    gameTeams={gameTeams}
                                    gameTeamsSetter={gameTeamsSetter}/>
        }
        case GameState.END: {
            return <EndScreen gameStateSetter={gameStateSetter} gameTeams={gameTeams}/>
        }
        case GameState.STAT: {
            return <PreviousGamesScreen gameStateSetter={gameStateSetter}/>
        }
    }
}
