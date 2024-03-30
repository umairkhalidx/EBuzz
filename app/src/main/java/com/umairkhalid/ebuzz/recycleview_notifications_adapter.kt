package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recycleview_notifications_adapter(val itemslist: ArrayList<recycleview_notifications_data>)
    : RecyclerView.Adapter<recycleview_notifications_adapter.notifications_recycler_viewholder>()
{
    inner class notifications_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var user_img : ImageView
        lateinit var message : TextView

        init {
            user_img= itemView.findViewById(R.id.item_user_img)
            message = itemView.findViewById(R.id.item_message_txt)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notifications_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_notification_item,parent,false)

        return notifications_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: notifications_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.message.setText(itemslist[position].message)

    }
}