package com.example.imagecropper

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

const val success = "Image is successfully saved. \n\n Location:"
const val failed = "External media doesn't exist."
const val exception = "Please ocured"


class SaveImageToFile {


    fun saveImageToFile(bitmap: Bitmap): String {

        Log.d("bitmap location", "$bitmap")
        val externalStorageState = Environment.getExternalStorageState()
        val storageDirectory = Environment.getExternalStorageDirectory().toString()

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {

            val i = Date().time

            val file = File(storageDirectory, "ImageCropper-image$i.jpeg")

            try {
                val stream: OutputStream = FileOutputStream(file)
                stream.apply {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                    flush()
                    close()
                }
                return success + file
            } catch (e: Exception) {
                Log.d("testing", "$e")
                return exception
            }

        } else {
            Log.d("testing", "else block")
            return failed
        }
    }
}