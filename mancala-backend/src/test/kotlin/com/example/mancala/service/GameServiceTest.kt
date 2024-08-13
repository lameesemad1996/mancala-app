package com.example.mancala.service

import com.example.mancala.exception.InvalidMoveException
import com.example.mancala.model.Board
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

class GameServiceTest {
    private lateinit var gameService: GameService

    @BeforeEach
    fun setup() {
        gameService = GameService()
        gameService.resetGame()
    }
    @Test
    fun `should initialize game state correctly`() {
        val initialGameState = gameService.getGameState()
        val stateAfterMove = gameService.processMove(0)

        assertEquals(0, stateAfterMove.pits[0]) // Pit 0 should be empty after the move
    }

    @Test
    fun `should process move correctly`() {
        val stateAfterMove = gameService.processMove(0)

        // Check that the first pit is now empty
        assertEquals(0, stateAfterMove.pits[0])

        // Check that the stones from the first pit have been distributed correctly
        assertEquals(7, stateAfterMove.pits[1])
        assertEquals(7, stateAfterMove.pits[2])

        // Ensure that the current player is still Player 1
        assertEquals(0, stateAfterMove.currentPlayer)

        // Ensure that score has changed
        assertEquals(1, stateAfterMove.scores[0])
    }


    @Test
    fun `should reset game state to initial state`() {
        val initialGameState = gameService.getGameState()

        gameService.processMove(0)
        val stateAfterMove = gameService.getGameState()
        assertEquals(initialGameState.pits[0], 0)

        gameService.resetGame()
        val resetGameState = gameService.getGameState()
        assertEquals(resetGameState.pits[0], 6)
    }

    @Test
    fun `should throw exception for invalid move`() {
        assertThrows(InvalidMoveException::class.java) {
            gameService.processMove(13)
        }
    }

    @Test
    fun `should capture stones correctly`() {
        gameService.resetGame()
        gameService.processMove(2)  // Player 1's move
        gameService.processMove(7)  // Player 2's move
        val stateAfterCapture = gameService.processMove(5)  // Player 1's capture

        assertEquals(0, stateAfterCapture.pits[5])
        assertTrue(stateAfterCapture.pits[Board.PLAYER_1_BIG_PIT_INDEX] > 0)
    }

    @Test
    fun `should detect game over condition and calculate final scores`() {
        // Configure the board to simulate the desired game state
        val board = Board().apply {
            pits = arrayOf(
                1, 2, 0, 0, 3, 6, 25,  // Player 1 pits and big pit
                0, 0, 0, 0, 0, 0, 20   // Player 2 pits and big pit
            )
        }

        // Pass the configured board to the GameService
        val gameService = GameService(board)

        gameService.processMove(0)  // Trigger game over
        val state = gameService.getGameState()

        // Assertions
        assertTrue(state.pits.slice(0..5).all { it == 0 })  // Player 1 pits should be empty
        assertEquals(37, state.pits[Board.PLAYER_1_BIG_PIT_INDEX])  // 25 + (1 + 2 + 3 + 6) = 37
        assertEquals(20, state.pits[Board.PLAYER_2_BIG_PIT_INDEX])  // 20 stones in Player 2's big pit
    }

    @Test
    fun `should grant extra turn if last stone lands in big pit`() {
        gameService.resetGame()

        gameService.processMove(0)  // Player 1's move
        val gameState = gameService.getGameState()

        assertEquals(0, gameState.currentPlayer)  // Player 1 should get another turn
        assertEquals(1, gameState.pits[Board.PLAYER_1_BIG_PIT_INDEX])  // 1 stone in Player 1's big pit
    }

    @Test
    fun `should throw exception for move on opponent's pit`() {
        assertThrows(InvalidMoveException::class.java) {
            gameService.processMove(8)  // Attempt to move from Player 2's pit during Player 1's turn
        }
    }

}