package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Search2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search2)

        val back_btn=findViewById<ImageButton>(R.id.search_back_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, HomePageActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()

        }

        var adapter_data_list : ArrayList<recycleview_search_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.search_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_search_data("","John Doe","")
        val v2  = recycleview_search_data("","Emma Phillips","")
        val v3  = recycleview_search_data("","John Cooper","")

        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)

        // 3- Adapter
        val adapter = recycleview_search_adapter(adapter_data_list)
        recyclerView.adapter = adapter

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        recyclerView.startAnimation(slideUp)
    }
}