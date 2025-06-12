package com.example.listask.view.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.listask.databinding.ItemTareaBinding
import com.example.listask.model.Tarea
import java.text.SimpleDateFormat
import java.util.*

class TareaAdapter(
    private val tareas: List<Tarea>,
    private val onDeleteClick: (Tarea) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    inner class TareaViewHolder(val binding: ItemTareaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]
        with(holder.binding) {
            tareaName.text = tarea.name
            tareaDescription.text = tarea.description
            tareaDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(tarea.date)

            root.setOnLongClickListener {
                onDeleteClick(tarea)
                true
            }
        }
        holder.binding.apply {
            tareaName.text = tarea.name
            tareaDescription.text = tarea.description
            tareaDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(tarea.date)

            deleteButton.setOnClickListener {
                onDeleteClick(tarea)
            }
        }
        holder.binding.editButton.setOnClickListener {
            val action = PendingFragmentDirections.actionPendingFragmentToAddTaskFragment(tarea)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = tareas.size
}
