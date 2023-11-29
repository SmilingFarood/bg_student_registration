package com.abdulolatunde.bgstudentregistration.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdulolatunde.bgstudentregistration.use_cases.Student
import com.abdulolatunde.bgstudentregistration.use_cases.StudentDao
import com.abdulolatunde.bgstudentregistration.use_cases.SortType
import com.abdulolatunde.bgstudentregistration.use_cases.ValidateFirstname
import com.abdulolatunde.bgstudentregistration.use_cases.ValidateLastname
import com.abdulolatunde.bgstudentregistration.use_cases.ValidateStudentCourse
import com.abdulolatunde.bgstudentregistration.use_cases.ValidateStudentFaculty
import com.abdulolatunde.bgstudentregistration.use_cases.ValidateStudentLGA
import com.abdulolatunde.bgstudentregistration.use_cases.ValidateStudentStateOfOrg
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import kotlin.math.min

class StudentViewModel(
    private val dao: StudentDao,
    private val validateFirstname: ValidateFirstname = ValidateFirstname(),
    private val validateLastName: ValidateLastname = ValidateLastname(),
    private val validateStudentCourse: ValidateStudentCourse = ValidateStudentCourse(),
    private val validateStudentFaculty: ValidateStudentFaculty = ValidateStudentFaculty(),
    private val validateStudentLGA: ValidateStudentLGA = ValidateStudentLGA(),
    private val validateStudentStateOfOrg: ValidateStudentStateOfOrg = ValidateStudentStateOfOrg()
) : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _students = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.FIRST_NAME -> dao.getAllStudentsOrderedByFirstName()
                SortType.COURSE -> dao.getAllStudentsOrderedByCourse()
                SortType.FACULTY -> dao.getAllStudentsOrderedByFaculty()
                SortType.LOCATION_STATE -> dao.getAllStudentsOrderedByState()
                SortType.FILTER_COURSE -> dao.getAllStudentsByCourse(state.filterString!!).also {
                    state.filterString = ""
                }

                SortType.FILTER_FACULTY -> dao.getAllStudentsByFaculty(state.filterString!!).also {
                    state.filterString = ""
                }

                SortType.FILTER_STATE -> dao.getAllStudentsByState(state.filterString!!).also {
                    state.filterString = ""
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _state2 = MutableStateFlow(StudentRegistrationFormState())

    val state2 = combine(_state2, _sortType, _students) { state, sortType, students ->
        state.copy(
            students = students,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StudentRegistrationFormState())


    //        --------------------------------------------------------------------
    var state by mutableStateOf(StudentRegistrationFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvent = validationEventChannel.receiveAsFlow()


    private val _bitmaps = MutableStateFlow<Bitmap?>(null)
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value = bitmap
    }

    fun getBitmapFromFilePath(filePath: String): Bitmap? {
        return BitmapFactory.decodeFile(filePath)
    }

    fun onEvent(event: StudentRegistrationFormEvent) {

        when (event) {

            is StudentRegistrationFormEvent.FirstNameChanged -> {
                state = state.copy(firstName = event.firstName)
            }

            is StudentRegistrationFormEvent.LastNameChanged -> {
                state = state.copy(lastName = event.lastName)
            }

            is StudentRegistrationFormEvent.CourseChanged -> {
                state = state.copy(course = event.course)
            }

            is StudentRegistrationFormEvent.FacultyChanged -> {
                state = state.copy(faculty = event.faculty)
            }

            is StudentRegistrationFormEvent.LGAChanged -> {
                state = state.copy(lga = event.lga)
            }

            is StudentRegistrationFormEvent.StateOfOrg -> {
                state = state.copy(stateOfOrg = event.stateOfOrg)
            }

            is StudentRegistrationFormEvent.ImageChanged -> {
                state = state.copy(imagePath = event.imagePath)
            }

            is StudentRegistrationFormEvent.Submit -> {
                submitForm()
            }

            is StudentRegistrationFormEvent.GetStudentList -> {
                _sortType.value = event.sortType
            }

            is StudentRegistrationFormEvent.GetStudentById -> {
                viewModelScope.launch {
                    state = state.copy(
                        student = dao.getStudentById(event.id)
                    )
                }
            }

            is StudentRegistrationFormEvent.BlacklistStudent -> {
                viewModelScope.launch {
                    val sta = event.student.copy(
                        isBlackList = !event.student.isBlackList
                    )
                    dao.upsertStudent(sta)
                }
            }

            is StudentRegistrationFormEvent.HideDialog -> {
                _state2.update {
                    it.copy(
                        isFiltering = false
                    )
                }
            }

            is StudentRegistrationFormEvent.ShowDialog -> {
                _state2.update {
                    it.copy(
                        isFiltering = true
                    )
                }
            }

            is StudentRegistrationFormEvent.FilterStringChanged -> {
                state = state.copy(
                    filterString = event.filterString
                )

            }

            is StudentRegistrationFormEvent.AllStudentCount -> {
                viewModelScope.launch {
                    state = state.copy(
                        allStudentCount = dao.countAllStudent()
                    )
                }
            }

            is StudentRegistrationFormEvent.CountKeywordChanged -> {
                state = state.copy(
                    countKeyword = event.keyword
                )
            }

            is StudentRegistrationFormEvent.PerformSearchCount -> {
                Log.i("Farood", "General count")
                viewModelScope.launch {
                    state = state.copy(
                        searchCount = dao.searchCount(state.countKeyword)
                    )
                }
            }

            is StudentRegistrationFormEvent.ExportList -> {
                exportToExcel(
                    state2.value.students,
                    generateFilePath("${state2.value.sortType}.xlsx", Environment.DIRECTORY_DOCUMENTS)
                )
            }
        }
    }

    private fun submitForm() {
        val firstNameResult = validateFirstname.execute(state.firstName)
        val lastNameResult = validateLastName.execute(state.lastName)
        val courseResult = validateStudentCourse.execute(state.course)
        val facultyResult = validateStudentFaculty.execute(state.faculty)
        val lgaResult = validateStudentLGA.execute(state.lga)
        val stateOfOrgResult = validateStudentStateOfOrg.execute(state.stateOfOrg)

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            courseResult,
            facultyResult,
            lgaResult,
            stateOfOrgResult
        ).any { !it.successful }
        if (hasError) {
            state = state.copy(
                firstNameError = firstNameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                courseError = courseResult.errorMessage,
                facultyError = facultyResult.errorMessage,
                stateOfOrgError = facultyResult.errorMessage,
                lgaError = lgaResult.errorMessage,
            )
            Log.i("Farood", "There is a validation error")
            return
        }
        Log.i("Farood", "There is no error validating code")

        val student = Student(
            firstName = state.firstName,
            lastName = state.lastName,
            course = state.course,
            faculty = state.faculty,
            locationLGA = state.lga,
            locationState = state.stateOfOrg,
            isBlackList = false,
            studentId = generateStudentId(state.course),
            imagePath = state.imagePath,
        )
        Log.i("Farood", "Launching scope")
        viewModelScope.launch {
            dao.upsertStudent(student)
            Log.i("Farood", "student is: $student")
            validationEventChannel.send(ValidationEvent.Success)
        }.also {
            state = state.copy(
                firstName = "",
                lastName = "",
                stateOfOrg = "",
                lga = "",
                course = "",
                faculty = "",
                imagePath = "",
            )
            _bitmaps.value = null

        }
    }


    private fun exportToExcel(students: List<Student>, filePath: String) {
        val workbook: Workbook = XSSFWorkbook()
        val sheet: Sheet = workbook.createSheet("Sheet1")

        // Create header row
        val headerRow: Row = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("First Name")
        headerRow.createCell(1).setCellValue("Last Name")
        headerRow.createCell(2).setCellValue("Course")
        headerRow.createCell(3).setCellValue("Faculty")
        headerRow.createCell(4).setCellValue("State")
        headerRow.createCell(5).setCellValue("LGA")
        headerRow.createCell(6).setCellValue("Student ID")
        // Add other headers as needed

        // Populate data
        for ((index, obj) in students.withIndex()) {
            val dataRow: Row = sheet.createRow(index + 1)
            dataRow.createCell(0).setCellValue(obj.firstName)
            dataRow.createCell(1).setCellValue(obj.lastName)
            dataRow.createCell(2).setCellValue(obj.course)
            dataRow.createCell(3).setCellValue(obj.faculty)
            dataRow.createCell(4).setCellValue(obj.locationState)
            dataRow.createCell(5).setCellValue(obj.locationLGA)
            dataRow.createCell(6).setCellValue(obj.studentId)
            // Add other data as needed
        }

        // Write to file
        FileOutputStream(filePath).use { fileOut -> workbook.write(fileOut) }

        // Close workbook to release resources
        workbook.close()
    }

    private fun generateFilePath(
        fileName: String,
        directoryType: String
    ): String {
        // Get the public directory for the specified type
        val publicDirectory = Environment.getExternalStoragePublicDirectory(directoryType)

        // Create a subdirectory within the public directory
        val subdirectory = File(publicDirectory, "StudentRegistration")

        // Ensure that the subdirectory exists; create it if not
        if (!subdirectory.exists()) {
            subdirectory.mkdirs()
        }

        // Create a File object with the specified subdirectory and file name
        val file = File(subdirectory, fileName)

        // Return the absolute path of the file
        return file.absolutePath
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}

fun generateStudentId(course: String): String {
    val co = course.substring(0, min(3, course.length)).uppercase(Locale.ROOT)
    return "$co/${System.currentTimeMillis()}"
}