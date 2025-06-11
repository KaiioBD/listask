package com.example.listask.view.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.listask.R
import com.example.listask.databinding.FragmentAddTaskBinding
import com.example.listask.model.Tarea
import com.example.listask.utils.FragmentCommunicator
import com.example.listask.view.list.viewModel.AddTaskViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<AddTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        communicator = requireActivity() as ListActivity
        setupView()
        return binding.root

    }

    private fun setupView(){

        setupObservers()

    }

    private fun setupObservers(){

        viewModel.loaderState.observe(viewLifecycleOwner) {loaderState ->
            communicator.showLoader(loaderState)

        }

    }

    private fun updateUI(tarea: Tarea){

        binding.apply {
            nombreTextField.editText?.setText(tarea.name)
            descriptionTextField.editText?.setText(tarea.description)
            FechaTextField.editText?.setText(tarea.date)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_AddTaskFragment_to_PendingFragment)
        }

        binding.agregarButton.setOnClickListener {
            val nombre = binding.nombreTextField.editText?.text.toString().trim()
            val descripcion = binding.descriptionTextField.editText?.text.toString().trim()
            val fecha = binding.FechaTextField.editText?.text.toString().trim()

            if (nombre.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaTarea = Tarea(
                name = nombre,
                description = descripcion,
                date = fecha
            )

            viewModel.addTarea(nuevaTarea)

            Snackbar.make(binding.root, "Tarea agregada con éxito", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_AddTaskFragment_to_PendingFragment)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}