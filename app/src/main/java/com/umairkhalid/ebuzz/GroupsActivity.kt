package com.umairkhalid.ebuzz

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class GroupsActivity : AppCompatActivity() , ClickListener_Groups{

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var groupsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        val back_btn = findViewById<ImageButton>(R.id.groups_back_btn)
        val search_btn = findViewById<ImageButton>(R.id.groups_searchbar_btn)
        val search_txt = findViewById<EditText>(R.id.groups_searchbar_txt)

        back_btn.setOnClickListener{
            onBackPressed()
            finish()
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        groupsRef = database.getReference("groups")
        var userID = auth.currentUser?.uid.toString()

        val recyclerView : RecyclerView = findViewById(R.id.groups_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,
            false
        )

//        val v1  = recycleview_groups_data("","Group 1","","Group 2",2)
//        val v2  = recycleview_groups_data("","Group 3","","Group 4",2)
//        val v3  = recycleview_groups_data("","Group 5","","Group 6",2)
//        val v4  = recycleview_groups_data("","Group 7","","Group 8",2)
//        val v5  = recycleview_groups_data("","Group 9","","",1)
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//        adapter_data_list.add(v4)
//        adapter_data_list.add(v5)

        groupsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    Log.d("Log Event Inside onDataChange Event", "Starting getting groups data")
                    // This method is called when data changes at the `users` node
                    var adapter_data_list : ArrayList<recycleview_groups_data> = ArrayList()

                    // Iterate through each child node (user) in the `users` node
                    for (userSnapshot in dataSnapshot.children) {

                        // Retrieve the image URL attribute
                        val groupID: String = userSnapshot.child("groupID").getValue(String::class.java) ?: ""
                        val groupImage: String = userSnapshot.child("group_image").getValue(String::class.java) ?: ""
                        val groupName: String = userSnapshot.child("group_name").getValue(String::class.java) ?: ""
                        Log.d("Log Event Gotten Group image url:", "Image Url is $groupImage")
                        Log.d("Log Event Gotten Name:", "Name is $groupName")

                        // Create a data class instance with the retrieved values
                        val groupData = recycleview_groups_data(groupID, groupImage, groupName, 1)
                        adapter_data_list.add(groupData)
                    }

                    Log.d("Starting Adapter", "Initialising")
                    val adapter = recycleview_groups_adapter(adapter_data_list, this@GroupsActivity)
                    recyclerView.adapter = adapter
                } catch (e: Exception) {
                    Log.e("Error in onDataChange", e.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                Log.e("Firebase", "Failed to retrieve user data: ${databaseError.message}")
            }
        })


//        val adapter = recycleview_groups_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter
    }

    override fun onClickGroups_fun(position: Int, username:String, operation:Int, groupID: String, groupName: String, groupImage: String)
    {
        if(operation==0){
            Log.d("Going to GroupMessagingActivity of Group Name: $groupName", "Starting now")
            val intent = Intent(this, GroupMessagingActivity::class.java)
            intent.putExtra("GROUP_ID", groupID)
            intent.putExtra("GROUP_NAME", groupName)
            intent.putExtra("GROUP_IMAGE", groupImage)
            startActivity(intent)
        }
        else if (operation==1){
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to leave this group?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                // Handle 'Yes' button click
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                // Handle 'No' button click
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }


    }
}


//class GroupsActivity : AppCompatActivity() , ClickListner{
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_groups)
//
//        val back_btn = findViewById<ImageButton>(R.id.groups_back_btn)
//        val search_btn = findViewById<ImageButton>(R.id.groups_searchbar_btn)
//        val search_txt = findViewById<EditText>(R.id.groups_searchbar_txt)
//
//        back_btn.setOnClickListener{
//            onBackPressed()
//            finish()
//        }
//
//        var adapter_data_list : ArrayList<recycleview_groups_data> = ArrayList()
//
//        val recyclerView : RecyclerView = findViewById(R.id.groups_recycleview)
//        recyclerView.layoutManager = LinearLayoutManager(this,
//            LinearLayoutManager.VERTICAL,
//            false
//        )
//
//        val v1  = recycleview_groups_data("","Group 1","","Group 2",2)
//        val v2  = recycleview_groups_data("","Group 3","","Group 4",2)
//        val v3  = recycleview_groups_data("","Group 5","","Group 6",2)
//        val v4  = recycleview_groups_data("","Group 7","","Group 8",2)
//        val v5  = recycleview_groups_data("","Group 9","","",1)
//
//
//        adapter_data_list.add(v1)
//        adapter_data_list.add(v2)
//        adapter_data_list.add(v3)
//        adapter_data_list.add(v4)
//        adapter_data_list.add(v5)
//
//
//
//        val adapter = recycleview_groups_adapter(adapter_data_list,this)
//        recyclerView.adapter = adapter
//
//        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_layout)
//        recyclerView.startAnimation(slideUp)
//
//
//    }
//
//    override fun onCLick_fun(position: Int,username:String,operation:Int)
//    {
//        if(operation==0){
//            val intent = Intent(this, GroupMessagingActivity::class.java)
//            startActivity(intent)
//        }
//        else if (operation==1){
//            val builder = AlertDialog.Builder(this)
//
//            builder.setTitle("Confirmation")
//            builder.setMessage("Are you sure you want to leave this group?")
//
//            builder.setPositiveButton("Yes") { dialog, _ ->
//                // Handle 'Yes' button click
//                dialog.dismiss()
//            }
//
//            builder.setNegativeButton("No") { dialog, _ ->
//                // Handle 'No' button click
//                dialog.dismiss()
//            }
//
//            val dialog = builder.create()
//            dialog.show()
//        }
//
//
//    }
//}