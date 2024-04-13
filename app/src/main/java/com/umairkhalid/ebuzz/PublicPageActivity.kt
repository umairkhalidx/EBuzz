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

class PublicPageActivity : AppCompatActivity(),ClickListner {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_public_page)

        val back_btn = findViewById<ImageButton>(R.id.publicpage_back_btn)
        val follow_btn =findViewById<Button>(R.id.publicpage_follow_btn)
        val page_logo = findViewById<ImageView>(R.id.publicpage_user_img)
        val cover_img =findViewById<ImageView>(R.id.publicpage_cover_img)
        val description_txt =findViewById<TextView>(R.id.publicpage_description_txt)
        val leave_btn = findViewById<Button>(R.id.publicpage_leavepage_btn)
        val addpost_btn = findViewById<Button>(R.id.publicpage_addpost_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        addpost_btn.setOnClickListener{
            val intent = Intent(this, UploadPostActivity::class.java)
            startActivity(intent)
        }

        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.publicpage_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_post_data("","Pagename","","","","",1,0)
        val v2  = recycleview_post_data("","Pagename","","","","",0,0)
        val v3  = recycleview_post_data("","Pagename","","","","",1,0)
        val v4  = recycleview_post_data("","Pagename","","","","",1,0)


        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)
        adapter_data_list.add(v4)


        // 3- Adapter
        val adapter = recycleview_post_adapter(adapter_data_list,this)
        recyclerView.adapter = adapter

    }

    override fun onCLick_fun(position: Int, username: String, operation: Int) {
        Toast.makeText(this,"Will be Implemented Soon", Toast.LENGTH_LONG).show()
    }
}