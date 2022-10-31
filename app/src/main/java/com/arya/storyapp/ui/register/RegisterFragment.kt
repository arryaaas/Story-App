package com.arya.storyapp.ui.register

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
import com.arya.storyapp.databinding.FragmentRegisterBinding
import com.arya.storyapp.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpannableString()
        setupAction()
        observePostRegisterResult()
    }

    private fun setupSpannableString() {
        val string = resources.getString(R.string.ask_to_login)
        val substring = string.split(" ").last()
        val start = string.indexOf(substring)
        val end = start + substring.length
        val spannableString = SpannableString(string)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
                ds.color = ContextCompat.getColor(requireContext(), R.color.blue)
            }
        }

        spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.loginTextView.text = spannableString
        binding.loginTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.postRegister(name, email, password)
        }
    }

    private fun observePostRegisterResult() {
        viewModel.postRegisterResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.registerButton.setLoading(true)
                    }
                    is Result.Success -> {
                        binding.registerButton.setLoading(false)
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    is Result.Error -> {
                        binding.registerButton.setLoading(false)
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