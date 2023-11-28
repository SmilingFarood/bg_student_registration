package com.abdulolatunde.bgstudentregistration.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.abdulolatunde.bgstudentregistration.use_cases.Screen
import com.abdulolatunde.bgstudentregistration.use_cases.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    navController: NavController,
    viewModel: StudentViewModel,
) {
    val context = LocalContext.current
    val students by viewModel.state2.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var sortType by remember {
        mutableStateOf(SortType.FIRST_NAME)
    }
    Log.i("Farood", "Students is ${students.students}")

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(StudentRegistrationFormEvent.GetStudentList(SortType.FIRST_NAME))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddStudentScreen.route)
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Student") },
                text = {
                    Text(text = "Add Student")
                },
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(ColorConstants.myColor),
                title = {
                    Text("Student List")
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(StudentRegistrationFormEvent.ExportList).also {
                                Log.i("","Showing Toast")
                                Toast.makeText(
                                    context,
                                    "Exported to device Storage. You can find it in Internal Storage --> Documents --> StudentRegistration --> myDoc.xlsx",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Export List"
                        )
                    }
                    IconButton(
                        onClick = {
                            expanded = true
                        },
                        modifier = Modifier
                            .padding(end = 16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Perform actions"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Analytics") },
                            onClick = {
                                navController.navigate(Screen.AnalyticsScreen.route)
                                expanded = false
                            })
                        Text(text = "Sort By: ")

                        DropdownMenuItem(
                            text = { Text(text = "First Name") },
                            onClick = {
                                viewModel.onEvent(
                                    StudentRegistrationFormEvent.GetStudentList(SortType.FIRST_NAME)
                                )
                                sortType = SortType.FIRST_NAME
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = "Faculty") },
                            onClick = {
                                viewModel.onEvent(
                                    StudentRegistrationFormEvent.GetStudentList(SortType.FACULTY)
                                )
                                sortType = SortType.FACULTY
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = "Course") },
                            onClick = {
                                viewModel.onEvent(
                                    StudentRegistrationFormEvent.GetStudentList(SortType.COURSE)
                                )
                                sortType = SortType.COURSE
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = "State of Origin") },
                            onClick = {
                                viewModel.onEvent(
                                    StudentRegistrationFormEvent.GetStudentList(SortType.LOCATION_STATE)
                                )
                                expanded = false
                            })
                        Text(text = "-------------------------------------------")
                        Text(text = "Filter By: ")
                        DropdownMenuItem(
                            text = { Text(text = "Faculty") },
                            onClick = {
                                viewModel.onEvent(StudentRegistrationFormEvent.ShowDialog)
                                sortType = SortType.FILTER_FACULTY
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = "Course") },
                            onClick = {
                                viewModel.onEvent(StudentRegistrationFormEvent.ShowDialog)
                                sortType = SortType.FILTER_COURSE
                                expanded = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = "State of Origin") },
                            onClick = {
                                viewModel.onEvent(StudentRegistrationFormEvent.ShowDialog)
                                sortType = SortType.FILTER_STATE
                                expanded = false
                            })
                    }
                }
            )
        }

    ) { contentPadding ->
        if (students.isFiltering) {
            FilterStudentDialog(
                state = viewModel.state,
                modifier = Modifier,
                onEvent = { viewModel.onEvent(it) },
                sortType = sortType,
            )
        }

        if (students.students.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "You have not enrolled any students yet",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(contentPadding),
            ) {
                items(students.students) { student ->
                    ListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.StudentInfoScreen.navigateWithArgument(
                                        student.id
                                    )
                                )
                            },
                        colors = ListItemDefaults.colors(containerColor = if (student.isBlackList) Color.Gray else Color.Transparent),
                        headlineText = { Text(text = "${student.firstName}  ${student.lastName}") },
                        supportingText = { Text(text = "Fac: ${student.faculty} - Dept: ${student.course}") },
                        trailingContent = {
                            Icon(
                                if (student.isBlackList) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = "Add",
                                modifier = Modifier.clickable {
                                    viewModel.onEvent(
                                        StudentRegistrationFormEvent.BlacklistStudent(
                                            student
                                        )
                                    )
                                })
                        },
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(color = Color(0xffCCCCFF), shape = CircleShape),
                            ) {
                                student.imagePath?.let { imagePath ->
                                    if (student.imagePath.isNotBlank()) {
                                        viewModel.getBitmapFromFilePath(imagePath)?.let {
                                            Image(
                                                bitmap = it.asImageBitmap(),
                                                contentDescription = "Display Picture",
                                                modifier = Modifier.clip(CircleShape),
                                                contentScale = ContentScale.FillBounds,
                                            )
                                        }
                                    }
                                } ?: Log.i("Farood", "Image file is null or empty")
                            }
                        },
                        overlineText = {
                            Text(text = "Registration Number: ${student.studentId}")
                        }
                    )
                }
            }


        }
    }
}