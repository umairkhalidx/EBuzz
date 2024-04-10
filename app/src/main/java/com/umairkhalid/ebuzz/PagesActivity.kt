package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagesActivity : AppCompatActivity() , ClickListner{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pages)
        val back_btn = findViewById<ImageButton>(R.id.pages_back_btn)
        val search_btn = findViewById<ImageButton>(R.id.pages_searchbar_btn)
        val search_txt = findViewById<EditText>(R.id.pages_searchbar_txt)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        var adapter_data_list : ArrayList<recycleview_pages_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.pages_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_pages_data("","Pages 1","","Pages 2",2)
        val v2  = recycleview_pages_data("","Pages 3","","Pages 4",2)
        val v3  = recycleview_pages_data("","Pages 5","","Pages 6",2)
        val v4  = recycleview_pages_data("","Pages 7","","Pages 8",2)
        val v5  = recycleview_pages_data("","Pages 9","","",1)


        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)
        adapter_data_list.add(v4)
        adapter_data_list.add(v5)



        val adapter = recycleview_pages_adapter(adapter_data_list,this)
        recyclerView.adapter = adapter

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        recyclerView.startAnimation(slideUp)


    }

    override fun onCLick_fun(position: Int,username:String){
        val intent = Intent(this, PublicPageActivity::class.java)
        startActivity(intent)

    }
}