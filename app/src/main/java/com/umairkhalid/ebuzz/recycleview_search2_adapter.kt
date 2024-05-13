package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class recycleview_search2_adapter (val itemslist: ArrayList<recycleview_search_data>, private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_search2_adapter.search_recycler_viewholder>()
{
    inner class search_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var user_img : ImageView
        lateinit var username : TextView
        lateinit var type : TextView

        val viewmore_btn = itemView.findViewById<Button>(R.id.search_viewmore_btn)

        init {
            user_img= itemView.findViewById(R.id.item_user_img)
            username= itemView.findViewById(R.id.item_username)
            type = itemView.findViewById(R.id.item_profileType)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): search_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_search_item,parent,false)

        return search_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: search_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
        holder.username.setText(itemslist[position].username)
        holder.type.setText(itemslist[position].type)

        try {
            Picasso.get().load(itemslist[position].img).into(holder.user_img)
        } catch (e: Exception) {

        }


        holder.viewmore_btn.setOnClickListener{
            if(itemslist[position].category==0){
                listener.onCLick_fun(position,itemslist[position].id.toString(),itemslist[position].category)
            }
            else if(itemslist[position].category==1){
                listener.onCLick_fun(position,itemslist[position].username.toString(),itemslist[position].category)
            }
        }

    }
}