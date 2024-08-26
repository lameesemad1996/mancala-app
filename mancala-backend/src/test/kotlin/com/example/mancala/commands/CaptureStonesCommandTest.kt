package com.example.mancala.commands

import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.service.BoardService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.isA
import java.util.*

class CaptureStonesCommandTest {

    private lateinit var boardService: BoardService

    @BeforeEach
    fun setUp() {
        boardService = mock(BoardService::class.java)
    }

    @Test
    fun `execute should capture opponent's stones when conditions are met`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(0, 1, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10))
        val gameState = GameState(gameId, initialBoard, 0, true)

        val expectedBoard = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0))
        `when`(boardService.catchingOpponentsStones(initialBoard, 1, 0)).thenReturn(expectedBoard)

        val command = CaptureStonesCommand(boardService, 1)
        val result = command.execute(gameState)

        assertEquals(expectedBoard, result.board)
        assertEquals(20, result.board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
    }

    @Test
    fun `execute should not capture stones if conditions are not met`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(0, 2, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10))
        val gameState = GameState(gameId, initialBoard, 0, true)

        // In this case, capture conditions are not met, so the board should not change
        val command = CaptureStonesCommand(boardService, 1)
        val result = command.execute(gameState)

        verify(boardService, never()).catchingOpponentsStones(isA<Board>(), anyInt(), anyInt())
        // Board should remain unchanged
        assertEquals(initialBoard, result.board)
    }

    @Test
    fun `execute should not capture stones if lastPitIndex is a big pit`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 10, 10, 10, 10))
        val gameState = GameState(gameId, initialBoard, 0, true)

        val command = CaptureStonesCommand(boardService, 6)
        val result = command.execute(gameState)

        verify(boardService, never()).catchingOpponentsStones(isA<Board>(), anyInt(), anyInt())
        // Board should remain unchanged
        assertEquals(initialBoard, result.board)
    }

    @Test
    fun `execute should not capture stones if the last stone is not in an empty pit`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(0, 2, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 10))
        val gameState = GameState(gameId, initialBoard, 0, true)

        // Index 1 has more than 1 stone
        val command = CaptureStonesCommand(boardService, 1)
        val result = command.execute(gameState)

        verify(boardService, never()).catchingOpponentsStones(isA<Board>(), anyInt(), anyInt())
        // Board should remain unchanged
        assertEquals(initialBoard, result.board)
    }

    @Test
    fun `execute should not capture stones if the pit is not on the current player's side`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(0, 1, 0, 0, 0, 0, 10, 0, 1, 0, 0, 0, 0, 10))
        val gameState = GameState(gameId, initialBoard, 0, true)

        // Pit 8 is on PLAYER_2's side, so no capture should happen
        val command = CaptureStonesCommand(boardService, 8)
        val result = command.execute(gameState)

        verify(boardService, never()).catchingOpponentsStones(isA<Board>(), anyInt(), anyInt())
        // Board should remain unchanged
        assertEquals(initialBoard, result.board)
    }
}
