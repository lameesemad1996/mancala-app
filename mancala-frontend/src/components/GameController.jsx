import React, { useState, useEffect} from "react";
import ApiService from "../services/ApiService";
import GameBoard from "./GameBoard";
import PlayerInfo from "./PlayerInfo";
import {Link, useLocation} from 'react-router-dom';
import './GameController.scss';
import './../index.scss';
import { useSnackbar } from 'notistack';

/**
 * GameController component
 * Manages the state of the game and interactions between the UI and backend API
 */
const GameController = () => {
    const { enqueueSnackbar } = useSnackbar();
    const location = useLocation();
    const { player1Name, player2Name } = location.state || { player1Name: '', player2Name: '' };
    const [gameState, setGameState] = useState(null);

    // Fetch the game state from the API once when the component loads
    useEffect(() => {
        ApiService.getGameState().then((response) => {
            setGameState(response.data)
        }).catch((error) => {
            console.error("Error fetching game state:", error);
            enqueueSnackbar("Failed to load game state.", { variant: "error" });
        });
    }, []);

    /**
     * Handles a move by making an API call and updating the game state
     * @param {number} pitIndex - The index of the pit clicked by the player
     */
    const handleMove = (pitIndex) => {
        ApiService.makeMove(pitIndex).then((response) => {
            setGameState(response.data);
        }).catch((error) => {
            console.error("Error making move:", error);
            if (error.response && error.response.status === 400) {
                enqueueSnackbar(error.response.data, { variant: "error" }); // Show error message from backend
            } else {
                enqueueSnackbar("An unexpected error occurred.", { variant: "error" });
            }
        });
    }

    /**
     * Resets the game state by making an API call
     */
    const handleReset = () => {
        ApiService.resetGame().then((response) => {
            setGameState(response.data)
        }).catch((error) => {
            console.error("Error resetting game:", error);
            enqueueSnackbar("Failed to reset the game.", { variant: "error" });
        });
    }

    if (!gameState) return <div>Loading...</div>;

    return (
        <div className={'game-controller-container'}>
            <div className="title">Welcome to the Mancala Game</div>
            <div className="title player-names"> {player1Name} VS. {player2Name} </div>

            <GameBoard className="game-board"
                pits={gameState.pits}
                currentPlayer={gameState.currentPlayer}
                onPitClick={handleMove}
            />
            <br/>
            <PlayerInfo
                currentPlayer={gameState.currentPlayer}
                player1Name={player1Name}
                player1Score={gameState.pits[6]}
                player2Name={player2Name}
                player2Score={gameState.pits[13]}
                onReset={handleReset}
            />
            <Link to="/">
                <div className="button-container">
                    <button className="new-game-button" onClick={handleReset}>Start A New Game</button>
                </div>
            </Link>
            <footer>Powered by Bol.com - Built by LK Studios</footer>
        </div>
    )
}

export default GameController;