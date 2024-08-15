package com.example.mancala.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import jakarta.validation.ConstraintViolationException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * Global exception handler for the application.
 * This class will handle exceptions thrown by the application controllers and return an appropriate response.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(InvalidMoveException::class)
    fun handleInvalidMoveException(ex: InvalidMoveException): ResponseEntity<Map<String, String?>> {
        logger.error("InvalidMoveException: ${ex.message}")
        val errorResponse = mapOf("message" to ex.message)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Map<String, String>> {
        logger.error("Exception: ${ex.message}", ex)
        val errorResponse = mapOf("message" to "An unexpected error occurred.")
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<Map<String, String?>> {
        val errorMessage = ex.constraintViolations.joinToString(", ") { violation ->
            "${violation.propertyPath.toString().split(".").last()}: ${violation.message}"
        }
        val errorResponse = mapOf("message" to errorMessage)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Map<String, String?>> {
        logger.error("MethodArgumentTypeMismatchException: ${ex.message}")
        val errorResponse = mapOf("message" to "Pit index must be an integer")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingServletRequestParameterException(ex: MissingServletRequestParameterException): ResponseEntity<Map<String, String?>> {
        logger.error("MissingServletRequestParameterException: ${ex.message}")
        val errorResponse = mapOf("message" to "Pit index is required")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
