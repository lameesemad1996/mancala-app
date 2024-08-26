package com.example.mancala.commands

import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.service.BoardService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class MoveStonesCommandTest {

    private lateinit var boardService: BoardService

    @BeforeEach
    fun setUp() {
        boardService = mock(BoardService::class.java)
    }

    @Test
    fun `execute should move stones and update the game state`() {
        val gameId = UUID.randomUUID()
        val initialBoard = Board(pits = mutableListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0))
        val gameState = GameState(gameId, initialBoard, 0, true)

        val expectedBoard = Board(pits = mutableListOf(0, 5, 5, 5, 5, 5, 1, 4, 4, 4, 4, 4, 4, 0))
        `when`(boardService.moveStones(initialBoard, 0, 0)).thenReturn(Pair(5, expectedBoard))

        val command = MoveStonesCommand(boardService, 0)
        val result = command.execute(gameState)

        assertEquals(expectedBoard, result.board)
        assertEquals(5, command.lastPitIndex)
    }
}
