import React from 'react';
import Pit from './Pit';

/**
 * GameBoard component
 * Displays the Mancala game board with 14 pits, including big pits positioned independently
 * @param {Array} pits - Array containing the number of stones in each pit
 * @param {number} currentPlayer - The current player's index (0 or 1)
 * @param {function} onPitClick - Callback for handling pit clicks
 */
const GameBoard = ({ pits, currentPlayer, onPitClick }) => {
    return (
        <div className="game-board">
            {/* Big Pit for Player 2 */}
            <div className="big-pit-container player2-big-pit">
                <Pit stones={pits[13]} bigPit />
            </div>

            {/* Small Pits */}
            <div className="small-pits">
                <div className="row player2-row">
                    {pits.slice(7, 13).reverse().map((stones, index) => (
                        <Pit
                            key={12 - index}
                            stones={stones}
                            onClick={() => onPitClick(12 - index)}
                            clickable={currentPlayer === 1} />
                    ))}
                </div>
                <div className="row player1-row">
                    {pits.slice(0, 6).map((stones, index) => (
                        <Pit
                            key={index}
                            stones={stones}
                            onClick={() => onPitClick(index)}
                            clickable={currentPlayer === 0}
                        />
                    ))}
                </div>
            </div>

            {/* Big Pit for Player 1 */}
            <div className="big-pit-container player1-big-pit">
                <Pit stones={pits[6]} bigPit />
            </div>
        </div>
    );
};

export default GameBoard;
