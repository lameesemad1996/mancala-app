import React, { useState } from "react";
import "./StartPage.scss";
import './../index.scss';
import {useNavigate} from "react-router-dom";
import {useSnackbar} from "notistack";
import { useGame } from "../context/GameContext";
import { sanitizeAndValidatePlayerName } from '../utils';

/**
 * StartPage component
 * Collects player names before starting the game
 */
const StartPage = () => {
    const navigate = useNavigate();
    const [player1Name, setPlayer1NameInput] = useState("");
    const [player2Name, setPlayer2NameInput] = useState("");
    const { enqueueSnackbar } = useSnackbar();
    const { setPlayer1Name, setPlayer2Name } = useGame();

    const handleGameStart = () => {
        const player1Validation = sanitizeAndValidatePlayerName(player1Name, enqueueSnackbar);
        const player2Validation = sanitizeAndValidatePlayerName(player2Name, enqueueSnackbar);

        if (!player1Validation.isValid || !player2Validation.isValid) {
            // If either name is invalid, stop further execution
            return;
        }

        // Proceed with the sanitized names if both are valid
        setPlayer1Name(player1Validation.sanitizedName);
        setPlayer2Name(player2Validation.sanitizedName);
        navigate('/game');
    };

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
                        onChange={(event) => setPlayer1NameInput(event.target.value)}
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
                        onChange={(event) => setPlayer2NameInput(event.target.value)}
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
