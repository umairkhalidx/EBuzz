package com.umairkhalid.ebuzz

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class FriendsListActivity : AppCompatActivity(), ClickListner {

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
        setContentView(R.layout.activity_friends_list)

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

        val back_btn = findViewById<ImageButton>(R.id.friendslist_back_btn)
//        val search_text= findViewById<TextView>(R.id.friendslist_searchbar_txt)
//        val search_btn=findViewById<ImageButton>(R.id.friendslist_searchbar_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show()

            val sharedPref = getSharedPreferences("FRIENDSLIST_PREF", Context.MODE_PRIVATE)
            val json = sharedPref.getString("FRIENDSLIST", "")

            if(!(json.isNullOrEmpty())){
                val gson = Gson()
                val type = object : TypeToken<ArrayList<recycleview_friendslist_data>>() {}.type

                var adapter_data_list : ArrayList<recycleview_friendslist_data> = gson.fromJson(json, type)

                val recyclerView : RecyclerView = findViewById(R.id.friendslist_recycleview)
                recyclerView.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                // 3- Adapter
                val adapter = recycleview_friendslist_adapter(adapter_data_list,this@FriendsListActivity)
                recyclerView.adapter = adapter
            }

        }


        var adapter_data_list : ArrayList<recycleview_friendslist_data> = ArrayList()

        val recyclerView : RecyclerView = findViewById(R.id.friendslist_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()

        val friendsRef = database.getReference("users").child(userId).child("friends")

        friendsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (friendSnapshot in dataSnapshot.children) {
                    val friendId = friendSnapshot.key.toString()

                    val database2 = FirebaseDatabase.getInstance()
                    val usersRef2 = database2.getReference("users")
                    usersRef2.child(friendId).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val userName = dataSnapshot.child("name").getValue(String::class.java).toString()
                                val userImage = dataSnapshot.child("picture").getValue(String::class.java).toString()
                                val data  = recycleview_friendslist_data(friendId,userImage,userName)
                                adapter_data_list.add(data)

                                // 3- Adapter
                                val adapter = recycleview_friendslist_adapter(adapter_data_list,this@FriendsListActivity)
                                recyclerView.adapter = adapter

                                val gson = Gson()
                                val json = gson.toJson(adapter_data_list)
                                val sharedPref = getSharedPreferences("FRIENDSLIST_PREF", Context.MODE_PRIVATE)
                                val editor = sharedPref.edit()
                                editor.putString("FRIENDSLIST", json)
                                editor.apply()

                            } else {
                                Toast.makeText(this@FriendsListActivity,"User Does not Exsist",Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                            Toast.makeText(this@FriendsListActivity,"Unable to Fetch Data",Toast.LENGTH_LONG).show()
                        }
                    })

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Toast.makeText(this@FriendsListActivity, "Unable to Fetch Friends Data", Toast.LENGTH_LONG).show()
            }
        })



//        val v1  = recycleview_friendslist_data("","John Doe")
//        val v2  = recycleview_friendslist_data("","Emma Phillips")
//        val v3  = recycleview_friendslist_data("","John Cooper")
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//
//        // 3- Adapter
//        val adapter = recycleview_friendslist_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter
//
//        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
//        recyclerView.startAnimation(slideUp)

    }

    override fun onCLick_fun(position: Int,userid:String,operation:Int)
    {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("USERID", userid)
        startActivity(intent)

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