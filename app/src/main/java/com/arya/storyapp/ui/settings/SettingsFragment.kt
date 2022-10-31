package com.arya.storyapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arya.storyapp.R
import com.arya.storyapp.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveSession()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        observeRetrieveSessionResult()
    }

    private fun observeRetrieveSessionResult() {
        viewModel.retrieveSessionResult.observe(viewLifecycleOwner) { session ->
            if (session.isLogin) {
                binding.nameTextView.text = session.name
            } else {
                findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
            }
        }
    }

    private fun setupAction() {
        binding.languageTextView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.logoutTextView.setOnClickListener {
            viewModel.deleteSession()
        }
    }
}