import React, { createContext, useContext, useState } from 'react';
import PropTypes from "prop-types";

const GameContext = createContext();
export const useGame = () => useContext(GameContext);
export const GameProvider = ({ children }) => {
    const [isGameOver, setIsGameOver] = useState(false);
    const [player1Name, setPlayer1Name] = useState('');
    const [player2Name, setPlayer2Name] = useState('');
    const [gameId, setGameId] = useState(null); // Added gameId to the context

    return (
        <GameContext.Provider value={{
            isGameOver, setIsGameOver,
            player1Name, setPlayer1Name,
            player2Name, setPlayer2Name,
            gameId, setGameId // Provide gameId and its setter
        }}>
            {children}
        </GameContext.Provider>
    );
};

GameProvider.propTypes = {
    children: PropTypes.node.isRequired,
};
