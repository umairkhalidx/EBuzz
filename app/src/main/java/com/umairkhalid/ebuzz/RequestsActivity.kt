package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RequestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        val back_btn = findViewById<ImageButton>(R.id.requests_back_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }


        var adapter_data_list : ArrayList<recycleview_requests_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.requests_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_requests_data("","John Doe")
        val v2  = recycleview_requests_data("","Emma Phillips")
        val v3  = recycleview_requests_data("","John Cooper")

        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)

        // 3- Adapter
        val adapter = recycleview_requests_adapter(adapter_data_list)
        recyclerView.adapter = adapter

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        recyclerView.startAnimation(slideUp)
    }
}