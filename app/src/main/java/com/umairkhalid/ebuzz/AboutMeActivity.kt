package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class AboutMeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_me)

        val back_btn = findViewById<ImageButton>(R.id.aboutme_back_btn)
        val update_btn=findViewById<Button>(R.id.aboutme_update_btn)
        val aboutme_txt=findViewById<TextView>(R.id.aboutme_txt)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        update_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        val aboutme_layout=findViewById<LinearLayout>(R.id.aboutme_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        aboutme_layout.startAnimation(slideUp)

    }
}