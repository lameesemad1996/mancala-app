import React, { useState, useEffect} from "react";
import ApiService from "../services/ApiService";
import GameBoard from "./GameBoard";
import PlayerInfo from "./PlayerInfo";
import {Link, useNavigate} from 'react-router-dom';
import './GameController.scss';
import './../index.scss';
import { useSnackbar } from 'notistack';
import GameRules from "./GameRules";
import { useGame } from '../context/GameContext';

/**
 * GameController component
 * Manages the state of the game and interactions between the UI and backend API
 */
const GameController = () => {
    const navigate = useNavigate();
    const [gameState, setGameState] = useState(null);
    const [showRules, setShowRules] = useState(false);
    const { enqueueSnackbar } = useSnackbar();
    const { setIsGameOver, player1Name, player2Name, setPlayer1Name, setPlayer2Name } = useGame();

    // Fetch the game state from the API once when the component loads
    useEffect(() => {
        fetchGameState();
    }, []);

    // Check if the game is over and navigate to the game over page
    useEffect(() => {
        if (gameState) {
            const isGameOver = gameState.pits.slice(0, 6).every(pit => pit === 0) || gameState.pits.slice(7, 13).every(pit => pit === 0);
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
        ApiService.getGameState()
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
        console.error(defaultMessage, error);
        if (error.response && error.response.status === 400) {
            enqueueSnackbar(error.response.data.message, { variant: "error" });
        } else {
            enqueueSnackbar(defaultMessage, { variant: "error" });
        }
    };

    /**
     * Handles a move by making an API call and updating the game state
     * @param {number} pitIndex - The index of the pit clicked by the player
     */
    const handleMove = (pitIndex) => {
        ApiService.makeMove(pitIndex).then((response) => {
            setGameState(response.data);
        }).catch((error) => {
            handleApiError(error, "Error making move.");
        });
    };

    /**
     * Resets the game state by making an API call
     */
    const handleReset = (isStartingANewGame = false) => {
        ApiService.resetGame()
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

    if (!gameState) return <div>Loading...</div>;
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
                       pits={gameState.pits}
                       currentPlayer={gameState.currentPlayer}
                       onPitClick={handleMove}
            />
            <br/>
            <div className="game-status">
                <PlayerInfo
                    currentPlayer={gameState.currentPlayer}
                    player1Name={player1Name}
                    player1Score={gameState.pits[6]}
                    player2Name={player2Name}
                    player2Score={gameState.pits[13]}
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
