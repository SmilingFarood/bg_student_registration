package com.abdulolatunde.bgstudentregistration.use_cases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.abdulolatunde.bgstudentregistration.use_cases.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Upsert
    suspend fun upsertStudent(student: Student)

    @Query("SELECT * FROM Students ORDER BY firstName ASC")
    fun getAllStudentsOrderedByFirstName(): Flow<List<Student>>

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM Students WHERE id = :id")
    suspend fun getStudentById(id: Int): Student

    @Query("SELECT * FROM Students ORDER BY course")
    fun getAllStudentsOrderedByCourse(): Flow<List<Student>>

    @Query("SELECT * FROM Students ORDER BY faculty")
    fun getAllStudentsOrderedByFaculty(): Flow<List<Student>>

    @Query("SELECT * FROM Students ORDER BY locationState")
    fun getAllStudentsOrderedByState(): Flow<List<Student>>

    @Query("SELECT * FROM Students WHERE faculty = :faculty")
    fun getAllStudentsByFaculty(faculty: String): Flow<List<Student>>

    @Query("SELECT * FROM Students WHERE course = :course")
    fun getAllStudentsByCourse(course: String): Flow<List<Student>>

    @Query("SELECT * FROM Students WHERE locationState = :stateOfOrigin")
    fun getAllStudentsByState(stateOfOrigin: String): Flow<List<Student>>

    @Query("SELECT COUNT(*) FROM Students")
    suspend fun countAllStudent(): Int

    @Query("SELECT COUNT (*) FROM Students WHERE course = :searchTerm OR faculty = :searchTerm OR locationState = :searchTerm OR studentId = :searchTerm")
    suspend fun searchCount(searchTerm: String): Int
}