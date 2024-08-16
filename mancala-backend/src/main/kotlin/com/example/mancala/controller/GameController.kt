package com.example.mancala.controller

import com.example.mancala.entity.GameState
import com.example.mancala.service.GameService
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = ["http://localhost:3000"])
@Validated
class GameController(private val gameService: GameService) {

    private val logger = LoggerFactory.getLogger(GameController::class.java)

    @PostMapping("/create")
    fun createGame(): GameState {
        logger.info("Received request to create a new game")
        val gameState = gameService.createGame()
        logger.info("New game created with ID: ${gameState.id}")
        return gameState
    }

    @GetMapping("/state/{gameId}")
    fun getGameState(@PathVariable gameId: UUID): GameState {
        logger.info("Received request to get the state of game with ID: $gameId")
        val gameState = gameService.getGameState(gameId)
        logger.info("Returning game state for game ID: $gameId")
        return gameState
    }

    @PostMapping("/move/{gameId}")
    fun makeMove(
        @PathVariable gameId: UUID,
        @RequestParam @Min(0, message = "Invalid pit index") @Max(12, message = "Invalid pit index") pitIndex: Int
    ): GameState {
        logger.info("Received request to make a move in game ID: $gameId at pit index: $pitIndex")
        val gameState = gameService.processMove(gameId, pitIndex)
        logger.info("Move processed for game ID: $gameId, current player is now: ${gameState.currentPlayer}")
        return gameState
    }

    @PostMapping("/reset/{gameId}")
    fun resetGame(@PathVariable gameId: UUID): GameState {
        logger.info("Received request to reset the game with ID: $gameId")
        val gameState = gameService.resetGame(gameId)
        logger.info("Game with ID: $gameId has been reset to initial state")
        return gameState
    }
}
