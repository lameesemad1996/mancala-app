import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { useLocation, useNavigate } from 'react-router-dom';
import GameOver from '../src/components/GameOver';

// Mock the useLocation and useNavigate hooks
jest.mock('react-router-dom', () => ({
    useLocation: jest.fn(),
    useNavigate: jest.fn(),
}));

describe('GameOver Component', () => {
    const mockNavigate = jest.fn();

    beforeEach(() => {
        useNavigate.mockReturnValue(mockNavigate);
    });

    test('displays winner correctly when player1 wins', () => {
        useLocation.mockReturnValue({
            state: {
                player1Name: 'Alice',
                player2Name: 'Bob',
                gameState: { pits: [0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 5] },
            },
        });

        render(<GameOver />);

        expect(screen.getByText(/Congratulations, Alice!/i)).toBeInTheDocument();
        expect(screen.getByText(/Alice: 10/i)).toBeInTheDocument();
        expect(screen.getByText(/Bob: 5/i)).toBeInTheDocument();
    });

    test('displays winner correctly when player2 wins', () => {
        useLocation.mockReturnValue({
            state: {
                player1Name: 'Alice',
                player2Name: 'Bob',
                gameState: { pits: [0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 10] },
            },
        });

        render(<GameOver />);

        expect(screen.getByText(/Congratulations, Bob!/i)).toBeInTheDocument();
        expect(screen.getByText(/Alice: 5/i)).toBeInTheDocument();
        expect(screen.getByText(/Bob: 10/i)).toBeInTheDocument();
    });

    test('displays tie correctly', () => {
        useLocation.mockReturnValue({
            state: {
                player1Name: 'Alice',
                player2Name: 'Bob',
                gameState: { pits: [0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10] },
            },
        });

        render(<GameOver />);

        expect(screen.getByText(/It's a Tie!/i)).toBeInTheDocument();
        expect(screen.getByText(/Alice: 10/i)).toBeInTheDocument();
        expect(screen.getByText(/Bob: 10/i)).toBeInTheDocument();
    });

    test('navigates to /game on button click', () => {
        useLocation.mockReturnValue({
            state: {
                player1Name: 'Alice',
                player2Name: 'Bob',
                gameState: { pits: [0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 5] },
            },
        });

        render(<GameOver />);

        const button = screen.getByRole('button', { name: /Play Again/i });
        fireEvent.click(button);

        expect(mockNavigate).toHaveBeenCalledWith('/game');
    });
});