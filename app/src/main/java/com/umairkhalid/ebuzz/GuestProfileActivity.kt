package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GuestProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_profile)

        val back_btn = findViewById<ImageButton>(R.id.guestprofile_back_btn)
        val logout_btn =findViewById<Button>(R.id.guestprofile_logout_btn)
        val user_img = findViewById<ImageView>(R.id.guestprofile_user_img)
        val cover_img =findViewById<ImageView>(R.id.guestprofile_cover_img)
        val settings_btn = findViewById<Button>(R.id.guestprofile_settings_btn)
        val aboutme_btn = findViewById<Button>(R.id.guestprofile_aboutme_btn)
        val abooutme_txt =findViewById<TextView>(R.id.guestprofile_aboutme_txt)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        settings_btn.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        aboutme_btn.setOnClickListener{
            val intent = Intent(this, AboutMeActivity::class.java)
            startActivity(intent)
        }

    }
}