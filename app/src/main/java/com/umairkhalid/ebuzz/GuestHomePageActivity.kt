package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GuestHomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_home_page)

        val notifications_btn = findViewById<Button>(R.id.guesthomepage_notification_btn)
        val requests_btn = findViewById<Button>(R.id.guesthomepage_requests_btn)
        val pages_btn = findViewById<Button>(R.id.guesthomepage_pages_btn)
        val groups_btn = findViewById<Button>(R.id.guesthomepage_groups_btn)
        val username =findViewById<TextView>(R.id.guesthomepage_username_txt)

        notifications_btn.setOnClickListener{
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
//            finish()
        }

        requests_btn.setOnClickListener{
            Toast.makeText(this,"This feature is not available in Guest Mode",Toast.LENGTH_LONG).show()
        }

        groups_btn.setOnClickListener{
            val intent = Intent(this, GroupsActivity::class.java)
            startActivity(intent)
        }

        pages_btn.setOnClickListener{
            val intent = Intent(this, PagesActivity::class.java)
            startActivity(intent)
        }

        val search_btn =findViewById<ImageButton>(R.id.guesthomepage_search_btn)
        val chats_btn =findViewById<ImageButton>(R.id.guesthomepage_chat_btn)
        val profile_btn =findViewById<ImageButton>(R.id.guesthomepage_profile_btn)
        val add_btn =findViewById<ImageView>(R.id.guesthomepage_add_btn)

        chats_btn.setOnClickListener{
            Toast.makeText(this,"This feature is not available in Guest Mode",Toast.LENGTH_LONG).show()
        }

        profile_btn.setOnClickListener{
            val intent = Intent(this, GuestProfileActivity::class.java)
            startActivity(intent)
        }

        search_btn.setOnClickListener{
            val intent = Intent(this, Search1Activity::class.java)
            startActivity(intent)
        }

        add_btn.setOnClickListener{
            Toast.makeText(this,"This feature is not available in Guest Mode",Toast.LENGTH_LONG).show()
        }

    }
}