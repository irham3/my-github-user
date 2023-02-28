package com.example.mygithubuser.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.ui.adapter.UserAdapter
import com.example.mygithubuser.databinding.FragmentUserFollowBinding
import com.example.mygithubuser.ui.viewmodel.DetailUserViewModel
import com.example.mygithubuser.ui.viewmodel.ViewModelFactory
import com.example.mygithubuser.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserFollowFragment : Fragment() {
    private lateinit var binding: FragmentUserFollowBinding
    private lateinit var userAdapter: UserAdapter
    private val detailUserViewModel: DetailUserViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = UserAdapter()

        showRecyclerView()
        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME)

        if (position == 1) {
            // Followers Tab
            CoroutineScope(Dispatchers.Main).launch {
                detailUserViewModel.findFollowers((username.toString())).observe(viewLifecycleOwner) { result ->
                    when(result) {
                        is Result.Loading -> {
                            binding.progressBarDetailFragmentUser.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBarDetailFragmentUser.visibility = View.GONE
                            val usersData = result.data
                            userAdapter.submitList(usersData)
                        }
                        is Result.Error -> {
                            binding.progressBarDetailFragmentUser.visibility = View.GONE
                            binding.tvErrorMessage.text = result.message
                        }
                    }
                }
            }
        } else if(position == 2) {
            // Following Tab
            CoroutineScope(Dispatchers.Main).launch {
                detailUserViewModel.findFollowing((username.toString())).observe(viewLifecycleOwner) { result ->
                    when(result) {
                        is Result.Loading -> {
                            binding.progressBarDetailFragmentUser.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBarDetailFragmentUser.visibility = View.GONE
                            val usersData = result.data
                            userAdapter.submitList(usersData)
                        }
                        is Result.Error -> {
                            binding.progressBarDetailFragmentUser.visibility = View.GONE
                            binding.tvErrorMessage.text = result.message
                        }
                    }
                }
            }
        }
    }
    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        binding.rvFollow.apply {
            visibility = View.VISIBLE
            adapter = userAdapter
            addItemDecoration(DividerItemDecoration(requireActivity(), layoutManager.orientation))
        }
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}