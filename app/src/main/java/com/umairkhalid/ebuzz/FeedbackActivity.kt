package com.umairkhalid.ebuzz

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_feedback)

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

        val back_btn = findViewById<ImageButton>(R.id.feedback_back_btn)
        val submit_btn = findViewById<Button>(R.id.feedback_submit_btn)
        val email_txt = findViewById<EditText>(R.id.feedback_email_txt)
        val description =findViewById<EditText>(R.id.feedback_description_txt)
        val rating_bar = findViewById<RatingBar>(R.id.feedback_ratingbar)

        val feedback_layout = findViewById<LinearLayout>(R.id.feedback_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        feedback_layout.startAnimation(slideUp)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        submit_btn.setOnClickListener{

            val email = email_txt.text.toString().trim()
            val description_txt=description.text.toString().trim()
            val rating = rating_bar.rating.toString()

            if (description_txt.isNotEmpty() && email.isNotEmpty()){

                val database_C = FirebaseDatabase.getInstance()
                val credentialsRef = database_C.getReference("credentials")

                credentialsRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {

                            val userSnapshot = dataSnapshot.children.first()
                            // Get the userID field from the userSnapshot
                            val userID = userSnapshot.child("userID").getValue(String::class.java)

                            val database = FirebaseDatabase.getInstance()
                            val feedbackRef = database.getReference("feedbacks")

                            // Get current user ID
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val id = currentUser?.uid.toString()

                            if(userID == id){

                                // Get current date and time
                                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                                val dateTime = "$currentDate $currentTime"


                                val feedbackKey = feedbackRef.push().key.toString()

                                feedbackRef.child(feedbackKey).setValue(null)
                                feedbackRef.child(feedbackKey).child("userID").setValue(id)
                                feedbackRef.child(feedbackKey).child("email").setValue(email)
                                feedbackRef.child(feedbackKey).child("description").setValue(description_txt)
                                feedbackRef.child(feedbackKey).child("feedback").setValue(rating)
                                feedbackRef.child(feedbackKey).child("time").setValue(dateTime)


                                Toast.makeText(this@FeedbackActivity,"Feedback Submitted Successfully", Toast.LENGTH_LONG).show()

                                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        return@addOnCompleteListener
                                    }
                                    val token = task.result
                                    sendPushNotification(
                                        token,
                                        "EBuzz",
                                        "Subtitle: Class",
                                        "Thank You for Submitting Your Feedback",
                                        mapOf("key1" to "value1", "key2" to "value2")
                                    )

                                }

                                onBackPressed()
                                finish()
                            }
                            else{
                                Toast.makeText(this@FeedbackActivity,"Please Enter your Email Address", Toast.LENGTH_LONG).show()
                            }



                        } else {
                            // No user found with the specified email address
                            Toast.makeText(this@FeedbackActivity,"Email Address Not Found", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors
                        Toast.makeText(this@FeedbackActivity,"Please try Again", Toast.LENGTH_LONG).show()
                        Log.d("Feedback Error", databaseError.message)

                    }
                })


            }else{
                Toast.makeText(this,"Please fill in all fields", Toast.LENGTH_LONG).show()
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