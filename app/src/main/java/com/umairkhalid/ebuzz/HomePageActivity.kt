package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePageActivity : AppCompatActivity(), ClickListner {
    private lateinit var comments_layout : LinearLayout
    private lateinit var send_layout : LinearLayout
    private lateinit var main_layout : ConstraintLayout
    private lateinit var transparentView : View
    private lateinit var click_layout : LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        val notifications_btn = findViewById<Button>(R.id.homepage_notification_btn)
        val requests_btn = findViewById<Button>(R.id.homepage_requests_btn)
        val pages_btn = findViewById<Button>(R.id.homepage_pages_btn)
        val groups_btn = findViewById<Button>(R.id.homepage_groups_btn)
        val username =findViewById<TextView>(R.id.homepage_username_txt)


        main_layout = findViewById(R.id.rootLayout)
        transparentView=findViewById(R.id.transparentView)
        click_layout = findViewById(R.id.click_layout)
        comments_layout = findViewById(R.id.homepage_comments_layout)
        send_layout = findViewById(R.id.homepage_send_layout)
        transparentView.visibility=View.GONE
        comments_layout.visibility=View.GONE
        send_layout.visibility=View.GONE
        click_layout.visibility=View.GONE





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
        val add_btn =findViewById<ImageView>(R.id.homepage_add_btn)

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

        add_btn.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }


        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.homepage_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val v1  = recycleview_post_data("","John Doe.","","","","",1,0)
        val v2  = recycleview_post_data("","Emma Phillips.","","","","",0,0)
        val v3  = recycleview_post_data("","Jack Watson.","","","","",1,0)
        val v4  = recycleview_post_data("","John Doe.","","","","",1,0)


        adapter_data_list.add(v1)
        adapter_data_list.add(v2)
        adapter_data_list.add(v3)
        adapter_data_list.add(v4)


        // 3- Adapter
        val adapter = recycleview_post_adapter(adapter_data_list,this)
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

    override fun onCLick_fun(position: Int, username: String, operation: Int) {

        if(operation==1){

            send_layout.visibility=View.GONE
            click_layout.visibility=View.VISIBLE
            comments_layout.visibility=View.VISIBLE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            comments_layout.post{

                val backBtnId = comments_layout.findViewById<ImageButton>(R.id.comments_back_btn)
                val send_btn = comments_layout.findViewById<ImageButton>(R.id.comments_send_btn_1)
                val message_txt = comments_layout.findViewById<EditText>(R.id.comments_comment_txt)


                backBtnId?.setOnClickListener {

                    transparentView.visibility = View.GONE

                    val slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_layout)
                    slideDownAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            // Animation start event
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            comments_layout.visibility = View.GONE
                            click_layout.visibility = View.GONE
                            send_layout.visibility = View.GONE

                        }

                        override fun onAnimationRepeat(animation: Animation) {
                        }
                    })
                    click_layout.startAnimation(slideDownAnimation)
                }

                var adapter_data_list : ArrayList<recycleview_comment_data> = ArrayList()

                val recyclerView : RecyclerView = findViewById(R.id.comments_recycleView)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                val v1  = recycleview_comment_data("","John Doe","",0)
                val v2  = recycleview_comment_data("","Emma Phillips","",0)
                val v3  = recycleview_comment_data("","Jack Watson","",0)


                adapter_data_list.add(v1)
                adapter_data_list.add(v2)
                adapter_data_list.add(v3)


                val adapter = recycleview_comment_adapter(adapter_data_list,this)
                recyclerView.adapter = adapter

            }

        }else{

            send_layout.visibility=View.VISIBLE
            click_layout.visibility=View.VISIBLE
            comments_layout.visibility=View.GONE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            send_layout.post{

                val backBtnId = send_layout.findViewById<ImageButton>(R.id.send_back_btn)
                val search_btn = send_layout.findViewById<ImageButton>(R.id.send_searchbar_btn)
                val search_txt = send_layout.findViewById<EditText>(R.id.send_searchbar_txt)
                val send_btn = send_layout.findViewById<Button>(R.id.send_send_btn)


                backBtnId?.setOnClickListener {

                    transparentView.visibility = View.GONE

                    val slideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down_layout)
                    slideDownAnimation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation) {
                            // Animation start event
                        }

                        override fun onAnimationEnd(animation: Animation) {
                            comments_layout.visibility = View.GONE
                            click_layout.visibility = View.GONE
                            send_layout.visibility = View.GONE
                        }

                        override fun onAnimationRepeat(animation: Animation) {
                        }
                    })
                    click_layout.startAnimation(slideDownAnimation)
                }

                var adapter_data_list : ArrayList<recycleview_sendpost_data> = ArrayList()

                val recyclerView : RecyclerView = findViewById(R.id.send_recycleView)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                val v1  = recycleview_sendpost_data("","User 1","","User 2","","User 3",3)
                val v2  = recycleview_sendpost_data("","User 4","","User 5","","User 6",3)
                val v3  = recycleview_sendpost_data("","User 7","","User 8","","",2)


                adapter_data_list.add(v1)
                adapter_data_list.add(v2)
                adapter_data_list.add(v3)


                val adapter = recycleview_sendpost_adapter(adapter_data_list,this)
                recyclerView.adapter = adapter

            }

        }

    }
}