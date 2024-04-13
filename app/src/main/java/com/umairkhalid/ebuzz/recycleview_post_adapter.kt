package com.umairkhalid.ebuzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class recycleview_post_adapter (val itemslist: ArrayList<recycleview_post_data>,private val listener: ClickListner)
    : RecyclerView.Adapter<recycleview_post_adapter.post_recycler_viewholder>()
{
    inner class post_recycler_viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var user_image : ImageView
        lateinit var user_name : TextView
        lateinit var post_image : ImageView
        lateinit var post_video : VideoView
        lateinit var post_text : TextView
        lateinit var description : TextView
        var post_type : Int = 0
        var status :Int = 0

        lateinit var heart_btn : ImageButton
        lateinit var comment_btn : ImageButton
        lateinit var send_btn : ImageButton

        init {
            user_image= itemView.findViewById(R.id.post_user_image)
            user_name = itemView.findViewById(R.id.post_user_name)
            post_image = itemView.findViewById(R.id.post_image_view)
            post_video = itemView.findViewById(R.id.post_video_view)
            post_text= itemView.findViewById(R.id.post_text_view)
            description= itemView.findViewById(R.id.post_description)

            heart_btn = itemView.findViewById(R.id.post_heart_btn)
            comment_btn = itemView.findViewById(R.id.post_comment_btn)
            send_btn = itemView.findViewById(R.id.post_send_btn)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): post_recycler_viewholder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_post_item,parent,false)

        return post_recycler_viewholder(v)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }

    override fun onBindViewHolder(holder: post_recycler_viewholder, position: Int) {

//        Glide.with(holder.itemView.context)
//            .load(itemslist[position].img)
//            .into(holder.display_pic)
//        holder.message.setText(itemslist[position].message)
        holder.user_name.setText((itemslist[position].username))
        if(itemslist[position].post_type == 0 ){
            holder.post_image.visibility=View.GONE
            holder.post_video.visibility=View.GONE
            holder.post_text.visibility=View.VISIBLE
            holder.description.visibility=View.GONE


        }

        holder.comment_btn.setOnClickListener{
            listener.onCLick_fun(position,"",1)
        }
        holder.send_btn.setOnClickListener{
            listener.onCLick_fun(position,"",2)
        }


    }
}