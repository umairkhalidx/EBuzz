package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val btn=findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
//            finish()
        }

        val btn_2=findViewById<Button>(R.id.button_2)
        btn_2.setOnClickListener{
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
//            finish()
        }

        val btn_3=findViewById<Button>(R.id.button_3)
        btn_3.setOnClickListener{
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
//            finish()
        }

    }
}