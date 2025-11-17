package com.example.myapplication.model

enum class Raca(
    val movimento: String,
    val infraviso: String,
    val alinhamento: String,
    val habilidades: List<String>
) {
    HUMANO("9 metros","Não","Qualquer", listOf("Aprendizado", "Adaptabilidade")),
    ANAO("6 metros","18 metros","Ordem", listOf("Mineradores", "Vigoroso", "Armas Grandes", "Inimigos")),
    GNOMO("6 metros","18 metros","Neutro", listOf("Avaliadores", "Sagazes e Vigorosos", "Restrições")),
    ELFO("9 metros","18 metros","Neutro", listOf("Percepção Natural", "Graciosos", "Treinamento Racial", "Imunidades")),
    HALFLING("6 metros","Não","Neutro", listOf("Furtivo","Destemido","Bons de Mira","Pequenos","Restrições"))
}