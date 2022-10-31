package com.arya.storyapp.ui.login

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.arya.storyapp.R
import com.arya.storyapp.data.Result
import com.arya.storyapp.data.local.SessionModel
import com.arya.storyapp.databinding.FragmentLoginBinding
import com.arya.storyapp.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveSession()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpannableString()
        setupAction()
        observeRetrieveSessionResult()
        observePostLoginResult()
    }

    private fun setupSpannableString() {
        val string = resources.getString(R.string.ask_to_register)
        val substring = string.split(" ").last()
        val start = string.indexOf(substring)
        val end = start + substring.length
        val spannableString = SpannableString(string)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
                ds.color = ContextCompat.getColor(requireContext(), R.color.blue)
            }
        }

        spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.registerTextView.text = spannableString
        binding.registerTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.postLogin(email, password)
        }
    }

    private fun observeRetrieveSessionResult() {
        viewModel.retrieveSessionResult.observe(viewLifecycleOwner) { session ->
            if (session.isLogin) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    private fun observePostLoginResult() {
        viewModel.postLoginResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loginButton.setLoading(true)
                    }
                    is Result.Success -> {
                        binding.loginButton.setLoading(false)
                        val sessionModel = SessionModel(
                            result.data.loginResult.name,
                            result.data.loginResult.token,
                            true,
                        )
                        viewModel.saveSession(sessionModel)
                    }
                    is Result.Error -> {
                        binding.loginButton.setLoading(false)
                        binding.root.showSnackbar(
                            result.error,
                            Snackbar.LENGTH_SHORT,
                            null
                        ) {}
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}