package com.example.mancala.repository

import com.example.mancala.entity.GameState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameStateRepository : JpaRepository<GameState, UUID>
