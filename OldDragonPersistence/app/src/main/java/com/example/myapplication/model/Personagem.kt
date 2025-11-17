package com.example.myapplication.model

data class Personagem(
    val nome: String,
    val idade: Int,
    var raca: Raca? = null,
    var classe: Classe? = null,
    val atributos: Atributos = Atributos(),
    var estiloAventura: Int = 0 // 1 = Clássico, 2 = Aventureiro, 3 = Heróico
) {
    // Funções de resumo serão chamadas pela View para exibir informações
    fun getResumo(): String {
        return """
            === Resumo do Personagem ===
            Nome: $nome
            Idade: $idade anos
            Raça: ${raca?.name ?: "Não Definida"}
            Classe: ${classe?.name ?: "Não Definida"} - ${classe?.descricao ?: ""}
            Movimento: ${raca?.movimento ?: "Não Definido"}
            Infravisão: ${raca?.infraviso ?: "Não Definido"}
            Alinhamento: ${raca?.alinhamento ?: "Não Definido"}
            Habilidades: ${raca?.habilidades?.joinToString(", ") ?: "Nenhuma"}
        """.trimIndent()
    }
}