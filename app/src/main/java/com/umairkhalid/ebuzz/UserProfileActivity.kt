package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val back_btn = findViewById<ImageButton>(R.id.user_profile_back_btn)
        val message_btn =findViewById<ImageButton>(R.id.user_profile_message_btn)
        val unfriend_btn =findViewById<Button>(R.id.user_profile_unfriend_btn)
        val request_btn =findViewById<Button>(R.id.user_profile_request_btn)
        val city = findViewById<TextView>(R.id.user_profile_city_txt)
        val user_img = findViewById<ImageView>(R.id.user_profile_user_img)
        val cover_img =findViewById<ImageView>(R.id.user_profile_cover_img)
        val abooutme_txt =findViewById<TextView>(R.id.user_profile_aboutme_txt)
        val private_layout = findViewById<LinearLayout>(R.id.user_profile_private_layout)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        message_btn.setOnClickListener{
            val intent = Intent(this, MessagingActivity::class.java)
            startActivity(intent)
        }



    }
}