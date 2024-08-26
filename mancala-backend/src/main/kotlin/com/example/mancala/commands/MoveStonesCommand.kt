package com.example.mancala.commands

import com.example.mancala.entity.GameState
import com.example.mancala.service.BoardService

/**
 * Command to move stones from a specific pit.
 * This command returns a new game state with the updated board and the last pit index where a stone was placed.
 */
class MoveStonesCommand(
    private val boardService: BoardService,
    private val pitIndex: Int
) : GameCommand {

    var lastPitIndex: Int = -1

    override fun execute(gameState: GameState): GameState {
        val board = gameState.board
        // Move stones and get the last pit index and updated board
        val (lastIndex, updatedBoard) = boardService.moveStones(board, pitIndex, gameState.currentPlayer)
        lastPitIndex = lastIndex
        // Return a new GameState with the updated board
        return gameState.copy(board = updatedBoard)
    }
}
