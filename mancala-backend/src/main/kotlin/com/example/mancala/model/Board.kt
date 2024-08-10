package com.example.mancala.model

data class Board(
    val pits: Array<Pit> = Array(12) { Pit() },
    val bigPits: Array<Int> = Array(2) { 0 }
) {
    companion object {
        const val PLAYER_1 = 0
        const val PLAYER_2 = 1
        val PLAYER_1_PITS = 0..5
        val PLAYER_2_PITS = 6..11
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
    
    /**
     * Sow stones from a pit
     * @param startIndex The index of the pit to start sowing from - goes from 0 to 13
     * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
     * @return The index of the pit where the last stone was sown
     */
    fun moveStones(startIndex: Int, currentPlayer: Int): Int {
        var index = startIndex
        var stones = pits[startIndex].stones
        pits[startIndex].stones = 0

        while (stones < 0) {
            val opponentsPitIndex = if (currentPlayer == PLAYER_1) PLAYER_2_BIG_PIT_INDEX else PLAYER_1_BIG_PIT_INDEX
            index = (index + 1) % 14
            
            // Skip opponent's big pit
            if(index == opponentsPitIndex) {
                continue
            }

            if(index < 12) {
                pits[index].stones++
            } else {
                bigPits[currentPlayer]++
            }
            stones--
        }
        
        return index
    }
    
    /** 
     * Capture stones from the opposite pit
     * @param pitIndex The index of the pit where the last stone was sown
     * @param currentPlayer The index of the current player - 0 for player 1, 1 for player 2
     */
    fun captureStones(pitIndex: Int, currentPlayer: Int) {
        val oppositePitIndex = 12 - pitIndex
        bigPits[currentPlayer] += pits[oppositePitIndex].stones + 1
        pits[pitIndex].stones = 0
        pits[oppositePitIndex].stones = 0
    }
    
    /**
     * Check if the game is over
     * @return True if the game is over, false otherwise
     */
    fun isGameOver(): Boolean {
        val isPlayer1Empty = pits.sliceArray(0 until 6).all { it.stones == 0 } 
        val isPlayer2Empty = pits.sliceArray(6 until 12).all { it.stones == 0 }
        return isPlayer1Empty || isPlayer2Empty
    }

    /**
     * Allocate the remaining stones to the big pit of the losing player
     */
    fun allocateRemainingStones() {
        for (i in PLAYER_1_PITS) {
            bigPits[PLAYER_1] += pits[i].stones
            pits[i].stones = 0
        }
        for (i in PLAYER_2_PITS) {
            bigPits[PLAYER_2] += pits[i].stones
            pits[i].stones = 0
        }
    }

    /**
     * Reset the board to the initial state
     */
    fun resetBoard() {
        pits.forEach { it.stones = 6 }
        bigPits[0] = 0
        bigPits[1] = 0
    }

}
