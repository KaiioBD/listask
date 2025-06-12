package com.example.listask.network

import com.example.listask.core.ResultWrapper
import com.example.listask.core.safeCall
import com.example.listask.model.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TareaRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    private val tareaCollection = firestore.collection("Tasks")

    suspend fun addTarea(tarea: Tarea): ResultWrapper<Void> = safeCall {
        val id = tareaCollection.document().id
        val tareaConId = tarea.copy(id = id)
        tareaCollection.document(id).set(tareaConId).await()
    }

    suspend fun getTarea(id: String): ResultWrapper<Tarea> = safeCall {
        val snapShot = tareaCollection.document(id).get().await()
        snapShot.toObject(Tarea::class.java) ?: throw Exception("Tarea no encontrada")
    }

    suspend fun getAllTareas(): ResultWrapper<List<Tarea>> = safeCall {
        val userId = firebaseAuth.currentUser?.uid ?: throw Exception("Usuario no autenticado")

        val querySnapshot = tareaCollection.whereEqualTo("userId", userId).get().await()

        querySnapshot.documents.mapNotNull { document ->
            document.toObject(Tarea::class.java)
        }
    }

    suspend fun updateTarea(tarea: Tarea): ResultWrapper<Void> = safeCall {
        tareaCollection.document(tarea.id).set(tarea).await()
    }

    suspend fun deleteTarea(id: String): ResultWrapper<Void> = safeCall {
        tareaCollection.document(id).delete().await()
    }
}