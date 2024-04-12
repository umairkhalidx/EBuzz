package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView

class VIdeoCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        val username =findViewById<TextView>(R.id.videocall_username_txt)
        val reciever_video =findViewById<VideoView>(R.id.videocall_receiver_video)
//        val called_video =findViewById<VideoView>(R.id.videocall_caller_video)
        val timer =findViewById<TextView>(R.id.videocall_timer_txt)
        val mute_btn =findViewById<ImageButton>(R.id.videocall_mute_btn)
        val speaker_btn =findViewById<ImageButton>(R.id.videocall_speaker_btn)
        val close_btn =findViewById<ImageButton>(R.id.videocall_close_btn)

        close_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

    }
}