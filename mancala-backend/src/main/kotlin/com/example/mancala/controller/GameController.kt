package com.example.mancala.controller

import com.example.mancala.model.GameState
import com.example.mancala.service.GameService
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Min
import javax.validation.constraints.Max

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = ["http://localhost:3000"])
class GameController(private val gameService: GameService) {

    @GetMapping("/state")
    fun getGameState(): GameState {
        return gameService.getGameState()
    }

    @PostMapping("/move")
    fun makeMove(@RequestParam @Min(0) @Max(12) pitIndex: Int): GameState {
        return gameService.processMove(pitIndex)
    }

    @PostMapping("/reset")
    fun resetGame(): GameState {
        gameService.resetGame()
        return gameService.getGameState()
    }
}