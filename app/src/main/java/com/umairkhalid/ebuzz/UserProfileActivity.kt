package com.umairkhalid.ebuzz

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class UserProfileActivity : AppCompatActivity(), ClickListner{
    private var mAuth = FirebaseAuth.getInstance();


    private lateinit var comments_layout : LinearLayout
    private lateinit var send_layout : LinearLayout
    private lateinit var main_layout : ConstraintLayout
    private lateinit var transparentView : View
    private lateinit var click_layout : LinearLayout

    private var current_layout: Int = -1
    private lateinit var comment_backBtnId: ImageButton
    private lateinit var send_backBtnId: ImageButton
    var flag = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        current_layout = 1


        val back_btn = findViewById<ImageButton>(R.id.user_profile_back_btn)
        val message_btn =findViewById<ImageButton>(R.id.user_profile_message_btn)
        val username =findViewById<TextView>(R.id.user_profile_username_txt)
        val unfriend_btn =findViewById<Button>(R.id.user_profile_unfriend_btn)
        val request_btn =findViewById<Button>(R.id.user_profile_request_btn)
        val city = findViewById<TextView>(R.id.user_profile_city_txt)
        val user_img = findViewById<ImageView>(R.id.user_profile_user_img)
        val cover_img =findViewById<ImageView>(R.id.user_profile_cover_img)
        val abooutme_txt =findViewById<TextView>(R.id.user_profile_aboutme_txt)
        val private_layout = findViewById<LinearLayout>(R.id.user_profile_private_layout)
        val recyclerView_layout = findViewById<LinearLayout>(R.id.recyclerLayout)


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


        val userID = intent.getStringExtra("USERID").toString()

        unfriend_btn.visibility = View.GONE
        request_btn.visibility = View.GONE
        recyclerView_layout.visibility=View.GONE
        username.text = ""


        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("users")
        var pic_url:String=""
        var cover_url:String=""
        my_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child(userID).child("name").value.toString()
                val city_txt = dataSnapshot.child(userID).child("province").value.toString()
                val about = dataSnapshot.child(userID).child("about").value.toString()
                pic_url = dataSnapshot.child(userID).child("picture").value.toString()
                cover_url = dataSnapshot.child(userID).child("cover").value.toString()
                username.text = name
                city.text = city_txt

                if(about!="null"){
                    abooutme_txt.text=about
                }
                if (pic_url.length>10) {
                    Picasso.get().load(pic_url).into(user_img)
                }
                if (cover_url.length>10 ) {
                    Picasso.get().load(cover_url).into(cover_img)

                }

                val database = FirebaseDatabase.getInstance()
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid.toString()

                val usersRef = database.getReference("users").child(userId).child("friends").child(userID)

                usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The friend exists in the user's friends list
                            unfriend_btn.visibility = View.VISIBLE
                            request_btn.visibility = View.GONE
                            flag=1

                        } else {
                            // The friend does not exist in the user's friends list
                            unfriend_btn.visibility = View.GONE
                            request_btn.visibility = View.VISIBLE
                            flag=0
                        }

                        val usersRef = FirebaseDatabase.getInstance().getReference("users")

                        usersRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    val type = dataSnapshot.child("profiletype").getValue(String::class.java).toString()
                                    if(type=="Public" || flag ==1){
                                        private_layout.visibility=View.GONE
                                        recyclerView_layout.visibility=View.VISIBLE

                                        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

                                        val recyclerView : RecyclerView = findViewById(R.id.user_profile_recyclerview)

                                        recyclerView.layoutManager = LinearLayoutManager(this@UserProfileActivity,
                                            LinearLayoutManager.VERTICAL,
                                            true
                                        )

                                        val databaseref = FirebaseDatabase.getInstance()
                                        var postRef = databaseref.getReference("users").child(userID).child("posts")

                                        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                                for (Snapshot in dataSnapshot.children) {
                                                    val userID = Snapshot.child("userID").getValue(String::class.java)
                                                    val postID = Snapshot.child("postID").getValue(String::class.java)
                                                    val type = Snapshot.child("type").getValue(String::class.java)
                                                    val text = Snapshot.child("text").getValue(String::class.java)
                                                    val photo = Snapshot.child("photo").getValue(String::class.java)
                                                    val video = Snapshot.child("video").getValue(String::class.java)
                                                    val time = Snapshot.child("time").getValue(String::class.java)
                                                    val description_txt = Snapshot.child("description").getValue(String::class.java)

                                                    var postType = -1
                                                    if(type == "Text"){
                                                        postType= 0
                                                    }else if(type == "Photo"){
                                                        postType= 1
                                                    }
                                                    else if(type == "Video"){
                                                        postType= 2
                                                    }
                                                    val databaseUser = FirebaseDatabase.getInstance()
                                                    val usersRef = databaseUser.getReference("users")

                                                    usersRef.child(userID.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                                        override fun onDataChange(Snapshot: DataSnapshot) {
                                                            if (Snapshot.exists()) {
                                                                val userName = Snapshot.child("name").getValue(String::class.java)
                                                                val userPicture = Snapshot.child("picture").getValue(String::class.java)

                                                                val postData  = recycleview_post_data(userID,postID,userPicture,userName,photo,"",text,description_txt,postType,0)
                                                                adapter_data_list.add(postData)

                                                                val adapter = recycleview_post_adapter(adapter_data_list, this@UserProfileActivity)
                                                                recyclerView.adapter = adapter
                                                            }
                                                        }

                                                        override fun onCancelled(databaseError: DatabaseError) {
                                                            // Handle error
                                                            Log.d("TAG", "Error getting user data: ${databaseError.message}")
                                                        }
                                                    })
                                                }

                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                // Handle error
                                                Log.d("Error", databaseError.message)
                                                Toast.makeText(this@UserProfileActivity,"Unable to Fetch Data",Toast.LENGTH_LONG).show()
                                            }
                                        })

                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error
                                Toast.makeText(this@UserProfileActivity,"Error Fetching Data",Toast.LENGTH_LONG).show()
                            }
                        })



                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                        Toast.makeText(this@UserProfileActivity, "Unable to Fetch User Data", Toast.LENGTH_LONG).show()
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.d("TAG", "Unable to retrieve Data")
                Toast.makeText(this@UserProfileActivity,"Unable to Retrieve Data",Toast.LENGTH_LONG).show()

            }
        })

        request_btn.setOnClickListener{
            if(username.text!=""){
                val user = username.text.toString().trim()
                val curr = mAuth.currentUser
                val uid = curr?.uid.toString()
                val database = FirebaseDatabase.getInstance()
                var my_ref = database.getReference("users").child(userID)

                my_ref.child("requests").child(uid).setValue(true)

                Toast.makeText(this, "Friend Request Sent", Toast.LENGTH_LONG).show()
                onBackPressed()
                finish()

            }else{
                Toast.makeText(this, "Fetching Data", Toast.LENGTH_LONG).show()

            }

        }


        unfriend_btn.setOnClickListener{
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to unfriend this account?")

            builder.setPositiveButton("Yes") { dialog, _ ->

                var currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser?.uid.toString()
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("users").child(uid).child("friends")

                usersRef.child(userID).removeValue()
                    .addOnSuccessListener {
//                    adapter_data_list.removeAt(position)
//                    recyclerView.adapter?.notifyItemRemoved(position)
                        Toast.makeText(this@UserProfileActivity,"Friend Removed",Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        onBackPressed()
                        finish()

                    }
                    .addOnFailureListener { e ->
                        // Handle failure
                        Toast.makeText(this@UserProfileActivity,"Please Try Again",Toast.LENGTH_LONG).show()
                        Log.e("RemoveEntry", "Error removing type entry: ${e.message}")
                    }

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

//
//        val v1  = recycleview_post_data("","","","Username","","","","",1,0)
//        val v2  = recycleview_post_data("","","","Username","","","","",0,0)
//        val v3  = recycleview_post_data("","","","Username","","","","",1,0)
//        val v4  = recycleview_post_data("","","","Username","","","","",1,0)
//
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//        adapter_data_list.add(v4)
//
//
//
//        // 3- Adapter
//        val adapter = recycleview_post_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter

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