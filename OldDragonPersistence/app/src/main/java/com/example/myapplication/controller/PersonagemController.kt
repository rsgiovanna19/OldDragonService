package com.example.myapplication.controller

import BatalhaController
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication.model.*
import kotlin.random.Random


class PersonagemController {
    private val _personagem = mutableStateOf(Personagem(nome = "Novo Herói", idade = 0, estiloAventura = 1))
    val personagem: State<Personagem> get() = _personagem

    fun setPersonagem(novoPersonagem: Personagem) {
        _personagem.value = novoPersonagem
    }

    var estiloAventura by mutableStateOf(1) // 1 = Clássico, 2 = Aventureiro, 3 = Heróico

    fun atualizarEstilo(estilo: Int) {
        estiloAventura = estilo
        _personagem.value = _personagem.value.copy(estiloAventura = estilo)
        gerarAtributos()
    }

    // 1. CORREÇÃO: Lógica completa para gerar atributos (recuperada do seu código inicial)
    fun gerarAtributos() {
        val novosAtributos = when (estiloAventura) {
            1, 2 -> Atributos(
                forca = rolar3d6(),
                destreza = rolar3d6(),
                constituicao = rolar3d6(),
                inteligencia = rolar3d6(),
                sabedoria = rolar3d6(),
                carisma = rolar3d6()
            )
            3 -> Atributos(
                forca = rolar4d6DropLowest(),
                destreza = rolar4d6DropLowest(),
                constituicao = rolar4d6DropLowest(),
                inteligencia = rolar4d6DropLowest(),
                sabedoria = rolar4d6DropLowest(),
                carisma = rolar4d6DropLowest()
            )
            else -> Atributos()
        }
        _personagem.value = _personagem.value.copy(atributos = novosAtributos)
    }

    // Métodos para atualização de campos (necessários para completar o Controller)
    fun atualizarNome(novoNome: String) {
        _personagem.value = _personagem.value.copy(nome = novoNome)
    }

    fun atualizarIdade(novaIdade: String) {
        _personagem.value = _personagem.value.copy(idade = novaIdade.toIntOrNull() ?: 0)
    }

    fun atualizarRaca(novaRaca: Raca) {
        _personagem.value = _personagem.value.copy(raca = novaRaca)
    }

    fun atualizarClasse(novaClasse: Classe) {
        _personagem.value = _personagem.value.copy(classe = novaClasse)
    }
    private fun rolarD6() = Random.nextInt(1, 7)
    private fun rolar3d6() = rolarD6() + rolarD6() + rolarD6()
    private fun rolar4d6DropLowest(): Int {
        val dados = List(4) { rolarD6() }
        return dados.sortedDescending().take(3).sum()
    }

    private fun rolarD20() = Random.nextInt(1, 21)
    private fun rolarDado(lados: Int) = Random.nextInt(1, lados + 1)
    fun calcularModificador(atributo: Int) = when (atributo) {
        in 3..4 -> -2
        in 5..8 -> -1
        in 9..12 -> 0
        in 13..16 -> 1
        in 17..18 -> 2
        else -> 0
    }

    fun calcularCA(personagem: Personagem): Int {
        val modDestreza = calcularModificador(personagem.atributos.destreza)
        return 10 + modDestreza
    }

    fun calcularPV(personagem: Personagem): Int {
        val dv = when (personagem.classe) {
            Classe.GUERREIRO -> 8
            Classe.LADRAO, Classe.MAGO -> 6
            null -> 6
        }
        val modConst = calcularModificador(personagem.atributos.constituicao)
        val pv = rolarDado(dv) + modConst
        return maxOf(1, pv)
    }


    fun calcularBBA(personagem: Personagem): Int {
        return when (personagem.classe) {
            Classe.GUERREIRO -> 1
            Classe.LADRAO, Classe.MAGO -> 0
            else -> 0
        }
    }

    fun iniciarSimulacaoTeste(): String {
        val batalhaController = BatalhaController(this)
        val log = batalhaController.simularBatalha(Monstro.Esqueleto())
        return log
    }
}