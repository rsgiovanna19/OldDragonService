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

        // 1. Inicializa o Canal de Notificação (Sempre no onCreate)
        NotificationHelper.createNotificationChannel(this)

        // 2. Setup e Salvamento do Personagem (Necessário para o Worker)
        controller.atualizarRaca(Raca.HUMANO)
        controller.atualizarClasse(Classe.GUERREIRO)
        controller.gerarAtributos()
        // Salva o personagem para garantir que o BatalhaWorker o carregue do Room
        controller.salvarLocal(this)

        // 3. Agenda a Batalha (Inicia o Service)
        val batalhaRequest = OneTimeWorkRequestBuilder<BatalhaWorker>()
            // .setInitialDelay(5, java.util.concurrent.TimeUnit.SECONDS) // Opcional: para atrasar o início
            .build()

        WorkManager.getInstance(this).enqueue(batalhaRequest)

        // 4. Exibe a UI
        setContent {
            PersonagemFlow(controller = controller)
        }
    }
}