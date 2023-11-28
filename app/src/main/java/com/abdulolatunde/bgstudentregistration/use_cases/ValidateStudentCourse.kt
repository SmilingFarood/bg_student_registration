package com.abdulolatunde.bgstudentregistration.use_cases

class ValidateStudentCourse {
    fun execute (studentCourse: String): ValidationResult {
        return if(studentCourse.isBlank()){
            ValidationResult(
                successful = false,
                errorMessage = "Student should have a Course"
            )
        }else{
            ValidationResult(
                successful = true,
                errorMessage = null,
            )
        }
    }
}