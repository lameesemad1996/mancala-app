import './App.css';
import GameBoard from "./components/GameBoard";

function App() {
  return (
    <div className="App">
      <GameBoard pits={[6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0]} onPitClick={() => {console.log(`I clicked a pit`)}} />
    </div>
  );
}

export default App;
