package com.example.mancala.commands

import com.example.mancala.entity.GameState

/**
 * Interface for a game command.
 * Each command encapsulates an action related to the game state and returns a modified game state.
 */
interface GameCommand {
    fun execute(gameState: GameState): GameState
}
