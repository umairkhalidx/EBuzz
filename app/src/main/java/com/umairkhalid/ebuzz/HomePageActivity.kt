package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        groups_btn.setOnClickListener{
            val intent = Intent(this, GroupsActivity::class.java)
            startActivity(intent)
        }

        pages_btn.setOnClickListener{
            val intent = Intent(this, PagesActivity::class.java)
            startActivity(intent)
        }

        val search_btn =findViewById<ImageButton>(R.id.homepage_search_btn)
        val chats_btn =findViewById<ImageButton>(R.id.homepage_chat_btn)
        val profile_btn =findViewById<ImageButton>(R.id.homepage_profile_btn)

        chats_btn.setOnClickListener{
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }

        profile_btn.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        search_btn.setOnClickListener{
            val intent = Intent(this, Search1Activity::class.java)
            startActivity(intent)
        }


        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.homepage_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_post_data("","John Doe.","","","","",0,0)
        val v2  = recycleview_post_data("","Emma Phillips.","","","","",0,0)
        val v3  = recycleview_post_data("","Jack Watson.","","","","",0,0)

        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)

        // 3- Adapter
        val adapter = recycleview_post_adapter(adapter_data_list)
        recyclerView.adapter = adapter


//
//        val btn=findViewById<Button>(R.id.button)
//        btn.setOnClickListener{
//            val intent = Intent(this, MessagingActivity::class.java)
//            startActivity(intent)
////            finish()
//        }
//
//        val btn_1=findViewById<Button>(R.id.button_1)
//        btn_1.setOnClickListener{
//            val intent = Intent(this, MyProfileActivity::class.java)
//            startActivity(intent)
////            finish()
//        }
//
//        val btn_2=findViewById<Button>(R.id.button_2)
//        btn_2.setOnClickListener{
//            val intent = Intent(this, GroupMessagingActivity::class.java)
//            startActivity(intent)
////            finish()
//        }
//
//        val btn_3=findViewById<Button>(R.id.button_3)
//        btn_3.setOnClickListener{
//            val intent = Intent(this, ChatsActivity::class.java)
//            startActivity(intent)
////            finish()
//        }

    }
}