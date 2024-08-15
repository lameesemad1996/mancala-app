package com.example.mancala

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main entry point for the Mancala application.
 */
@SpringBootApplication
class MancalaApplication

fun main(args: Array<String>) {
    runApplication<MancalaApplication>(*args)
}
