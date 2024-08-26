package com.example.mancala.service

import com.example.mancala.entity.Board
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BoardService {

    private val logger = LoggerFactory.getLogger(BoardService::class.java)

    /**
     * Moves the stones from the specified pit in a counter-clockwise direction
     * and returns a pair containing the index of the last pit where a stone was sown
     * and the updated game board.
     *
     * @param board The game board.
     * @param startIndex The index of the pit from which stones are picked up.
     * @param currentPlayer The index of the current player (0 for player 1, 1 for player 2).
     * @return A Pair containing the index of the last pit where a stone was placed and the updated Board.
     */
    fun moveStones(board: Board, startIndex: Int, currentPlayer: Int): Pair<Int, Board> {
        logger.info("Moving stones from pit $startIndex for player $currentPlayer")

        var index = startIndex
        var stones = board.pits[startIndex]
        val newPits = board.pits.toMutableList().apply { this[startIndex] = 0 }

        while (stones > 0) {
            index = (index + 1) % 14

            // Skip opponent's big pit
            if ((currentPlayer == Board.PLAYER_1 && index == Board.PLAYER_2_BIG_PIT_INDEX) ||
                (currentPlayer == Board.PLAYER_2 && index == Board.PLAYER_1_BIG_PIT_INDEX)) {
                continue
            }

            newPits[index]++
            stones--
        }

        logger.info("Last stone placed in pit $index")
        return Pair(index, board.copy(pits = newPits))
    }

    /**
     * Capture the stones in the opposite pit and the current pit and add them to the current player's big pit
     * @param pitIndex The index of the pit
     * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
     */
    fun catchingOpponentsStones(board: Board, pitIndex: Int, currentPlayer: Int): Board {
        logger.info("Capturing stones for player $currentPlayer from pit $pitIndex")

        val oppositePitIndex = 12 - pitIndex
        val currentPlayerBigPitIndex = if (currentPlayer == Board.PLAYER_1) Board.PLAYER_1_BIG_PIT_INDEX else Board.PLAYER_2_BIG_PIT_INDEX
        val newPits = board.pits.toMutableList().apply {
            this[currentPlayerBigPitIndex] += this[oppositePitIndex] + this[pitIndex]
            this[oppositePitIndex] = 0
            this[pitIndex] = 0
        }

        logger.info("Stones captured. Player $currentPlayer's big pit now has ${newPits[currentPlayerBigPitIndex]} stones")
        return board.copy(pits = newPits)
    }

    /**
     * Check if the game is over
     * @return True if the game is over, false otherwise
     */
    fun checkGameOver(board: Board): Boolean {
        val isPlayer1Empty = board.pits.slice(Board.PLAYER_1_SMALL_PITS).all { it == 0 }
        val isPlayer2Empty = board.pits.slice(Board.PLAYER_2_SMALL_PITS).all { it == 0 }

        val gameOver = isPlayer1Empty || isPlayer2Empty
        logger.info("Checking if game is over: $gameOver")

        return gameOver
    }

    /**
     * Allocate the remaining stones to the big pit of the losing player
     */
    fun allocateRemainingStones(board: Board): Board {
        logger.info("Allocating remaining stones to the big pits")

        val newPits = board.pits.toMutableList().apply {
            // Sum up all the stones in the small pits and add them to the big pits
            this[Board.PLAYER_1_BIG_PIT_INDEX] += this.slice(Board.PLAYER_1_SMALL_PITS).sum()
            this[Board.PLAYER_2_BIG_PIT_INDEX] += this.slice(Board.PLAYER_2_SMALL_PITS).sum()
            // Clear all small pits
            for (i in Board.PLAYER_1_SMALL_PITS) {
                this[i] = 0
            }
            for (i in Board.PLAYER_2_SMALL_PITS) {
                this[i] = 0
            }
        }

        logger.info("Stones allocated. Player 1 big pit: ${newPits[Board.PLAYER_1_BIG_PIT_INDEX]}, Player 2 big pit: ${newPits[Board.PLAYER_2_BIG_PIT_INDEX]}")
        return board.copy(pits = newPits)
    }

    /**
     * Reset the board to the initial state
     */
    fun resetBoard(board: Board): Board {
        logger.info("Resetting the board to the initial state")

        val newPits = board.pits.toMutableList().apply {
            // Set the initial number of stones in each pit of the small pits
            for (i in Board.PLAYER_1_SMALL_PITS) {
                this[i] = 6
            }
            for (i in Board.PLAYER_2_SMALL_PITS) {
                this[i] = 6
            }
            // Set the big pits to 0
            this[Board.PLAYER_1_BIG_PIT_INDEX] = 0
            this[Board.PLAYER_2_BIG_PIT_INDEX] = 0
        }

        logger.info("Board reset completed")
        return board.copy(pits = newPits)
    }
}
