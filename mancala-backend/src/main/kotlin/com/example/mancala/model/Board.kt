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
            return pitIndex / 6 == currentPlayer
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

    fun captureStones(pitIndex: Int, currentPlayer: Int) {
        val oppositePitIndex = 12 - pitIndex
        val currentPlayerBigPitIndex = if (currentPlayer == PLAYER_1) PLAYER_1_BIG_PIT_INDEX else PLAYER_2_BIG_PIT_INDEX
        pits[currentPlayerBigPitIndex] += pits[oppositePitIndex] + pits[pitIndex]
        pits[oppositePitIndex] = 0
        pits[pitIndex] = 0
    }

    fun isGameOver(): Boolean {
        val isPlayer1Empty = pits.slice(PLAYER_1_PITS).all { it == 0 }
        val isPlayer2Empty = pits.slice(PLAYER_2_PITS).all { it == 0 }
        return isPlayer1Empty || isPlayer2Empty
    }

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
}
