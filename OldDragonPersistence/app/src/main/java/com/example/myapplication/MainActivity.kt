// src/main/java/com.example.myapplication/MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import android.util.Log // Adicione este import
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.controller.PersonagemController
import com.example.myapplication.model.Classe // Adicione este import (se usar no teste)
import com.example.myapplication.model.Raca // Adicione este import (se usar no teste)
import com.example.myapplication.view.PersonagemFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = PersonagemController() // seu controller

        // --- CÓDIGO DE TESTE INSERIDO AQUI ---
        // 1. Garante que o personagem tenha atributos para a batalha
        controller.atualizarRaca(Raca.HUMANO)
        controller.atualizarClasse(Classe.GUERREIRO)
        controller.gerarAtributos()

        // 2. Inicia a simulação
        val logBatalha = controller.iniciarSimulacaoTeste()

        // 3. Imprime o resultado no Logcat
        Log.d("BATALHA_OD", logBatalha)
        // ------------------------------------

        setContent {
            PersonagemFlow(controller = controller) // chama o fluxo de telas
        }
    }
}