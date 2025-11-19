// src/main/java/com.example.myapplication/MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.controller.PersonagemController
import com.example.myapplication.model.Classe
import com.example.myapplication.model.Raca
import com.example.myapplication.view.PersonagemFlow
import com.example.myapplication.workers.BatalhaWorker
import com.example.myapplication.notifications.NotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = PersonagemController()

        NotificationHelper.createNotificationChannel(this)

        controller.atualizarRaca(Raca.HUMANO)
        controller.atualizarClasse(Classe.GUERREIRO)
        controller.gerarAtributos()
        controller.salvarLocal(this) {}

        val batalhaRequest = OneTimeWorkRequestBuilder<BatalhaWorker>()
            .build()

        WorkManager.getInstance(this).enqueue(batalhaRequest)

        // 4. Exibe a UI
        setContent {
            PersonagemFlow(controller = controller)
        }
    }
}


