import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { SnackbarProvider } from 'notistack';
import { describe, expect, test, jest, beforeEach } from '@jest/globals';
import ApiService from "../src/services/gameApiService";
import GameController from "../src/components/GameController";
import { getAllPits } from "./TestUtils";
import {GameProvider, useGame} from "../src/context/gameContext";

jest.mock('../src/services/gameApiService');

describe('GameController Component', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        ApiService.getGameState.mockResolvedValue({
            data: {
                board: { pits: Array(14).fill(4) },
                currentPlayer: 0,
            }
        });
        ApiService.makeMove.mockResolvedValue({
            data: {
                board: { pits: Array(14).fill(4) },
                currentPlayer: 0,
            }
        });
        ApiService.resetGame.mockResolvedValue({
            data: {
                board: { pits: Array(14).fill(4) },
                currentPlayer: 0,
            }
        });

        render(
            <GameProvider>
                <TestWrapper gameId={1}>
                    <BrowserRouter>
                        <SnackbarProvider>
                            <GameController />
                        </SnackbarProvider>
                    </BrowserRouter>
                </TestWrapper>
            </GameProvider>
        );
    });

    test('fetches and displays game state on load', async () => {
        expect(screen.getByText('Loading...')).toBeInTheDocument();
        await waitFor(() => {
            const pits = getAllPits();
            expect(pits).toHaveLength(14);
        });
    });

    test('handles pit click and updates game state', async () => {
        const pits = await waitFor(() => getAllPits());
        fireEvent.click(pits[8]);
        await waitFor(() => {
            expect(ApiService.makeMove).toHaveBeenCalledTimes(1);
        });
    });

    test('displays error snackbar when API call fails', async () => {
        ApiService.getGameState.mockRejectedValueOnce({
            response: {
                status: 400,
                data: { message: 'Failed to load game state.' },
            }
        });

        render(
            <GameProvider>
                <TestWrapper gameId={1}>
                    <BrowserRouter>
                        <SnackbarProvider>
                            <GameController />
                        </SnackbarProvider>
                    </BrowserRouter>
                </TestWrapper>
            </GameProvider>
        );
        await waitFor(() => expect(screen.getByText('Failed to load game state.')).toBeInTheDocument());
    });

    test('displays error when trying to move stones from an empty pit', async () => {
        // Adjusting the pit values to simulate an empty pit at index 8
        ApiService.getGameState.mockResolvedValueOnce({
            data: {
                board: { pits: [4, 4, 4, 4, 4, 4, 0, 0, 0, 4, 4, 4, 4, 4] },
                currentPlayer: 0,
            }
        });

        // Mock makeMove to reject when attempting to move from an empty pit
        ApiService.makeMove.mockRejectedValueOnce({
            response: {
                status: 400,
                data: { message: 'Invalid move. You cannot select an empty pit.' },
            }
        });

        // Re-render the component to load the new game state
        render(
            <GameProvider>
                <TestWrapper gameId={1}>
                    <BrowserRouter>
                        <SnackbarProvider>
                            <GameController />
                        </SnackbarProvider>
                    </BrowserRouter>
                </TestWrapper>
            </GameProvider>
        );

        // Wait for the game state to load
        const pits = await waitFor(() => getAllPits());

        // Simulate a click on an empty pit (index 8)
        fireEvent.click(pits[8]);

        // Expect the snackbar to display the error message
        await waitFor(() => {
            expect(ApiService.makeMove).toHaveBeenCalledTimes(1);
            expect(screen.getByText('Invalid move. You cannot select an empty pit.')).toBeInTheDocument();
        });
    });
});

// Helper component to wrap GameController with GameContext state setup
const TestWrapper = ({ children, gameId }) => {
    const { setGameId } = useGame();

    React.useEffect(() => {
        setGameId(gameId);
    }, [gameId]);

    return children;
};
