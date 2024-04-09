package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class GroupMessagingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_messaging)

        val back_btn = findViewById<ImageButton>(R.id.groupmessaging_back_btn)
        val user_img = findViewById<ImageView>(R.id.groupmessaging_user_img)
        val username = findViewById<TextView>(R.id.groupmessaging_username_txt)
        val camera_btn = findViewById<ImageButton>(R.id.groupmessaging_camera_btn_1)
        val send_btn = findViewById<ImageButton>(R.id.groupmessaging_send_btn_1)
        val message_txt = findViewById<EditText>(R.id.groupmessaging_message_txt)
        val audio_btn = findViewById<ImageButton>(R.id.groupmessaging_mic_btn)
        val gallery_btn = findViewById<ImageButton>(R.id.groupmessaging_gallery_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

    }
}