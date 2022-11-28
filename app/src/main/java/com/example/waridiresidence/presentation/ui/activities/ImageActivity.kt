package com.example.waridiresidence.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.waridiresidence.R
import com.example.waridiresidence.databinding.ActivityImageBinding
import com.example.waridiresidence.util.Utils.toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_image.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*


class ImageActivity : AppCompatActivity() {

    /**
     * Link1 (Philip lackner): https://github.com/aridworld/FirebaseStorage/blob/master/app/src/main/java/com/androiddevs/firebasestorage/MainActivity.kt
     * GeeksForGeeks: https://www.geeksforgeeks.org/android-upload-an-image-on-firebase-storage-with-kotlin/
     */

    private lateinit var binding: ActivityImageBinding

    private var storageRef= Firebase.storage.reference;

    lateinit var  imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /**
        // button Click listener
        // invoke on user interaction
        binding.imageView.setOnClickListener {

            // PICK INTENT picks item from data
            // and returned selected item ;;;;; or Intent.ACTION_PICK
            val galleryIntent = Intent(Intent.ACTION_PICK)
            // here item is type of image
            galleryIntent.type = "image/"
            //activity Result launcher callback
            imagePickerActivityResult
        }
        */


        // button Click listener
        // invoke on user interaction
        binding.imageView.setOnClickListener {
            // PICK INTENT picks item from data
            // and returned selected item
            val galleryIntent = Intent(Intent.ACTION_PICK)
            // here item is type of image
            galleryIntent.type = "image/*"
            // ActivityResultLauncher callback
            imagePickerActivityResult.launch(galleryIntent)
        }

        binding.upLoadBtn.setOnClickListener {
            upload()
        }


    }



    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                 imageUri= result.data?.data!!

                binding.uploadTV.visibility = View.VISIBLE
                binding.upLoadBtn.visibility = View.VISIBLE
                binding.uploadTV.text = "$imageUri"

                //set image in ui
                Glide.with(this@ImageActivity)
                    .load(imageUri)
                    .into(binding.imageView)
            }
        }


    private fun upload() {
        // val fileName = imageUri?.pathSegments?.last()

        showProgressBar()
        // extract the file name with extension
        val sd = getFileName(applicationContext, imageUri!!)

        // Upload Task with upload to directory 'file'
        // and name of the file remains same
        val uploadTask = storageRef.child("file/$sd").putFile(imageUri)

        // On success, download the file URL and display it
        uploadTask.addOnSuccessListener {
            // using glide library to display the image
            storageRef.child("file/$sd").downloadUrl.addOnSuccessListener {
                hideProgressBar()
                Glide.with(this@ImageActivity)
                    .load(it)
                    .into(binding.imageView)

                Log.e("Firebase", "download passed")
                this.toast("Success downloading Image.")
            }.addOnFailureListener {
                hideProgressBar()
                Log.e("Firebase", "Failed in downloading")
                this.toast("Error: Failed downloading image \nHint: ${it.message}")
            }
        }.addOnFailureListener {
            hideProgressBar()
            Log.e("Firebase", "Image Upload fail")
            this.toast("Error: Image Upload fail.\n Hint: ${it.message}")
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }


    private fun showProgressBar(){
        binding.progressbarView.loadingProgress.visibility = View.VISIBLE
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressbarView.loadingProgress.visibility = View.GONE
        binding.progressbar.visibility = View.GONE
    }

}