package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FriendsListActivity : AppCompatActivity(), ClickListner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_list)

        val back_btn = findViewById<ImageButton>(R.id.friendslist_back_btn)
        val search_text= findViewById<TextView>(R.id.friendslist_searchbar_txt)
        val search_btn=findViewById<ImageButton>(R.id.friendslist_searchbar_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }


        var adapter_data_list : ArrayList<recycleview_friendslist_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.friendslist_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_friendslist_data("","John Doe")
        val v2  = recycleview_friendslist_data("","Emma Phillips")
        val v3  = recycleview_friendslist_data("","John Cooper")

        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)

        // 3- Adapter
        val adapter = recycleview_friendslist_adapter(adapter_data_list,this)
        recyclerView.adapter = adapter

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        recyclerView.startAnimation(slideUp)

    }

    override fun onCLick_fun(position: Int,username:String,operation:Int)
    {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)

    }
}