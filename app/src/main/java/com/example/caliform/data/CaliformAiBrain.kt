package com.example.caliform.data

import com.example.caliform.model.BodyArea
import com.example.caliform.model.ChatMessage
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.DurationCategory
import com.example.caliform.model.Equipment
import com.example.caliform.model.HistoryRecord
import com.example.caliform.model.MissionStatus
import com.example.caliform.model.UserProfile
import com.example.caliform.model.Workout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object CaliformAiBrain {

    /**
     * Processa a mensagem do aluno com compreensão contextual profunda
     * de lesões, retoma, fadiga, tempo personalizado (ex: 100m, 90m, 2h),
     * equipamento do parque e objetivos calisténicos.
     */
    fun processUserMessage(
        userPrompt: String,
        currentUser: UserProfile,
        historyList: List<HistoryRecord>,
        chatHistory: List<ChatMessage>
    ): ChatMessage {
        val lower = userPrompt.lowercase().trim()
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val recentHistory = historyList.firstOrNull()

        var replyText = ""
        var recommendedWorkout: Workout? = null

        // 1. Deteção de Lesão / Dor / Recuperação / Retoma Leve
        val isInjuryOrRehab = lower.contains("lesion") || lower.contains("lesão") ||
            lower.contains("dor") || lower.contains("mago") || lower.contains("ombro") ||
            lower.contains("pulso") || lower.contains("punho") || lower.contains("cotovelo") ||
            lower.contains("lombar") || lower.contains("joelho") || lower.contains("leve") ||
            lower.contains("repor") || lower.contains("reabilit") || lower.contains("cuidado")

        // 2. Deteção de tempo específico em minutos ou horas (ex: 100 min, 90 min, 2h, 45m)
        val extractedMinutes = extractCustomMinutes(lower)

        // 3. Deteção de retoma após longo período parado
        val isReturningAfterBreak = lower.contains("parado") || lower.contains("recomeçar") ||
            lower.contains("voltar") || lower.contains("meses") || lower.contains("enferruj") ||
            lower.contains("destrein")

        // 4. Deteção de fadiga muscular / cansaço de treinos anteriores
        val isFatigued = lower.contains("cansad") || lower.contains("moído") || lower.contains("exausto") ||
            lower.contains("dor muscular") || lower.contains("fadiga") || lower.contains("sem energia")

        // 5. Deteção de Habilidades específicas da calistenia
        val isMuscleUp = lower.contains("muscle") || lower.contains("muscle-up") || lower.contains("muscle up")
        val isPlanche = lower.contains("planche") || lower.contains("prancha reta")
        val isLsit = lower.contains("l-sit") || lower.contains("lsit")
        val isHandstand = lower.contains("pino") || lower.contains("parada de mãos") || lower.contains("handstand")
        val isDragonFlag = lower.contains("dragon") || lower.contains("bruce lee")

        // 6. Deteção de Equipamento específico do parque
        val mentionsBarra175 = lower.contains("1.75") || lower.contains("barra baixa") || lower.contains("australiana")
        val mentionsBarra225 = lower.contains("2.25") || lower.contains("barra alta") || lower.contains("elevações")
        val mentionsParalelas = lower.contains("paralela") || lower.contains("1m") || lower.contains("fundos") || lower.contains("dips")
        val mentionsBanco = lower.contains("banco") || lower.contains("cama") || lower.contains("abdominal") || lower.contains("core")

        when {
            // ================= CASO 1: LESÃO, DOR OU RETOMA LEVE =================
            isInjuryOrRehab -> {
                replyText = "Lamento saber da tua lesão, ${currentUser.name}! Na calistenia, a integridade dos tendões e articulações é a regra de ouro. " +
                    "Para repores os treinos em segurança sem risco de recaída:\n\n" +
                    "1. 🚫 **Evita movimentos explosivos** ou amplitudes extremas sob carga máxima (como fundos pesados ou kipping na barra alta).\n" +
                    "2. 🟢 **Foca na Barra Baixa de 1.75m**: apoia os pés firmes no solo para aliviar entre 30% a 50% do peso corporal em remadas suaves.\n" +
                    "3. 🧘 **Ativação isométrica e mobilidade**: pranchas no solo e elevações suaves de tronco no banco inclinado para reativar a circulação sanguínea sem impacto mecânico articular.\n\n" +
                    "Preparei esta **Missão de Reabilitação & Retoma Leve** (15 min) com cadência controlada (3s a descer, 2s a subir). Se sentires qualquer picada ou desconforto, pára imediatamente!"

                recommendedWorkout = Workout(
                    id = "rehab_mission_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Reabilitação Leve: Mobilidade e Retoma Segura",
                    subtitle = "Sessão adaptada de baixo impacto para proteção articular e retorno gradual",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = DifficultyLevel.BEGINNER,
                    bodyArea = BodyArea.FULL_BODY_SKILLS,
                    equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
                    description = "Treino terapêutico formulado pela IA: remadas assistidas na barra de 1.75m com pés no chão, contrações abdominais no banco sem impacto e estabilização isométrica suave. Foco exclusivo na circulação e cadência.",
                    exerciseIds = listOf("remada_barra_175", "abdominais_banco", "plank_solo"),
                    rounds = 2,
                    workSecPerExercise = 30,
                    restSecBetweenExercises = 35,
                    calorieBurnEstimate = 85,
                    isAiGenerated = true
                )
            }

            // ================= CASO 2: PEDIDO DE TEMPO ESPECÍFICO (Ex: 100m, 90m, 2h, etc.) =================
            extractedMinutes != null -> {
                val mins = extractedMinutes
                val hoursText = when {
                    mins >= 120 -> "2 horas"
                    mins >= 60 -> "${mins / 60}h ${if (mins % 60 > 0) "${mins % 60}m" else ""}"
                    else -> "${mins} minutos"
                }

                val rounds = when {
                    mins <= 10 -> 2
                    mins <= 20 -> 3
                    mins <= 45 -> 4
                    mins <= 90 -> 6
                    else -> 8
                }

                replyText = "Perfeito, ${currentUser.name}! Queres treinar durante **$hoursText** ($mins min). " +
                    "Para uma sessão com esta duração específica, estruturei a periodização do parque em 3 fases:\n\n" +
                    "• **Aquecimento & Ativação (15% do tempo)**: mobilidade nos pulsos, ombros e suspensão na barra baixa de 1.75m.\n" +
                    "• **Bloco Principal de Força (70% do tempo)**: séries sólidas nas paralelas de 1m, barra alta de 2.25m e banco.\n" +
                    "• **Resistência & Retorno à Calma (15% do tempo)**: descompressão espinhal e alongamentos de tronco.\n\n" +
                    "Aqui tens o teu plano calibrado para exatamente $mins minutos. Vamos somar pontos no ranking!"

                recommendedWorkout = Workout(
                    id = "custom_time_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Personalizada IA: Treino de $hoursText ($mins min)",
                    subtitle = "Volume calculado para exatamente $mins minutos nos equipamentos do parque",
                    durationCategory = if (mins >= 120) DurationCategory.TWO_HOURS else if (mins >= 60) DurationCategory.SIXTY_MIN else DurationCategory.THIRTY_MIN,
                    durationMinutes = mins,
                    difficulty = when {
                        currentUser.studentLevel.contains("Avançado", ignoreCase = true) -> DifficultyLevel.ADVANCED
                        currentUser.studentLevel.contains("Intermédio", ignoreCase = true) -> DifficultyLevel.INTERMEDIATE
                        else -> DifficultyLevel.BEGINNER
                    },
                    bodyArea = BodyArea.FULL_BODY_SKILLS,
                    equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.BARRA_225, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
                    description = "Rotina de calistenia gerada com tempo rigoroso de $mins minutos. Distribuída por rounds com descanso equilibrado para preservar a forma técnica até ao último minuto.",
                    exerciseIds = listOf("elevacoes_barra_225", "dips_paralelas_1m", "remada_barra_175", "elevacao_pernas_banco", "pushup_solo", "squat_solo"),
                    rounds = rounds,
                    workSecPerExercise = 45,
                    restSecBetweenExercises = 35,
                    calorieBurnEstimate = mins * 9,
                    isAiGenerated = true
                )
            }

            // ================= CASO 3: RECOMEÇAR APÓS LONGA PARAGEM =================
            isReturningAfterBreak -> {
                replyText = "Bem-vindo de volta ao parque, ${currentUser.name}! A memória muscular existe e os teus ganhos vão voltar mais rápido do que imaginas.\n\n" +
                    "O erro mais comum ao regressar é tentar fazer o mesmo volume de antes e ficar com dores incapacitantes 48h depois. " +
                    "O nosso foco esta semana é reeducação motora: 3 séries com 3 repetições de reserva (RPE 6/10), dando ênfase à conexão mente-músculo e respiração diafragmática.\n\n" +
                    "Aceita esta missão de reiniciação e vamos desbloquear a tua ofensiva no ranking!"

                recommendedWorkout = Workout(
                    id = "return_mission_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Retoma Progressiva: Reativação Neuromuscular",
                    subtitle = "Reativação dos padrões de puxar, empurrar e estabilizar o core",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = DifficultyLevel.BEGINNER,
                    bodyArea = BodyArea.FULL_BODY_SKILLS,
                    equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
                    description = "Missão de reintegração com foco na qualidade motora: remadas na barra 1.75m, flexões suaves no solo e elevações no banco inclinado.",
                    exerciseIds = listOf("remada_barra_175", "flexao_barra_175", "abdominais_banco", "squat_solo"),
                    rounds = 3,
                    workSecPerExercise = 35,
                    restSecBetweenExercises = 25,
                    calorieBurnEstimate = 120,
                    isAiGenerated = true
                )
            }

            // ================= CASO 4: FADIGA OU DORES MUSCULARES =================
            isFatigued -> {
                replyText = "Ouvir o corpo é o que separa um atleta comum de um atleta duradouro, ${currentUser.name}! " +
                    "Quando o sistema nervoso central ou a musculatura está em sobrecarga, um treino de choque só geraria atrito articular.\n\n" +
                    "Recomendo uma sessão de **Recuperação Ativa de 5 a 10 minutos**: apenas suspensão passiva na barra alta de 2.25m para descomprimir a coluna e abrir a caixa torácica, associada a mobilidade lombar e core leve no banco.\n\n" +
                    "Aceitas esta sessão regenerativa?"

                recommendedWorkout = Workout(
                    id = "recovery_mission_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Regenerativa: Descompressão e Recuperação Ativa",
                    subtitle = "Sessão leve para baixar a fadiga e manter a ofensiva ativa",
                    durationCategory = DurationCategory.FIVE_MIN,
                    durationMinutes = 5,
                    difficulty = DifficultyLevel.BEGINNER,
                    bodyArea = BodyArea.CORE,
                    equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.BANCO_ABDOMINAIS),
                    description = "Foco em alívio da tensão: suspensão ativa e passiva na barra de 2.25m para descompressão vertebral e ativação respiratória no banco de abdominais.",
                    exerciseIds = listOf("suspensao_barra_225", "abdominais_banco"),
                    rounds = 2,
                    workSecPerExercise = 30,
                    restSecBetweenExercises = 30,
                    calorieBurnEstimate = 45,
                    isAiGenerated = true
                )
            }

            // ================= CASO 5: OBJETIVO MUSCLE-UP =================
            isMuscleUp -> {
                replyText = "O Muscle-Up é o rito de passagem definitivo na calistenia! 💥\n\n" +
                    "Para desbloqueá-lo na nossa Barra Alta de 2.25m, precisas de 3 pilares:\n" +
                    "1. **Puxada Explosiva ao Peito/Umbigo**: puxar não até ao queixo, mas levar a barra à linha do esterno.\n" +
                    "2. **Transição Rápida da Cabeça**: no ponto alto, projetar o peito para cima da barra rapidamente.\n" +
                    "3. **Fundos Profundos nas Paralelas de 1m**: para garantir a força de extensão final.\n\n" +
                    "Estruturei esta missão específica de potência para te aproximar do teu primeiro Muscle-Up limpo!"

                recommendedWorkout = Workout(
                    id = "skill_mu_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Progressão de Muscle-Up: Potência e Transição",
                    subtitle = "Elevações explosivas na barra de 2.25m e fundos profundos nas paralelas de 1m",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    bodyArea = BodyArea.FULL_BODY_SKILLS,
                    equipmentNeeded = listOf(Equipment.BARRA_225, Equipment.PARALELAS_1M),
                    description = "Sessão focada na aceleração vertical: elevações altas na barra de 2.25m, transições isométricas e fundos com descida controlada nas paralelas de 1m.",
                    exerciseIds = listOf("elevacoes_barra_225", "dips_paralelas_1m", "elevacao_pernas_barra_225"),
                    rounds = 3,
                    workSecPerExercise = 40,
                    restSecBetweenExercises = 30,
                    calorieBurnEstimate = 175,
                    isAiGenerated = true
                )
            }

            // ================= CASO 6: L-SIT OU DRAGON FLAG =================
            isLsit || isDragonFlag -> {
                replyText = "Excelente escolha de habilidade! O L-sit nas nossas Barras Paralelas de 1m e o Dragon Flag no Banco de Abdominais são o expoente máximo de compressão abdominal e depressão escapular.\n\n" +
                    "Dica técnica essencial: bloqueia completamente os cotovelos nas paralelas e empurra os ombros para baixo com força máxima (depressão escapular ativa). " +
                    "Aqui tens uma missão focada em isometrias e compressão de pernas!"

                recommendedWorkout = Workout(
                    id = "skill_lsit_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Tensão Estática: L-Sit nas Paralelas & Dragon Flag",
                    subtitle = "Compressão abdominal e força escapular pura",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    bodyArea = BodyArea.CORE,
                    equipmentNeeded = listOf(Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS),
                    description = "Alternância entre sustentações de L-sit nas barras paralelas de 1m e descidas excêntricas de pernas no banco inclinado de abdominais.",
                    exerciseIds = listOf("lsit_paralelas_1m", "dragon_flag_banco", "elevacao_pernas_banco"),
                    rounds = 3,
                    workSecPerExercise = 30,
                    restSecBetweenExercises = 30,
                    calorieBurnEstimate = 150,
                    isAiGenerated = true
                )
            }

            // ================= CASO 7: FOCO NO BANCO DE ABDOMINAIS =================
            mentionsBanco -> {
                replyText = "O nosso Banco de Abdominais (tipo cama inclinada) oferece uma das melhores alavancas do parque! " +
                    "Por ter inclinação, permite trabalhar o reto abdominal na fase excêntrica com o dobro da ativação em relação ao solo plano.\n\n" +
                    "Criei uma rotina com flexão de tronco, elevação controlada de pernas e isometria. Vamos queimar o core e somar pontos!"

                recommendedWorkout = Workout(
                    id = "bench_core_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Foco Banco de Abdominais: Tronco e Elevação de Pernas",
                    subtitle = "Aproveitamento completo da cama inclinada do parque",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = DifficultyLevel.BEGINNER,
                    bodyArea = BodyArea.CORE,
                    equipmentNeeded = listOf(Equipment.BANCO_ABDOMINAIS, Equipment.SOLO),
                    description = "Exercícios dedicados no banco: elevações de pernas com controle da descida, flexões de tronco e sustentação isométrica.",
                    exerciseIds = listOf("abdominais_banco", "elevacao_pernas_banco", "plank_solo"),
                    rounds = 3,
                    workSecPerExercise = 40,
                    restSecBetweenExercises = 20,
                    calorieBurnEstimate = 140,
                    isAiGenerated = true
                )
            }

            // ================= CASO 8: FOCO NAS BARRAS PARALELAS 1M =================
            mentionsParalelas -> {
                replyText = "As nossas duas barras paralelas laterais de 1m são o aparelho perfeito para construir peito, tríceps e estabilidade escapular. " +
                    "Com 1m de altura, permitem descidas profundas sem tocar no chão.\n\n" +
                    "Lembrete de postura: mantém o peito ligeiramente inclinado para a frente para recrutar o peitoral inferior e não deixes os ombros subirem em direção às orelhas!\n\n" +
                    "Preparado para esta missão nas paralelas?"

                recommendedWorkout = Workout(
                    id = "dips_mission_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Potência nas Paralelas de 1m: Fundos e Apoios",
                    subtitle = "Volume focado de peitoral e tríceps nas barras paralelas",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    bodyArea = BodyArea.UPPER_BODY,
                    equipmentNeeded = listOf(Equipment.PARALELAS_1M, Equipment.SOLO),
                    description = "Série dedicada às barras paralelas de 1m: fundos estritos, apoios isométricos no topo e flexões de complemento no solo.",
                    exerciseIds = listOf("dips_paralelas_1m", "suporte_paralelas_1m", "pushup_solo"),
                    rounds = 3,
                    workSecPerExercise = 40,
                    restSecBetweenExercises = 25,
                    calorieBurnEstimate = 160,
                    isAiGenerated = true
                )
            }

            // ================= CASO 9: FOCO NAS BARRAS 1.75M OU 2.25M =================
            mentionsBarra175 || mentionsBarra225 -> {
                replyText = "Temos o combo ideal de barras no parque: a Barra Baixa de 1.75m para remadas horizontais perfeitas para a espessura das costas, " +
                    "e a Barra Alta de 2.25m para suspensão e elevações completas!\n\n" +
                    "Estruturei uma missão de puxada vertical e horizontal combinando as duas alturas de barra."

                recommendedWorkout = Workout(
                    id = "bars_mission_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Domínio das Barras 1.75m & 2.25m: Puxada Total",
                    subtitle = "Combinação da barra baixa para remadas e barra alta para elevações",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = DifficultyLevel.INTERMEDIATE,
                    bodyArea = BodyArea.UPPER_BODY,
                    equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.BARRA_225),
                    description = "Trabalho completo de costas e bíceps alternando remadas australianas na barra de 1.75m com elevações estritas na barra de 2.25m.",
                    exerciseIds = listOf("remada_barra_175", "elevacoes_barra_225", "remada_barra_175"),
                    rounds = 3,
                    workSecPerExercise = 40,
                    restSecBetweenExercises = 25,
                    calorieBurnEstimate = 165,
                    isAiGenerated = true
                )
            }

            // ================= CASO 10: MEMÓRIA DO HISTÓRICO RECENTE =================
            lower.contains("ontem") || lower.contains("último") || lower.contains("passado") -> {
                if (recentHistory != null) {
                    val exList = recentHistory.completedExerciseNames.joinToString(", ")
                    replyText = "Consultando a tua ficha de atleta: no teu último treino completaste '${recentHistory.workoutTitle}' (${recentHistory.durationMinutes} min).\n" +
                        "Exercícios concluídos: $exList.\n\n" +
                        "Como já gerámos fadiga nesses grupos, hoje a estratégia fisiológica certa é trabalhar os músculos antagonistas e o core para equilibrar o corpo e continuar a somar pontos no ranking!"

                    recommendedWorkout = Workout(
                        id = "antagonist_" + UUID.randomUUID().toString().take(6),
                        title = "Missão IA: Antagonistas & Equilíbrio Muscular",
                        subtitle = "Adaptação calculada pós-${recentHistory.workoutTitle}",
                        durationCategory = DurationCategory.FIFTEEN_MIN,
                        durationMinutes = 15,
                        difficulty = recentHistory.difficulty,
                        bodyArea = BodyArea.CORE,
                        equipmentNeeded = listOf(Equipment.BANCO_ABDOMINAIS, Equipment.PARALELAS_1M, Equipment.SOLO),
                        description = "Sessão compensatória para evitar desequilíbrios posturais. Foco em abdominais na cama inclinada e pernas.",
                        exerciseIds = listOf("abdominais_banco", "elevacao_pernas_banco", "squat_solo"),
                        rounds = 3,
                        workSecPerExercise = 40,
                        restSecBetweenExercises = 25,
                        calorieBurnEstimate = 150,
                        isAiGenerated = true
                    )
                } else {
                    replyText = "Ainda não tens registos anteriores nesta conta, ${currentUser.name}! " +
                        "Que tal começarmos hoje com um treino clássico de introdução ao parque de calistenia para inaugurar a tua posição no ranking?"
                }
            }

            // ================= RESPOSTA CONVERSACIONAL INTELIGENTE GERAL =================
            else -> {
                replyText = "Compreendi perfeitamente o teu ponto, ${currentUser.name}! Como teu treinador de calistenia, " +
                    "estou aqui para adaptar o parque às tuas necessidades diárias — quer precises de focar numa lesão, ajustar o tempo (desde 5 min até 2 horas), " +
                    "ou dominar um aparelho específico (Barra 1.75m, 2.25m, Paralelas 1m ou Banco de Abdominais).\n\n" +
                    "Com base no teu nível atual (${currentUser.studentLevel}), formulei esta missão balanceada para o dia de hoje. Podes aceitar para começar agora ou dizer-me o que preferes afinar!"

                recommendedWorkout = Workout(
                    id = "adaptive_" + UUID.randomUUID().toString().take(6),
                    title = "Missão Equilibrada IA: Força & Cadência",
                    subtitle = "Projetado para o teu nível atual (${currentUser.studentLevel})",
                    durationCategory = DurationCategory.FIFTEEN_MIN,
                    durationMinutes = 15,
                    difficulty = when {
                        currentUser.studentLevel.contains("Avançado", ignoreCase = true) -> DifficultyLevel.ADVANCED
                        currentUser.studentLevel.contains("Intermédio", ignoreCase = true) -> DifficultyLevel.INTERMEDIATE
                        else -> DifficultyLevel.BEGINNER
                    },
                    bodyArea = BodyArea.FULL_BODY_SKILLS,
                    equipmentNeeded = listOf(Equipment.BARRA_175, Equipment.PARALELAS_1M, Equipment.BANCO_ABDOMINAIS),
                    description = "Rotina equilibrada de calistenia no parque com foco em cadência de movimento e controle neuromuscular.",
                    exerciseIds = listOf("remada_barra_175", "dips_paralelas_1m", "abdominais_banco"),
                    rounds = 3,
                    workSecPerExercise = 40,
                    restSecBetweenExercises = 25,
                    calorieBurnEstimate = 150,
                    isAiGenerated = true
                )
            }
        }

        return ChatMessage(
            id = "ai_msg_" + UUID.randomUUID().toString().take(6),
            isFromAi = true,
            text = replyText,
            timestamp = timeNow,
            recommendedWorkout = recommendedWorkout,
            missionStatus = if (recommendedWorkout != null) MissionStatus.PENDING else MissionStatus.ACCEPTED
        )
    }

    /**
     * Extrai minutos de expressões como "100 minutos", "100m", "90 min", "2 horas", "2h", "45 min"
     */
    private fun extractCustomMinutes(prompt: String): Int? {
        // "2 horas" ou "2h"
        if (prompt.contains("2 horas") || prompt.contains("2h") || prompt.contains("2 hora")) return 120
        if (prompt.contains("1 hora") || prompt.contains("1h") || prompt.contains("60 min") || prompt.contains("60m")) return 60
        if (prompt.contains("1h30") || prompt.contains("1h 30") || prompt.contains("90 min") || prompt.contains("90m") || prompt.contains("90 minutos")) return 90
        if (prompt.contains("100 min") || prompt.contains("100m") || prompt.contains("100 minutos")) return 100
        if (prompt.contains("45 min") || prompt.contains("45m") || prompt.contains("45 minutos")) return 45
        if (prompt.contains("30 min") || prompt.contains("30m") || prompt.contains("30 minutos")) return 30
        if (prompt.contains("15 min") || prompt.contains("15m") || prompt.contains("15 minutos")) return 15
        if (prompt.contains("10 min") || prompt.contains("10m") || prompt.contains("10 minutos")) return 10
        if (prompt.contains("5 min") || prompt.contains("5m") || prompt.contains("5 minutos")) return 5

        // Regex genérica para capturar dígitos seguidos de "min" ou "minutos"
        val regex = Regex("""\b(\d{1,3})\s*(min|minuto|m)\b""")
        val match = regex.find(prompt)
        if (match != null) {
            val num = match.groupValues[1].toIntOrNull()
            if (num != null && num in 3..180) return num
        }
        return null
    }
}
