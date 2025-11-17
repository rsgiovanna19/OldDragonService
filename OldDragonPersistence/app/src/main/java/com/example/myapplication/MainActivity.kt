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
}
