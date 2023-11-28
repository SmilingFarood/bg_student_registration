package com.abdulolatunde.bgstudentregistration.use_cases

class ValidateLastname {
    fun execute(lastname: String): ValidationResult {
        return if (lastname.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "Student should have a Last Name"
            )
        } else {
            ValidationResult(
                successful = true,
                errorMessage = null,
            )
        }
    }
}