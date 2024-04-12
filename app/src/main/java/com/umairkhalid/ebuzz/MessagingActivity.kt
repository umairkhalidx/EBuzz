package com.umairkhalid.ebuzz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class MessagingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        val back_btn = findViewById<ImageButton>(R.id.messaging_back_btn)
        val user_img = findViewById<ImageView>(R.id.messaging_user_img)
        val username = findViewById<TextView>(R.id.messaging_username_txt)
        val audio_call_btn = findViewById<ImageButton>(R.id.messaging_audiocall_btn)
        val video_call_btn = findViewById<ImageButton>(R.id.messaging_videocall_btn)
        val camera_btn = findViewById<ImageButton>(R.id.messaging_camera_btn_1)
        val send_btn = findViewById<ImageButton>(R.id.messaging_send_btn_1)
        val message_txt = findViewById<EditText>(R.id.messaging_message_txt)
        val audio_btn = findViewById<ImageButton>(R.id.messaging_mic_btn)
        val gallery_btn = findViewById<ImageButton>(R.id.messaging_gallery_btn)

        back_btn.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        audio_call_btn.setOnClickListener{
            val intent = Intent(this, AudioCallActivity::class.java)
            startActivity(intent)
        }

        video_call_btn.setOnClickListener{
            val intent = Intent(this, VIdeoCallActivity::class.java)
            startActivity(intent)
        }


    }
}