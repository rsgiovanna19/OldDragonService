package com.example.myapplication.model

import kotlin.random.Random

data class Atributos(
        var forca: Int = 0,
        var destreza: Int = 0,
        var constituicao: Int = 0,
        var inteligencia: Int = 0,
        var sabedoria: Int = 0,
        var carisma: Int = 0
) {
    companion object {
        private fun rolarD6() = Random.nextInt(1, 7)
        private fun rolar3d6() = rolarD6() + rolarD6() + rolarD6()
        private fun rolar4d6DropLowest(): Int {
            val dados = listOf(rolarD6(), rolarD6(), rolarD6(), rolarD6())
            return dados.sortedDescending().take(3).sum()
        }

        fun descreverAtributo(valor: Int, tipo: String) = when(tipo.lowercase()) {
            "forca", "força" -> when(valor) { in 3..8 -> "Fraco"; in 9..12 -> "Mediano"; in 13..16 -> "Forte"; in 17..18 -> "Muito Forte"; else -> "Indefinido" }
            "destreza" -> when(valor) { in 3..8 -> "Letárgico"; in 9..12 -> "Mediano"; in 13..16 -> "Ágil"; in 17..18 -> "Preciso"; else -> "Indefinido" }
            "constituicao", "constituição" -> when(valor) { in 3..8 -> "Frágil"; in 9..12 -> "Mediano"; in 13..16 -> "Resistente"; in 17..18 -> "Vigoroso"; else -> "Indefinido" }
            "inteligencia", "inteligência" -> when(valor) { in 3..8 -> "Inepto"; in 9..12 -> "Mediano"; in 13..16 -> "Inteligente"; in 17..18 -> "Gênio"; else -> "Indefinido" }
            "sabedoria" -> when(valor) { in 3..8 -> "Tolo"; in 9..12 -> "Mediano"; in 13..16 -> "Intuitivo"; in 17..18 -> "Presciente"; else -> "Indefinido" }
            "carisma" -> when(valor) { in 3..8 -> "Descortês"; in 9..12 -> "Mediano"; in 13..16 -> "Influente"; in 17..18 -> "Ídolo"; else -> "Indefinido" }
            else -> "Desconhecido"
        }
    }
}