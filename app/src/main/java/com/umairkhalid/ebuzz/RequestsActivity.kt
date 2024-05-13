package com.umairkhalid.ebuzz

import android.Manifest
import android.content.pm.PackageManager
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

class RequestsActivity : AppCompatActivity(), ClickListner {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter_data_list: ArrayList<recycleview_requests_data>

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
        setContentView(R.layout.activity_requests)

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

        val back_btn = findViewById<ImageButton>(R.id.requests_back_btn)

        back_btn.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }


        adapter_data_list = ArrayList()

       recyclerView = findViewById(R.id.requests_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            true
        )

        var currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users").child(userId).child("requests")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (requestSnapshot in dataSnapshot.children) {
                    val user = requestSnapshot.key.toString()

                    var database = FirebaseDatabase.getInstance()
                    val my_ref = database.getReference("users")
                    var pic_url: String = ""
                    var cover_url: String = ""

                    if (userId != null) {

                        my_ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val name = dataSnapshot.child(user).child("name").value.toString()
                                val id = dataSnapshot.child(user).child("userID").value.toString()
                                pic_url = dataSnapshot.child(user).child("picture").value.toString()

                                if (pic_url.length > 10) {

                                    val data = recycleview_requests_data(id,pic_url, name)
                                    adapter_data_list.add(data)

                                    val adapter = recycleview_requests_adapter(adapter_data_list,this@RequestsActivity)
                                    recyclerView.adapter = adapter
                                }
                                else{
                                    val data = recycleview_requests_data(id,"", name)
                                    adapter_data_list.add(data)

                                    val adapter = recycleview_requests_adapter(adapter_data_list,this@RequestsActivity)
                                    recyclerView.adapter = adapter
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error
                                Log.d("TAG", "Unable to retrieve Data")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                println("Error fetching usernames: ${databaseError.message}")
                Toast.makeText(this@RequestsActivity, "Unable To Fetch Data", Toast.LENGTH_LONG)
                    .show()
            }
        })

//        val v1 = recycleview_requests_data("", "John Doe")
//        val v2 = recycleview_requests_data("", "Emma Phillips")
//        val v3 = recycleview_requests_data("", "John Cooper")
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//
//        // 3- Adapter
//        val adapter = recycleview_requests_adapter(adapter_data_list)
//        recyclerView.adapter = adapter
//
//        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
//        recyclerView.startAnimation(slideUp)
    }

    override fun onCLick_fun(position: Int, userid: String, operation: Int) {
        recyclerView.adapter = null
        recyclerView.layoutManager = null

        if(operation==1){

            val database = FirebaseDatabase.getInstance()
            var currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid.toString()
            val usersRef = database.getReference("users").child(userId)

//            usersRef.child(userId).child("friends").setValue(null)
            usersRef.child("friends").child(userid).setValue(true)

            val Ref = database.getReference("users").child(userid)

            Ref.child("friends").child(userId).setValue(true)

            var currentUser2 = FirebaseAuth.getInstance().currentUser
            val uid = currentUser2?.uid.toString()
            val database2 = FirebaseDatabase.getInstance()
            val usersRef2 = database2.getReference("users").child(uid).child("requests")

            usersRef2.child(userid).removeValue()
                .addOnSuccessListener {
//                    adapter_data_list.removeAt(position)
//                    recyclerView.adapter?.notifyItemRemoved(position)
                    Toast.makeText(this@RequestsActivity,"Request Accepted",Toast.LENGTH_LONG).show()

                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@addOnCompleteListener
                        }
                        val token = task.result
                        sendPushNotification(
                            token,
                            "EBuzz",
                            "Subtitle: Class",
                            "You have made 1 new friend",
                            mapOf("key1" to "value1", "key2" to "value2")
                        )

                    }

                    onBackPressed()
                    finish()

                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Toast.makeText(this@RequestsActivity,"Please Try Again",Toast.LENGTH_LONG).show()
                    Log.e("RemoveEntry", "Error removing type entry: ${e.message}")
                }

        }else if (operation==2){

            var currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid.toString()
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users").child(uid).child("requests")

            usersRef.child(userid).removeValue()
                .addOnSuccessListener {
//                    adapter_data_list.removeAt(position)
//                    recyclerView.adapter?.notifyItemRemoved(position)
                    Toast.makeText(this@RequestsActivity,"Request Declined",Toast.LENGTH_LONG).show()
                    onBackPressed()
                    finish()

                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Toast.makeText(this@RequestsActivity,"Please Try Again",Toast.LENGTH_LONG).show()
                    Log.e("RemoveEntry", "Error removing type entry: ${e.message}")
                }
        }

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