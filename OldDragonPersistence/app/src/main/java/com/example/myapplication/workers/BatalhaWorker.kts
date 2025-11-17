// package com.example.myapplication.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.myapplication.controller.BatalhaController
import com.example.myapplication.controller.PersonagemController
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.toModel
import com.example.myapplication.model.Monstro
import com.example.myapplication.notifications.NotificationHelper
import kotlinx.coroutines.flow.first

class BatalhaWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val appContext = applicationContext

        // 1. Carregar Personagem Salvo (o Worker não tem acesso direto à UI/Controller)
        val db = AppDatabase.getInstance(appContext)
        val personagemDao = db.personagemDao()
        val personagemEntity = personagemDao.getAll().first().firstOrNull()

        if (personagemEntity == null) {
            // Não há personagem salvo para lutar.
            return Result.failure()
        }

        // 2. Preparar Controller e Batalha
        val tempController = PersonagemController()
        tempController.personagem.value = personagemEntity.toModel()

        // Garante que o personagem tem PV/BBA corretos no tempController
        tempController.gerarAtributos()

        val batalhaController = BatalhaController(tempController)
        val monstro = Monstro.Esqueleto()

        // 3. Executar a simulação (o trabalho em segundo plano)
        val logBatalha = batalhaController.simularBatalha(monstro)

        // 4. Notificar em caso de Morte (Passo 3)
        val personagemMorreu = logBatalha.contains("[GATILHO DE MORTE]")

        if (personagemMorreu) {
            NotificationHelper.showNotification(
                appContext,
                "💀 Fim da Jornada",
                "${tempController.personagem.value.nome} foi derrotado(a) pelo(a) ${monstro.nome}!",
                logBatalha // O log completo como texto expandido
            )
        }

        // Em um projeto completo, você salvaria o status (vivo/morto) do personagem aqui.

        return Result.success() // Indica que o trabalho foi concluído com sucesso
    }
}