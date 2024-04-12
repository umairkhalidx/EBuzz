package com.umairkhalid.ebuzz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class AudioCallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_call)

        val username =findViewById<TextView>(R.id.audiocall_username_txt)
        val reciever_img =findViewById<ImageView>(R.id.audiocall_receiver_img)
        val called_img =findViewById<ImageView>(R.id.audiocall_caller_img)
        val timer =findViewById<TextView>(R.id.audiocall_time_txt)
        val mute_btn =findViewById<ImageButton>(R.id.audiocall_mute_btn)
        val speaker_btn =findViewById<ImageButton>(R.id.audiocall_speaker_btn)
        val close_btn =findViewById<ImageButton>(R.id.audiocall_close_btn)

        close_btn.setOnClickListener{
            onBackPressed()
            finish()
        }
    }
}