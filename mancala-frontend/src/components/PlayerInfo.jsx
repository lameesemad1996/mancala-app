import React from "react";

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