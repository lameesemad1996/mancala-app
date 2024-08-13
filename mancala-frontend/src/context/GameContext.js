import React, { createContext, useContext, useState } from 'react';

const GameContext = createContext();
export const useGame = () => useContext(GameContext);
export const GameProvider = ({ children }) => {
    const [isGameOver, setIsGameOver] = useState(false);
    return (
        <GameContext.Provider value={{ isGameOver, setIsGameOver }}>
            {children}
        </GameContext.Provider>
    );
};