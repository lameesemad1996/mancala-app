package com.example.mancala.service

import com.example.mancala.exception.InvalidMoveException
import com.example.mancala.model.Board
import com.example.mancala.model.GameState
import org.springframework.stereotype.Service

@Service
class GameService (private val board: Board = Board()) {
    private var currentPlayer = 0

    /**
     * Process a move in the game
     * @param pitIndex The index of the pit to move stones from
     * @return The updated game state
     */
    fun processMove(pitIndex: Int): GameState {
        if(pitIndex < 0 || pitIndex >= board.pits.size || pitIndex / 7 != currentPlayer) {
            throw InvalidMoveException("Invalid move. You must select a pit on your side with stones.")
        }
        if(board.pits[pitIndex] == 0) {
            throw InvalidMoveException("Invalid move. You cannot select an empty pit.")
        }

        val lastPitIndex = board.moveStones(pitIndex, currentPlayer)
        val stonesInLastPit = board.pits[lastPitIndex]
        val isPitOnCurrentPlayerSide = Board.isPitOnCurrentPlayerSide(lastPitIndex, currentPlayer)
        val playersBigPit = if(currentPlayer == Board.PLAYER_1) Board.PLAYER_1_BIG_PIT_INDEX else Board.PLAYER_2_BIG_PIT_INDEX

        // Check capture condition
        if(lastPitIndex <= 12 && stonesInLastPit == 1 && isPitOnCurrentPlayerSide && lastPitIndex != playersBigPit) {
            board.catchingOpponentsStones(lastPitIndex, currentPlayer)
        }

        // Check if the game is over
        if(board.isGameOver()) {
            board.allocateRemainingStones()
        } else {
            // Check if the current player gets another turn
            // Increment the currentPlayer if last stone does not land in the current player's big pit
            if(!Board.isLastStoneInBigPit(lastPitIndex, currentPlayer)) {
                currentPlayer = (currentPlayer + 1) % 2
            }
        }

        return getGameState()
    }

    /**
     * Get the current game state
     * @return The current game state
     */
    fun getGameState(): GameState {
        return GameState(
            pits = board.pits,
            currentPlayer = currentPlayer,
            scores = arrayOf(board.pits[Board.PLAYER_1_BIG_PIT_INDEX], board.pits[Board.PLAYER_2_BIG_PIT_INDEX])
        )
    }

    /**
     * Reset the game
     */
    fun resetGame() {
        board.resetBoard()
        currentPlayer = 0
    }
}