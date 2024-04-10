package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val back_btn=findViewById<ImageButton>(R.id.notifications_back_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, HomePageActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()

        }


        var adapter_data_list : ArrayList<recycleview_notifications_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.notifications_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_notifications_data("","You have recieved one new notification.")
        val v2  = recycleview_notifications_data("","You have recieved one new notification.")
        val v3  = recycleview_notifications_data("","You have recieved one new notification.")
        val v4  = recycleview_notifications_data("","You have recieved one new notification.")

        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)
        adapter_data_list.add(v4)


        // 3- Adapter
        val adapter = recycleview_notifications_adapter(adapter_data_list)
        recyclerView.adapter = adapter

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        recyclerView.startAnimation(slideUp)

    }
}