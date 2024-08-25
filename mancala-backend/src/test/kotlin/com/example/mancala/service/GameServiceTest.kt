package com.example.mancala.service

import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.exception.GameNotFoundException
import com.example.mancala.exception.InvalidMoveException
import com.example.mancala.repository.GameStateRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.isA
import java.util.*

class GameServiceTest {

    private lateinit var gameStateRepository: GameStateRepository
    private lateinit var boardService: BoardService
    private lateinit var gameService: GameService

    @BeforeEach
    fun setUp() {
        gameStateRepository = mock(GameStateRepository::class.java)
        boardService = mock(BoardService::class.java)
        gameService = GameService(gameStateRepository, boardService)
    }

    @Test
    fun `createGame should create and save a new game`() {
        val gameStateCaptor = argumentCaptor<GameState>()
        `when`(gameStateRepository.save(any(GameState::class.java))).thenAnswer { it.arguments[0] }

        val gameState = gameService.createGame()

        verify(gameStateRepository).save(gameStateCaptor.capture())
        assertNotNull(gameState.id)
        assertEquals(0, gameState.currentPlayer)
        assertTrue(gameState.active)
        assertNotNull(gameState.board)
    }

    @Test
    fun `getGameState should return game state when game exists`() {
        val gameId = UUID.randomUUID()
        val gameState = GameState(gameId, Board(), 0, true)
        `when`(gameStateRepository.findById(gameId)).thenReturn(Optional.of(gameState))

        val result = gameService.getGameState(gameId)

        assertEquals(gameState, result)
    }

    @Test
    fun `getGameState should throw exception when game does not exist`() {
        val gameId = UUID.randomUUID()
        `when`(gameStateRepository.findById(gameId)).thenReturn(Optional.empty())

        assertThrows<GameNotFoundException> {
            gameService.getGameState(gameId)
        }
    }

    @Test
    fun `processMove should throw exception for empty pit`() {
        val gameId = UUID.randomUUID()
        val board = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        val gameState = GameState(gameId, board, 0, true)
        `when`(gameStateRepository.findById(gameId)).thenReturn(Optional.of(gameState))

        assertThrows<InvalidMoveException> {
            gameService.processMove(gameId, 0)
        }
    }

    @Test
    fun `handleCaptureIfNeeded should capture opponent's stones when conditions are met`() {
        val gameId = UUID.randomUUID()
        val board = Board(pits = mutableListOf(0, 1, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10))
        val gameState = GameState(gameId, board, Board.PLAYER_1, true)

        // Mock the board service to simulate the capturing of stones
        val expectedBoard = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0))
        `when`(boardService.catchingOpponentsStones(isA<Board>(), eq(1), eq(Board.PLAYER_1)))
            .thenReturn(expectedBoard)

        val result = gameService.handleCaptureIfNeeded(gameState, 1)

        assertEquals(expectedBoard, result.board)
        assertEquals(20, result.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])  // Check if stones were captured correctly
    }

    @Test
    fun `handleGameOverIfNeeded should end the game when conditions are met`() {
        val gameId = UUID.randomUUID()
        val board = Board(pits = mutableListOf(0,0,0,0,0,0,48,1,5,3,6,0,0,20))
        val gameState = GameState(gameId, board, Board.PLAYER_1, true)

        `when`(boardService.checkGameOver(isA<Board>())).thenReturn(true)
        `when`(boardService.allocateRemainingStones(isA<Board>())).thenAnswer {
            Board(pits = mutableListOf(0,0,0,0,0,0,48,0,0,0,0,0,0,35))
        }

        val result = gameService.handleGameOverIfNeeded(gameState)

        assertFalse(result.active)
        assertEquals(48, result.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
        assertEquals(35, result.board.pits[Board.PLAYER_2_BIG_PIT_INDEX])
    }

    @Test
    fun `resetGame should reset the board and game state`() {
        val gameId = UUID.randomUUID()
        val gameState = GameState(gameId, Board(), 1, false)
        `when`(gameStateRepository.findById(gameId)).thenReturn(Optional.of(gameState))
        `when`(gameStateRepository.save(any(GameState::class.java))).thenAnswer { it.arguments[0] }
        `when`(boardService.resetBoard(gameState.board)).thenReturn(Board())

        val result = gameService.resetGame(gameId)

        verify(boardService).resetBoard(gameState.board)
        assertEquals(0, result.currentPlayer)
        assertTrue(result.active)
    }
}