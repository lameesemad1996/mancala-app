import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/game";

const createGame = () => {
    return axios.post(`${API_BASE_URL}/create`);
};

const getGameState = (gameId) => {
    return axios.get(`${API_BASE_URL}/state/${gameId}`);
};

const makeMove = (gameId, pitIndex) => {
    return axios.post(`${API_BASE_URL}/move/${gameId}`, null, { params: { pitIndex } });
};

const resetGame = (gameId) => {
    return axios.post(`${API_BASE_URL}/reset/${gameId}`);
};

export default {
    createGame,
    getGameState,
    makeMove,
    resetGame
};
