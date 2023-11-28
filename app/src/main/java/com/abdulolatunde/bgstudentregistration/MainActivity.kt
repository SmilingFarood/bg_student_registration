package com.abdulolatunde.bgstudentregistration

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.abdulolatunde.bgstudentregistration.presentation.LoginScreen
import com.abdulolatunde.bgstudentregistration.use_cases.Screen
import com.abdulolatunde.bgstudentregistration.use_cases.StudentDao
import com.abdulolatunde.bgstudentregistration.use_cases.StudentDatabase
import com.abdulolatunde.bgstudentregistration.presentation.StudentListScreen
import com.abdulolatunde.bgstudentregistration.presentation.TakePictureScreen
import com.abdulolatunde.bgstudentregistration.presentation.AddStudentScreen
import com.abdulolatunde.bgstudentregistration.presentation.AnalyticsScreen
import com.abdulolatunde.bgstudentregistration.presentation.StudentInfoScreen
import com.abdulolatunde.bgstudentregistration.presentation.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
        }
    }
}


object PermissionHelper {
    val CAMERAX_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
}

fun hasRequiredPermissions(applicationContext: Context): Boolean {
    return PermissionHelper.CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            applicationContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun Navigation() {

    val applicationContext = LocalContext.current
    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            StudentDatabase::class.java,
            "students.db"
        ).build()
    }
    var shared by remember {
        mutableStateOf(OnBoardingSharedPref(applicationContext))
    }
    val viewModel = viewModel<StudentViewModel>(
        factory = FactoryViewModel(db.dao)
    )

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController, shared = shared)
        }
        composable(route = Screen.StudentListScreen.route) {
            StudentListScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.AddStudentScreen.route) {
            AddStudentScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screen.TakePictureScreen.route) {
            TakePictureScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screen.StudentInfoScreen.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType }
            )
        ) {
            it.arguments?.getInt("id")?.let { studentId ->
                StudentInfoScreen(
                    navController = navController,
                    id = studentId,
                    viewModel = viewModel
                )
            } ?: Log.i("Farood", "The student ID is null")


        }
        composable(route = Screen.AnalyticsScreen.route) {
            AnalyticsScreen(navController = navController, viewModel = viewModel)
        }
    }
}


class FactoryViewModel(
    private val dbDao: StudentDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StudentViewModel(dbDao) as T
    }
}

var ONBOARDING_STORE: String = ""

class OnBoardingSharedPref(context: Context) {
    private val sharedPref = context.getSharedPreferences(ONBOARDING_STORE, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()
    fun updateSharedPref(state: String) {
        editor.putString("FIRST_LAUNCH", state).apply()
    }

    fun getSharedPref(): String? {
        return sharedPref.getString("FIRST_LAUNCH", "")
    }
}