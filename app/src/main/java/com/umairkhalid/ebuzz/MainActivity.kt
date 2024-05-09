package com.umairkhalid.ebuzz

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import android.Manifest
class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CAMERA = 1001
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if camera permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_CAMERA
            )
        } else {
            // Camera permission has been granted, check for record audio permission
            checkRecordAudioPermission()
        }
    }

    private fun checkRecordAudioPermission() {
        // Check if record audio permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            // Request record audio permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
        } else {
            // Record audio permission has been granted
            // Proceed with your app logic
            initializeUI()
        }
    }

    private fun initializeUI() {
        val animation = findViewById<LottieAnimationView>(R.id.login_loading_animation)
        animation.playAnimation()

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, check record audio permission
                    checkRecordAudioPermission()
                } else {
                    // Camera permission denied
                }
            }
            PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Record audio permission granted
                    initializeUI()
                } else {
                    // Record audio permission denied
                }
            }
        }
    }
}
