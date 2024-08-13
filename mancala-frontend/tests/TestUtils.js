import { screen } from '@testing-library/react';


/**
 * Retrieves an element based on the index by test id.
 *
 * @param {number} index - The index to use for retrieving the element.
 * @returns {HTMLElement} - The DOM element found by the test id.
 */
export function getPitByTestId(index) {
    return screen.getAllByTestId(`pit-${index}`)[0];
}

/**
 * Retrieves all elements that match the pit test id pattern.
 *
 * @returns {Array<HTMLElement>} - An array of all DOM elements matching the pit test id pattern.
 */
export function getAllPits() {
    return screen.getAllByTestId(/^pit-/);
}
