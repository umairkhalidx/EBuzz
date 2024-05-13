package com.umairkhalid.ebuzz

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class recycleview_groups_adapter(val itemslist: ArrayList<recycleview_groups_data>,private val listener: ClickListener_Groups)
    : RecyclerView.Adapter<recycleview_groups_adapter.groups_recycler_viewholder>()
{
    inner class groups_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var group_layout_1 : LinearLayout
        lateinit var group_img_1 : ImageView
        lateinit var groupname_1 : TextView
        var index : Int = 0
        lateinit var leavegroup_btn_1 : Button
        lateinit var viewmore_btn_1 : Button


        init {
            group_layout_1 = itemView.findViewById(R.id.groups_layout_1)
            group_img_1= itemView.findViewById(R.id.groups_groupimage_1)
            groupname_1= itemView.findViewById(R.id.groups_groupname_1)
            leavegroup_btn_1 = itemView.findViewById(R.id.groups_leave_btn_1)
            viewmore_btn_1 = itemView.findViewById(R.id.groups_viewmore_btn_1)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): groups_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_groups_item,parent,false)

        return groups_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: groups_recycler_viewholder, position: Int) {

        // Setting Group Image
//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].groupimage_1)
//            .into(holder.group_img_1)
//        Picasso.get().load(itemslist[position].groupimage_1).into(holder.group_img_1)
        Picasso.get()
            .load(itemslist[position].groupimage_1.toString())
            .into(holder.group_img_1, object : Callback {
                override fun onSuccess() {
                    // Image loaded successfully
                }

                override fun onError(e: Exception?) {
                    // Error loading image
                    Log.e("Picasso", "Error loading image: ${e?.message}")
                }
            })
        // Setting the rest
        holder.index=itemslist[position].count
        holder.groupname_1.text = itemslist[position].groupname_1

        val gID = itemslist[position].groupID
        val gName = itemslist[position].groupname_1.toString()
        val gImage = itemslist[position].groupimage_1.toString()

        holder.group_img_1.setOnClickListener{
            listener.onClickGroups_fun(position,"",0, gID, gName, gImage)
        }

        holder.groupname_1.setOnClickListener{
            listener.onClickGroups_fun(position,"",0, gID, gName, gImage)
        }

        holder.leavegroup_btn_1.setOnClickListener{
            listener.onClickGroups_fun(position,"",1, gID, gName, gImage)
        }

        holder.viewmore_btn_1.setOnClickListener{
            listener.onClickGroups_fun(position,"",0, gID, gName, gImage)
        }

    }
}