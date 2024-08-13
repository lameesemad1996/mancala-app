import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../src/App';
import { describe, expect, test } from '@jest/globals';

describe('App Component', () => {
  test('renders StartPage initially', () => {
    render(
      <App />
    );
    expect(screen.getByText('Mancala')).toBeInTheDocument();
  });

  test('navigates to GameController when route changes to /game', () => {
    window.history.pushState({}, 'Game', '/game');
    render(
      <App />
    );
    expect(screen.getByText('Loading...')).toBeInTheDocument();  // Expect loading state initially
  });

  test('renders GameRules when route changes to /rules', () => {
    window.history.pushState({}, 'Game Rules', '/rules');
    render(
        <App />
    );
    expect(screen.getByText(/Mancala is a strategy game where the objective is to collect/i)).toBeInTheDocument();
  });
});
