package com.aditya.internshiptask.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aditya.internshiptask.R
import com.aditya.internshiptask.adapter.RecordsListAdapter
import com.aditya.internshiptask.databinding.FragmentHomeBinding
import com.aditya.internshiptask.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var sortFlag = 0
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = RecordsListAdapter(requireActivity())
        binding.recyclerView.adapter = adapter
        viewModel.recordsData.observe(viewLifecycleOwner, {
            adapter.setRecordsData(it, sortFlag)
            binding.progressBar.visibility = View.GONE
            binding.progressText.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        })
        viewModel.networkFailureStatus.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    Snackbar.make(binding.container, "Network Request Failed", Snackbar.LENGTH_LONG)
                        .show()
                    binding.progressText.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
        binding.firstName.setOnClickListener {
            sortFlag = 1
            viewModel.recordsData.observe(viewLifecycleOwner, {
                adapter.setRecordsData(it, sortFlag)
            })
        }
        binding.lastName.setOnClickListener {
            sortFlag = 2
            viewModel.recordsData.observe(viewLifecycleOwner, {
                adapter.setRecordsData(it, sortFlag)
            })
        }
    }

}