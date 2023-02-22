package com.example.mygithubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mygithubuser.ui.fragment.DetailUserFollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    lateinit var username: String
    override fun createFragment(position: Int): Fragment {
        val fragment = DetailUserFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(DetailUserFollowFragment.ARG_POSITION, position + 1)
            putString(DetailUserFollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}