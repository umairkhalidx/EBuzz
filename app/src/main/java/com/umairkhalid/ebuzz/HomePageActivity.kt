package com.umairkhalid.ebuzz

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class HomePageActivity : AppCompatActivity(), ClickListner {

    @SuppressLint("MissingInflatedId")
    
    private lateinit var comments_layout: LinearLayout
    private lateinit var send_layout: LinearLayout
    private lateinit var main_layout: ConstraintLayout
    private lateinit var transparentView: View
    private lateinit var click_layout: LinearLayout

    private var current_layout: Int = -1
    private lateinit var comment_backBtnId: ImageButton
    private lateinit var send_backBtnId: ImageButton

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, you can proceed with sending notifications
        } else {
            // Permission is not granted, handle accordingly
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        current_layout = 1

        FirebaseApp.initializeApp(this)
        //firebase token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("MyToken", token)
        })

        askNotificationPermission()

        var requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),)
        { isGranted: Boolean ->
            if (isGranted) {
                // FCM SDK (and your app) can post notifications.
            } else {
                askNotificationPermission()
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = task.result
            sendPushNotification(
                token,
                "EBuzz",
                "Subtitle: Class",
                "Welcome User",
                mapOf("key1" to "value1", "key2" to "value2")
            )

        }

        val notifications_btn = findViewById<Button>(R.id.homepage_notification_btn)
        val requests_btn = findViewById<Button>(R.id.homepage_requests_btn)
        val pages_btn = findViewById<Button>(R.id.homepage_pages_btn)
        val groups_btn = findViewById<Button>(R.id.homepage_groups_btn)
        val username = findViewById<TextView>(R.id.homepage_username_txt)


        main_layout = findViewById(R.id.rootLayout)
        transparentView = findViewById(R.id.transparentView)
        click_layout = findViewById(R.id.click_layout)
        comments_layout = findViewById(R.id.homepage_comments_layout)
        send_layout = findViewById(R.id.homepage_send_layout)
        transparentView.visibility = View.GONE
        comments_layout.visibility = View.GONE
        send_layout.visibility = View.GONE
        click_layout.visibility = View.GONE




        notifications_btn.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
//            finish()
        }

        requests_btn.setOnClickListener {
            val intent = Intent(this, RequestsActivity::class.java)
            startActivity(intent)
        }

        groups_btn.setOnClickListener {
            val intent = Intent(this, GroupsActivity::class.java)
            startActivity(intent)
        }

        pages_btn.setOnClickListener {
            val intent = Intent(this, PagesActivity::class.java)
            startActivity(intent)
        }

        val search_btn = findViewById<ImageButton>(R.id.homepage_search_btn)
        val chats_btn = findViewById<ImageButton>(R.id.homepage_chat_btn)
        val profile_btn = findViewById<ImageButton>(R.id.homepage_profile_btn)
        val add_btn = findViewById<ImageView>(R.id.homepage_add_btn)

        chats_btn.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }

        profile_btn.setOnClickListener {
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        search_btn.setOnClickListener {
            val intent = Intent(this, Search1Activity::class.java)
            startActivity(intent)
        }

        add_btn.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()

            val sharedPref = getSharedPreferences("HOMEPAGE_PREF", Context.MODE_PRIVATE)
            val storedUsername = sharedPref.getString("username", "")
            username.text=storedUsername

            val json = sharedPref.getString("USER_FEED", "")
            if(!(json.isNullOrEmpty())) {
                val gson = Gson()
                val type = object : TypeToken<ArrayList<recycleview_post_data>>() {}.type
                val adapter_data_list: ArrayList<recycleview_post_data> = gson.fromJson(json, type)

                val recyclerView: RecyclerView = findViewById(R.id.homepage_recyclerview)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    true
                )

                val adapter = recycleview_post_adapter(adapter_data_list, this@HomePageActivity)
                recyclerView.adapter = adapter

            }

        }

        var database1 = FirebaseDatabase.getInstance()
        val my_ref1 = database1.getReference("users")
        var currentUser1 = FirebaseAuth.getInstance().currentUser
        val userId1 = currentUser1?.uid

        if(userId1!=null) {

            my_ref1.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val name = dataSnapshot.child(userId1).child("name").value.toString()
                    username.text = name

                    val sharedPref = getSharedPreferences("HOMEPAGE_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("username", name)
                    editor.apply()

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.d("TAG", "Unable to retrieve Data")
                }
            })
        }


        var adapter_data_list: ArrayList<recycleview_post_data> = ArrayList()

        val recyclerView: RecyclerView = findViewById(R.id.homepage_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                true
        )

        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()
        Toast.makeText(this,"Loading....",Toast.LENGTH_LONG).show()

        val friendsRef = database.getReference("users").child(userId).child("friends")

        friendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (friendSnapshot in dataSnapshot.children) {
                    val friendId = friendSnapshot.key.toString()

                    val database = FirebaseDatabase.getInstance()
                    val usersRef = database.getReference("users")

                    val postsQuery = usersRef.child(friendId).child("posts").orderByChild("time").limitToLast(1)

                    postsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (postSnapshot in dataSnapshot.children) {
                                    val postId = postSnapshot.key
                                    val postText = postSnapshot.child("text").getValue(String::class.java)
                                    val postPhoto = postSnapshot.child("photo").getValue(String::class.java)

                                    val userID = postSnapshot.child("userID").getValue(String::class.java)
                                    val postID = postSnapshot.child("postID").getValue(String::class.java)
                                    val type = postSnapshot.child("type").getValue(String::class.java)
                                    val text = postSnapshot.child("text").getValue(String::class.java)
                                    val photo = postSnapshot.child("photo").getValue(String::class.java)
                                    val video = postSnapshot.child("video").getValue(String::class.java)
                                    val time = postSnapshot.child("time").getValue(String::class.java)
                                    val description_txt = postSnapshot.child("description").getValue(String::class.java)

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
                                    val usersRef2 = databaseUser.getReference("users")

                                    usersRef2.child(userID.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(Snapshot: DataSnapshot) {
                                            if (Snapshot.exists()) {
                                                val userName = Snapshot.child("name").getValue(String::class.java)
                                                val userPicture = Snapshot.child("picture").getValue(String::class.java)

                                                val postData  = recycleview_post_data(userID,postID,userPicture,userName,photo,"",text,description_txt,postType,0)
                                                adapter_data_list.add(postData)

                                                val adapter = recycleview_post_adapter(adapter_data_list, this@HomePageActivity)
                                                recyclerView.adapter = adapter

                                                val gson = Gson()
                                                val json = gson.toJson(adapter_data_list)
                                                val sharedPref = getSharedPreferences("HOMEPAGE_PREF", Context.MODE_PRIVATE)
                                                val editor = sharedPref.edit()
                                                editor.putString("USER_FEED", json)
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
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                            println("Error fetching last post data: ${databaseError.message}")
                        }
                    })

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@HomePageActivity, "Unable to Fetch Friends Data", Toast.LENGTH_LONG).show()
            }
        })


//        val v1 = recycleview_post_data("","","", "John Doe.", "", "", "", "", 1, 0)
//        val v2 = recycleview_post_data("","","", "Emma Phillips.", "", "", "", "", 0, 0)
//        val v3 = recycleview_post_data("","","", "Jack Watson.", "", "", "", "", 1, 0)
//        val v4 = recycleview_post_data("","","", "John Doe.", "", "", "", "", 1, 0)
//
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//        adapter_data_list.add(v4)
//
//
//        // 3- Adapter
//        val adapter = recycleview_post_adapter(adapter_data_list, this)
//        recyclerView.adapter = adapter
    }

    override fun onCLick_fun(position: Int, username: String, operation: Int) {

        if (operation == 1) {

            current_layout = 2

            send_layout.visibility = View.GONE
            click_layout.visibility = View.VISIBLE
            comments_layout.visibility = View.VISIBLE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            comments_layout.post {

                var adapter_data_list: ArrayList<recycleview_comment_data> = ArrayList()

                val recyclerView: RecyclerView = findViewById(R.id.comments_recycleView)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                comment_backBtnId = comments_layout.findViewById(R.id.comments_back_btn)
                val send_btn = comments_layout.findViewById<ImageButton>(R.id.comments_send_btn_1)
                val message_txt = comments_layout.findViewById<EditText>(R.id.comments_comment_txt)

                send_btn.setOnClickListener{
                    val txt = message_txt.text.toString().trim()
                    if(txt.isNotEmpty()){


                        val v1 = recycleview_comment_data("", "Umair Khalid", txt, 0)
                        adapter_data_list.add(v1)

                        val adapter = recycleview_comment_adapter(adapter_data_list, this)
                        recyclerView.adapter = adapter

                    }else{
                        Toast.makeText(this,"Please type a Comment",Toast.LENGTH_LONG).show()
                    }
                }

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

                val v1 = recycleview_comment_data("", "Umair Khalid", "Checking Comments Funtionality", 0)
                val v2 = recycleview_comment_data("", "Admin", "Great Post", 0)
                val v3 = recycleview_comment_data("", "Kainat Ali", "This is a Comment", 0)


                adapter_data_list.add(v1)
                adapter_data_list.add(v2)
                adapter_data_list.add(v3)


                val adapter = recycleview_comment_adapter(adapter_data_list, this)
                recyclerView.adapter = adapter

            }

        } else {

            current_layout = 3

            send_layout.visibility = View.VISIBLE
            click_layout.visibility = View.VISIBLE
            comments_layout.visibility = View.GONE
            transparentView.visibility = View.VISIBLE
            transparentView.setOnTouchListener { _, _ -> true }

            val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
            click_layout.startAnimation(slideUp)

            send_layout.post {

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

                var adapter_data_list: ArrayList<recycleview_sendpost_data> = ArrayList()

                val recyclerView: RecyclerView = findViewById(R.id.send_recycleView)
                recyclerView.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false
                )

                val v1 = recycleview_sendpost_data("", "User 1", "", "User 2", "", "User 3", 3)
                val v2 = recycleview_sendpost_data("", "User 4", "", "User 5", "", "User 6", 3)
                val v3 = recycleview_sendpost_data("", "User 7", "", "User 8", "", "", 2)


                adapter_data_list.add(v1)
                adapter_data_list.add(v2)
                adapter_data_list.add(v3)


                val adapter = recycleview_sendpost_adapter(adapter_data_list, this)
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

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun sendPushNotification(token: String, title: String, subtitle: String, body: String, data: Map<String, String> = emptyMap()) {
        val url = "https://fcm.googleapis.com/fcm/send"
        val bodyJson = JSONObject()
        bodyJson.put("to", token)
        bodyJson.put("notification",
            JSONObject().also {
                it.put("title", title)
                it.put("subtitle", subtitle)
                it.put("body", body)
                it.put("sound", "social_notification_sound.wav")
            }
        )
        Log.d("TAG", "sendPushNotification: ${JSONObject(data)}")
        if (data.isNotEmpty()) {
            bodyJson.put("data", JSONObject(data))
        }

        var key="AAAAExD1IZA:APA91bHOZLPe_ydAcYTjkfD2nrwml9OoIVEpWhMIgKPzt1XffR8wc2ET8em5OHjkvQEiUPUVYdYqNzHSlRutewXFak0Pk7LsnIGcwxxNOG4AsuAFlzMYl8Kjr4TaL8EMPcMe70SuayVQ"
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "key=$key")
            .post(
                bodyJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            )
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    println("Received data: ${response.body?.string()}")
                    Log.d("TAG", "onResponse: ${response}   ")
                    Log.d("TAG", "onResponse Message: ${response.message}   ")
                }

                override fun onFailure(call: Call, e: IOException) {
                    println(e.message.toString())
                    Log.d("TAG", "onFailure: ${e.message.toString()}")
                }
            }
        )
    }


}