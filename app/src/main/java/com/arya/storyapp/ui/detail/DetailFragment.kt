package com.arya.storyapp.ui.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arya.storyapp.databinding.FragmentDetailBinding
import com.arya.storyapp.utils.withDateFormat
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater
                .from(context)
                .inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopAppBar()
        setupView()
    }

    private fun setupTopAppBar() {
        binding.topAppBar.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun setupView() {
        val storyItem = args.storyItem

        with(binding) {
            Glide.with(requireContext())
                .load(storyItem.photoUrl)
                .into(photoImageView)

            nameTextView.text = storyItem.name
            dateTextView.text = storyItem.createdAt.withDateFormat()
            descriptionTextView.text = storyItem.description
        }
    }
}