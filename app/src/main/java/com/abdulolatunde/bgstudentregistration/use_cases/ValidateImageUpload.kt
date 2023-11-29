package com.abdulolatunde.bgstudentregistration.use_cases

import android.graphics.Bitmap

class ValidateImageUpload {
    fun execute(image: Bitmap?): ValidationResult {
        return if (image == null) {
            ValidationResult(
                successful = false,
                errorMessage = "Student should have an image"
            )
        } else {
            ValidationResult(
                successful = true,
                errorMessage = null,
            )
        }
    }
}