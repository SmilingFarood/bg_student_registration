package com.abdulolatunde.bgstudentregistration.use_cases

class ValidateFirstname {
    fun execute (firstname: String): ValidationResult {
        return if(firstname.isBlank()){
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