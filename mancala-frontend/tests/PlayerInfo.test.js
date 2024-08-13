import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import PlayerInfo from './../src/components/PlayerInfo';
import {describe, expect, test, jest} from "@jest/globals";

describe('PlayerInfo Component', () => {
    const handleReset = jest.fn();

    test('renders player names and scores correctly', () => {
        render(
            <PlayerInfo
                player1Name="Alice"
                player2Name="Bob"
                player1Score={10}
                player2Score={5}
                currentPlayer={0}
                onReset={handleReset}
            />
        );

        expect(screen.getAllByText('Alice')[0]).toBeInTheDocument();
        expect(screen.getAllByText('Score:')[0]).toBeInTheDocument();
        expect(screen.getAllByText('10')[0]).toBeInTheDocument();
        expect(screen.getByText('Bob')).toBeInTheDocument();
        expect(screen.getAllByText('5')[0]).toBeInTheDocument();
        expect(screen.getByText('Current Player:')).toBeInTheDocument();
        expect(screen.getAllByText('Alice')[1]).toBeInTheDocument();
    });

    test('calls onReset when reset button is clicked', () => {
        render(
            <PlayerInfo
                player1Name="Alice"
                player2Name="Bob"
                player1Score={10}
                player2Score={5}
                currentPlayer={0}
                onReset={handleReset}
            />
        );

        fireEvent.click(screen.getByText('Reset Game'));
        expect(handleReset).toHaveBeenCalledTimes(1);
    });
});
