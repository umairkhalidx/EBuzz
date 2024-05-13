package com.umairkhalid.ebuzz

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class CreatePageActivity : AppCompatActivity() {
    private var mAuth = FirebaseAuth.getInstance();

    private val PICK_IMAGE_REQUEST = 2
    private var imageUri: Uri? = null
    var status = false

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
        setContentView(R.layout.activity_create_page)

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

        val back_btn = findViewById<ImageButton>(R.id.createpage_back_btn)
        val name_txt = findViewById<EditText>(R.id.createpage_pagename_txt)
        val description = findViewById<EditText>(R.id.createpage_description_txt)
        val upload_btn = findViewById<Button>(R.id.createpage_upload_photo_btn)
        val create_btn =findViewById<Button>(R.id.createpage_create_btn)
        val page_type_spinner = findViewById<Spinner>(R.id.createpage_pagetype_spinner)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        val createpage_layout=findViewById<LinearLayout>(R.id.createpage_layout)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        createpage_layout.startAnimation(slideUp)

        page_type_spinner.prompt = "Select Page Type"
        val page_types = arrayOf("Select Page Type","Educational", "Business", "Motivational",
            "Entertainment","Other")

        val page_type_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, page_types)
        page_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        page_type_spinner.adapter = page_type_adapter

        page_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                page_type_spinner.prompt = page_types[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                page_type_spinner.prompt = "Select Page Type"
            }
        }

        upload_btn.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        create_btn.setOnClickListener{
            val pageName = name_txt.text.toString().trim()
            val description_txt = description.text.toString().trim()
            val pageType = page_type_spinner.selectedItem.toString().trim()

            if(pageName.isNotEmpty() && description_txt.isNotEmpty() && pageType.isNotEmpty()){

                if(status == true){

                    Toast.makeText(this,"Creating Page...",Toast.LENGTH_LONG).show()


                    val database = FirebaseDatabase.getInstance()
                    val curr = mAuth.currentUser
                    val owner= curr?.uid.toString()
                    val pagesRef = database.getReference("pages")
                    val imageURL = imageUri.toString()
                    val pageKey = pagesRef.push().key

                    val userRef = database.getReference("users").child(owner)
                    userRef.child("page").setValue(pageKey)


                    pagesRef.child(pageName).setValue(null)
                    pagesRef.child(pageName).child("owner").setValue(owner)
                    pagesRef.child(pageName).child("name").setValue(pageName)
                    pagesRef.child(pageName).child("type").setValue(pageType)
                    pagesRef.child(pageName).child("description").setValue(description_txt)
                    pagesRef.child(pageName).child("key").setValue(pageKey)
                    pagesRef.child(pageName).child("photo").setValue(imageURL)


                    val storageRef = FirebaseStorage.getInstance().reference

                    val postImageRef = storageRef.child("page_images/$owner.jpg")
                    val uploadTask = postImageRef.putFile(imageUri!!)

                    uploadTask.addOnSuccessListener { taskSnapshot ->

                        Toast.makeText(this,"Please Wait....",Toast.LENGTH_SHORT).show()

                        postImageRef.downloadUrl.addOnSuccessListener { uri ->

                            val image_url = uri.toString()
                            val database = FirebaseDatabase.getInstance()
                            val myRef = database.getReference("pages/$pageName/photo")

                            myRef.setValue(image_url).addOnSuccessListener {

                                Toast.makeText(this@CreatePageActivity,"Page Created Successfully", Toast.LENGTH_LONG).show()

                                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        return@addOnCompleteListener
                                    }
                                    val token = task.result
                                    sendPushNotification(
                                        token,
                                        "EBuzz",
                                        "Subtitle: Class",
                                        "Your New Page Has been Created",
                                        mapOf("key1" to "value1", "key2" to "value2")
                                    )

                                }

                                onBackPressed()
                                finish()

                            }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                                    Log.d("TAG", "Failed To Upload Image")

                                }
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Unable to Upload,Please Try Again", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "Failed To Upload Image")

                    }

                }
                else{
                    Toast.makeText(this,"Uploading Image...",Toast.LENGTH_LONG).show()

                }


            }
            else{
                Toast.makeText(this,"Please fill all Fields", Toast.LENGTH_LONG).show()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            // Gallery intent result, handle the selected image
            imageUri = data.data
            status = true

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