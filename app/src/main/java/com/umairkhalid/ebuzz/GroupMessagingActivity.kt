package com.umairkhalid.ebuzz

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.UUID

class GroupMessagingActivity : AppCompatActivity() {

    private lateinit var group_name: TextView
    private lateinit var group_image: ImageView
    private lateinit var back_btn: ImageButton
    private lateinit var camera_btn: ImageButton
    private lateinit var messageToSend: EditText
    private lateinit var voiceMessage_btn: ImageButton
    private lateinit var gallery_btn: ImageButton
    private lateinit var send_btn: ImageButton
    private lateinit var message_recycle_view: RecyclerView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var messages_ref: DatabaseReference
    private lateinit var storage_ref: StorageReference

    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_REQUEST_CODE = 1002
    private val message_list = mutableListOf<message_data>()
    private var selected_msg_position: Int = -1
    private lateinit var message_adapter: message_adapter

    private lateinit var audio_recorder: message_audio_record

    private val offline_messages = mutableListOf<message_data>()

    //////////////////////////////////////////////////////
    //             On Create Starts here                //
    //////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_group_messaging)

        group_name = findViewById(R.id.groupmessaging_username_txt)
        group_image = findViewById(R.id.groupmessaging_user_img)
        back_btn = findViewById(R.id.groupmessaging_back_btn)
        camera_btn = findViewById(R.id.groupmessaging_camera_btn_1)
        messageToSend = findViewById(R.id.groupmessaging_message_txt)
        voiceMessage_btn = findViewById(R.id.groupmessaging_mic_btn)
        gallery_btn = findViewById(R.id.groupmessaging_gallery_btn)
        send_btn = findViewById(R.id.groupmessaging_send_btn_1)
        message_recycle_view = findViewById(R.id.group_messaging_recycleView)

        Log.d("Inside GroupMessagingActivity", "Now")
        val groupID = intent.getStringExtra("GROUP_ID").toString()
        val groupName = intent.getStringExtra("GROUP_NAME").toString()
        val groupImageUrl = intent.getStringExtra("GROUP_IMAGE").toString()

        group_name.text = groupName
        Picasso.get()
            .load(groupImageUrl)
            .into(group_image)

        //////////////////////////////////////////////////////
        //                Buttons Actions                   //
        //////////////////////////////////////////////////////
        back_btn.setOnClickListener{
//            val intent = Intent(this, GroupsActivity::class.java)
//            startActivity(intent)
            onBackPressed()
            finish()
        }

        //////////////////////////////////////////////////////
        //                Firebase Work Starts              //
        //////////////////////////////////////////////////////
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()

        messages_ref = database.reference.child("groups").child(groupID).child("messages")
        storage_ref = FirebaseStorage.getInstance().reference

        audio_recorder = message_audio_record()

        message_adapter = message_adapter(message_list, object : message_adapter.OnMessageClickListener {
            override fun onMessageClick(position: Int) {
                selected_msg_position = position
                val message = message_list[position]
                if (message.audioUrl != null) {
                    // Play audio
                    play_audio_func(message.audioUrl)
                } else {
                    // Handle other message types
                    show_edit_dialogbox(message)
                }
            }

            override fun onMessageLongClick(position: Int) {
                selected_msg_position = position
                //show_delete_dialog()
            }
        })

        message_recycle_view.apply {
            val layoutManager = LinearLayoutManager(this@GroupMessagingActivity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
//            layoutManager.reverseLayout = true // Display items from bottom
            layoutManager.stackFromEnd = true
            this.layoutManager = layoutManager
            adapter = message_adapter
        }

        // Send message button click listener
        send_btn.setOnClickListener {
            val messageText = messageToSend.text.toString().trim()
            if (messageText.isNotEmpty()) {
                send_message_func(messageText, groupName)
            }
        }

        // Image upload button click listener
        gallery_btn.setOnClickListener {
            pickImageFromGallery()
        }

        // Camera button click listener
        camera_btn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                open_camera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        }

        // Voice message button click listener
        voiceMessage_btn.setOnClickListener {
            //Ask for audio permission here
            if (audio_recorder.isRecording) {
                audio_recorder.stopRecording { audioUri ->
                    if (audioUri != null) {
                        Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show()
                        upload_audio_toFirebase(audioUri)
                    } else {
                        // Handle audio recording failure
                        Toast.makeText(this, "Failed to record audio", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                audio_recorder.startRecording()
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            }
        }

        // Listen for new messages
        messages_ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(message_data::class.java)
                if (message != null) {
                    message_list.add(message)
                    message_adapter.notifyItemInserted(message_list.size - 1)
                    scrollToBottom()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedMessage = snapshot.getValue(message_data::class.java)
                if (updatedMessage != null) {
                    val existingMessage = message_list.find { it.userId == updatedMessage.userId }
                    if (existingMessage != null) {
                        val index = message_list.indexOf(existingMessage)
                        message_list[index] = updatedMessage
                        message_adapter.notifyItemChanged(index)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedMessage = snapshot.getValue(message_data::class.java)
                if (removedMessage != null) {
                    val existingMessage = message_list.find { it.userId == removedMessage.userId }
                    if (existingMessage != null) {
                        val index = message_list.indexOf(existingMessage)
                        message_list.removeAt(index)
                        message_adapter.notifyItemRemoved(index)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    open_camera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    data?.data?.let { imageUri ->
                        upload_img_to_firebase(imageUri)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        val imageUri = getImageUriFromBitmap(it)
                        upload_img_to_firebase(imageUri)
                    }
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Image Title",
            null
        )
        return Uri.parse(path)
    }

    private fun upload_img_to_firebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val imageRef = storage_ref.child("group_chat_images/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = message_data(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        null,
                        downloadUri.toString(),
                        null
                    )
                    messages_ref.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun upload_audio_toFirebase(audioUri: Uri) {

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val audioRef = storage_ref.child("group_audio_files/${UUID.randomUUID()}")
            val uploadTask = audioRef.putFile(audioUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                audioRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = message_data(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        downloadUri.toString(),
                        null,
                        null
                    )
                    messages_ref.push().setValue(message)
                    Log.d("AudioUpload", "Audio uploaded successfully: $downloadUri")
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload audio", Toast.LENGTH_SHORT).show()
                    Log.e("AudioUpload", "Failed to upload audio")
                }
            }
        }
    }

    private fun update_message(oldMessage: message_data, newMessageText: String) {
        val messageRef = messages_ref.child(oldMessage.userId ?: "")
        val updatedMessage = oldMessage.copy(messageText = newMessageText)
        messageRef.setValue(updatedMessage)
            .addOnSuccessListener {
                // Update local list and notify adapter
                val index = message_list.indexOf(oldMessage)
                if (index != -1) {
                    message_list[index] = updatedMessage
                    message_adapter.notifyItemChanged(index)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update message", Toast.LENGTH_SHORT).show()
            }
    }

    private fun delete_message(message: message_data) {
        val messageRef = messages_ref.child(message.userId ?: "")
        messageRef.removeValue()
            .addOnSuccessListener {
                // Remove from local list and notify adapter
                val index = message_list.indexOf(message)
                if (index != -1) {
                    message_list.removeAt(index)
                    message_adapter.notifyItemRemoved(index)
                } else {
                    // Message not found in the list
                    // Handle this case if needed
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete message", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        audio_recorder.cancelRecording()
    }

    private fun open_camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun scrollToBottom() {
        message_recycle_view.scrollToPosition(message_adapter.itemCount - 1)
    }

    private fun send_message_func(messageText: String,mentor_name:String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val message = message_data(userId, messageText, System.currentTimeMillis(), null, null, null)
            messages_ref.push().setValue(message)
                .addOnSuccessListener {
                    messageToSend.text.clear()
                }
                .addOnFailureListener {
                    // Handle message sending failure
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                    // Add message to offline list
                    offline_messages.add(message)
                }
        }
    }

    private fun play_audio_func(audioUrl: String) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepare()
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }

    private fun show_edit_dialogbox(message: message_data) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit this Message")

        val input = EditText(this)
        input.setText(message.messageText)
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, which ->

            val editedMessage = input.text.toString().trim()

            if (editedMessage.isNotEmpty()) {
                update_message(message, editedMessage)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

}