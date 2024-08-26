package com.example.mancala.commands

import com.example.mancala.entity.Board
import com.example.mancala.entity.GameState
import com.example.mancala.service.BoardService
import org.slf4j.LoggerFactory

class CaptureStonesCommand(
    private val boardService: BoardService,
    private val lastPitIndex: Int
) : GameCommand {

    private val logger = LoggerFactory.getLogger(CaptureStonesCommand::class.java)

    override fun execute(gameState: GameState): GameState {
        val board = gameState.board
        val stonesInLastPit = board.pits[lastPitIndex]
        val isPitOnCurrentPlayerSide = Board.isPitOnCurrentPlayerSide(lastPitIndex, gameState.currentPlayer)
        val playersBigPit = if (gameState.currentPlayer == Board.PLAYER_1) {
            Board.PLAYER_1_BIG_PIT_INDEX
        } else {
            Board.PLAYER_2_BIG_PIT_INDEX
        }

        // Check capture condition
        return if (lastPitIndex <= 12 && stonesInLastPit == 1 && isPitOnCurrentPlayerSide && lastPitIndex != playersBigPit) {
            logger.info("Capture condition met for player ${gameState.currentPlayer + 1}, capturing stones at pit $lastPitIndex")
            val updatedBoard = boardService.catchingOpponentsStones(board, lastPitIndex, gameState.currentPlayer)
            gameState.copy(board = updatedBoard)
        } else {
            gameState
        }
    }
}
