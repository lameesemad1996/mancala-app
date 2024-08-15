import React from 'react';
import { jest } from '@jest/globals';  // Add this line
import {render, screen, fireEvent, waitFor} from '@testing-library/react';
import { SnackbarProvider } from 'notistack';
import { BrowserRouter } from 'react-router-dom';
import StartPage from './../src/components/StartPage';
import { describe, expect, test, afterEach } from '@jest/globals';
import { GameProvider } from '../src/context/gameContext';
import ApiService from '../src/services/gameApiService';

// Mock the useNavigate hook from react-router-dom
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));

// Mock the createGame function from ApiService
jest.mock('../src/services/gameApiService', () => ({
    ...jest.requireActual('../src/services/gameApiService'),
    createGame: jest.fn(),
}));

afterEach(() => {
    jest.clearAllMocks();
});

describe('StartPage Component', () => {
    test('renders title and input fields correctly', () => {
        render(
            <GameProvider>
                <BrowserRouter>
                    <SnackbarProvider>
                        <StartPage />
                    </SnackbarProvider>
                </BrowserRouter>
            </GameProvider>
        );

        expect(screen.getByText('Mancala')).toBeInTheDocument();
        expect(screen.getByTestId('player1input')).toBeInTheDocument();
        expect(screen.getByTestId('player2input')).toBeInTheDocument();
    });

    test('shows warning snackbar when player names are not entered', async () => {
        render(
            <GameProvider>
                <BrowserRouter>
                    <SnackbarProvider>
                        <StartPage />
                    </SnackbarProvider>
                </BrowserRouter>
            </GameProvider>
        );

        fireEvent.click(screen.getByText('Start Game'));

        // Wait for the snackbar message to appear
        const warningMessage = await screen.getAllByText('Name cannot be empty.')[0];
        expect(warningMessage).toBeInTheDocument();
    });

    test('navigates to game page when player names are entered and createGame succeeds', async () => {
        // Mock createGame to resolve successfully
        ApiService.createGame.mockResolvedValue({
            data: {
                gameId: '12345',
            },
        });

        render(
            <GameProvider>
                <BrowserRouter>
                    <SnackbarProvider>
                        <StartPage />
                    </SnackbarProvider>
                </BrowserRouter>
            </GameProvider>
        );

        fireEvent.change(screen.getByTestId('player1input'), { target: { value: 'Alice' } });
        fireEvent.change(screen.getByTestId('player2input'), { target: { value: 'Bob' } });
        fireEvent.click(screen.getByText('Start Game'));

        // Ensure createGame was called with correct parameters
        expect(ApiService.createGame).toHaveBeenCalled();

        // Simulate successful navigation to /game
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith('/game');
        });
    });

    test('shows error snackbar when createGame fails', async () => {
        // Mock createGame to reject with an error
        ApiService.createGame.mockRejectedValue(new Error('Failed to create game'));

        render(
            <GameProvider>
                <BrowserRouter>
                    <SnackbarProvider>
                        <StartPage />
                    </SnackbarProvider>
                </BrowserRouter>
            </GameProvider>
        );

        fireEvent.change(screen.getByTestId('player1input'), { target: { value: 'Alice' } });
        fireEvent.change(screen.getByTestId('player2input'), { target: { value: 'Bob' } });
        fireEvent.click(screen.getByText('Start Game'));

        // Ensure createGame was called
        expect(ApiService.createGame).toHaveBeenCalled();

        // Wait for the error snackbar message to appear
        const errorMessage = await screen.findByText('Failed to start a new game.');
        expect(errorMessage).toBeInTheDocument();
    });
});
