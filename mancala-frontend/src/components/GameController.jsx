import React, { useState, useEffect} from "react";
import ApiService from "../services/ApiService";
import GameBoard from "./GameBoard";
import PlayerInfo from "./PlayerInfo";

/**
 * GameController component
 * Manages the state of the game and interactions between the UI and backend API
 */
const GameController = () => {
    const [gameState, setGameState] = useState(null);

    // Fetch the game state from the API once when the component loads
    useEffect(() => {
        ApiService.getGameState().then((response) => {
            setGameState(response.data);
        });
    }, []);

    /**
     * Handles a move by making an API call and updating the game state
     * @param {number} pitIndex - The index of the pit clicked by the player
     */
    const handleMove = (pitIndex) => {
        ApiService.makeMove(pitIndex).then((response) => {
            setGameState(response.data);
        });
    }

    /**
     * Resets the game state by making an API call
     */
    const handleReset = () => {
        ApiService.resetGame().then((response) => {
            setGameState(response.data);
        });
    }

    if (!gameState) return <div>Loading...</div>;

    return (
        <div>
            <GameBoard
                pits={gameState.pits}
                currentPlayer={gameState.currentPlayer}
                onPitClick={handleMove}
            />
            <br />
            <PlayerInfo
                currentPlayer = {gameState.currentPlayer}
                player1Name = "Player 1"
                player1Score = {gameState.pits[6]}
                player2Name = "Player 2"
                player2Score = {gameState.pits[13]}
                onReset = {handleReset}
            />
        </div>
    )
}

export default GameController;