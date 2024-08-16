package com.example.mancala.service

import com.example.mancala.entity.Board
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*


class BoardServiceTest {

    private val boardService = BoardService()

    @Test
    fun `should move stones correctly`() {
        val board = Board(pits = mutableListOf(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0))
        val lastPitIndex = boardService.moveStones(board, 0, Board.PLAYER_1)

        assertEquals(0, board.pits[0])
        assertEquals(7, board.pits[1])
        assertEquals(7, board.pits[2])
        assertEquals(1, board.pits[6])
        assertEquals(6, lastPitIndex)
    }

    @Test
    fun `should capture opponent's stones correctly`() {
        val board = Board(pits = mutableListOf(0, 6, 1, 0, 6, 0, 5, 6, 6, 6, 6, 0, 1, 0))
        boardService.catchingOpponentsStones(board, 2, Board.PLAYER_1)

        assertEquals(0, board.pits[2])
        assertEquals(0, board.pits[3])
        assertEquals(12, board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
    }

    @Test
    fun `should check if game is over correctly`() {
        val board1 = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 24, 6, 6, 6, 6, 6, 6, 0))
        val board2 = Board(pits = mutableListOf(6, 6, 6, 6, 6, 6, 0, 0, 0, 0, 0, 0, 0, 24))

        assertTrue(boardService.checkGameOver(board1))
        assertTrue(boardService.checkGameOver(board2))

        val board3 = Board(pits = mutableListOf(1, 0, 0, 0, 0, 0, 24, 6, 6, 6, 6, 6, 6, 0))
        assertFalse(boardService.checkGameOver(board3))
    }

    @Test
    fun `should allocate remaining stones correctly`() {
        val board = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 24, 1, 1, 1, 1, 1, 1, 0))
        boardService.allocateRemainingStones(board)

        assertEquals(24, board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
        assertEquals(6, board.pits[Board.PLAYER_2_BIG_PIT_INDEX])
        assertEquals(0, board.pits[1])
        assertEquals(0, board.pits[12])
    }

    @Test
    fun `should reset the board correctly`() {
        val board = Board(pits = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        boardService.resetBoard(board)

        assertTrue(board.pits.slice(Board.PLAYER_1_PITS).all { it == 6 })
        assertTrue(board.pits.slice(Board.PLAYER_2_PITS).all { it == 6 })
        assertEquals(0, board.pits[Board.PLAYER_1_BIG_PIT_INDEX])
        assertEquals(0, board.pits[Board.PLAYER_2_BIG_PIT_INDEX])
    }
}
