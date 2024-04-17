package com.umairkhalid.ebuzz

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserProfileActivity : AppCompatActivity(), ClickListner{

    private lateinit var comments_layout : LinearLayout
    private lateinit var send_layout : LinearLayout
    private lateinit var main_layout : ConstraintLayout
    private lateinit var transparentView : View
    private lateinit var click_layout : LinearLayout

    private var current_layout: Int = -1
    private lateinit var comment_backBtnId: ImageButton
    private lateinit var send_backBtnId: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        current_layout = 1

        val back_btn = findViewById<ImageButton>(R.id.user_profile_back_btn)
        val message_btn =findViewById<ImageButton>(R.id.user_profile_message_btn)
        val unfriend_btn =findViewById<Button>(R.id.user_profile_unfriend_btn)
        val request_btn =findViewById<Button>(R.id.user_profile_request_btn)
        val city = findViewById<TextView>(R.id.user_profile_city_txt)
        val user_img = findViewById<ImageView>(R.id.user_profile_user_img)
        val cover_img =findViewById<ImageView>(R.id.user_profile_cover_img)
        val abooutme_txt =findViewById<TextView>(R.id.user_profile_aboutme_txt)
        val private_layout = findViewById<LinearLayout>(R.id.user_profile_private_layout)


        main_layout = findViewById(R.id.rootLayout)
        transparentView=findViewById(R.id.transparentView)
        click_layout = findViewById(R.id.click_layout)
        comments_layout = findViewById(R.id.user_profile_comments_layout)
        send_layout = findViewById(R.id.user_profile_send_layout)
        transparentView.visibility=View.GONE
        comments_layout.visibility=View.GONE
        send_layout.visibility=View.GONE
        click_layout.visibility=View.GONE


        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        unfriend_btn.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to unfriend this account?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                // Handle 'Yes' button click
                dialog.dismiss()
                onBackPressed()
                finish()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                // Handle 'No' button click
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        message_btn.setOnClickListener{
            val intent = Intent(this, MessagingActivity::class.java)
            startActivity(intent)
        }

        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.user_profile_recyclerview)
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
        val adapter = recycleview_post_adapter(adapter_data_list,this)
        recyclerView.adapter = adapter

    }
    override fun onCLick_fun(position: Int, username: String, operation: Int) {

        if(operation==1){

            current_layout = 2

            send_layout.visibility= View.GONE
            click_layout.visibility= View.VISIBLE
            comments_layout.visibility= View.VISIBLE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            comments_layout.post{

                comment_backBtnId = comments_layout.findViewById(R.id.comments_back_btn)
                val send_btn = comments_layout.findViewById<ImageButton>(R.id.comments_send_btn_1)
                val message_txt = comments_layout.findViewById<EditText>(R.id.comments_comment_txt)


                comment_backBtnId.setOnClickListener {

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

            current_layout = 3

            send_layout.visibility= View.VISIBLE
            click_layout.visibility= View.VISIBLE
            comments_layout.visibility= View.GONE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            send_layout.post{

                send_backBtnId = send_layout.findViewById(R.id.send_back_btn)
                val search_btn = send_layout.findViewById<ImageButton>(R.id.send_searchbar_btn)
                val search_txt = send_layout.findViewById<EditText>(R.id.send_searchbar_txt)
                val send_btn = send_layout.findViewById<Button>(R.id.send_send_btn)
                val repost_btn = send_layout.findViewById<Button>(R.id.send_repost_btn)


                send_backBtnId.setOnClickListener {

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
    override fun onBackPressed() {

        if (current_layout == 1) {
            this.finish()
            super.onBackPressed()
        } else if (current_layout == 2) {
            current_layout = 1
            comment_backBtnId.performClick()

        } else if (current_layout == 3) {
            current_layout = 1
            send_backBtnId.performClick()
        }

    }
}