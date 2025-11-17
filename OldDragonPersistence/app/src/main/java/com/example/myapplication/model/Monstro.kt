package com.example.myapplication.model

data class Monstro(
    val nome: String,
    val ca: Int, // Classe de Armadura
    val pv: Int, // Pontos de Vida
    val bba: Int, // Bônus Base de Ataque (para o d20)
    val danoDado: Int, // Tamanho do dado de dano (ex: 6 para d6)
    val danoMod: Int = 0 // Modificador de dano (ex: +2)
) {
    // Exemplo de Monstro Simples para o teste
    companion object {
        fun Esqueleto() = Monstro(
            nome = "Esqueleto",
            ca = 13, // CA 7 na tabela, que é 13 na CA Descendente (3d6 de Old Dragon)
            pv = 5, // 1d6 PV médio
            bba = 1, // BBA base para Nível 1
            danoDado = 6, // Dano base d6
            danoMod = 0
        )
    }
}