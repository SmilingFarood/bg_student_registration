package com.abdulolatunde.bgstudentregistration.use_cases

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Students", indices = [Index(value = ["id"], unique = true)])
data class Student(
    val firstName: String? = null,
    val lastName: String? = null,
    val locationState: String? = null,
    val locationLGA: String? = null,
    val faculty: String? = null,
    val course: String? = null,
    val imagePath: String? = null,
    var isBlackList: Boolean = false,
    val studentId: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
