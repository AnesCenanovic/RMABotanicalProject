package com.example.lab2_cinaeste

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ResultLauncherActivity(private val listener: OnImageCapturedListener?) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Simulate successful image capture (replace with actual camera logic if needed)
        val mockBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val resultIntent = Intent().apply {
            putExtras(Bundle().apply { putParcelable("data", mockBitmap) })
        }
        setResult(Activity.RESULT_OK, resultIntent)
        listener?.onImageCaptured(mockBitmap) // Inform listener about captured image (optional)
        finish() // Finish the launcher activity
    }

    interface OnImageCapturedListener {
        fun onImageCaptured(bitmap: Bitmap)
    }
}
