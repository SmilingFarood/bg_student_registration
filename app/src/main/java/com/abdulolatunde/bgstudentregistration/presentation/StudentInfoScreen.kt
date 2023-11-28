package com.abdulolatunde.bgstudentregistration.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentInfoScreen(navController: NavController, id: Int, viewModel: StudentViewModel) {


    val student = viewModel.state.student
    LaunchedEffect(key1 = true) {
        viewModel.onEvent(StudentRegistrationFormEvent.GetStudentById(id = id))
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(ColorConstants.myColor),
                title = {
                    Text("${student.firstName} ${student.lastName}")
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Perform actions"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxHeight(.3f)
                        .fillMaxWidth(.5f)
                        .background(color = Color.Blue, shape = RoundedCornerShape(50.dp)),
                    contentAlignment = Alignment.Center,

                    ) {

                    student.imagePath?.let { imagePath ->
                        if (imagePath.isNotBlank()) {
                            viewModel.getBitmapFromFilePath(imagePath)?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Display Picture",
                                    modifier = Modifier.clip(RoundedCornerShape(50.dp)),
                                    contentScale = ContentScale.FillBounds,
                                )
                            }

                        }
                    } ?: Log.i("Farood", "Image file is null or empty")

                }
                StudentDetailRow(
                    description = "Registration Number",
                    data = "${student.studentId}"
                )
                Spacer(modifier = Modifier.height(10.dp))
                StudentDetailRow(
                    description = "Firstname",
                    data = "${student.firstName}"
                )
                Spacer(modifier = Modifier.height(10.dp))
                StudentDetailRow(
                    description = "Lastname",
                    data = "${student.lastName}"
                )
                Spacer(modifier = Modifier.height(10.dp))
                StudentDetailRow(description = "Course", data = "${student.course}")
                Spacer(modifier = Modifier.height(10.dp))
                StudentDetailRow(description = "Faculty", data = "${student.faculty}")
                Spacer(modifier = Modifier.height(10.dp))
                StudentDetailRow(
                    description = "State of Origin",
                    data = "${student.locationState}"
                )
                Spacer(modifier = Modifier.height(10.dp))
                StudentDetailRow(
                    description = "Local Govt",
                    data = "${student.locationLGA}"
                )
                Spacer(modifier = Modifier.height(10.dp))
                StudentDetailRow(
                    description = "Status",
                    data = if (student.isBlackList) "Blacklisted Student" else "Active Student"
                )
                Spacer(modifier = Modifier.height(10.dp))
                FilledTonalButton(onClick = {
                    viewModel.onEvent(StudentRegistrationFormEvent.BlacklistStudent(student))
                    navController.popBackStack()
                }) {
                    Text(if (student.isBlackList) "Revive Student" else "Blacklist Student")
                }
            }

        }
    }
}


@Composable
fun StudentDetailRow(description: String, data: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = description,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        Text(
            text = data,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )
    }
}