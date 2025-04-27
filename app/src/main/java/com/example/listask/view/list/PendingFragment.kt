package com.example.listask.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.listask.R
import com.example.listask.databinding.FragmentPendingBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PendingFragment : Fragment() {

    private var _binding: FragmentPendingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPendingBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.AddTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_PendingFragment_to_AddTaskFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}