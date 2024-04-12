package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GroupsActivity : AppCompatActivity() , ClickListner{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        val back_btn = findViewById<ImageButton>(R.id.groups_back_btn)
        val search_btn = findViewById<ImageButton>(R.id.groups_searchbar_btn)
        val search_txt = findViewById<EditText>(R.id.groups_searchbar_txt)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        var adapter_data_list : ArrayList<recycleview_groups_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.groups_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_groups_data("","Group 1","","Group 2",2)
        val v2  = recycleview_groups_data("","Group 3","","Group 4",2)
        val v3  = recycleview_groups_data("","Group 5","","Group 6",2)
        val v4  = recycleview_groups_data("","Group 7","","Group 8",2)
        val v5  = recycleview_groups_data("","Group 9","","",1)


        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)
        adapter_data_list.add(v4)
        adapter_data_list.add(v5)



        val adapter = recycleview_groups_adapter(adapter_data_list,this)
        recyclerView.adapter = adapter

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        recyclerView.startAnimation(slideUp)


    }

    override fun onCLick_fun(position: Int,username:String,operation:Int)
    {
        val intent = Intent(this, GroupMessagingActivity::class.java)
        startActivity(intent)

    }
}