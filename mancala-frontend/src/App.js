import './App.css';
import GameController from "./components/GameController";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import StartPage from "./components/StartPage";
import {SnackbarProvider} from "notistack";

function App() {
  return (
      <Router>
          <Routes>
              <Route path="/" element={
                  <SnackbarProvider maxSnack={3}>
                      <StartPage />
                  </SnackbarProvider>
              } />

              <Route path="/game" element={
                  <SnackbarProvider maxSnack={3}>
                      <GameController />
                  </SnackbarProvider>
              } />
          </Routes>
      </Router>
  );
}

export default App;
