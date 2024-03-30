package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        val back_btn = findViewById<ImageButton>(R.id.myprofile_back_btn)
        val edit_btn =findViewById<ImageButton>(R.id.myprofile_edit_btn)
        val logout_btn =findViewById<Button>(R.id.myprofile_logout_btn)
        val city = findViewById<TextView>(R.id.myprofile_city_txt)
        val friends_list_btn = findViewById<Button>(R.id.myprofile_friends_list_btn)
        val user_img = findViewById<ImageView>(R.id.myprofile_user_img)
        val cover_img =findViewById<ImageView>(R.id.myprofile_cover_img)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

    }
}