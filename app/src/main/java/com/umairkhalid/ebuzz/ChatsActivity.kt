package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        val back_btn=findViewById<ImageButton>(R.id.chats_back_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, HomePageActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()

        }


        var adapter_data_list : ArrayList<recycleview_chats_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.chats_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_chats_data("","Jhon Doe.")
        val v2  = recycleview_chats_data("","Emma Phillips.")
        val v3  = recycleview_chats_data("","Jack Watson.")

        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)

        // 3- Adapter
        val adapter = recycleview_chats_adapter(adapter_data_list)
        recyclerView.adapter = adapter

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        recyclerView.startAnimation(slideUp)

    }
}