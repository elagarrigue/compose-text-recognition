package com.example.textrecognition


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.example.textrecognition.composables.MLKitTextRecognition
import com.example.textrecognition.ui.theme.TextRecognitionTheme


private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST = 34
private fun foregroundPermissionApproved(context: Context): Boolean {
    return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    )
}

private fun requestForegroundPermission(context: Context) {
    val provideRationale = foregroundPermissionApproved(context)

    if (provideRationale) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST
        )
    } else {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var openDialog by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf("") }

            TextRecognitionTheme {
                Surface(color = Color.White) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        requestForegroundPermission(this@MainActivity)

                        MLKitTextRecognition {
                            openDialog = true
                            text = it
                        }
                    }

                    if (openDialog) {
                        AlertDialog(
                            onDismissRequest = { openDialog = false },
                            text = { Text(text) },
                            confirmButton = {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { openDialog = false }
                                ) {
                                    Text("Dismiss")
                                }
                            }

                        )
                    }
                }
            }
        }
    }
}
