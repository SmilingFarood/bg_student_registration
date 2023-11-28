package com.abdulolatunde.bgstudentregistration.use_cases

class ValidateStudentStateOfOrg {
    fun execute (stateOfOrg: String): ValidationResult {
        return if(stateOfOrg.isBlank()){
            ValidationResult(
                successful = false,
                errorMessage = "Student should have a State Of Origin"
            )
        }else{
            ValidationResult(
                successful = true,
                errorMessage = null,
            )
        }
    }
}