package com.abdulolatunde.bgstudentregistration.presentation

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.abdulolatunde.bgstudentregistration.PermissionHelper
import com.abdulolatunde.bgstudentregistration.hasRequiredPermissions

@Composable
fun TakePictureScreen(navController: NavController, viewModel: StudentViewModel) {
//    val scope = rememberCoroutineScope()
    //If you are not in the main activity, you can use

    val applicationContext = LocalContext.current
    val activityContext = applicationContext as ComponentActivity
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        Text(text = "hello")
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            onClick = {

                if (!hasRequiredPermissions(applicationContext)) {
                    ActivityCompat.requestPermissions(
                        activityContext,
                        PermissionHelper.CAMERAX_PERMISSIONS,
                        0
                    )
                } else {
                    takePhoto(
                        controller = controller,
                        onPhotoTaken = viewModel::onTakePhoto,
                        applicationContext = applicationContext,
                        navController = navController
                    )
                }

            }) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Take Photo"
            )
        }
    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    applicationContext: Context,
    onPhotoTaken: (Bitmap) -> Unit,
    navController: NavController,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationContext),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                //                TODO: Bring back to live later
//                val matrix = Matrix().apply {
//                    postRotate(image.imageInfo.rotationDegrees.toFloat())
//                    postScale(-1f, 1f)
//                }

//                val rotatedBitmap = Bitmap.createBitmap(
//                    image.toBitmap(),
//                    0,
//                    0,
//                    image.width,
//                    image.height,
//                    matrix,
//                    true
//                )
                onPhotoTaken(image.toBitmap())
                navController.popBackStack()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo", exception)
            }
        }
    )
}
