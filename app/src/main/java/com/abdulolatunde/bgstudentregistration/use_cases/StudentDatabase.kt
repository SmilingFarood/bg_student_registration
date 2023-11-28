package com.abdulolatunde.bgstudentregistration.use_cases


import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdulolatunde.bgstudentregistration.use_cases.Student
import com.abdulolatunde.bgstudentregistration.use_cases.StudentDao


@Database(
    entities = [Student::class],
    version = 1, exportSchema = false
)
abstract class StudentDatabase : RoomDatabase(){
    abstract val dao : StudentDao
}