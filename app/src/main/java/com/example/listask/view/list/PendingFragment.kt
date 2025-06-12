package com.example.listask.view.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listask.R
import com.example.listask.databinding.FragmentPendingBinding
import com.example.listask.model.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PendingFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val tareasList = mutableListOf<Tarea>()
    private lateinit var adapter: TareaAdapter
    private var _binding: FragmentPendingBinding? = null
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

        adapter = TareaAdapter(tareasList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PendingFragment.adapter
        }

        loadUserTasks()

    }

    private fun loadUserTasks() {
        val userId = auth.currentUser?.uid ?: return
        Log.d("USER_ID", "Buscando tareas para UID: $userId")

        db.collection("Tasks")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                tareasList.clear()
                for (document in result) {
                    val tarea = document.toObject(Tarea::class.java)
                    tareasList.add(tarea)
                }
                Log.d("TAREAS", "Tareas cargadas: ${tareasList.size}")
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al cargar tareas: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("TAREAS", "Error: ", e)
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}