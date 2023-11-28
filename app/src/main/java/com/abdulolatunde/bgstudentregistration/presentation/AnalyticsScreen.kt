package com.abdulolatunde.bgstudentregistration.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, viewModel: StudentViewModel) {
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(StudentRegistrationFormEvent.AllStudentCount)
    }

    Scaffold(
        modifier = Modifier,

        topBar = {
            CenterAlignedTopAppBar(

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(ColorConstants.myColor),
                title = { Text(text = "Analytics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
            ) {

                StudentDetailRow(
                    description = "Number of students enrolled in total",
                    data = "${state.allStudentCount}"
                )
                StudentDetailRow(
                    description = "Number of ${state.countKeyword} students enrolled",
                    data = "${state.searchCount}"
                )
                TextField(
                    value = state.countKeyword,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        viewModel.onEvent(StudentRegistrationFormEvent.CountKeywordChanged(it))
                    },
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.onEvent(StudentRegistrationFormEvent.PerformSearchCount)
                    }) {
                    Text(text = "Count")
                }
            }
        }

    }
}

