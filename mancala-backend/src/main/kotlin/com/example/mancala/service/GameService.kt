package com.example.mancala.service

import com.example.mancala.model.Board
import com.example.mancala.model.GameState
import org.springframework.stereotype.Service

@Service
class GameService {
    private val board = Board()
    private var currentPlayer = 0
    private var gameOver = false

    /**
     * Process a move in the game
     * @param pitIndex The index of the pit to move stones from
     * @return The updated game state
     */
    fun processMove(pitIndex: Int): GameState {
        if(gameOver) {
            throw IllegalStateException("Game is over")
        }
        if(pitIndex < 0 || pitIndex >= board.pits.size || pitIndex / 6 != currentPlayer) {
            throw IllegalArgumentException("Invalid move")
        }
        if(board.pits[pitIndex].stones == 0) {
            throw IllegalArgumentException("Cannot select an empty pit")
        }

        val lastPitIndex = board.moveStones(pitIndex, currentPlayer)
        val stonesInLastPit = board.pits[lastPitIndex].stones
        val isPitOnCurrentPlayerSide = Board.isPitOnCurrentPlayerSide(lastPitIndex, currentPlayer)

        // Check capture condition
        if(lastPitIndex < 12 && stonesInLastPit == 1 && isPitOnCurrentPlayerSide) {
            board.captureStones(lastPitIndex, currentPlayer)
        }

        // Check if the game is over
        if(board.isGameOver()) {
            gameOver = true
            board.allocateRemainingStones()
        } else {
            // Check if the current player gets another turn
            // Check if stone lands in the current player's big pit
            if(Board.isLastStoneInBigPit(lastPitIndex, currentPlayer)) {
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
            pits = board.pits.map { it.stones }.toTypedArray(),
            bigPits = board.bigPits,
            currentPlayer = currentPlayer,
            scores = arrayOf(board.bigPits[0], board.bigPits[1])
        )
    }

    /**
     * Reset the game
     */
    fun resetGame() {
        board.resetBoard()
        currentPlayer = 0
        gameOver = false
    }
}