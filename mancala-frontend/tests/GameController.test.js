import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { SnackbarProvider } from 'notistack';
import {describe, expect, test, jest, beforeEach} from '@jest/globals';
import ApiService from "../src/services/ApiService";
import GameController from "../src/components/GameController";
import {getAllPits} from "./TestUtils";

jest.mock('../src/services/ApiService');

describe('GameController Component', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        ApiService.getGameState.mockResolvedValue({ data: { pits: Array(14).fill(4), currentPlayer: 0 } });
        ApiService.makeMove.mockResolvedValue({ data: { pits: Array(14).fill(4), currentPlayer: 0 } });
        ApiService.resetGame.mockResolvedValue({ data: { pits: Array(14).fill(4), currentPlayer: 0 } });
    });

    test('fetches and displays game state on load', async () => {
        render(
            <BrowserRouter>
                <SnackbarProvider>
                    <GameController />
                </SnackbarProvider>
            </BrowserRouter>
        );

        expect(screen.getByText('Loading...')).toBeInTheDocument();
        await waitFor(() => {
            const pits = getAllPits();
            expect(pits).toHaveLength(14)
        });
    });

    test('handles pit click and updates game state', async () => {
        render(
            <BrowserRouter>
                <SnackbarProvider>
                    <GameController />
                </SnackbarProvider>
            </BrowserRouter>
        );
        const pits = await waitFor(() => getAllPits());
        fireEvent.click(pits[8]);
        await waitFor(() => {
            expect(ApiService.makeMove).toHaveBeenCalledTimes(1);
        });
    });

    test('displays error snackbar when API call fails', async () => {
        ApiService.getGameState.mockRejectedValue(new Error('API failure'));
        render(
            <BrowserRouter>
                <SnackbarProvider>
                    <GameController />
                </SnackbarProvider>
            </BrowserRouter>
        );

        await waitFor(() => expect(screen.getByText('Failed to load game state.')).toBeInTheDocument());
    });
});
