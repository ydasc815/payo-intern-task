package com.aditya.internshiptask.view

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.aditya.internshiptask.R
import com.aditya.internshiptask.databinding.FragmentProfileBinding
import com.aditya.internshiptask.model.SignUpData
import com.aditya.internshiptask.utils.logs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().getReference("userData")
        val preferences: SharedPreferences =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dbKey: String = preferences.getString("db-key", "")!!
        logs("key = $dbKey")

    }

    override fun onStart() {
        super.onStart()
        val preferences: SharedPreferences =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())
        val dbKey: String = preferences.getString("db-key", "")!!
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data: SignUpData = snapshot.child(dbKey).getValue(SignUpData::class.java)!!
                binding.address.text = data.address
                binding.email.text = data.email
                binding.firstName.text = data.firstName
                binding.lastName.text = data.lastName
                binding.mobNo.text = data.phnNo
                binding.container.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.progressText.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.container, "We ran into a problem !", Snackbar.LENGTH_SHORT)
                    .show()
                logs("db error -> ${error.message}")
            }

        })
    }
}