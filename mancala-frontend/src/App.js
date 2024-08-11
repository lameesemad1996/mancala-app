import './App.css';
import GameController from "./components/GameController";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import StartPage from "./components/StartPage";
import {SnackbarProvider} from "notistack";
import GameRules from "./components/GameRules";

function App() {
  return (
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

              <Route path="/rules" element={<GameRules />} />
          </Routes>
      </Router>
  );
}

export default App;
