import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080/game';

/**
 * ApiService
 * Service to interact with the backend API for the Mancala game
 */
const ApiService = {
    getGameState: () => axios.get('/state').then(response => {
        return response
    }),
    makeMove: (pitIndex) => axios.post(`/move?pitIndex=${pitIndex}`).then(response => {
        return response
    }),
    resetGame: () => axios.post('/reset')
};

export default ApiService;