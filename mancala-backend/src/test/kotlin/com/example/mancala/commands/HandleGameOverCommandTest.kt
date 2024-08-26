package com.example.mancala.commands

import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.service.BoardService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class HandleGameOverCommandTest {

    private lateinit var boardService: BoardService

    @BeforeEach
    fun setUp() {
        boardService = mock(BoardService::class.java)
    }

    @Test
    fun `execute should end the game when conditions are met`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 48, 1, 5, 3, 6, 0, 0, 20))
        val gameState = GameState(gameId, initialBoard, Board.PLAYER_1, true)

        val expectedBoard = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 48, 0, 0, 0, 0, 0, 0, 35))
        `when`(boardService.checkGameOver(initialBoard)).thenReturn(true)
        `when`(boardService.allocateRemainingStones(initialBoard)).thenReturn(expectedBoard)

        val command = HandleGameOverCommand(boardService)
        val result = command.execute(gameState)

        assertFalse(result.active)
        assertEquals(expectedBoard, result.board)
        assertEquals(48, result.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
        assertEquals(35, result.board.pits[Board.PLAYER_2_BIG_PIT_INDEX])
    }

    @Test
    fun `execute should not end the game if conditions are not met`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 48, 1, 5, 3, 6, 0, 0, 20))
        val gameState = GameState(gameId, initialBoard, Board.PLAYER_1, true)

        `when`(boardService.checkGameOver(initialBoard)).thenReturn(false)

        val command = HandleGameOverCommand(boardService)
        val result = command.execute(gameState)

        assertEquals(initialBoard, result.board)
        assertEquals(gameState.active, result.active)  // Game should remain active
    }
}
