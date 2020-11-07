package com.aditya.internshiptask.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.aditya.internshiptask.R
import com.aditya.internshiptask.databinding.FragmentSignUpBinding
import com.aditya.internshiptask.model.SignUpData
import com.aditya.internshiptask.utils.logs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logInBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("userData")
        binding.signUp.setOnClickListener {
            if (!checkNullable(
                    binding.firstName.text.toString(),
                    binding.lastName.text.toString(),
                    binding.address.text.toString(),
                    binding.password.text.toString(),
                    binding.confirmPassword.text.toString(),
                    binding.mobNo.text.toString(),
                    binding.email.text.toString()
                )
            ) {
                Snackbar.make(
                    binding.container,
                    "Fields cannot be left blank",
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (!checkEmail(binding.email.text.toString())) {
                Snackbar.make(binding.container, "Invalid Email", Snackbar.LENGTH_LONG).show()
            } else if (checkPassword(
                    binding.password.text.toString(),
                    binding.confirmPassword.text.toString()
                )
            ) {
                binding.container.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.progressText.visibility = View.VISIBLE
                firebaseAuth.createUserWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
                    .addOnCompleteListener(requireActivity()) {
                        if (it.isSuccessful) {
                            logs("Sign Up Successful")
                            val id: String = databaseReference.push().key!!
                            println("id = $id")
                            databaseReference.child(id).setValue(
                                SignUpData(
                                    binding.firstName.text.toString(),
                                    binding.lastName.text.toString(),
                                    binding.email.text.toString(),
                                    binding.mobNo.text.toString(),
                                    binding.address.text.toString()
                                )
                            )
                            val preferences: SharedPreferences =
                                androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                                    requireContext()
                                )
                            val editor: SharedPreferences.Editor = preferences.edit()
                            editor.putString("db-key", id)
                            editor.apply()
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                        } else {
                            binding.container.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            binding.progressText.visibility = View.GONE
                            Snackbar.make(
                                binding.container,
                                "Could not create user",
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

    private fun checkNullable(
        fname: String,
        lname: String,
        address: String,
        pwd: String,
        cnfPwd: String,
        phnNo: String,
        email: String
    ): Boolean {
        if (fname == "" || lname == "" || address == "" || pwd == "" || cnfPwd == "" || phnNo == "" || email == "") return false
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

    private fun checkPassword(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            Snackbar.make(
                binding.container,
                "Password and Confirm Password do not match",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        } else if (password.length < 8) {
            Snackbar.make(binding.container, "Password too short", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }
}

