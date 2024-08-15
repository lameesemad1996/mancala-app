package com.example.mancala.service

import com.example.mancala.exception.InvalidMoveException
import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.repository.GameStateRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@SpringBootTest
class GameServiceTest {

    // Mock the GameStateRepository to avoid actual database operations
    @MockBean
    private lateinit var gameStateRepository: GameStateRepository

    private lateinit var gameService: GameService
    private lateinit var gameId: UUID

    @BeforeEach
    fun setup() {
        gameService = GameService(gameStateRepository)
        gameId = UUID.randomUUID()
        val initialGameState = GameState(id = gameId)
        Mockito.`when`(gameStateRepository.findById(gameId)).thenReturn(Optional.of(initialGameState))
        Mockito.`when`(gameStateRepository.save(Mockito.any(GameState::class.java))).thenAnswer { invocation -> invocation.getArgument(0) } // Return the saved GameState object when GameStateRepository.save is called
    }

    @Test
    fun `should create a new game`() {
        val gameState = gameService.createGame()

        // Verify the game state was created correctly
        assertNotNull(gameState.id)
        assertEquals(0, gameState.currentPlayer)
        assertEquals(14, gameState.board.pits.size)
        assertTrue(gameState.board.pits.slice(Board.PLAYER_1_PITS).all { it == 6 })
        assertTrue(gameState.board.pits.slice(Board.PLAYER_2_PITS).all { it == 6 })
        assertEquals(0, gameState.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
        assertEquals(0, gameState.board.pits[Board.PLAYER_2_BIG_PIT_INDEX])
    }

    @Test
    fun `should process move correctly`() {
        val stateAfterMove = gameService.processMove(gameId, 0)

        // Check that the first pit is now empty
        assertEquals(0, stateAfterMove.board.pits[0])

        // Check that the stones from the first pit have been distributed correctly
        assertEquals(7, stateAfterMove.board.pits[1])
        assertEquals(7, stateAfterMove.board.pits[2])

        // Ensure that the current player is still Player 1
        assertEquals(0, stateAfterMove.currentPlayer)

        // Ensure that score has changed
        assertEquals(1, stateAfterMove.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
    }

    @Test
    fun `should reset game state to initial state`() {
        gameService.processMove(gameId, 0)
        val stateAfterMove = gameService.getGameState(gameId)

        assertEquals(0, stateAfterMove.board.pits[0]) // After move, pit 0 should be empty

        gameService.resetGame(gameId)
        val resetGameState = gameService.getGameState(gameId)

        // Verify the reset state
        assertEquals(6, resetGameState.board.pits[0])
        assertTrue(resetGameState.board.pits.slice(Board.PLAYER_1_PITS).all { it == 6 })
        assertTrue(resetGameState.board.pits.slice(Board.PLAYER_2_PITS).all { it == 6 })
    }

    @Test
    fun `should throw exception for invalid move`() {
        assertThrows(InvalidMoveException::class.java) {
            gameService.processMove(gameId, 13)
        }
    }

    @Test
    fun `should capture stones correctly`() {
        gameService.resetGame(gameId)
        gameService.processMove(gameId, 2)  // Player 1's move
        gameService.processMove(gameId, 7)  // Player 2's move
        val stateAfterCapture = gameService.processMove(gameId, 5)  // Player 1's capture

        // Verify that Player 1's pit 5 is empty and stones are captured
        assertEquals(0, stateAfterCapture.board.pits[5])
        // Check that Player 1's big pit has increased by the captured stones
        assertTrue(stateAfterCapture.board.pits[Board.PLAYER_1_BIG_PIT_INDEX] > 0)
    }

    @Test
    fun `should detect game over condition and calculate final scores`() {
        // Configure the board to simulate the desired game state
        val board = Board().apply {
            pits = listOf(
                1, 2, 0, 0, 3, 6, 25,  // Player 1 pits and big pit
                0, 0, 0, 0, 0, 0, 20   // Player 2 pits and big pit
            )
        }
        val gameState = GameState(id = gameId, board = board, currentPlayer = 0, active = true)
        Mockito.`when`(gameStateRepository.findById(gameId)).thenReturn(Optional.of(gameState))

        gameService.processMove(gameId, 0)  // Trigger game over
        val state = gameService.getGameState(gameId)

        // Assertions
        assertTrue(state.board.pits.slice(0..5).all { it == 0 })  // Player 1 pits should be empty
        assertEquals(37, state.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])  // 25 + (1 + 2 + 3 + 6) = 37
        assertEquals(20, state.board.pits[Board.PLAYER_2_BIG_PIT_INDEX])  // 20 stones in Player 2's big pit
        assertFalse(state.active)  // The game should be marked as inactive
    }

    @Test
    fun `should grant extra turn if last stone lands in big pit`() {
        gameService.resetGame(gameId)

        gameService.processMove(gameId, 0)  // Player 1's move
        val gameState = gameService.getGameState(gameId)

        // Assertions
        assertEquals(0, gameState.currentPlayer)  // Player 1 should get another turn
        assertEquals(1, gameState.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])  // 1 stone in Player 1's big pit
    }

    @Test
    fun `should throw exception for move on opponent's pit`() {
        assertThrows(InvalidMoveException::class.java) {
            gameService.processMove(gameId, 8)  // Attempt to move from Player 2's pit during Player 1's turn
        }
    }
}
