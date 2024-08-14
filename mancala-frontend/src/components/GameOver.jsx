import React from 'react';
import './GameOver.scss';
import {useLocation, useNavigate} from "react-router-dom";
import {useGame} from "../context/GameContext";

const GameOver = () => {
    let winner = '';
    const location = useLocation();
    const navigate = useNavigate();
    const { player1Name, player2Name } = useGame();

    const { gameState } = location.state || { gameState: {pits: Array(14).fill(0)} };
    const player1Score = gameState.pits[6];
    const player2Score = gameState.pits[13];

    if (player1Score > player2Score) {
        winner = player1Name;
    } else if (player2Score > player1Score) {
        winner = player2Name;
    } else {
        winner = 'It\'s a Tie!';
    }

    return (
        <div className="game-over-container">
            <div className="game-over">
                <h1>Game Over</h1>
                <div className="scores">
                    <div>{player1Name}: {player1Score}</div>
                    <div>{player2Name}: {player2Score}</div>
                </div>
                <h2>{winner !== 'It\'s a Tie!' ? `Congratulations, ${winner}!` : winner}</h2>
                <button className="restart-button" onClick={() => navigate("/game")}>Play Again</button>
            </div>
        </div>
    );
}

export default GameOver;
