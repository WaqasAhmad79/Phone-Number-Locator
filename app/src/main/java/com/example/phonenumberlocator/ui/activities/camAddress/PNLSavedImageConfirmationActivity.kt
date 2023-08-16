package com.example.phonenumberlocator.ui.activities.camAddress

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.databinding.ActivitySavedImageConfirmationBinding
import com.example.phonenumberlocator.ui.pnlDialog.PNLResumeLoadingDialog
import java.io.File
import java.io.OutputStream


class PNLSavedImageConfirmationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedImageConfirmationBinding
    private var imageBitmap: Bitmap? = null // Declare the variable here
    private var dialog: PNLResumeLoadingDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* dialog = PNLResumeLoadingDialog(this)
        if (isTimeDifferenceGreaterThan30Seconds()) {
            if (isNetworkAvailable()) {
                dialog?.show()
                showAdmobInterstitial({
                }, { dialog?.dismiss() }, {
                    if(delayAdShown)
                    {
                        interstitialCounter =0
                    }

                    Handler().postDelayed({
                        dialog?.dismiss()
                    }, 1000)
                })
            }
        }*/
        val imagePath = intent.getStringExtra("imagePath")
        if (imagePath != null) {
            val imageFile = File(imagePath)
            imageBitmap = BitmapFactory.decodeFile(imageFile.absolutePath) // Assign the bitmap here
        } else {
            Toast.makeText(this, "Failed to load the image", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.done.setOnClickListener {
            saveImageToGallery(imageBitmap!!)
            onBackPressed()
        }
        binding.share.setOnClickListener {
            if (imageBitmap != null) {
                share(imageBitmap!!)
            }
        }
    }

    private fun share(bitmap: Bitmap) {
        val pathOfBmp = MediaStore.Images.Media.insertImage(
            contentResolver, bitmap, "title", null // Use contentResolver here
        )
        val uri = Uri.parse(pathOfBmp)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Star App")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "")
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }
    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "MyImage_${System.currentTimeMillis()}.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val resolver = contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        imageUri?.let { uri ->
            val outputStream: OutputStream? = resolver.openOutputStream(uri)
            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.flush()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }

        } ?: run {
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

}