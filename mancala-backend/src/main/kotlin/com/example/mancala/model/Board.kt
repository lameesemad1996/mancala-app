package com.example.mancala.model

data class Board(
    var pits: Array<Int> = Array(14) { if (it == 6 || it == 13) 0 else 6 }
) {
    companion object {
        const val PLAYER_1 = 0
        const val PLAYER_2 = 1
        val PLAYER_1_PITS = 0..5
        val PLAYER_2_PITS = 7..12
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

    /**
     * Move the stones from the pit in a counter-clockwise direction and return the index of the last pit where a stone was sown
     * @param pitIndex The index of the pit
     * @return The number of stones in the pit
     */
    fun moveStones(startIndex: Int, currentPlayer: Int): Int {
        var index = startIndex
        var stones = pits[startIndex]
        pits[startIndex] = 0

        while (stones > 0) {
            index = (index + 1) % 14

            // Skip opponent's big pit
            if ((currentPlayer == PLAYER_1 && index == PLAYER_2_BIG_PIT_INDEX) ||
                (currentPlayer == PLAYER_2 && index == PLAYER_1_BIG_PIT_INDEX)) {
                continue
            }

            pits[index]++
            stones--
        }

        return index
    }

    /**
     * Capture the stones in the opposite pit and the current pit and add them to the current player's big pit
     * @param pitIndex The index of the pit
     * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
     */
    fun catchingOpponentsStones(pitIndex: Int, currentPlayer: Int) {
        val oppositePitIndex = 12 - pitIndex
        val currentPlayerBigPitIndex = if (currentPlayer == PLAYER_1) PLAYER_1_BIG_PIT_INDEX else PLAYER_2_BIG_PIT_INDEX
        pits[currentPlayerBigPitIndex] += pits[oppositePitIndex] + pits[pitIndex]
        pits[oppositePitIndex] = 0
        pits[pitIndex] = 0
    }

    /**
     * Check if the game is over
     * @param lastPitIndex The index of the last pit where a stone was sown
     * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
     * @return True if the game is over, false otherwise
     */
    fun isGameOver(): Boolean {
        val isPlayer1Empty = pits.slice(PLAYER_1_PITS).all { it == 0 }
        val isPlayer2Empty = pits.slice(PLAYER_2_PITS).all { it == 0 }
        return isPlayer1Empty || isPlayer2Empty
    }

    /**
     * Allocate the remaining stones to the big pit of the losing player
     */
    fun allocateRemainingStones() {
        pits[PLAYER_1_BIG_PIT_INDEX] += pits.slice(PLAYER_1_PITS).sum()
        pits[PLAYER_2_BIG_PIT_INDEX] += pits.slice(PLAYER_2_PITS).sum()
        for (i in PLAYER_1_PITS) pits[i] = 0
        for (i in PLAYER_2_PITS) pits[i] = 0
    }

    fun resetBoard() {
        for (i in PLAYER_1_PITS) pits[i] = 6
        for (i in PLAYER_2_PITS) pits[i] = 6
        pits[PLAYER_1_BIG_PIT_INDEX] = 0
        pits[PLAYER_2_BIG_PIT_INDEX] = 0
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Board) return false
        return pits.contentEquals(other.pits)
    }

    override fun hashCode(): Int {
        return pits.contentHashCode()
    }
}
