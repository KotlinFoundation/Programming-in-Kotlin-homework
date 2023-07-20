import {useEffect, useState} from "react";
import axios from "axios";
import {alias} from "common-types";
import Card = alias.JsCard;
import {GameState} from "../GameScreen";
import {GameTeams} from "../../models/Team";
import AliasCard from "../AliasCard";

type GameRoundScreenProps = {
    gameStateSetter: (gs: GameState) => void
    gameTeamsSetter: (gt: GameTeams) => void
    gameTeams: GameTeams
}

type CardState = {
    index: number;
    card: Card | null;
}

export default function GameRoundScreen({gameStateSetter, gameTeams, gameTeamsSetter}: GameRoundScreenProps) {
    let [maxIndex, maxIndexSetter] = useState<number>(-1)
    let [currentCardState, cardSetter] = useState<CardState>({index: -1, card: null})
    let [roundPoints, roundPointsSetter] = useState<number>(0)

    function nextCard() {
        if (maxIndex != null && currentCardState.index < maxIndex) {
            axios.get(`/cards/card?index=${currentCardState.index + 1}`).then((response) => {
                let newCard = response.data as Card
                cardSetter({index: currentCardState.index + 1, card: newCard})
            })
        }
        if (currentCardState.index >= maxIndex) {
            endGame()
        }
    }

    // Load initial data
    useEffect(() => {
        axios.get("/cards/amount").then((response) => {
            maxIndex = response.data as number;
            maxIndexSetter(maxIndex)
            nextCard();
        })
    }, []);

    function endGame() {
        gameTeams.addPoints(roundPoints)
        gameTeamsSetter(gameTeams)
        gameStateSetter(GameState.END)
    }
    return (
        <div className="App-cards-container">
            <p className="App-game-p font-link-base">
                Current team: {gameTeams.teams[gameTeams.currentTeamIndex].name}
            </p>
            <AliasCard card={currentCardState.card}/>
            <div className="App-buttons-container">
                <button className="App-button-base App-game-button-base App-game-button-base-skip" onClick={
                    () => {
                        nextCard()
                        if (roundPoints - 1 < 0) {
                            roundPointsSetter(0)
                        } else {
                            roundPointsSetter(roundPoints - 1)
                        }
                    }
                }></button>
                <button className="App-button-base App-game-button-base App-game-button-base-next" onClick={
                            () => {
                                nextCard()
                                roundPointsSetter(roundPoints + 1)
                            }
                        }>
                </button>
            </div>

            <div className="App-buttons-container">
                <button className="App-button-base App-game-button-bottom-base App-game-button-bottom-next-round" onClick={
                    () => {
                        gameTeams.addPoints(roundPoints)
                        nextCard()
                        roundPointsSetter(0)
                    }
                }></button>
                |
                <button className="App-button-base App-game-button-bottom-base App-game-button-bottom-finish" onClick={
                    () => endGame()
                }></button>
            </div>
        </div>
    );
}
