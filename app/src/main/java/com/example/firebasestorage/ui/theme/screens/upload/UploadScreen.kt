package com.example.firebasestorage.ui.theme.screens.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebasestorage.data.UploadViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun UploadScreen(navController:NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val storageViewModel: UploadViewModel = viewModel()
    val imageBitmap: ImageBitmap? = storageViewModel.imageBitmap
    val uploading = storageViewModel.uploading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the uploaded image
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to upload an image
        Button(
            onClick = { storageViewModel.uploadImage(auth.currentUser?.uid) },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uploading) {
                CircularProgressIndicator()
            } else {
                Text("Upload Image")
            }
        }
    }
}

@Preview
@Composable
fun UploadScreenPreview() {
    UploadScreen(rememberNavController())
}