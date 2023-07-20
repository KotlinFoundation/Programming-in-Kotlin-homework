import './App.css';
import './util/util'
import {useState} from "react";
import GameScreen, {GameState} from "./components/GameScreen";

function App() {
    let [gameState, gameStateSetter] = useState(GameState.START)

    switch (gameState) {
        case GameState.START: {
            return (<div className="App">
                <header className="App-header-base App-header-black">
                    <GameScreen state={gameState}
                                gameStateSetter={gameStateSetter}
                    />
                </header>
            </div>);
        }
        default: {
            return (<div className="App">
                <header className="App-header-base App-header-white">
                    <GameScreen state={gameState}
                                gameStateSetter={gameStateSetter}
                    />
                </header>
            </div>);
        }
    }
}

export default App;
