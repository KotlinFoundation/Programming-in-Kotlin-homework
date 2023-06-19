import {alias} from "common-types";
import Card = alias.JsCard;

type AliasCardProps = {
    card: Card | null
}

export default function AliasCard({card}: AliasCardProps) {
    return (<div className="App-container App-word-container-cards">
        {
            card?.words?.map((word, index) => (
                <div key={index + 1} className="App-word-container">
                    <div>{word}</div>
                </div>
            ))
        }
    </div>)
}