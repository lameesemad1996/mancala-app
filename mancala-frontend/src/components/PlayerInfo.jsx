import React from "react";

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
        <div className={"player-info"}>
            <div> {player1Name} Score: {player1Score} </div>
            <div> {player2Name} Score: {player2Score} </div>
            <div> Current Player: {currentPlayer === 0 ? player1Name : player2Name} </div>
            <button onClick={onReset}> Reset Game </button>
        </div>
    );
};

export default PlayerInfo;