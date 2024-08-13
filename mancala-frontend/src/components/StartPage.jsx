import React, { useState } from "react";
import "./StartPage.scss";
import './../index.scss';
import {useNavigate} from "react-router-dom";
import {useSnackbar} from "notistack";

/**
 * StartPage component
 * Collects player names before starting the game
 */
const StartPage = () => {
    const navigate = useNavigate();
    const [player1Name, setPlayer1Name] = useState("");
    const [player2Name, setPlayer2Name] = useState("");
    const { enqueueSnackbar } = useSnackbar();

    const handleGameStart = () => {
        if (player1Name.trim() && player2Name.trim()) {
            navigate('/game', { state: { player1Name, player2Name } });
        } else {
            enqueueSnackbar("Please enter both player names.", { variant: "warning" });
        }
    }

    return (
        <div className="start-page">
            <div className="title">Mancala</div>
            <div className="input-group">
                <div className="input-container">
                    <label className="input-label-1">Please enter the name of the first player:</label>
                    <input
                        type="text"
                        name="player1"
                        value={player1Name}
                        placeholder={"Player 1"}
                        data-testid="player1input"
                        onChange={(event) => setPlayer1Name(event.target.value)}
                    />
                </div>
                <div className="input-container">
                    <label className="input-label-2">Please enter the name of the second player:</label>
                    <input
                        type="text"
                        name="player2"
                        value={player2Name}
                        placeholder={"Player 2"}
                        data-testid="player2input"
                        onChange={(event) => setPlayer2Name(event.target.value)}
                    />
                </div>
            </div>
            <div>
                <button className="game-rules-button" onClick={() => navigate("/rules")}>Game Rules</button>
                <button className="start-button" onClick={handleGameStart}>Start Game</button>
            </div>
            <footer>Powered by Bol.com - Built by LK Studios</footer>
        </div>

    );
}

export default StartPage;
