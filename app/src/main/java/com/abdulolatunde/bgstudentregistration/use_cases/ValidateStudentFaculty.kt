package com.abdulolatunde.bgstudentregistration.use_cases

class ValidateStudentFaculty {
    fun execute (studentFaculty: String): ValidationResult {
        return if(studentFaculty.isBlank()){
            ValidationResult(
                successful = false,
                errorMessage = "Student should have a First Name"
            )
        }else{
            ValidationResult(
                successful = true,
                errorMessage = null,
            )
        }
    }
}