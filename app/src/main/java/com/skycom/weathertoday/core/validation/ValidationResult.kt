package com.skycom.weathertoday.core.validation

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val reason: ValidationReason) : ValidationResult
}

enum class ValidationReason {
    TOO_SHORT,
    INVALID_CHARACTERS,
}