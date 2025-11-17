package com.example.myapplication.controller

//importações p utilização
import com.example.myapplication.data.local.AppDatabase
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.data.local.toModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication.model.*
import kotlin.random.Random

class PersonagemController {
    private val _personagem = mutableStateOf(Personagem(nome = "Novo Herói", idade = 0, estiloAventura = 1))
    val personagem: State<Personagem> get() = _personagem

    var estiloAventura by mutableStateOf(1) // 1 = Clássico, 2 = Aventureiro, 3 = Heróico

    fun atualizarEstilo(estilo: Int) {
        estiloAventura = estilo
        _personagem.value = _personagem.value.copy(estiloAventura = estilo)
        gerarAtributos()
    }

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

    /**
     * Salva o personagem atual no DB local usando Room.
     * onSaved roda no background; para atualizar UI use runOnUiThread/Toast no compose com context.
     */
    fun salvarLocal(context: Context, onSaved: (Long) -> Unit = {}) {
        val db = AppDatabase.getInstance(context)
        val dao = db.personagemDao()
        val entity = personagem.value.toEntity()
        CoroutineScope(Dispatchers.IO).launch {
            val id = dao.insert(entity)
            onSaved(id)
        }
    }

    fun carregarUltimoSalvoOnce(context: Context, onLoaded: (Boolean) -> Unit = {}) {
        val db = AppDatabase.getInstance(context)
        val dao = db.personagemDao()
        CoroutineScope(Dispatchers.IO).launch {
            val list = dao.getAll().first() // lê uma vez
            if (list.isNotEmpty()) {
                val first = list.first()
                _personagem.value = first.toModel()
                onLoaded(true)
            } else {
                onLoaded(false)
            }
        }
    }


    private fun rolarD6() = Random.nextInt(1, 7)
    private fun rolar3d6() = rolarD6() + rolarD6() + rolarD6()
    private fun rolar4d6DropLowest(): Int {
        val dados = List(4) { rolarD6() }
        return dados.sortedDescending().take(3).sum()
    }

    // Dentro da classe PersonagemController { ... }

    // Função Auxiliar para rolar o D20
    private fun rolarD20() = Random.nextInt(1, 21)
    private fun rolarDado(lados: Int) = Random.nextInt(1, lados + 1)
    private fun calcularModificador(atributo: Int) = when (atributo) {
        in 3..4 -> -2
        in 5..8 -> -1
        in 9..12 -> 0
        in 13..16 -> 1
        in 17..18 -> 2
        else -> 0
    }

    // Cálculo da CA (Simplificação de Old Dragon)
    fun calcularCA(personagem: Personagem): Int {
        // CA base é 10. Assumindo CA Ascendente: 10 + Mod. Destreza + Armadura (Simplificando: sem armadura, CA = 10 + Mod. Destreza)
        val modDestreza = calcularModificador(personagem.atributos.destreza)
        return 10 + modDestreza // Simplificado para Nível 1 sem armadura/escudo
    }

    // Cálculo dos Pontos de Vida (PV) para Nível 1
    fun calcularPV(personagem: Personagem): Int {
        // PV = Dado de Vida (DV) da Classe (d8 para Guerreiro, d6 para Ladrão/Mago) + Mod. Constituição.
        val dv = when (personagem.classe) {
            Classe.GUERREIRO -> 8
            Classe.LADRAO, Classe.MAGO -> 6
            null -> 6 // Padrão
        }
        val modConst = calcularModificador(personagem.atributos.constituicao)
        val pv = rolarDado(dv) + modConst
        return maxOf(1, pv) // PV mínimo é 1
    }

    // Cálculo do BBA (Simplificação)
    fun calcularBBA(personagem: Personagem): Int {
        // Nível 1: BBA é 1 para Guerreiro e 0 para Ladrão/Mago.
        return when (personagem.classe) {
            Classe.GUERREIRO -> 1
            Classe.LADRAO, Classe.MAGO -> 0
            else -> 0
        }
    }
}