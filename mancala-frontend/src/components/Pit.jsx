import React from "react";

/**
 * Pit component
 * Displays a single pit on the Mancala board
 * @param {number} stones - Number of stones in the pit
 * @param {boolean} bigPit - Whether this pit is a big pit (store)
 * @param {function} onClick - Callback for when the pit is clicked
 * @param {boolean} clickable - If true, allows the pit to be clicked
 */
const Pit = ({ stones, bigPit, onClick, clickable, dataTestId }) => {
    return (
        <div
            className={`pit ${bigPit ? "big-pit" : ""} ${clickable ? 'clickable' : ''}`}
            onClick={clickable ? onClick : undefined} data-testid={dataTestId}>
                {stones}
        </div>
    );
};

export default Pit;