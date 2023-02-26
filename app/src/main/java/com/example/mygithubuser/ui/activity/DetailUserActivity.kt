package com.example.mygithubuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mygithubuser.R
import com.example.mygithubuser.data.local.entity.FavouriteUser
import com.example.mygithubuser.ui.adapter.SectionsPagerAdapter
import com.example.mygithubuser.databinding.ActivityDetailUserBinding
import com.example.mygithubuser.data.remote.response.DetailUserResponse
import com.example.mygithubuser.ui.viewmodel.DetailUserViewModel
import com.example.mygithubuser.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var favUser: FavouriteUser
    private var isFavourite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val detailUserViewModel: DetailUserViewModel by viewModels {
            ViewModelFactory.getInstance(application)
        }

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_user_title)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if(detailUserViewModel.detailUser.value == null) {
            detailUserViewModel.findDetailUser(username?:"")
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailUserViewModel.detailUser.observe(this) {detailData ->
            showDetailUserData(detailData)
            favUser = FavouriteUser(detailData.login.toString(), detailData.avatarUrl)
        }

        detailUserViewModel.getFavouriteByUsername(username!!).observe(this) { listFavUser ->
            isFavourite = listFavUser.isNotEmpty()
            setFabIcon(isFavourite)
            binding.fabAddToFavourites.setOnClickListener {
                if(isFavourite)
                    detailUserViewModel.removeFromFavourites(favUser)
                else
                    detailUserViewModel.addToFavourites(favUser)
            }
        }

        detailUserViewModel.toastMessage.observe(this) {
            it.getContentIfNotHandled()?.let { toastMessage ->
                Toast.makeText(this@DetailUserActivity, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun showDetailUserData(detailData: DetailUserResponse?) {
        Glide.with(this@DetailUserActivity)
            .load(detailData?.avatarUrl)
            .apply(RequestOptions().override(180, 180))
            .into(binding.imgDetailUserPhoto)
        binding.tvDetailUserName.text = detailData?.name?:"Tidak Ada Nama"
        binding.tvDetailUserUsername.text = detailData?.login
        binding.tvDetailUserFollower.text =
            getString(R.string.detail_user_followers, detailData?.followers)
        binding.tvDetailUserFollowing.text =
            getString(R.string.detail_user_following, detailData?.following)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutMainDetailUser.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBarDetailUser.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setFabIcon(isFavourite: Boolean) {
        if (isFavourite) {
            binding.fabAddToFavourites.setImageDrawable(ContextCompat
                .getDrawable(binding.fabAddToFavourites.context, R.drawable.ic_favorite_24))
        } else {
            binding.fabAddToFavourites.setImageDrawable(ContextCompat
                .getDrawable(binding.fabAddToFavourites.context, R.drawable.ic_favorite_border_24))
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