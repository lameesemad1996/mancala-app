import React from "react";

/**
 * Pit component
 * Displays a single pit on the Mancala board
 * @param {number} stones - Number of stones in the pit
 * @param {function} onClick - Callback for when the pit is clicked
 * @param {boolean} bigPit - Whether this pit is a big pit (store)
 */
const Pit = ({ stones, onClick, bigPit }) => {
    return (
        <div
            className={`pit ${bigPit ? "big-pit" : ""} ${onClick ? 'clickable' : ''}`}
            onClick={onClick ? onClick : undefined}>
                {stones}
        </div>
    );
};

export default Pit;