package com.umairkhalid.ebuzz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class MyProfileActivity : AppCompatActivity(),ClickListner {

    private var  mAuth = FirebaseAuth.getInstance();

    private val PICK_IMAGE_REQUEST = 71
    private var type:Int =0
    private var file_path: Uri? = null
    private lateinit var user_img :ImageView
    private lateinit var cover_img:ImageView


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
        setContentView(R.layout.activity_my_profile)

        Toast.makeText(this,"Loading...",Toast.LENGTH_LONG).show()


        current_layout = 1

        val back_btn = findViewById<ImageButton>(R.id.myprofile_back_btn)
        val edit_btn =findViewById<ImageButton>(R.id.myprofile_edit_btn)
        val logout_btn =findViewById<Button>(R.id.myprofile_logout_btn)
        val username = findViewById<TextView>(R.id.myprofile_username_txt)
        val province = findViewById<TextView>(R.id.myprofile_city_txt)
        val friends_list_btn = findViewById<Button>(R.id.myprofile_friends_list_btn)
        user_img = findViewById<ImageView>(R.id.myprofile_user_img)
        cover_img =findViewById<ImageView>(R.id.myprofile_cover_img)
        val mypage_btn = findViewById<Button>(R.id.myprofile_mypage_btn)
        val settings_btn = findViewById<Button>(R.id.myprofile_settings_btn)
        val aboutme_btn = findViewById<Button>(R.id.myprofile_aboutme_btn)
        val abooutme_txt =findViewById<TextView>(R.id.myprofile_aboutme_txt)


        main_layout = findViewById(R.id.rootLayout)
        transparentView=findViewById(R.id.transparentView)
        click_layout = findViewById(R.id.click_layout)
        comments_layout = findViewById(R.id.myprofile_comments_layout)
        send_layout = findViewById(R.id.myprofile_send_layout)
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
        logout_btn.setOnClickListener{
            mAuth.signOut()
            finish()
            finishAffinity();
            val nextActivityIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextActivityIntent)
        }

        edit_btn.setOnClickListener{
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        settings_btn.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        mypage_btn.setOnClickListener{
            val intent = Intent(this, PagesActivity::class.java)
            startActivity(intent)
        }

        aboutme_btn.setOnClickListener{
            val intent = Intent(this, AboutMeActivity::class.java)
            startActivity(intent)
        }

        friends_list_btn.setOnClickListener{
            val intent = Intent(this, FriendsListActivity::class.java)
            startActivity(intent)
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()

            val sharedPref = getSharedPreferences("MYPROFILE_PREF", Context.MODE_PRIVATE)
            val storedUsername = sharedPref.getString("username", "")
            username.text=storedUsername

            val storedcity = sharedPref.getString("city", "")
            val pic_url = sharedPref.getString("profile_pic", "").toString()
            val cover_url = sharedPref.getString("cover_pic", "").toString()
            username.text=storedUsername
            province.text=storedcity

            if (pic_url.length>10) {
                Picasso.get().load(pic_url).into(user_img)
            }
            if (cover_url.length>10 ) {
                Picasso.get().load(cover_url).into(cover_img)

            }

            val json = sharedPref.getString("PROFILE_FEED", "")
            if(!(json.isNullOrEmpty())) {
                val gson = Gson()
                val type = object : TypeToken<ArrayList<recycleview_post_data>>() {}.type

                val recyclerView : RecyclerView = findViewById(R.id.myprofile_recyclerview)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    true
                )
                val adapter_data_list: ArrayList<recycleview_post_data> = gson.fromJson(json, type)

                val adapter = recycleview_post_adapter(adapter_data_list, this@MyProfileActivity)
                recyclerView.adapter = adapter
            }
        }


        var database = FirebaseDatabase.getInstance()
        val my_ref = database.getReference("users")
        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        var pic_url:String=""
        var cover_url:String=""

        if(userId!=null) {

            my_ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val name = dataSnapshot.child(userId).child("name").value.toString()
                    val city = dataSnapshot.child(userId).child("province").value.toString()
                    val about = dataSnapshot.child(userId).child("about").value.toString()
                    pic_url = dataSnapshot.child(userId).child("picture").value.toString()
                    cover_url = dataSnapshot.child(userId).child("cover").value.toString()
                    username.text = name
                    province.text = city

                    if(about!="null"){
                        abooutme_txt.text=about
                    }
                    if (pic_url.length>10) {
                        Picasso.get().load(pic_url).into(user_img)
                    }
                    if (cover_url.length>10 ) {
                        Picasso.get().load(cover_url).into(cover_img)

                    }

                    val sharedPref = getSharedPreferences("MYPROFILE_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("username", name)
                    editor.putString("city",city)
                    editor.putString("about",about)
                    editor.putString("profile_pic",pic_url)
                    editor.putString("cover_pic",cover_url)
                    editor.apply()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.d("TAG", "Unable to retrieve Data")
                }
            })
        }



        user_img.setOnClickListener {
            type=1
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        cover_img.setOnClickListener{
            type=2
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

        }

        val recyclerView : RecyclerView = findViewById(R.id.myprofile_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            true
        )

        var adapter_data_list : ArrayList<recycleview_post_data> = ArrayList()

        val databaseref = FirebaseDatabase.getInstance()
        val curr = mAuth.currentUser
        val id= curr?.uid.toString()
        var postRef = databaseref.getReference("users").child(id).child("posts")

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

                                val adapter = recycleview_post_adapter(adapter_data_list, this@MyProfileActivity)
                                recyclerView.adapter = adapter

                                val gson = Gson()
                                val json = gson.toJson(adapter_data_list)
                                val sharedPref = getSharedPreferences("MYPROFILE_PREF", Context.MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("PROFILE_FEED", json)
                                editor.apply()

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
                Toast.makeText(this@MyProfileActivity,"Unable to Fetch Data",Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            file_path = data.data
            // Set the selected image to the profileImageView
            if(type==1){

                Picasso.get().load(file_path).into(user_img)

                val my_database = FirebaseDatabase.getInstance()
                var my_ref = my_database.getReference("users")
                val currUser = mAuth.currentUser
                val userId= currUser?.uid.toString()

                val imageURL = file_path.toString()
                my_ref.child(userId).child("picture").setValue(imageURL)


                val storageRef = FirebaseStorage.getInstance().reference

                val profileImageRef = storageRef.child("profile_images/$userId.jpg")
                val uploadTask = profileImageRef.putFile(file_path!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, now get the download URL
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save download URL to Firebase Realtime Database
                        val image_url = uri.toString()
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("users/$userId/picture")

                        myRef.setValue(image_url).addOnSuccessListener {
                            Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                                Log.d("TAG", "Failed To Upload Profile Image")

                            }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Failed To Upload Profile Image")

                }

            }
            else if(type==2){

                Picasso.get().load(file_path).into(cover_img)


                val my_database = FirebaseDatabase.getInstance()
                var my_ref = my_database.getReference("users")
                val currUser = mAuth.currentUser
                val userId= currUser?.uid.toString()

                val imageURL = file_path.toString()
                my_ref.child(userId).child("cover").setValue(imageURL)


                val storageRef = FirebaseStorage.getInstance().reference

                val profileImageRef = storageRef.child("cover_images/$userId.jpg")
                val uploadTask = profileImageRef.putFile(file_path!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Image uploaded successfully, now get the download URL
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save download URL to Firebase Realtime Database
                        val image_url = uri.toString()
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("users/$userId/cover")

                        myRef.setValue(image_url).addOnSuccessListener {
                            Toast.makeText(this, "Cover updated!", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                                Log.d("TAG", "Failed To Upload Cover Image")

                            }
                    }
                }.addOnFailureListener { e ->
                Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "Failed To Upload Cover Image")

                }

            }
        }
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}