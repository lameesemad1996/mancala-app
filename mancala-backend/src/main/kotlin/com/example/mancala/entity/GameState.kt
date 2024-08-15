package com.example.mancala.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "game_state")
data class GameState(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @Embedded
    var board: Board = Board(),

    @Column(name = "current_player")
    var currentPlayer: Int = 0,

    @Column(name = "active")
    var active: Boolean = true
) {
    constructor() : this(null) // No-arg constructor for JPA
}
