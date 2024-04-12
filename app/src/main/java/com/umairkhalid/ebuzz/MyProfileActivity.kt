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
        val requests_btn = findViewById<Button>(R.id.myprofile_requests_btn)
        val settings_btn = findViewById<Button>(R.id.myprofile_settings_btn)
        val aboutme_btn = findViewById<Button>(R.id.myprofile_aboutme_btn)
        val abooutme_txt =findViewById<TextView>(R.id.myprofile_aboutme_txt)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        edit_btn.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        settings_btn.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        requests_btn.setOnClickListener{
            val intent = Intent(this, RequestsActivity::class.java)
            startActivity(intent)
        }

        aboutme_btn.setOnClickListener{
            val intent = Intent(this, AboutMeActivity::class.java)
            startActivity(intent)
        }

        friends_list_btn.setOnClickListener{
            val intent = Intent(this, FriendsListActivity::class.java)
            startActivity(intent)
        }

        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.myprofile_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_post_data("","Username","","","","",1,0)
        val v2  = recycleview_post_data("","Username","","","","",0,0)
        val v3  = recycleview_post_data("","Username","","","","",1,0)
        val v4  = recycleview_post_data("","Username","","","","",1,0)


        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)
        adapter_data_list.add(v4)



        // 3- Adapter
        val adapter = recycleview_post_adapter(adapter_data_list)
        recyclerView.adapter = adapter

    }
}