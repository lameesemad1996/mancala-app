import React from 'react';
import './App.css';
import GameController from "./components/GameController";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import StartPage from "./components/StartPage";
import {SnackbarProvider} from "notistack";
import GameRules from "./components/GameRules";
import GameOver from "./components/GameOver";
import ProtectedRoute from "./components/ProtectedRoute";
import {GameProvider} from "./context/GameContext";

function App() {
  return (
      <GameProvider>
          <Router>
              <Routes>
                  <Route path="/" element={
                      <SnackbarProvider
                          maxSnack={3}
                          autoHideDuration={2000}
                          anchorOrigin={{
                              vertical: 'top',
                              horizontal: 'center',
                          }}>
                          <StartPage />
                      </SnackbarProvider>
                  } />

                  <Route path="/game" element={
                      <SnackbarProvider
                          maxSnack={3}
                          autoHideDuration={2000}
                          anchorOrigin={{
                              vertical: 'top',
                              horizontal: 'center',
                          }}>
                          <GameController />
                      </SnackbarProvider>
                  } />

                  <Route path="/game-over" element={
                      <ProtectedRoute>
                          <GameOver />
                      </ProtectedRoute>
                  } />

                  <Route path="/rules" element={<GameRules />} />
              </Routes>
          </Router>
      </GameProvider>
  );
}

export default App;
