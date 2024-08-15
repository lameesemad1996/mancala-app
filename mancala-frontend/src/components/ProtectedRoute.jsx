import React from 'react';
import { Navigate } from 'react-router-dom';
import { useGame } from '../context/gameContext';
import PropTypes from "prop-types";

const ProtectedRoute = ({ children }) => {
    const { isGameOver, gameId } = useGame(); // Added gameId to the context check

    // Check if the game is over or if there is no active game; if so, redirect to the start page
    if (!gameId || !isGameOver) {
        return <Navigate to="/" />;
    }

    // If the game is active and game over, render the child components
    return children;
};

ProtectedRoute.propTypes = {
    children: PropTypes.node.isRequired,
};

export default ProtectedRoute;