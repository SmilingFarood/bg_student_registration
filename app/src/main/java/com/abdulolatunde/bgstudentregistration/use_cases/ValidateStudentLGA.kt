package com.abdulolatunde.bgstudentregistration.use_cases

class ValidateStudentLGA {
    fun execute (studentLGA: String): ValidationResult {
        return if(studentLGA.isBlank()){
            ValidationResult(
                successful = false,
                errorMessage = "Student should have a LGA"
            )
        }else{
            ValidationResult(
                successful = true,
                errorMessage = null,
            )
        }
    }
}