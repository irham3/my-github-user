package com.example.mygithubuser.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.R
import com.example.mygithubuser.data.remote.response.ItemsItem
import com.example.mygithubuser.databinding.ActivityMainBinding
import com.example.mygithubuser.ui.adapter.UserAdapter
import com.example.mygithubuser.ui.viewmodel.MainViewModel
import com.example.mygithubuser.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel:MainViewModel by viewModels{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        mainViewModel.listUser.observe(this) { userData ->
            setUserData(userData)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.toastMessage.observe(this) {
            it.getContentIfNotHandled()?.let { toastMessage ->
                Toast.makeText(this@MainActivity, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                mainViewModel.findUsers(query)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_fav_users -> {
                showFavourites()
                true
            }
            R.id.menu_setting -> {
                showSetting()
                true
            }
            else -> true
        }
    }

    private fun setUserData(listUser: List<ItemsItem>) {
        val adapter = UserAdapter(listUser)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                showSelectedUser(username)
            }
        })
    }

    private fun showSelectedUser(username: String) {
        val moveToDetailIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        moveToDetailIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)

        startActivity(moveToDetailIntent)
    }

    private fun showFavourites() {
        val moveToFavouriteIntent = Intent(this@MainActivity, FavouriteUserActivity::class.java)
        startActivity(moveToFavouriteIntent)
    }

    private fun showSetting() {
        val moveToSettingIntent = Intent(this@MainActivity, SettingActivity::class.java)
        startActivity(moveToSettingIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarUsers.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}