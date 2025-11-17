package com.example.myapplication.data.local

import com.example.myapplication.model.Atributos
import com.example.myapplication.model.Personagem
import com.example.myapplication.model.Classe
import com.example.myapplication.model.Raca

fun Personagem.toEntity(): PersonagemEntity {
    return PersonagemEntity(
        nome = this.nome,
        idade = this.idade,
        classe = this.classe?.name,
        raca = this.raca?.name,
        atributos = this.atributos ?: Atributos(),
        estiloAventura = this.estiloAventura
    )
}

fun PersonagemEntity.toModel(): Personagem {
    val classeEnum = this.classe?.let {
        try { Classe.valueOf(it) } catch (e: Exception) { null }
    }

    val racaEnum = this.raca?.let {
        try { Raca.valueOf(it) } catch (e: Exception) { null }
    }

    return Personagem(
        nome = this.nome,
        idade = this.idade,
        raca = racaEnum,
        classe = classeEnum,
        atributos = this.atributos,
        estiloAventura = this.estiloAventura
    )
}
