import React from 'react';
import './GameRules.scss';

/**
 * GameRules component
 * Displays the rules of the Mancala game
 */
const GameRules = () => {
    return (
        <div className="game-rules">
            <h2>Game Rules</h2>
            <p>
                Mancala is a strategy game where the objective is to collect the most stones in your big pit.
                Each player has 6 small pits and 1 big pit (their store). On your turn, you pick up all stones
                from one of your small pits and distribute them counterclockwise, one by one, in the subsequent
                pits.
                If the last stone lands in your big pit, you get another turn. If it lands in an empty small pit on
                your side,
                you capture that stone and any stones in the opposite pit.
            </p>
            <br/>
            <img className="game-rules-img" src='/images/rules.jpeg' alt='image demonstrating some game rules'/>
        </div>
    );
};

export default GameRules;
