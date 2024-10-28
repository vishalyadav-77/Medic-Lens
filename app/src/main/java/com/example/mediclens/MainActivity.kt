 package com.example.mediclens

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

 class MainActivity : AppCompatActivity() {
    lateinit var cam: ImageView
    lateinit var gallery: ImageView
    val camera_code=100
    val  gallery_code=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cam= findViewById<ImageView>(R.id.camera)
        gallery= findViewById<ImageView>(R.id.gallery)

        cam.setOnClickListener{
            Toast.makeText(this,"Cam clicked", Toast.LENGTH_SHORT).show()
            OpenCamera()
        }
        gallery.setOnClickListener{
            Toast.makeText(this,"gallery clicked", Toast.LENGTH_SHORT).show()
            OpenGallery()
        }
    }
    fun OpenCamera(){
        //Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.CAMERA), camera_code)
        }else{
            //start camera Intent
            var cam_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(cam_intent)
        }
    }
     fun OpenGallery(){
         //Check for gallery permission
         if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
             == PackageManager.PERMISSION_DENIED){
             ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), gallery_code)
         }else{
             //start gallery intent
             var gal_intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
             startActivity(gal_intent)
         }
     }
     fun saveBitmapToFile(bitmap: Bitmap): Uri? {
         // Create a temporary file in the cache directory
         val file = File(cacheDir, "captured_image.jpg")
         try {
             FileOutputStream(file).use { out ->
                 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
             }
         } catch (e: IOException) {
             e.printStackTrace()
             return null
         }
         return Uri.fromFile(file)
     }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if(requestCode==camera_code && resultCode== Activity.RESULT_OK){
             val imageBitmap = data?.extras?.get("data") as Bitmap
             val imageUri= saveBitmapToFile(imageBitmap)

         }
     }
}