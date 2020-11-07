package com.aditya.internshiptask.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.aditya.internshiptask.R
import com.aditya.internshiptask.databinding.FragmentLoginBinding
import com.aditya.internshiptask.utils.logs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUpBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.logIn.setOnClickListener {
            if (!checkNullable(binding.password.text.toString(), binding.email.text.toString())) {
                Snackbar.make(
                    binding.container,
                    "Fields cannot be left blank",
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (!checkEmail(binding.email.text.toString())) {
                Snackbar.make(binding.container, "Invalid Email", Snackbar.LENGTH_LONG).show()
            } else {
                binding.container.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.progressText.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
                    .addOnCompleteListener(requireActivity()) {
                        if (it.isSuccessful) {
                            logs("Log In Successful")
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                        } else {
                            binding.container.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            binding.progressText.visibility = View.GONE
                            Snackbar.make(
                                binding.container,
                                "Authentication Failed !",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            logs("auth failed : user could not be created -> ${it.exception}")
                        }
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }
    }

    private fun checkNullable(pwd: String, email: String): Boolean {
        if (pwd == "" || email == "") return false
        return true
    }

    private fun checkEmail(email: String): Boolean {
        var x = 0
        var retVal = false
        while (x < email.length) {
            if (email[x] == '@') {
                retVal = true
                break
            }
            x++
        }
        return retVal
    }
}