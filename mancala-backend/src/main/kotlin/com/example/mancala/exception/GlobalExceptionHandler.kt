package com.example.mancala.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Global exception handler for the application.
 * This class will handle exceptions thrown by the application controllers and return an appropriate response.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GameOverException::class)
    fun handleGameOverException(ex: GameOverException): ResponseEntity<Map<String, String?>> {
        val errorResponse = mapOf("error" to ex.message)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidMoveException::class)
    fun handleInvalidMoveException(ex: InvalidMoveException): ResponseEntity<Map<String, String?>> {
        val errorResponse = mapOf("error" to ex.message)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Map<String, String>> {
        val errorResponse = mapOf("error" to "An unexpected error occurred.")
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
