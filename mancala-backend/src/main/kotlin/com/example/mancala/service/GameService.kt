package com.example.mancala.service

import com.example.mancala.exception.InvalidMoveException
import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.repository.GameStateRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(private val gameStateRepository: GameStateRepository, private val boardService: BoardService) {

    /**
     * Create a new game
     * @return The initial game state
     */
    fun createGame(): GameState {
        val newGameState = GameState(
            id = UUID.randomUUID(),
            board = Board(),
            currentPlayer = 0,
            active = true
        )
        return gameStateRepository.save(newGameState)
    }

    /**
     * Get the current game state
     * @param gameId The ID of the game
     * @return The current game state
     */
    fun getGameState(gameId: UUID): GameState {
        return gameStateRepository.findById(gameId)
            .orElseThrow { IllegalArgumentException("Game not found") }
    }

    /**
     * Process a move in the game
     * @param gameId The ID of the game
     * @param pitIndex The index of the pit to move stones from
     * @return The updated game state
     */
    fun processMove(gameId: UUID, pitIndex: Int): GameState {
        val gameState = getGameState(gameId)
        val board = gameState.board

        validateMove(gameState, pitIndex)

        val lastPitIndex = boardService.moveStones(board, pitIndex, gameState.currentPlayer)

        handleCaptureIfNeeded(gameState, lastPitIndex)
        handleGameOverIfNeeded(gameState)

        // Check if the current player gets another turn
        // Increment the currentPlayer if last stone does not land in the current player's big pit
        if (!Board.isLastStoneInBigPit(lastPitIndex, gameState.currentPlayer)) {
            gameState.currentPlayer = (gameState.currentPlayer + 1) % 2
        }

        return gameStateRepository.save(gameState)
    }

    /**
     * Validate the move
     * @param gameState The current game state
     * @param pitIndex The index of the pit to move stones from
     */
    private fun validateMove(gameState: GameState, pitIndex: Int) {
        val board = gameState.board
        if (pitIndex < 0 || pitIndex >= board.pits.size || pitIndex / 7 != gameState.currentPlayer) {
            throw InvalidMoveException("Invalid move. You must select a pit on your side with stones.")
        }
        if (board.pits[pitIndex] == 0) {
            throw InvalidMoveException("Invalid move. You cannot select an empty pit.")
        }
    }

    /**
     * Handle capturing stones from the opponent
     * @param gameState The current game state
     * @param lastPitIndex The index of the last pit where a stone was sown
     */
    private fun handleCaptureIfNeeded(gameState: GameState, lastPitIndex: Int) {
        val board = gameState.board
        val stonesInLastPit = board.pits[lastPitIndex]
        val isPitOnCurrentPlayerSide = Board.isPitOnCurrentPlayerSide(lastPitIndex, gameState.currentPlayer)
        val playersBigPit = if (gameState.currentPlayer == Board.PLAYER_1) Board.PLAYER_1_BIG_PIT_INDEX else Board.PLAYER_2_BIG_PIT_INDEX

        // Check capture condition
        if (lastPitIndex <= 12 && stonesInLastPit == 1 && isPitOnCurrentPlayerSide && lastPitIndex != playersBigPit) {
            boardService.catchingOpponentsStones(board, lastPitIndex, gameState.currentPlayer)
        }
    }

    /**
     * Handle game over condition
     * @param gameState The current game state
     */
    private fun handleGameOverIfNeeded(gameState: GameState) {
        val board = gameState.board
        if (boardService.checkGameOver(board)) {
            gameState.active = false
            boardService.allocateRemainingStones(board)
        }
    }

    /**
     * Reset the game
     * @param gameId The ID of the game
     * @return The reset game state
     */
    fun resetGame(gameId: UUID): GameState {
        val gameState = getGameState(gameId)
        boardService.resetBoard(gameState.board)
        gameState.currentPlayer = 0
        gameState.active = true
        return gameStateRepository.save(gameState)
    }
}
