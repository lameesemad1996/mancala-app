package com.example.mancala.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

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
