import React from "react";
import "./PlayerInfo.scss";

/**
 * PlayerInfo component
 * Displays the current scores and the active player with personalized messages
 * @param {string} player1Name - Name of Player 1
 * @param {string} player2Name - Name of Player 2
 * @param {number} player1Score - Score of Player 1
 * @param {number} player2Score - Score of Player 2
 * @param {number} currentPlayer - Index of the current player (0 or 1)
 * @param {function} onReset - Callback function to reset the game
 */
const PlayerInfo = ({ currentPlayer, player1Name, player1Score, player2Name, player2Score, onReset }) => {
    return (
        <div className="player-info">
            <div className="player-score">
                <span className="player-name">{player1Name}</span> Score: <span className="score">{player1Score}</span>
            </div>
            <div className="player-score">
                <span className="player-name">{player2Name}</span> Score: <span className="score">{player2Score}</span>
            </div>
            <div className="current-player">
                Current Player: <span className="current-player-name">{currentPlayer === 0 ? player1Name : player2Name}</span>
            </div>
            <button className="reset-button" onClick={onReset}>Reset Game</button>
        </div>
    );
};

export default PlayerInfo;
