package com.umairkhalid.ebuzz

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

class SettingsActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_settings)

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

        val back_btn = findViewById<ImageButton>(R.id.settings_back_btn)
        val account_btn = findViewById<Button>(R.id.settings_account_btn)
        val password_btn = findViewById<Button>(R.id.settings_password_btn)
        val report_btn = findViewById<Button>(R.id.settings_report_btn)
        val feedback_btn = findViewById<Button>(R.id.settings_feedback_btn)
        val deactivate_btn = findViewById<Button>(R.id.settings_deactivate_btn)


        val settings_layout = findViewById<LinearLayout>(R.id.settings_layout)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
        settings_layout.startAnimation(slideUp)

        back_btn.setOnClickListener {
            onBackPressed()
            finish()
        }

        password_btn.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        feedback_btn.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }

        report_btn.setOnClickListener {
            val intent = Intent(this, ReportProblemActivity::class.java)
            startActivity(intent)
        }

        deactivate_btn.setOnClickListener {
            val intent = Intent(this, DeleteAccountActivity::class.java)
            startActivity(intent)
        }

        account_btn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            val dialogView = layoutInflater.inflate(R.layout.account_settings_menu, null)
            dialogBuilder.setView(dialogView)

            val profiletype_spinner =
                dialogView.findViewById<Spinner>(R.id.settingsmenu_profile_type)
            profiletype_spinner.prompt = "Select Profile Type"
            val types = arrayOf("Select Profile Type", "Public", "Private")

            val profiletype_adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            profiletype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            profiletype_spinner.adapter = profiletype_adapter

            profiletype_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        profiletype_spinner.prompt = types[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        profiletype_spinner.prompt = "Select Profile Type"
                    }
                }

            val profilecategory_spinner =
                dialogView.findViewById<Spinner>(R.id.settingsmenu_profile_category)
            profilecategory_spinner.prompt = "Select Profile Category"
            val categories = arrayOf(
                "Select Profile Category",
                "Creator",
                "Educator",
                "Entertainer",
                "Influencer"
            )

            val profilecategory_adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
            profilecategory_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            profilecategory_spinner.adapter = profilecategory_adapter

            profilecategory_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        profilecategory_spinner.prompt = categories[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        profilecategory_spinner.prompt = "Select Profile Category"
                    }
                }

            dialogBuilder.setPositiveButton("Save") { dialogInterface, _ ->

                val profiletype_txt = profiletype_spinner.selectedItem.toString().trim()
                val profilecategory_txt = profilecategory_spinner.selectedItem.toString().trim()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("users")
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userId = currentUser?.uid

                if (profiletype_txt != "Select Profile Type") {
                    if (userId != null) {
                        myRef.child(userId).child("profiletype").setValue(profiletype_txt)
                    }

                }
                if (profilecategory_txt != "Select Profile Category") {
                    if (userId != null) {
                        myRef.child(userId).child("category").setValue(profilecategory_txt)
                    }
                }

                Toast.makeText(this, "Profile Data Updated", Toast.LENGTH_LONG).show()

                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }
                    val token = task.result
                    sendPushNotification(
                        token,
                        "EBuzz",
                        "Subtitle: Class",
                        "New Settings Added",
                        mapOf("key1" to "value1", "key2" to "value2")
                    )

                }
                dialogInterface.dismiss()
                onBackPressed()
                finish()
            }

            dialogBuilder.setNegativeButton("Cancel") { dialogInterface, _ ->
                // Handle 'Cancel' button click
                dialogInterface.dismiss()
            }

            val alertDialog = dialogBuilder.create()
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.account_settings_menu_round)

            alertDialog.show()
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