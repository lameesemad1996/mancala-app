import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { SnackbarProvider } from 'notistack';
import { BrowserRouter } from 'react-router-dom';
import StartPage from './../src/components/StartPage';
import {describe, expect, test, afterEach} from "@jest/globals";

// Mock the useNavigate hook from react-router-dom
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useNavigate: () => mockNavigate,
}));

afterEach(() => {
    jest.clearAllMocks();
});

describe('StartPage Component', () => {
    test('renders title and input fields correctly', () => {
        render(
            <BrowserRouter>
                <SnackbarProvider>
                    <StartPage />
                </SnackbarProvider>
            </BrowserRouter>
        );

        expect(screen.getByText('Mancala')).toBeInTheDocument();
        expect(screen.getByTestId('player1input')).toBeInTheDocument();
        expect(screen.getByTestId('player2input')).toBeInTheDocument();
    });

    test('shows warning snackbar when player names are not entered', () => {
        render(
            <BrowserRouter>
                <SnackbarProvider>
                    <StartPage />
                </SnackbarProvider>
            </BrowserRouter>
        );

        fireEvent.click(screen.getByText('Start Game'));
        expect(screen.getByText('Please enter both player names.')).toBeInTheDocument();
    });

    test('navigates to game page when player names are entered', () => {
        render(
            <BrowserRouter>
                <SnackbarProvider>
                    <StartPage />
                </SnackbarProvider>
            </BrowserRouter>
        );

        fireEvent.change(screen.getByTestId('player1input'), { target: { value: 'Alice' } });
        fireEvent.change(screen.getByTestId('player2input'), { target: { value: 'Bob' } });
        fireEvent.click(screen.getByText('Start Game'));

        expect(mockNavigate).toHaveBeenCalledWith('/game', { state: { player1Name: 'Alice', player2Name: 'Bob' } });
    });
});
