package com.erdiansyah.mystory.presenter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.erdiansyah.mystory.R
import com.erdiansyah.mystory.data.Result
import com.erdiansyah.mystory.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel.loginResponseData.observe(viewLifecycleOwner) {
            if (it != null) {
                when(it) {
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Request Error", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        viewModel.getSession().observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.action_loginFragment_to_storyFragment)
            }
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.loginUser(email, password)
        }

        binding.registButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        setupAnimation()
        return binding.root
    }

    private fun setupAnimation() {
        val appName = ObjectAnimator.ofFloat(binding.appNameTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.quotesTextView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val registMessage = ObjectAnimator.ofFloat(binding.registMessage, View.ALPHA, 1f).setDuration(500)
        val registButton = ObjectAnimator.ofFloat(binding.registButton, View.ALPHA, 1f).setDuration(500)
        val togetherEmail = AnimatorSet().apply {
            playTogether(email, emailEdit)
        }
        val togetherPassword = AnimatorSet().apply {
            playTogether(password, passwordEdit)
        }
        val togetherButton = AnimatorSet().apply {
            playTogether(loginButton, registMessage, registButton)
        }
        val togetherTitle = AnimatorSet().apply {
            playTogether(appName,message)
        }
        AnimatorSet().apply {
            playSequentially(togetherTitle,togetherEmail,togetherPassword, togetherButton)
            start()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as AppCompatActivity).supportActionBar?.hide()
    }
}