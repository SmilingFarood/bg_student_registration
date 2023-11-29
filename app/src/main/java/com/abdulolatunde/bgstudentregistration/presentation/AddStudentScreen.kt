package com.abdulolatunde.bgstudentregistration.presentation

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.navigation.NavController
import com.abdulolatunde.bgstudentregistration.PermissionHelper
import com.abdulolatunde.bgstudentregistration.hasRequiredPermissions
import com.abdulolatunde.bgstudentregistration.use_cases.Screen
import com.abdulolatunde.bgstudentregistration.use_cases.Student
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreen(navController: NavController, viewModel: StudentViewModel) {
    val imageState by viewModel.bitmaps.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(ColorConstants.myColor),
                title = { Text(text = "Add Student") },
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
        val scrollState = rememberScrollState()
        val state = viewModel.state
        val context = LocalContext.current
        val activityContext = context as ComponentActivity
        LaunchedEffect(key1 = context) {
            viewModel.validationEvent.collect { event ->
                when (event) {
                    is StudentViewModel.ValidationEvent.Success -> {
                        Toast.makeText(
                            context,
                            "Registration Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.popBackStack()
                    }

                }
            }
        }

//        if (viewModel.state.isConfirmingStudentInfo) {
//            ConfirmationAlertDialog(
//                onConfirmation = {
//                    viewModel.onEvent(StudentRegistrationFormEvent.Submit)
//                },
//                onDismissRequest = {
//                    viewModel.onEvent(StudentRegistrationFormEvent.HideConfirmRegistrationDialog)
//                },
//                student = Student(
//                    firstName = state.firstName,
//                    lastName = state.lastName,
//                    locationState = state.stateOfOrg,
//                    locationLGA = state.lga,
//                    imagePath = state.imagePath,
//                    course = state.course,
//                    faculty = state.faculty,
//                )
//            )
//        }
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = ColorConstants.myColor.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .clickable {
                        if (!hasRequiredPermissions(context)) {
                            ActivityCompat.requestPermissions(
                                activityContext,
                                PermissionHelper.CAMERAX_PERMISSIONS,
                                0
                            )
                        } else {
                            navController.navigate(Screen.TakePictureScreen.route)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Back"
                )
                imageState?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                color = Color.Transparent,
                                shape = CircleShape,
                            )
                    )
                }
            }
            if (state.imagePathError != null) Text(
                text = "${state.imagePathError}",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.error,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                value = state.firstName,
                label = {
                    Text(text = "First Name")
                },
                onValueChange = {
                    viewModel.onEvent(StudentRegistrationFormEvent.FirstNameChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default,
            )
            if (state.firstNameError != null) Text(
                text = state.firstNameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.lastName,
                label = {
                    Text(text = "Last Name")
                },
                onValueChange = {
                    viewModel.onEvent(StudentRegistrationFormEvent.LastNameChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (state.lastNameError != null) Text(
                text = state.lastNameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.stateOfOrg,
                label = {
                    Text(text = "Enter your State")
                },
                onValueChange = {
                    viewModel.onEvent(StudentRegistrationFormEvent.StateOfOrg(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (state.lastNameError != null) Text(
                text = state.lastNameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.lga,
                label = {
                    Text(text = "Enter your LGA")
                },
                onValueChange = {
                    viewModel.onEvent(StudentRegistrationFormEvent.LGAChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (state.lgaError != null) Text(
                text = state.lgaError,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.course,
                label = {
                    Text(text = "Enter your desired course")
                },
                onValueChange = {
                    viewModel.onEvent(StudentRegistrationFormEvent.CourseChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (state.courseError != null) Text(
                text = state.courseError,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.faculty,
                label = {
                    Text(text = "Enter your desired Faculty")
                },
                onValueChange = {
                    viewModel.onEvent(StudentRegistrationFormEvent.FacultyChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (state.courseError != null) Text(
                text = state.courseError,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(32.dp))
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (imageState != null) {
                        createImageFile(context = context) {
                            val path = it.absolutePath
                            saveBitmapToFile(bitmap = imageState!!, filePath = path)
                            viewModel.onEvent(StudentRegistrationFormEvent.ImageChanged(path))
                        }
                    }
                    viewModel.onEvent(StudentRegistrationFormEvent.Submit)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ColorConstants.myColor)
            ) {
                Text(text = "Create Student")
            }
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}


@Composable
fun ConfirmationAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    student: Student,
) {
    Dialog(
        onDismissRequest = { onDismissRequest },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "Confirm your student information here before proceeding",
                    modifier = Modifier.padding(16.dp),
                )
//                Image(
//                    painter = painter,
//                    contentDescription = imageDescription,
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier
//                        .height(160.dp)
//                )
            }
        }
    }
}


private fun createImageFile(context: Context, onFileCreated: (File) -> Unit) {
    val fileName = System.currentTimeMillis().toString() + ".jpg"
    val fileDir = File(context.filesDir, "images")
    if (!fileDir.exists()) fileDir.mkdir()
    val file = File(fileDir, fileName)
    onFileCreated(file)
}

fun saveBitmapToFile(bitmap: Bitmap, filePath: String) {
    val outputStream = FileOutputStream(filePath)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
}

