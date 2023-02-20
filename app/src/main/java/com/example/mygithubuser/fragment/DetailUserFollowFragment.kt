package com.example.mygithubuser.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.adapter.UserAdapter
import com.example.mygithubuser.databinding.FragmentDetailUserFollowBinding
import com.example.mygithubuser.response.ItemsItem
import com.example.mygithubuser.viewmodel.DetailUserViewModel

class DetailUserFollowFragment : Fragment() {
    private lateinit var binding: FragmentDetailUserFollowBinding
    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailUserFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)

        if (position == 1 && detailUserViewModel.listFollowers.value == null) {
            detailUserViewModel.findFollowers(username?:"")
        }

        if (position == 2 && detailUserViewModel.listFollowing.value == null) {
            detailUserViewModel.findFollowing(username?:"")
        }

        detailUserViewModel.listFollowers.observe(viewLifecycleOwner) {
            detailUserViewModel.listFollowers.value?.let { setUsersData(it) }
        }

        detailUserViewModel.listFollowing.observe(viewLifecycleOwner) {
            detailUserViewModel.listFollowing.value?.let { setUsersData(it) }
        }

        detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }


    private fun setUsersData(listUser: List<ItemsItem>) {
        val adapter = UserAdapter(listUser)
        binding.rvFollow.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvFollow.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBarDetailFragmentUser.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}