package com.example.mancala.controller

import com.example.mancala.entity.GameState
import com.example.mancala.service.GameService
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = ["http://localhost:3000"])
@Validated
class GameController(private val gameService: GameService) {

    @PostMapping("/create")
    fun createGame(): GameState {
        return gameService.createGame()
    }

    @GetMapping("/state/{gameId}")
    fun getGameState(@PathVariable gameId: UUID): GameState {
        return gameService.getGameState(gameId)
    }

    @PostMapping("/move/{gameId}")
    fun makeMove(
        @PathVariable gameId: UUID,
        @RequestParam @Min(0, message = "Invalid pit index") @Max(12, message = "Invalid pit index") pitIndex: Int
    ): GameState {
        return gameService.processMove(gameId, pitIndex)
    }

    @PostMapping("/reset/{gameId}")
    fun resetGame(@PathVariable gameId: UUID): GameState {
        return gameService.resetGame(gameId)
    }
}
