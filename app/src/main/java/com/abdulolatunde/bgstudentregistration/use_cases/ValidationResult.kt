package com.abdulolatunde.bgstudentregistration.use_cases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
)
