package com.example.mygithubuser.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.ui.adapter.UserAdapter
import com.example.mygithubuser.data.remote.response.ItemsItem
import com.example.mygithubuser.databinding.FragmentUserFollowBinding
import com.example.mygithubuser.ui.activity.DetailUserActivity
import com.example.mygithubuser.ui.viewmodel.DetailUserViewModel
import com.example.mygithubuser.ui.viewmodel.ViewModelFactory

class UserFollowFragment : Fragment() {
    private lateinit var binding: FragmentUserFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailUserViewModel: DetailUserViewModel by viewModels {
            ViewModelFactory.getInstance(requireActivity())
        }

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)

        if (position == 1 && detailUserViewModel.listFollowers.value == null) {
            detailUserViewModel.findFollowers(username?:"")

            detailUserViewModel.listFollowers.observe(viewLifecycleOwner) {
                detailUserViewModel.listFollowers.value?.let { setUsersData(it) }
            }

            detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        } else if(position == 2 && detailUserViewModel.listFollowing.value == null) {
            detailUserViewModel.findFollowing(username?:"")

            detailUserViewModel.listFollowing.observe(viewLifecycleOwner) {
                detailUserViewModel.listFollowing.value?.let { setUsersData(it) }
            }

            detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
    }

    private fun setUsersData(listUser: List<ItemsItem>) {
        val adapter = UserAdapter(listUser)
        binding.rvFollow.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                showSelectedUser(username)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvFollow.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.progressBarDetailFragmentUser.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedUser(username: String) {
        val moveToDetailIntent = Intent(requireActivity(), DetailUserActivity::class.java)
        moveToDetailIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)

        startActivity(moveToDetailIntent)
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}