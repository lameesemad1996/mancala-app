import './App.css';
import GameBoard from "./components/GameBoard";

function App() {
  return (
    <div className="App">
      <GameBoard pits={[4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0]} onPitClick={() => {console.log(`I clicked a pit`)}} />
    </div>
  );
}

export default App;
