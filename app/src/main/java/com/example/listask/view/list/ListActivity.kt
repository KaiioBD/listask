package com.example.listask.view.list

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.listask.R
import com.example.listask.databinding.ActivityListBinding
import com.example.listask.utils.FragmentCommunicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListActivity : AppCompatActivity(), FragmentCommunicator {

    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun showLoader(value: Boolean) {
        binding.list.visibility = if (value) View.VISIBLE else View.GONE
    }
}