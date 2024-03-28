package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.postDelayed
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animation=findViewById<LottieAnimationView>(R.id.login_loading_animation)
        animation.playAnimation()

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)

//        val logo :ImageView
//        logo=findViewById(R.id.mainactivity_logo)
//
//        Handler().postDelayed({
//
//            logo.animate().apply {
//                duration = 3000
//                rotationYBy(360f)
//
//            }.withEndAction {
//
//                Handler().postDelayed({
//                    logo.animate().apply {
//                        duration = 3000
//                        rotationYBy(360f)
//
//                    }.withEndAction {
//
//                        Handler().postDelayed({
//
//                            val intent = Intent(this, LoginActivity::class.java)
//                            startActivity(intent)
//                            finish()
//
//                        }, 600)
//                    }.start()
//
//                }, 800)
//
//            }.start()
//
//        }, 800)


    }
}