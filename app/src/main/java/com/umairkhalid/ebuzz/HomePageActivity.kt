package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val notifications_btn = findViewById<Button>(R.id.homepage_notification_btn)
        val requests_btn = findViewById<Button>(R.id.homepage_requests_btn)
        val pages_btn = findViewById<Button>(R.id.homepage_pages_btn)
        val groups_btn = findViewById<Button>(R.id.homepage_groups_btn)
        val username =findViewById<TextView>(R.id.homepage_username_txt)

        notifications_btn.setOnClickListener{
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
//            finish()
        }

        requests_btn.setOnClickListener{
            val intent = Intent(this, RequestsActivity::class.java)
            startActivity(intent)
        }


        val btn=findViewById<Button>(R.id.button)
        btn.setOnClickListener{
            val intent = Intent(this, MessagingActivity::class.java)
            startActivity(intent)
//            finish()
        }

        val btn_1=findViewById<Button>(R.id.button_1)
        btn_1.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
//            finish()
        }

        val btn_2=findViewById<Button>(R.id.button_2)
        btn_2.setOnClickListener{
            val intent = Intent(this, GroupMessagingActivity::class.java)
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