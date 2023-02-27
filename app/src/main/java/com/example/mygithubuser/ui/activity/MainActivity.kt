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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.R
import com.example.mygithubuser.databinding.ActivityMainBinding
import com.example.mygithubuser.ui.adapter.UserAdapter
import com.example.mygithubuser.ui.viewmodel.MainViewModel
import com.example.mygithubuser.ui.viewmodel.ViewModelFactory
import com.example.mygithubuser.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val mainViewModel:MainViewModel by viewModels{
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()
        showRecyclerView()
        showUsers(getString(R.string.search_default))
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
                showUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
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

    private fun showUsers(keyword: String) {
        CoroutineScope(Dispatchers.Main).launch{
            mainViewModel.findUsers(keyword).observe(this@MainActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarUsers.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBarUsers.visibility = View.GONE
                        val usersData = result.data
                        userAdapter.submitList(usersData)
                    }
                    is Result.Error -> {
                        binding.progressBarUsers.visibility = View.GONE
                        Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        binding.rvUsers.apply {
            visibility = View.VISIBLE
            adapter = userAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, layoutManager.orientation))
        }
    }

    private fun showFavourites() {
        val moveToFavouriteIntent = Intent(this@MainActivity, FavouriteUserActivity::class.java)
        startActivity(moveToFavouriteIntent)
    }

    private fun showSetting() {
        val moveToSettingIntent = Intent(this@MainActivity, SettingActivity::class.java)
        startActivity(moveToSettingIntent)
    }

}