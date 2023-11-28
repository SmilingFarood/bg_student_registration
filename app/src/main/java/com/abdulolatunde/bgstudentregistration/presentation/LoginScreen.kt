package com.abdulolatunde.bgstudentregistration.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.abdulolatunde.bgstudentregistration.OnBoardingSharedPref
import com.abdulolatunde.bgstudentregistration.use_cases.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, shared: OnBoardingSharedPref) {
    val context = LocalContext.current
    var pin by remember {
        mutableStateOf("")
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .fillMaxSize()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(.3f)
//                    .background(color = Color.Yellow)
                    .align(Alignment.End)
            ) {
                Text(
                    text = "Sign in with your PIN",
                    modifier = Modifier
                        .fillMaxSize()
//                        .background(color = Color.Red)
                        .align(Alignment.BottomEnd)
                        .fillMaxHeight(),
                    textAlign = TextAlign.Center,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    TextField(
                        value = pin,
                        onValueChange = {
                            pin = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        if (shared.getSharedPref().isNullOrBlank()) {
                            if (pin.isNotEmpty()) {
                                shared.updateSharedPref(pin).also {
                                    navController.navigate(Screen.StudentListScreen.route)
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Your pin cannot be empty",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        } else if (shared.getSharedPref() != pin) {
                            Toast.makeText(
                                context,
                                "You have the wrong pin. The right pin is ${shared.getSharedPref()}",
                                Toast.LENGTH_LONG
                            ).show()
                        } else if (shared.getSharedPref() == pin) {
                            navController.navigate(Screen.StudentListScreen.route)
                        }
                    }
                    ) {
                        Text(text = "Sign In")
                    }
                }
            }
        }
    }
}