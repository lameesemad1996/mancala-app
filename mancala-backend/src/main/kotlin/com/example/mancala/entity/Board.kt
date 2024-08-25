package com.example.mancala.entity

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType

/**
 * The Board class represents the state of the game board.
 * It is embedded within the GameState entity and stored in the database.
 */
@Embeddable
data class Board(

    /**
     * Pits array representing the stones in each pit.
     * The array is stored as a list in the database using JPA's ElementCollection.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    var pits: List<Int> = List(14) { if (it == 6 || it == 13) 0 else 6 }
) {
    companion object {
        const val PLAYER_1 = 0
        const val PLAYER_2 = 1
        val PLAYER_1_SMALL_PITS = 0..5
        val PLAYER_2_SMALL_PITS = 7..12
        const val PLAYER_1_BIG_PIT_INDEX = 6
        const val PLAYER_2_BIG_PIT_INDEX = 13

        /**
         * Check if the pit is on the current player's side
         * @param pitIndex The index of the pit
         * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
         * @return True if the pit is on the current player's side, false otherwise
         */
        fun isPitOnCurrentPlayerSide(pitIndex: Int, currentPlayer: Int): Boolean {
            return pitIndex / 7 == currentPlayer
        }

        /**
         * Check if the last stone lands in the big pit of the current player
         * @param lastPitIndex The index of the pit where the last stone was sown
         * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
         * @return True if the last stone lands in the big pit of the current player, false otherwise
         */
        fun isLastStoneInBigPit(lastPitIndex: Int, currentPlayer: Int): Boolean {
            return (currentPlayer == PLAYER_1 && lastPitIndex == PLAYER_1_BIG_PIT_INDEX) ||
                    (currentPlayer == PLAYER_2 && lastPitIndex == PLAYER_2_BIG_PIT_INDEX)
        }
    }
}
