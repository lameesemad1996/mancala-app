import React, { useState } from "react";
import "./StartPage.scss";
import './../index.scss';
import {useNavigate} from "react-router-dom";

const StartPage = () => {
    const [player1Name, setPlayer1Name] = useState("");
    const [player2Name, setPlayer2Name] = useState("");
    const navigate = useNavigate();

    const handleGameStart = () => {
        if (player1Name && player2Name) {
            navigate('/game', { state: { player1Name, player2Name } });
        } else {
            alert('Please enter both player names.');
        }
    }

    return (
        <div className="start-page">
            <div className="title">Welcome to the Mancala Game</div>
            <div className="input-group">
                <label>Player 1 Name:</label>
                <input
                    type="text"
                    name="player1"
                    value={player1Name}
                    onChange={(event) => setPlayer1Name(event.target.value)}
                />
                <br/>
                <label>Player 2 Name:</label>
                <input
                    type="text"
                    name="player2"
                    value={player2Name}
                    onChange={(event) => setPlayer2Name(event.target.value)}
                />
            </div>
            <button className="start-button" onClick={handleGameStart}>Start Game</button>
            <footer>Powered by Bol.com - Built by LK Studios</footer>
        </div>

    );
}

export default StartPage;
