package com.arya.storyapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arya.storyapp.R
import com.arya.storyapp.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveSession()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeRetrieveSessionResult()
    }

    private fun observeRetrieveSessionResult() {
        viewModel.retrieveSessionResult.observe(viewLifecycleOwner) { session ->
            Handler(Looper.getMainLooper()).postDelayed({
                if (session.isLogin) {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                } else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                }
            }, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}