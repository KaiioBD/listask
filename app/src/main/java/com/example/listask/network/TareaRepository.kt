package com.example.listask.network

import com.example.listask.core.ResultWrapper
import com.example.listask.core.safeCall
import com.example.listask.model.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TareaRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val tareaCollection = firestore.collection("Tasks")

    suspend fun addTarea(tarea: Tarea): ResultWrapper<Void> = safeCall {
        val id = tareaCollection.document().id
        tarea.id = id
        tareaCollection.document(id).set(tarea).await()
    }

    suspend fun getTarea(id: String): ResultWrapper<Tarea> = safeCall {
        val document = tareaCollection.document(id).get().await()
        document.toObject(Tarea::class.java) ?: throw Exception("Tarea no encontrada")
    }

    suspend fun getAllTareas(): ResultWrapper<List<Tarea>> = safeCall {
        val snapshot = tareaCollection.get().await()
        snapshot.toObjects(Tarea::class.java)
    }

    suspend fun updateTarea(tarea: Tarea): ResultWrapper<Void> = safeCall {
        tareaCollection.document(tarea.id).set(tarea).await()
    }

    suspend fun deleteTarea(id: String): ResultWrapper<Void> = safeCall {
        tareaCollection.document(id).delete().await()
    }
}