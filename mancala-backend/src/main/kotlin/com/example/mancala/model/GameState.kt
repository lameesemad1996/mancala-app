package com.example.mancala.model

data class GameState(
    val pits: Array<Int>,
    val currentPlayer: Int,
    val scores: Array<Int>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (!pits.contentEquals(other.pits)) return false
        if (currentPlayer != other.currentPlayer) return false
        if (!scores.contentEquals(other.scores)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pits.contentHashCode()
        result = 31 * result + currentPlayer
        result = 31 * result + scores.contentHashCode()
        return result
    }
}
