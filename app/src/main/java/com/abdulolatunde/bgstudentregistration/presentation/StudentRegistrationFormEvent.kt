package com.abdulolatunde.bgstudentregistration.presentation

import com.abdulolatunde.bgstudentregistration.use_cases.Student
import com.abdulolatunde.bgstudentregistration.use_cases.SortType

sealed interface StudentRegistrationFormEvent {
    data class FirstNameChanged(val firstName: String) : StudentRegistrationFormEvent
    data class LastNameChanged(val lastName: String) : StudentRegistrationFormEvent
    data class CourseChanged(val course: String) : StudentRegistrationFormEvent
    data class FacultyChanged(val faculty: String) : StudentRegistrationFormEvent
    data class StateOfOrg(val stateOfOrg: String) : StudentRegistrationFormEvent
    data class LGAChanged(val lga: String) : StudentRegistrationFormEvent
    data class ImageChanged(val imagePath: String) : StudentRegistrationFormEvent
    data class GetStudentList(val sortType: SortType) :
        StudentRegistrationFormEvent

    data class BlacklistStudent(val student: Student) : StudentRegistrationFormEvent
    data class GetStudentById(val id: Int) : StudentRegistrationFormEvent
    object Submit : StudentRegistrationFormEvent
    object ShowDialog : StudentRegistrationFormEvent
    object HideDialog : StudentRegistrationFormEvent
    data class FilterStringChanged(val filterString: String?) : StudentRegistrationFormEvent
    object AllStudentCount : StudentRegistrationFormEvent

    object PerformSearchCount : StudentRegistrationFormEvent

    data class CountKeywordChanged(val keyword: String) : StudentRegistrationFormEvent

    object ExportList : StudentRegistrationFormEvent


}