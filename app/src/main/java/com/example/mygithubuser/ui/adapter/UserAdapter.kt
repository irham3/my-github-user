package com.example.mygithubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mygithubuser.R
import com.example.mygithubuser.data.remote.response.ItemsItem

class UserAdapter (private val listUser: List<ItemsItem>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(viewHolder.itemView.context)
            .load(user.avatarUrl)
            .apply(RequestOptions().override(55, 55))
            .into(viewHolder.imgUserPhoto)

        viewHolder.tvUserUsername.text = user.login
        viewHolder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(user.login?:"")}
    }

    override fun getItemCount() = listUser.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgUserPhoto: ImageView = view.findViewById(R.id.img_user_photo)
        val tvUserUsername: TextView = view.findViewById(R.id.tv_user_username)
    }

}