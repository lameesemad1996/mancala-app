import React from 'react';
import { Navigate } from 'react-router-dom';
import { useGame } from '../context/GameContext';
import PropTypes from "prop-types";

const ProtectedRoute = ({ children }) => {
  const { isGameOver } = useGame();

  // Check if the game is over; if not, redirect to the start page
  if (!isGameOver) {
    return <Navigate to="/" />;
  }

  // If the game is over, render the child components
  return children;
};

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
};

export default ProtectedRoute;