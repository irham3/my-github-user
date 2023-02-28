package com.example.mygithubuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mygithubuser.R
import com.example.mygithubuser.data.Result
import com.example.mygithubuser.data.local.entity.FavouriteUser
import com.example.mygithubuser.ui.adapter.SectionsPagerAdapter
import com.example.mygithubuser.databinding.ActivityDetailUserBinding
import com.example.mygithubuser.ui.viewmodel.DetailUserViewModel
import com.example.mygithubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var favUser: FavouriteUser
    private lateinit var username: String
    private val detailUserViewModel: DetailUserViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_user_title)

        username = intent.getStringExtra(EXTRA_USERNAME).toString()

        showDetailUser()
        setFabIcon()

        // Menambahkan viewpager
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDetailUser() {
        CoroutineScope(Dispatchers.Main).launch{
            detailUserViewModel.findDetailUser(username).observe(this@DetailUserActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarDetailUser.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        val detailData = result.data
                        favUser = FavouriteUser(detailData?.login.toString(), detailData?.avatarUrl)
                        binding.progressBarDetailUser.visibility = View.GONE
                        Glide.with(this@DetailUserActivity)
                            .load(detailData?.avatarUrl)
                            .apply(RequestOptions().override(180, 180))
                            .into(binding.imgDetailUserPhoto)

                        binding.apply {
                            tvDetailUserName.text = detailData?.name?:getString(R.string.detail_user_noname)
                            tvDetailUserUsername.text = detailData?.login
                            tvDetailUserFollower.text = getString(R.string.detail_user_followers, detailData?.followers)
                            tvDetailUserFollowing.text = getString(R.string.detail_user_following, detailData?.following)
                        }
                    }
                    is Result.Error -> {
                        binding.progressBarDetailUser.visibility = View.GONE
                        Toast.makeText(this@DetailUserActivity, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun setFabIcon() {
        detailUserViewModel.getFavouriteByUsername(username).observe(this@DetailUserActivity) { favUserData ->
            val isFavourite = favUserData != null

            // Menentukan Icon Fab
            if (isFavourite) {
                binding.fabAddToFavourites.setImageDrawable(ContextCompat
                    .getDrawable(binding.fabAddToFavourites.context, R.drawable.ic_favorite_24))
            } else {
                binding.fabAddToFavourites.setImageDrawable(ContextCompat
                    .getDrawable(binding.fabAddToFavourites.context, R.drawable.ic_favorite_border_24))
            }

            // Menentukan fungsi ketika diklik (menghapus atau menambah)
            binding.fabAddToFavourites.setOnClickListener {
                if(isFavourite)
                    detailUserViewModel.removeFromFavourites(favUser)
                else
                    detailUserViewModel.addToFavourites(favUser)
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.detail_user_tab_text_1,
            R.string.detail_user_tab_text_2
        )
    }
}