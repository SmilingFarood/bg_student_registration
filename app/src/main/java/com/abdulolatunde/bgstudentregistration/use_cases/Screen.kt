package com.abdulolatunde.bgstudentregistration.use_cases

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login-screen")
    object StudentListScreen : Screen("student-list")
    object StudentInfoScreen : Screen("student-info-screen/{id}") {
        fun navigateWithArgument(id: Int): String {
            return this.route.replace("{id}", "$id")
        }
    }

    object AddStudentScreen : Screen("add-student")
    object TakePictureScreen : Screen("take-picture-screen")

    object AnalyticsScreen : Screen("analytics-screen")
}

