package com.example.listask.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.listask.R
import com.example.listask.databinding.FragmentAddTaskBinding
import com.example.listask.model.Tarea
import com.example.listask.utils.FragmentCommunicator
import com.example.listask.view.list.viewModel.AddTaskViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<AddTaskViewModel>()
    private var tareaEditando: Tarea? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator

        // Obtenemos la tarea que se está editando si la hay
        tareaEditando = arguments?.getParcelable("tarea")

        // Si hay tarea, cargamos sus datos en los campos
        tareaEditando?.let { tarea ->
            binding.nombreTextField.editText?.setText(tarea.name)
            binding.descriptionTextField.editText?.setText(tarea.description)
            val fechaFormateada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(tarea.date)
            binding.FechaTextField.editText?.setText(fechaFormateada)
            binding.agregarButton.text = "Actualizar"
        }

        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.agregarButton.setOnClickListener {
            val nombre = binding.nombreTextField.editText?.text.toString().trim()
            val descripcion = binding.descriptionTextField.editText?.text.toString().trim()
            val fechaStr = binding.FechaTextField.editText?.text.toString().trim()

            if (nombre.isEmpty() || descripcion.isEmpty() || fechaStr.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val parsedDate: Date = try {
                formatter.parse(fechaStr) ?: throw IllegalArgumentException("Fecha inválida")
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Fecha inválida. Usa formato yyyy-MM-dd", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Creamos la tarea, si estamos editando usamos el mismo ID y userId, si es nueva generamos nuevo ID y userId
            val tareaFinal = Tarea(
                id = tareaEditando?.id ?: UUID.randomUUID().toString(),
                name = nombre,
                description = descripcion,
                date = java.sql.Date(parsedDate.time),
                userId = tareaEditando?.userId ?: FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
            )

            // Si editamos, actualizamos; si no, creamos nueva
            if (tareaEditando != null) {
                viewModel.updateTaskInfo(tareaFinal)
            } else {
                viewModel.createTaskInfo(tareaFinal)
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_AddTaskFragment_to_PendingFragment)
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.operationSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                val mensaje = if (tareaEditando != null) "Tarea actualizada con éxito" else "Tarea agregada con éxito"
                Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_AddTaskFragment_to_PendingFragment)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}