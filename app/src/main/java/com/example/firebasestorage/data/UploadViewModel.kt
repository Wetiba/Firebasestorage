package com.example.firebasestorage.data

import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UploadViewModel:ViewModel() {
    private val storage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = storage.reference.child("images")

    var imageBitmap: ImageBitmap? by mutableStateOf(null)
    var uploading: Boolean by mutableStateOf(false)

    fun uploadImage(userId: String?) {
        userId?.let { uid ->
            viewModelScope.launch {
                try {
                    uploading = true

                    // Sample image from resources
                    val imageStream = javaClass.classLoader?.getResourceAsStream("sample_image.jpg")
                    val imageBytes = imageStream?.readBytes()

                    // Upload the image to Firebase Storage
                    val imageRef = storageReference.child("$uid/profile.jpg")
                    imageBytes?.let { bytes ->
                        imageRef.putBytes(bytes).await()

                        // Download the uploaded image and display it
                        val downloadUrl = imageRef.downloadUrl.await().toString()
                        val downloadedBitmap = withContext(Dispatchers.IO) {
                            val stream = java.net.URL(downloadUrl).openStream()
                            BitmapFactory.decodeStream(stream)
                        }
                        imageBitmap = downloadedBitmap.asImageBitmap()
                    }
                } catch (e: Exception) {
                    // Handle error
                } finally {
                    uploading = false
                }
            }
        }
    }
}