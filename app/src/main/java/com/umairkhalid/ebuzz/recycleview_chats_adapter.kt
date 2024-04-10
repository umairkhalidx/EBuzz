package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class recycleview_chats_adapter (val itemslist: ArrayList<recycleview_chats_data>,private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_chats_adapter.chats_recycler_viewholder>()
{
    inner class chats_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rootView: View = itemView
        lateinit var user_img : ImageView
        lateinit var username : TextView

        init {
            user_img= itemView.findViewById(R.id.item_user_img)
            username = itemView.findViewById(R.id.item_username)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chats_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_chats_item,parent,false)

        return chats_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: chats_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.username.setText(itemslist[position].username)

        holder.rootView.setOnClickListener{
            listener.onCLick_fun(position,"")
        }

    }
}