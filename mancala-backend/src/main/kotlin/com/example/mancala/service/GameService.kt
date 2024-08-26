package com.example.mancala.service

import com.example.mancala.commands.CaptureStonesCommand
import com.example.mancala.commands.HandleGameOverCommand
import com.example.mancala.commands.MoveStonesCommand
import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.exception.GameNotFoundException
import com.example.mancala.exception.InvalidMoveException
import com.example.mancala.repository.GameStateRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class GameService(
    private val gameStateRepository: GameStateRepository,
    private val boardService: BoardService
) {

    private val logger = LoggerFactory.getLogger(GameService::class.java)

    /**
     * Create a new game
     * @return The initial game state
     */
    fun createGame(): GameState {
        logger.info("Creating a new game...")
        val newGameState = GameState(
            id = UUID.randomUUID(),
            board = Board(),
            currentPlayer = 0,
            active = true
        )
        val savedGameState = gameStateRepository.save(newGameState)
        logger.info("New game created with ID: ${savedGameState.id}")
        return savedGameState
    }

    /**
     * Get the current game state
     * @param gameId The ID of the game
     * @return The current game state
     * @throws GameNotFoundException if the game does not exist
     */
    fun getGameState(gameId: UUID): GameState {
        logger.info("Fetching game state for game ID: $gameId")
        return gameStateRepository.findById(gameId)
            .orElseThrow {
                GameNotFoundException("Game not found")
            }
    }

    /**
     * Process a move in the game
     * @param gameId The ID of the game
     * @param pitIndex The index of the pit to move stones from
     * @return The updated game state
     * @throws InvalidMoveException if the move is invalid
     */
    @Transactional
    fun processMove(gameId: UUID, pitIndex: Int): GameState {
        logger.info("Processing move for game ID: $gameId at pit index: $pitIndex")
        var gameState = getGameState(gameId)
        validateMove(gameState, pitIndex)
        logger.info("Move validated for pit index: $pitIndex")

        // Execute MoveStonesCommand and get the updated GameState
        val moveCommand = MoveStonesCommand(boardService, pitIndex)
        gameState = moveCommand.execute(gameState)
        logger.info("Stones moved, last pit index: ${moveCommand.lastPitIndex}")

        // Execute CaptureStonesCommand using the last pit index from MoveStonesCommand
        val captureCommand = CaptureStonesCommand(boardService, moveCommand.lastPitIndex)
        gameState = captureCommand.execute(gameState)

        // Execute HandleGameOverCommand to check and handle game-over conditions
        val handleGameOverCommand = HandleGameOverCommand(boardService)
        gameState = handleGameOverCommand.execute(gameState)

        // Check if the current player gets another turn
        // Increment the currentPlayer if last stone does not land in the current player's big pit
        if (!Board.isLastStoneInBigPit(moveCommand.lastPitIndex, gameState.currentPlayer)) {
            gameState.currentPlayer = (gameState.currentPlayer + 1) % 2
            logger.info("Next player's turn: Player ${gameState.currentPlayer + 1}")
        }

        val updatedGameState = gameStateRepository.save(gameState)
        logger.info("Game state updated and saved for game ID: $gameId")
        return updatedGameState
    }

    /**
     * Validate the move
     * @param gameState The current game state
     * @param pitIndex The index of the pit to move stones from
     * @throws InvalidMoveException if the move is invalid
     */
    private fun validateMove(gameState: GameState, pitIndex: Int) {
        val board = gameState.board
        if (pitIndex / 7 != gameState.currentPlayer) {
            logger.warn("Invalid move: Player ${gameState.currentPlayer + 1} tried to select an opponent's pit")
            throw InvalidMoveException("Invalid move. You must select a pit on your side with stones.")
        }
        if (board.pits[pitIndex] == 0) {
            logger.warn("Invalid move: Player ${gameState.currentPlayer + 1} selected an empty pit")
            throw InvalidMoveException("Invalid move. You cannot select an empty pit.")
        }
    }

    /**
     * Reset the game
     * @param gameId The ID of the game
     * @return The reset game state
     * @throws GameNotFoundException if the game does not exist
     */
    fun resetGame(gameId: UUID): GameState {
        logger.info("Resetting game with ID: $gameId")
        val gameState = getGameState(gameId)
        val updatedBoard = boardService.resetBoard(gameState.board)
        gameState.currentPlayer = 0
        gameState.active = true
        gameState.board = updatedBoard
        val resetGameState = gameStateRepository.save(gameState)
        logger.info("Game reset complete for game ID: $gameId")
        return resetGameState
    }
}
