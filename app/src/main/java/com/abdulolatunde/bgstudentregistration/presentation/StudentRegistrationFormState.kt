package com.abdulolatunde.bgstudentregistration.presentation

import com.abdulolatunde.bgstudentregistration.use_cases.Student
import com.abdulolatunde.bgstudentregistration.use_cases.SortType

data class StudentRegistrationFormState(
    val students: List<Student> = emptyList(),
    var allStudentCount: Int = 0,
    var searchCount: Int = 0,
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val stateOfOrg: String = "",
    val stateOfOrgError: String? = null,
    val imagePath: String? = null,
    val imagePathError: String? = null,
    val lga: String = "",
    val lgaError: String? = null,
    val course: String = "",
    val courseError: String? = null,
    val faculty: String = "",
    val facultyError: String? = null,
    var student: Student = Student(),
    val sortType: SortType = SortType.FIRST_NAME,
    val isFiltering: Boolean = false,
    var filterString: String? = null,
    val countKeyword: String = "",
)
