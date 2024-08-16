import React, { useState, useEffect } from "react";
import ApiService from "../services/gameApiService";
import GameBoard from "./GameBoard";
import PlayerInfo from "./PlayerInfo";
import { Link, useNavigate } from 'react-router-dom';
import './GameController.scss';
import './../index.scss';
import { useSnackbar } from 'notistack';
import GameRules from "./GameRules";
import { useGame } from '../context/gameContext';

/**
 * GameController component
 * Manages the state of the game and interactions between the UI and backend API
 */
const GameController = () => {
    const navigate = useNavigate();
    const [gameState, setGameState] = useState(null);
    const [showRules, setShowRules] = useState(false);
    const { enqueueSnackbar } = useSnackbar();
    const { setIsGameOver, player1Name, player2Name, setPlayer1Name, setPlayer2Name, gameId } = useGame();

    // Fetch the game state from the API once when the component loads
    useEffect(() => {
        if (gameId) {
            fetchGameState();
        } else {
            handleReset(gameId);
        }
    }, [gameId]);

    // Check if the game is over and navigate to the game over page
    useEffect(() => {
        if (gameState) {
            const isGameOver = gameState.board.pits.slice(0, 6).every(pit => pit === 0) || gameState.board.pits.slice(7, 13).every(pit => pit === 0);
            if (isGameOver) {
                setIsGameOver(true);
                handleReset();
                navigate("/game-over", { state: { gameState } });
            }
        }
    }, [gameState, setIsGameOver, navigate, player1Name, player2Name]);

    /**
     * Fetches the game state from the API
     * @returns {Promise<void>}
     */
    const fetchGameState = () => {
        ApiService.getGameState(gameId)
            .then(response => setGameState(response.data))
            .catch(error => {
                console.error("Error fetching game state:", error);
                enqueueSnackbar("Failed to load game state.", { variant: "error" });
            });
    };

    /**
     * Handles API errors by logging the error and displaying a snackbar with the default message
     * @param error
     * @param defaultMessage
     */
    const handleApiError = (error, defaultMessage) => {
        if (error.response) {
            const { status, data } = error.response;

            if (status === 400) {
                enqueueSnackbar(data.message || "Bad Request", { variant: "error" });
            } else if (status === 404) {
                enqueueSnackbar("Resource not found", { variant: "error" });
            } else if (status === 500) {
                enqueueSnackbar("Server error, please try again later", { variant: "error" });
            } else {
                enqueueSnackbar(defaultMessage, { variant: "error" });
            }
        } else {
            enqueueSnackbar("Network error, please check your connection", { variant: "error" });
        }
    };

    /**
     * Handles a move by making an API call and updating the game state
     * @param {number} pitIndex - The index of the pit clicked by the player
     */
    const handleMove = (pitIndex) => {
        ApiService.makeMove(gameId, pitIndex).then((response) => setGameState(response.data)).catch((error) => {
            handleApiError(error, "Error making move.");
        });
    };

    /**
     * Resets the game state by making an API call
     */
    const handleReset = (isStartingANewGame = false) => {
        ApiService.resetGame(gameId)
            .then(response => {
                setGameState(response.data);
                if (isStartingANewGame) {
                    setPlayer1Name('');
                    setPlayer2Name('');
                }
            })
            .catch(error => {
                handleApiError(error, "Failed to reset the game.");
            });
    };

    if (!gameState) return <div>Loading...</div>; // Ensure gameState is defined before rendering

    if (showRules) {
        return (
            <div className="show-rules-container">
                <div className="game-rules-container">
                    <GameRules/>
                    <button
                        className='back-to-game-button'
                        onClick={() => setShowRules(false)}>
                        Back to Game
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className={'game-controller-container'}>
            <div className="title">Mancala</div>
            <div className="title player-names"> {player1Name} VS. {player2Name} </div>

            <GameBoard className="game-board"
                       pits={gameState.board.pits}
                       currentPlayer={gameState.currentPlayer}
                       onPitClick={handleMove}
            />
            <br/>
            <div className="game-status">
                <PlayerInfo
                    currentPlayer={gameState.currentPlayer}
                    player1Name={player1Name}
                    player1Score={gameState.board.pits[6]}
                    player2Name={player2Name}
                    player2Score={gameState.board.pits[13]}
                    onReset={() => handleReset(false)}
                />
                <button className="game-rules-button" onClick={() => setShowRules(true)}>Game Rules</button>
                <Link to="/">
                    <div className="button-container">
                        <button className="new-game-button" onClick={() => handleReset(true)}>Start A New Game</button>
                    </div>
                </Link>
            </div>
            <footer>Powered by Bol.com - Built by LK Studios</footer>
        </div>
    )
}

export default GameController;
