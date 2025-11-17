import com.example.myapplication.model.*
import com.example.myapplication.controller.PersonagemController // Para usar as funções de cálculo
import kotlin.random.Random

class BatalhaController(
    private val personagemController: PersonagemController
) {
    // Definição de objetos de combate que terão o PV alterado
    private data class Combatente(
        val nome: String,
        var pv: Int,
        val ca: Int,
        val bba: Int,
        val danoDado: Int,
        val danoMod: Int,
        val modAtributo: Int, // Modificador de Força ou Destreza
        val isPersonagem: Boolean
    )

    /**
     * Roda a simulação de batalha entre o Personagem e um Monstro.
     * @return O log completo da batalha.
     */
    fun simularBatalha(monstro: Monstro): String {
        // 1. Preparar Combatentes
        val personagemAtual = personagemController.personagem.value

        // Estatísticas do Personagem (assumindo arma básica d6 e usando Mod de Força/Destreza)
        val modAtributo = personagemController.calcularModificador(
            if (personagemAtual.classe == Classe.MAGO) personagemAtual.atributos.destreza else personagemAtual.atributos.forca
        )

        val heroi = Combatente(
            nome = personagemAtual.nome,
            pv = personagemController.calcularPV(personagemAtual),
            ca = personagemController.calcularCA(personagemAtual),
            bba = personagemController.calcularBBA(personagemAtual),
            danoDado = 6, // Simplificando para d6 de dano de arma
            danoMod = modAtributo,
            modAtributo = modAtributo,
            isPersonagem = true
        )

        // Estatísticas do Monstro
        val inimigo = Combatente(
            nome = monstro.nome,
            pv = monstro.pv,
            ca = monstro.ca,
            bba = monstro.bba,
            danoDado = monstro.danoDado,
            danoMod = monstro.danoMod,
            modAtributo = monstro.danoMod, // Monstro usa seu mod de dano como mod de atributo para simplificar
            isPersonagem = false
        )

        val log = StringBuilder()
        log.append("--- INÍCIO DA BATALHA: ${heroi.nome} vs ${inimigo.nome} ---\n")
        log.append("${heroi.nome} (PV: ${heroi.pv}, CA: ${heroi.ca}, BBA: ${heroi.bba})\n")
        log.append("${inimigo.nome} (PV: ${inimigo.pv}, CA: ${inimigo.ca}, BBA: ${inimigo.bba})\n")
        log.append("--------------------------------------------------\n")

        // 2. Iniciativa (Regra simples: 1d6)
        val iniHeroi = Random.nextInt(1, 7) + heroi.modAtributo // Adicionando Mod. Destreza/Força
        val iniInimigo = Random.nextInt(1, 7)

        val primeiro = if (iniHeroi >= iniInimigo) heroi else inimigo
        val segundo = if (iniHeroi < iniInimigo) heroi else inimigo

        log.append("Iniciativa - ${heroi.nome}: $iniHeroi | ${inimigo.nome}: $iniInimigo. Primeiro: ${primeiro.nome}.\n")
        log.append("--------------------------------------------------\n")

        var turno = 1

        // 3. Loop de Combate
        while (heroi.pv > 0 && inimigo.pv > 0 && turno < 20) { // Limite de 20 turnos para evitar loops infinitos
            log.append("\n=== TURNO $turno ===\n")

            realizarAtaque(primeiro, segundo, log)
            if (segundo.pv <= 0) break // O segundo morreu, fim da batalha

            realizarAtaque(segundo, primeiro, log)
            if (primeiro.pv <= 0) break // O primeiro morreu, fim da batalha

            turno++
        }

        // 4. Resultado Final
        log.append("\n=== FIM DA BATALHA ===\n")
        val vencedor = when {
            heroi.pv > 0 -> heroi.nome
            inimigo.pv > 0 -> inimigo.nome
            else -> "Ambos caíram! (Empate)"
        }
        log.append("Vencedor: $vencedor\n")
        log.append("${heroi.nome} PV restantes: ${maxOf(0, heroi.pv)}\n")
        log.append("${inimigo.nome} PV restantes: ${maxOf(0, inimigo.pv)}\n")

        // Se o personagem morreu, notificar (etapa 3 do seu exercício)
        if (heroi.pv <= 0) {
            log.append("[GATILHO DE MORTE] O personagem ${heroi.nome} foi derrotado por ${inimigo.nome} e precisa de um service de notificação.\n")
        }

        return log.toString()
    }

    /**
     * Lógica de um único ataque.
     */
    private fun realizarAtaque(atacante: Combatente, defensor: Combatente, log: StringBuilder) {
        if (atacante.pv <= 0) return // Não ataca se estiver morto

        // Rola o d20 para acertar a CA
        val d20 = Random.nextInt(1, 21)
        val jogadaDeAtaque = d20 + atacante.bba + atacante.modAtributo

        log.append("-> ${atacante.nome} ataca (${atacante.pv} PV) JOGADA: ($d20 + ${atacante.bba} + ${atacante.modAtributo}) = $jogadaDeAtaque\n")

        // 1. Acerto?
        if (jogadaDeAtaque >= defensor.ca) {

            // 2. Cálculo de Dano
            val dano = Random.nextInt(1, atacante.danoDado + 1) + atacante.danoMod
            defensor.pv -= dano

            log.append("    ACERTO! Causa $dano de dano em ${defensor.nome} (CA: ${defensor.ca}).\n")
            log.append("    ${defensor.nome} tem agora ${maxOf(0, defensor.pv)} PV.\n")

            // 3. Verifica Morte
            if (defensor.pv <= 0) {
                log.append("    *** ${defensor.nome} CAI DERROTADO! ***\n")
            }
        } else {
            log.append("    ERROU! Jogada $jogadaDeAtaque é menor que a CA ${defensor.ca} de ${defensor.nome}.\n")
        }
    }
}