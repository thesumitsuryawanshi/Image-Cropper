package com.example.imagecropper

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.imagecropper.databinding.ActivityMainBinding
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                binding.ivImageView.setImageBitmap(bitmap)
                saveImage(bitmap)
            }
        }
        binding.btnSelectImage.setOnClickListener {
            cropActivityResultLauncher.launch(null)
        }

    }

    private fun checkPermissions() {

        val PermissionArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            Toast.makeText(this, "Please Enable the Permissions.", Toast.LENGTH_SHORT).show()
            requestPermissions(PermissionArray, 1)
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        AlertDialog.Builder(this)
            .setTitle("Save Image")
            .setMessage("Save Image to gallery?")
            .setPositiveButton("YES")
            { _, _ ->

                val obj = SaveImageToFile()
                val msg = obj.saveImageToFile(bitmap)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("NO") { dialog, _ ->
                binding.btnSelectImage.text = "Reselect Image"
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {

        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setActivityTitle("Editing")
                .setMultiTouchEnabled(true)
                .setActivityMenuIconColor(R.color.purple_500)
                .setBorderLineColor(R.color.purple_500)
                .getIntent(this@MainActivity)

        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            intent?.type = "image/*"
            return CropImage.getActivityResult(intent)?.uri
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
        }
    }
}