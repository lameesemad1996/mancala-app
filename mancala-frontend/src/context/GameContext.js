import React, { createContext, useContext, useState } from 'react';

const GameContext = createContext();
export const useGame = () => useContext(GameContext);
export const GameProvider = ({ children }) => {
    const [isGameOver, setIsGameOver] = useState(false);
    const [player1Name, setPlayer1Name] = useState('');
    const [player2Name, setPlayer2Name] = useState('');

    return (
        <GameContext.Provider value={{
            isGameOver, setIsGameOver,
            player1Name, setPlayer1Name,
            player2Name, setPlayer2Name
        }}>
            {children}
        </GameContext.Provider>
    );
};