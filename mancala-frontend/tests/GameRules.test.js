import React from 'react';
import { render, screen } from '@testing-library/react';
import GameRules from './../src/components/GameRules';
import {describe, expect, test} from "@jest/globals";

describe('GameRules Component', () => {
    test('renders game rules correctly', () => {
        render(<GameRules />);
        expect(screen.getByText('Game Rules')).toBeInTheDocument();
        expect(screen.getByText(/Mancala is a strategy game where the objective is to collect/i)).toBeInTheDocument();
    });

    test('displays game rules image', () => {
        render(<GameRules />);
        expect(screen.getByAltText('image demonstrating some game rules')).toBeInTheDocument();
    });
});
