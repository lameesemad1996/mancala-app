package com.example.mancala.service

import com.example.mancala.entity.Board
import org.springframework.stereotype.Service

@Service
class BoardService {

    /**
     * Moves the stones from the specified pit in a counter-clockwise direction
     * and returns the index of the last pit where a stone was sown.
     *
     * @param board The game board.
     * @param startIndex The index of the pit from which stones are picked up.
     * @param currentPlayer The index of the current player (0 for player 1, 1 for player 2).
     * @return The index of the last pit where a stone was placed.
     */
    fun moveStones(board: Board, startIndex: Int, currentPlayer: Int): Int {
        var index = startIndex
        var stones = board.pits[startIndex]
        board.pits = board.pits.toMutableList().apply { this[startIndex] = 0 }

        while (stones > 0) {
            index = (index + 1) % 14

            // Skip opponent's big pit
            if ((currentPlayer == Board.PLAYER_1 && index == Board.PLAYER_2_BIG_PIT_INDEX) ||
                (currentPlayer == Board.PLAYER_2 && index == Board.PLAYER_1_BIG_PIT_INDEX)) {
                continue
            }

            board.pits = board.pits.toMutableList().apply { this[index]++ }
            stones--
        }

        return index
    }

    /**
     * Capture the stones in the opposite pit and the current pit and add them to the current player's big pit
     * @param pitIndex The index of the pit
     * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
     */
    fun catchingOpponentsStones(board: Board, pitIndex: Int, currentPlayer: Int) {
        val oppositePitIndex = 12 - pitIndex
        val currentPlayerBigPitIndex = if (currentPlayer == Board.PLAYER_1) Board.PLAYER_1_BIG_PIT_INDEX else Board.PLAYER_2_BIG_PIT_INDEX
        board.pits = board.pits.toMutableList().apply {
            this[currentPlayerBigPitIndex] += this[oppositePitIndex] + this[pitIndex]
            this[oppositePitIndex] = 0
            this[pitIndex] = 0
        }
    }

    /**
     * Check if the game is over
     * @return True if the game is over, false otherwise
     */
    fun checkGameOver(board: Board): Boolean {
        val isPlayer1Empty = board.pits.slice(Board.PLAYER_1_PITS).all { it == 0 }
        val isPlayer2Empty = board.pits.slice(Board.PLAYER_2_PITS).all { it == 0 }
        return isPlayer1Empty || isPlayer2Empty
    }

    /**
     * Allocate the remaining stones to the big pit of the losing player
     */
    fun allocateRemainingStones(board: Board) {
        board.pits = board.pits.toMutableList().apply {
            this[Board.PLAYER_1_BIG_PIT_INDEX] += this.slice(Board.PLAYER_1_PITS).sum()
            this[Board.PLAYER_2_BIG_PIT_INDEX] += this.slice(Board.PLAYER_2_PITS).sum()
            for (i in Board.PLAYER_1_PITS) this[i] = 0
            for (i in Board.PLAYER_2_PITS) this[i] = 0
        }
    }

    /**
     * Reset the board to the initial state
     */
    fun resetBoard(board: Board) {
        board.pits = board.pits.toMutableList().apply {
            for (i in Board.PLAYER_1_PITS) this[i] = 6
            for (i in Board.PLAYER_2_PITS) this[i] = 6
            this[Board.PLAYER_1_BIG_PIT_INDEX] = 0
            this[Board.PLAYER_2_BIG_PIT_INDEX] = 0
        }
    }
}
