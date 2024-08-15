package com.example.mancala.controller

import com.example.mancala.model.GameState
import com.example.mancala.service.GameService
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/game")
@CrossOrigin(origins = ["http://localhost:3000"])
@Validated
class GameController(private val gameService: GameService) {

    @GetMapping("/state")
    fun getGameState(): GameState {
        return gameService.getGameState()
    }

    @PostMapping("/move")
    fun makeMove(@RequestParam @Min(0, message = "Invalid pit index") @Max(12, message = "Invalid pit index") pitIndex: Int): GameState {
        return gameService.processMove(pitIndex)
    }

    @PostMapping("/reset")
    fun resetGame(): GameState {
        gameService.resetGame()
        return gameService.getGameState()
    }
}