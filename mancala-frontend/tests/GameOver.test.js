import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { useLocation, useNavigate } from 'react-router-dom';
import GameOver from '../src/components/GameOver';
import { GameProvider } from "../src/context/GameContext";
import { useGame } from "../src/context/GameContext";

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
                gameState: { pits: [0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 5] },
            },
        });

        render(
            <GameProvider>
                <TestWrapper player1Name="Alice" player2Name="Bob">
                    <GameOver />
                </TestWrapper>
            </GameProvider>
        );

        expect(screen.getByText(/Congratulations, Alice!/i)).toBeInTheDocument();
        expect(screen.getByText(/Alice: 10/i)).toBeInTheDocument();
        expect(screen.getByText(/Bob: 5/i)).toBeInTheDocument();
    });

    test('displays winner correctly when player2 wins', () => {
        useLocation.mockReturnValue({
            state: {
                gameState: { pits: [0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 10] },
            },
        });

        render(
            <GameProvider>
                <TestWrapper player1Name="Alice" player2Name="Bob">
                    <GameOver />
                </TestWrapper>
            </GameProvider>
        );

        expect(screen.getByText(/Congratulations, Bob!/i)).toBeInTheDocument();
        expect(screen.getByText(/Alice: 5/i)).toBeInTheDocument();
        expect(screen.getByText(/Bob: 10/i)).toBeInTheDocument();
    });

    test('displays tie correctly', () => {
        useLocation.mockReturnValue({
            state: {
                gameState: { pits: [0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10] },
            },
        });

        render(
            <GameProvider>
                <TestWrapper player1Name="Alice" player2Name="Bob">
                    <GameOver />
                </TestWrapper>
            </GameProvider>
        );

        expect(screen.getByText(/It's a Tie!/i)).toBeInTheDocument();
        expect(screen.getByText(/Alice: 10/i)).toBeInTheDocument();
        expect(screen.getByText(/Bob: 10/i)).toBeInTheDocument();
    });

    test('navigates to /game on button click', () => {
        useLocation.mockReturnValue({
            state: {
                gameState: { pits: [0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 5] },
            },
        });

        render(
            <GameProvider>
                <TestWrapper player1Name="Alice" player2Name="Bob">
                    <GameOver />
                </TestWrapper>
            </GameProvider>
        );

        const button = screen.getByRole('button', { name: /Play Again/i });
        fireEvent.click(button);

        expect(mockNavigate).toHaveBeenCalledWith('/game');
    });
});

// Helper component to wrap GameOver with GameContext state setup
const TestWrapper = ({ children, player1Name, player2Name }) => {
    const { setPlayer1Name, setPlayer2Name } = useGame();

    React.useEffect(() => {
        setPlayer1Name(player1Name);
        setPlayer2Name(player2Name);
    }, [setPlayer1Name, setPlayer2Name, player1Name, player2Name]);

    return children;
};
