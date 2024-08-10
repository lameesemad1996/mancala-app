package com.example.mancala.model

data class GameState(
    val pits: Array<Int>,
    val bigPits: Array<Int>,
    val currentPlayer: Int,
    val scores: Array<Int>
)
