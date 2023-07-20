import cards from "../../assets/Kotlin_Alias_cover.svg";
import {GameState} from "../GameScreen";

type StartScreenProps = {
    gameStateSetter: (gs: GameState) => void
}

export default function StartScreen({gameStateSetter}: StartScreenProps) {
    return (
        <div className="App-cards-container">
            <img src={cards} className="App-cards" alt="cards"/>
            <p className="App-big-name font-link-bold">Alias Game</p>
            <p className="App-small-name font-link-base">by Kotlin Course</p>
            <div className="App-buttons-container App-display-flex">
                <button className="App-button-base App-button-start" onClick={() => gameStateSetter(GameState.TEAMS)} ></button>
                <button className="App-button-base App-stat-button App-cursor-none"><p className="font-link-light App-no-margin App-stat-button-p App-cursor-pointer" onClick={() => gameStateSetter(GameState.STAT)} >Previous games results</p></button>
            </div>
        </div>
    );
}
