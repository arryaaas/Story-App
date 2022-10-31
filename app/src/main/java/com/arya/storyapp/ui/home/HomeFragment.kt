package com.arya.storyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.arya.storyapp.R
import com.arya.storyapp.adapter.LoadingStateAdapter
import com.arya.storyapp.adapter.StoryAdapter
import com.arya.storyapp.data.local.entity.StoryEntity
import com.arya.storyapp.databinding.FragmentHomeBinding
import com.arya.storyapp.utils.gone
import com.arya.storyapp.utils.showSnackbar
import com.arya.storyapp.utils.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveSession()
        viewModel.retrieveSessionResult.observe(this) { session ->
            viewModel.getAllStory(session.token)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopAppBar()
        setupRecyclerView()
        setupAction()
        observeGetAllStoryResult()
    }

    private fun setupTopAppBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_story -> {
                    findNavController().navigate(R.id.addStoryFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter()

        binding.recyclerViewStories.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewStories.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        storyAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.visible()
                    binding.recyclerViewStories.gone()
                }
                is LoadState.NotLoading -> {
                    binding.progressBar.gone()
                    binding.recyclerViewStories.visible()
                }
                is LoadState.Error -> {
                    binding.progressBar.gone()
                    binding.recyclerViewStories.gone()
                    binding.root.showSnackbar(
                        resources.getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT,
                        null
                    ) {}
                }
            }
        }
    }

    private fun setupAction() {
        storyAdapter.onItemClick = { listStoryEntityItem: StoryEntity, extras: FragmentNavigator.Extras ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(listStoryEntityItem),
                extras
            )
        }
    }

    private fun observeGetAllStoryResult() {
        viewModel.getAllStoryResult.observe(viewLifecycleOwner) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}