import React from 'react';
import {fireEvent, render, screen} from '@testing-library/react';
import {describe, expect, test, jest, beforeEach} from '@jest/globals';
import GameBoard from "../src/components/GameBoard";
import { getPitByTestId, getAllPits } from './TestUtils';

describe('GameBoard Component', () => {
    const pits = Array(14)
        .fill('4', 0,6)
        .fill('4', 7, 13);
    pits[6] = '0';
    pits[13] = '0';
    const handlePitClick = jest.fn();

    // Reset the mock before each test
    beforeEach(() => {
        handlePitClick.mockClear();
    });

    test('renders all pits correctly', () => {
        render(<GameBoard pits={pits} currentPlayer={0} onPitClick={handlePitClick} />);
        expect(getAllPits()).toHaveLength(14);
    });

    test('calls onPitClick when a pit is clicked', () => {
        render(<GameBoard pits={pits} currentPlayer={0} onPitClick={handlePitClick} />);
        fireEvent.click(getPitByTestId(0));
        expect(handlePitClick).toHaveBeenCalledTimes(1);
    });

    test('disables pits when it is not the current player\'s turn', () => {
        render(<GameBoard pits={pits} currentPlayer={1} onPitClick={handlePitClick} />);
        fireEvent.click(getPitByTestId(0));
        expect(handlePitClick).not.toHaveBeenCalled();
    });
});
