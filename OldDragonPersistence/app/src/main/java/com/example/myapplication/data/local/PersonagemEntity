package com.example.myapplication.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.Atributos

@Entity(tableName = "personagens")
data class `PersonagemEntity`(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val idade: Int,
    val classe: String?, // salva Classe.name()
    val raca: String?,   // salva Raca.name()
    @Embedded val atributos: Atributos,
    val estiloAventura: Int,
    val createdAt: Long = System.currentTimeMillis()
)
