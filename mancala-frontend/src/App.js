import './App.css';
import GameController from "./components/GameController";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import StartPage from "./components/StartPage";

function App() {
  return (
      <Router>
          <Routes>
              <Route path="/" element={<StartPage />} />

              <Route path="/game" element={<GameController />} />
          </Routes>
      </Router>
  );
}

export default App;
