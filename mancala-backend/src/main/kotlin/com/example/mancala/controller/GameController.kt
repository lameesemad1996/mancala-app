package com.example.mancala.controller

import com.example.mancala.model.GameState
import com.example.mancala.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/game")
class GameController(private val gameService: GameService) {

    @GetMapping("/state")
    fun getGameState(): GameState {
        return gameService.getGameState()
    }

    @PostMapping("/move")
    fun makeMove(@RequestParam pitIndex: Int): GameState {
        return gameService.processMove(pitIndex)
    }

    @PostMapping("/reset")
    fun resetGame(): GameState {
        gameService.resetGame()
        return gameService.getGameState()
    }
}