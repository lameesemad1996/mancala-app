package com.example.mancala.commands

import com.example.mancala.entity.GameState
import com.example.mancala.service.BoardService
import org.slf4j.LoggerFactory

/**
 * Command to handle the game over condition.
 * This command checks if the game is over and, if so, finalizes the game state.
 */
class HandleGameOverCommand(
    private val boardService: BoardService
) : GameCommand {

    private val logger = LoggerFactory.getLogger(HandleGameOverCommand::class.java)

    override fun execute(gameState: GameState): GameState {
        val board = gameState.board

        // Check if the game is over
        return if (boardService.checkGameOver(board)) {
            // Allocate remaining stones
            val updatedBoard = boardService.allocateRemainingStones(board)
            logger.info("Game over detected for game ID: ${gameState.id}")

            // Return the updated game state with the game marked as inactive
            gameState.copy(active = false, board = updatedBoard)
        } else {
            gameState
        }
    }
}
