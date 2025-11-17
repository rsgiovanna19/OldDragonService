// src/main/java/com.example.myapplication/MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplication.controller.PersonagemController
import com.example.myapplication.view.PersonagemFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = PersonagemController() // seu controller
        setContent {
            PersonagemFlow(controller = controller) // chama o fluxo de telas
        }
    }
    import com.example.myapplication.model.* // Importe Monstro

    class PersonagemController {
        // ... (seus atributos e métodos existentes, como rolarD6, etc.)

        // ... (suas novas funções: calcularCA, calcularPV, calcularBBA, etc.)

        // Adicione esta função ao final da classe PersonagemController
        fun iniciarSimulacaoTeste(): String {
            // Note: Removemos o 'context' pois BatalhaController não precisa dele
            // e ele não está sendo usado no corpo desta função.
            val batalhaController = BatalhaController(this)

            // Define o monstro com quem o personagem vai lutar
            val monstroParaLuta = Monstro.Esqueleto()

            // Simula a batalha
            val log = batalhaController.simularBatalha(monstroParaLuta)

            return log
        }
    }

}

