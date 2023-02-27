package com.example.mygithubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuser.data.remote.response.ItemsItem
import com.example.mygithubuser.databinding.ItemUserBinding
import com.example.mygithubuser.ui.activity.DetailUserActivity
import com.example.mygithubuser.ui.activity.DetailUserActivity.Companion.EXTRA_USERNAME

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.ViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )
    }
    override fun onBindViewHolder(view: ViewHolder, position: Int) = with(view){
        bind(getItem(position))
    }

    // merepresentasikan setiap item pada daftar pengguna
    inner class ViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.apply {
                tvUserUsername.text = user.login
            }

            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .into(binding.imgUserPhoto)

            itemView.setOnClickListener {
                itemView.context.startActivity(
                    Intent(itemView.context, DetailUserActivity::class.java)
                        .putExtra(EXTRA_USERNAME, user.login)
                )
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem.login == newItem.login
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}