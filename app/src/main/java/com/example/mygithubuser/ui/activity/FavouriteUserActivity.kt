package com.example.mygithubuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.R
import com.example.mygithubuser.data.local.entity.FavouriteUser
import com.example.mygithubuser.data.remote.response.ItemsItem
import com.example.mygithubuser.databinding.ActivityFavouriteUserBinding
import com.example.mygithubuser.ui.adapter.UserAdapter
import com.example.mygithubuser.ui.viewmodel.FavouriteUserViewModel
import com.example.mygithubuser.ui.viewmodel.ViewModelFactory

class FavouriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteUserBinding
    private val favouriteUserViewModel: FavouriteUserViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favourite_users_title)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUsers.layoutManager = layoutManager

        favouriteUserViewModel.getAllFavourites().observe(this) { userList: List<FavouriteUser> ->
            showFavouriteUsers(userList)
        }
    }

    private fun showFavouriteUsers(userList: List<FavouriteUser>) {
        val items = arrayListOf<ItemsItem>()
        userList.map {
            val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
            items.add(item)
        }

        val adapter = UserAdapter()
        binding.rvFavUsers.adapter = adapter

    }

}