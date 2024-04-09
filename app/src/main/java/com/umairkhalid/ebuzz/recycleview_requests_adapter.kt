package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class recycleview_requests_adapter (val itemslist: ArrayList<recycleview_requests_data>)
    : RecyclerView.Adapter<recycleview_requests_adapter.requests_recycler_viewholder>()
{
    inner class requests_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var user_img : ImageView
        lateinit var username : TextView

        init {
            user_img= itemView.findViewById(R.id.item_user_img)
            username= itemView.findViewById(R.id.item_username)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): requests_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_requests_item,parent,false)

        return requests_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: requests_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.username.setText(itemslist[position].username)

    }
}