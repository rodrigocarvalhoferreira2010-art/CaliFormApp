package com.example.caliform.data

import android.content.Context
import android.content.SharedPreferences
import com.example.caliform.model.BodyArea
import com.example.caliform.model.ChatMessage
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.DurationCategory
import com.example.caliform.model.Equipment
import com.example.caliform.model.Exercise
import com.example.caliform.model.HistoryRecord
import com.example.caliform.model.MissionStatus
import com.example.caliform.model.StudentRank
import com.example.caliform.model.UserProfile
import com.example.caliform.model.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CaliformRepository(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("caliform_prefs", Context.MODE_PRIVATE)

    // Estado de Autenticação obrigatório
    // Por omissão: se já fez login antes mantém-se ligado; se abrir de novo ou der logout, exige login
    private val _isLoggedIn = MutableStateFlow(prefs.getBoolean("is_logged_in", false))
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Utilizador / Aluno atual
    private val _currentUser = MutableStateFlow(loadUser())
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Histórico de treinos e exercícios concluídos
    private val _history = MutableStateFlow(loadHistory())
    val history: StateFlow<List<HistoryRecord>> = _history.asStateFlow()

    // Lista de todos os alunos registados para o Ranking e troca de conta
    private val _allUsers = MutableStateFlow(loadAllUsers())
    val allUsers: StateFlow<List<UserProfile>> = _allUsers.asStateFlow()

    // Exercícios dominados pelo aluno atual
    private val _masteredExercises = MutableStateFlow(_currentUser.value.masteredExerciseIds)
    val masteredExercises: StateFlow<Set<String>> = _masteredExercises.asStateFlow()

    // Treinos disponíveis
    private val _allWorkouts = MutableStateFlow(CalisthenicsData.workouts)
    val allWorkouts: StateFlow<List<Workout>> = _allWorkouts.asStateFlow()

    // Mensagens do Chat com IA (com memória do histórico)
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    init {
        initAiChatMemory()
    }

    // ================= AUTENTICAÇÃO E SESSÃO =================

    fun loginWithEmailPassword(email: String, password: String): Boolean {
        if (email.isBlank()) return false
        val existing = _allUsers.value.find { it.email.equals(email.trim(), ignoreCase = true) }
        val user = existing ?: run {
            val nameFromEmail = email.substringBefore("@").replace(".", " ").replaceFirstChar { it.uppercase() }
            UserProfile(
                id = "user_" + UUID.randomUUID().toString().take(8),
                name = nameFromEmail,
                email = email.trim(),
                studentLevel = "Nível 1 • Aluno Iniciante",
                avatarInitial = nameFromEmail.firstOrNull()?.uppercase() ?: "A",
                targetGoal = "Dominar as bases do parque de calistenia",
                currentStreakDays = 1,
                totalWorkoutsCompleted = 0,
                totalMinutesTrained = 0,
                rankingPoints = 100,
                masteredExerciseIds = emptySet()
            )
        }

        if (existing == null) {
            val updated = _allUsers.value + user
            _allUsers.value = updated
            saveAllUsers(updated)
        }

        _currentUser.value = user
        _masteredExercises.value = user.masteredExerciseIds
        saveUser(user)

        prefs.edit().putBoolean("is_logged_in", true).apply()
        _isLoggedIn.value = true
        initAiChatMemory()
        return true
    }

    fun loginWithOAuth(provider: String, demoUser: UserProfile? = null): Boolean {
        val user = demoUser ?: run {
            val name = if (provider.contains("Google", ignoreCase = true)) "Aluno Google" else "Aluno Microsoft"
            UserProfile(
                id = "oauth_" + UUID.randomUUID().toString().take(8),
                name = name,
                email = "${name.lowercase().replace(" ", ".")}@caliform.pt",
                studentLevel = "Nível 1 • Aluno Iniciante",
                avatarInitial = "G",
                targetGoal = "Evoluir na calistenia com consistência",
                currentStreakDays = 2,
                totalWorkoutsCompleted = 2,
                totalMinutesTrained = 30,
                rankingPoints = 350,
                masteredExerciseIds = setOf("pushup_solo")
            )
        }

        val existingIndex = _allUsers.value.indexOfFirst { it.email.equals(user.email, ignoreCase = true) }
        val updated = if (existingIndex >= 0) {
            _allUsers.value
        } else {
            _allUsers.value + user
        }
        _allUsers.value = updated
        saveAllUsers(updated)

        _currentUser.value = user
        _masteredExercises.value = user.masteredExerciseIds
        saveUser(user)

        prefs.edit().putBoolean("is_logged_in", true).apply()
        _isLoggedIn.value = true
        initAiChatMemory()
        return true
    }

    fun register(name: String, email: String, password: String, studentLevel: String, targetGoal: String): Boolean {
        if (email.isBlank() || name.isBlank()) return false
        val initial = if (name.isNotBlank()) name.first().uppercase() else "A"
        val newUser = UserProfile(
            id = "user_" + UUID.randomUUID().toString().take(8),
            name = name.trim(),
            email = email.trim(),
            studentLevel = studentLevel,
            avatarInitial = initial,
            targetGoal = targetGoal.ifBlank { "Dominar flexões, elevações e fundos no parque" },
            currentStreakDays = 1,
            totalWorkoutsCompleted = 0,
            totalMinutesTrained = 0,
            rankingPoints = 150,
            masteredExerciseIds = emptySet()
        )

        val updated = listOf(newUser) + _allUsers.value.filter { it.email != newUser.email }
        _allUsers.value = updated
        saveAllUsers(updated)

        _currentUser.value = newUser
        _masteredExercises.value = emptySet()
        saveUser(newUser)

        prefs.edit().putBoolean("is_logged_in", true).apply()
        _isLoggedIn.value = true
        initAiChatMemory()
        return true
    }

    fun loginOrCreateUser(name: String, email: String, studentLevel: String, targetGoal: String) {
        register(name, email, "123456", studentLevel, targetGoal)
    }

    fun logout() {
        prefs.edit().putBoolean("is_logged_in", false).apply()
        _isLoggedIn.value = false
    }

    fun switchUser(user: UserProfile) {
        _currentUser.value = user
        _masteredExercises.value = user.masteredExerciseIds
        saveUser(user)
        val studentHistory = loadHistoryForUser(user.id)
        _history.value = studentHistory
        saveHistory(studentHistory)
        initAiChatMemory()
    }

    // ================= RANKING E SISTEMA DE PONTOS =================

    fun calculateMissionPoints(durationMinutes: Int, difficulty: DifficultyLevel): Int {
        val basePoints = when {
            durationMinutes <= 5 -> 100
            durationMinutes <= 15 -> 250
            durationMinutes <= 30 -> 450
            durationMinutes <= 60 -> 850
            durationMinutes <= 90 -> 1250
            durationMinutes <= 100 -> 1450
            else -> 1750
        }
        val difficultyMultiplier = when (difficulty) {
            DifficultyLevel.BEGINNER -> 1.0
            DifficultyLevel.INTERMEDIATE -> 1.3
            DifficultyLevel.ADVANCED -> 1.6
        }
        return (basePoints * difficultyMultiplier).toInt()
    }

    fun getStudentLeaderboard(): List<StudentRank> {
        val current = _currentUser.value
        val unifiedUsers = _allUsers.value.map { u ->
            if (u.id == current.id || u.email == current.email) current else u
        }
        val sorted = unifiedUsers.sortedByDescending { it.rankingPoints }
        return sorted.mapIndexed { index, user ->
            StudentRank(
                rank = index + 1,
                name = user.name,
                avatarInitial = user.avatarInitial,
                studentLevel = user.studentLevel,
                points = user.rankingPoints,
                workoutsCompleted = user.totalWorkoutsCompleted,
                isCurrentUser = (user.id == current.id || user.email == current.email)
            )
        }
    }

    fun toggleMastery(exerciseId: String) {
        val currentSet = _masteredExercises.value.toMutableSet()
        if (currentSet.contains(exerciseId)) {
            currentSet.remove(exerciseId)
        } else {
            currentSet.add(exerciseId)
        }
        _masteredExercises.value = currentSet
        val updatedUser = _currentUser.value.copy(masteredExerciseIds = currentSet)
        _currentUser.value = updatedUser
        saveUser(updatedUser)
        updateUserInAllUsers(updatedUser)
    }

    fun recordCompletedWorkout(
        workoutTitle: String,
        durationMinutes: Int,
        exerciseNames: List<String>,
        difficulty: DifficultyLevel,
        ratingStars: Int = 5,
        notes: String = ""
    ) {
        val hourFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val dateStr = "Hoje, $hourFormat"
        val pointsEarned = calculateMissionPoints(durationMinutes, difficulty)

        val record = HistoryRecord(
            id = "hist_" + UUID.randomUUID().toString().take(8),
            workoutTitle = workoutTitle,
            dateFormatted = dateStr,
            durationMinutes = durationMinutes,
            completedExerciseNames = exerciseNames,
            difficulty = difficulty,
            ratingStars = ratingStars,
            notes = notes.ifBlank { "Missão concluída com boa forma. +$pointsEarned pontos no ranking!" },
            pointsEarned = pointsEarned
        )

        val updatedHistory = listOf(record) + _history.value
        _history.value = updatedHistory
        saveHistory(updatedHistory)

        val user = _currentUser.value
        val updatedUser = user.copy(
            totalWorkoutsCompleted = user.totalWorkoutsCompleted + 1,
            totalMinutesTrained = user.totalMinutesTrained + durationMinutes,
            currentStreakDays = user.currentStreakDays + 1,
            rankingPoints = user.rankingPoints + pointsEarned
        )
        _currentUser.value = updatedUser
        saveUser(updatedUser)
        updateUserInAllUsers(updatedUser)

        onWorkoutCompletedAiNotice(record)
    }

    fun clearHistory() {
        _history.value = emptyList()
        prefs.edit().remove("history_json").apply()
        initAiChatMemory()
    }

    // ================= IA CHAT COM MEMÓRIA DE CONTA E HISTÓRICO =================

    fun initAiChatMemory() {
        val user = _currentUser.value
        val recentHistory = _history.value.firstOrNull()
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val greetingText: String
        val recommendedWorkout: Workout

        if (recentHistory != null) {
            val exSummary = recentHistory.completedExerciseNames.take(3).joinToString(", ")
            greetingText = "Olá ${user.name}! Sou o teu Treinador IA do CaliForm. 🧠\n\n" +
                "Analisei a tua conta e histórico recente: no teu último treino completaste '${recentHistory.workoutTitle}' (${recentHistory.durationMinutes} min) focando em: $exSummary.\n\n" +
                "Como recentemente estimulámos esses movimentos, hoje recomendo uma missão diferente para garantir equilíbrio muscular, recuperação articular ativa e ganho de pontos (+250 pts no ranking)! Aceitas este desafio?"

            recommendedWorkout = createAdaptiveMission(
                title = "Missão IA: Equilíbrio e Recuperação Ativa",
                subtitle = "Recomendação baseada no teu último treino ($exSummary)",
                targetDuration = 15,
                preferredLevel = if (recentHistory.difficulty == DifficultyLevel.ADVANCED) DifficultyLevel.INTERMEDIATE else recentHistory.difficulty,
                focusArea = if (recentHistory.workoutTitle.contains("Superiores", ignoreCase = true)) BodyArea.CORE else BodyArea.UPPER_BODY
            )
        } else {
            greetingText = "Olá ${user.name}! Sou o teu Treinador IA do CaliForm. 🧠\n\n" +
                "Vejo que estás no ${user.studentLevel} com o objetivo: '${user.targetGoal}'.\n\n" +
                "Como ainda não tens registos hoje, preparei uma missão introdutória no parque usando as nossas Barras e Solo para testar a tua cadência e começar a somar pontos no ranking!"

            recommendedWorkout = createAdaptiveMission(
                title = "Missão IA: Avaliação de Força Básica",
                subtitle = "Missão inicial para desbloquear pontos no ranking",
                targetDuration = 15,
                preferredLevel = DifficultyLevel.BEGINNER,
                focusArea = BodyArea.UPPER_BODY
            )
        }

        val initialMessages = listOf(
            ChatMessage(
                id = "ai_msg_intro",
                isFromAi = true,
                text = greetingText,
                timestamp = timeNow,
                recommendedWorkout = recommendedWorkout,
                missionStatus = MissionStatus.PENDING
            )
        )
        _chatMessages.value = initialMessages
    }

    fun sendUserChatMessage(userText: String) {
        if (userText.isBlank()) return
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            id = "msg_" + UUID.randomUUID().toString().take(6),
            isFromAi = false,
            text = userText,
            timestamp = timeNow
        )

        val updatedWithUser = _chatMessages.value + userMsg
        _chatMessages.value = updatedWithUser

        val aiResponse = generateAiChatResponse(userText)
        _chatMessages.value = _chatMessages.value + aiResponse
    }

    fun acceptMission(messageId: String): Workout? {
        var chosenWorkout: Workout? = null
        val updated = _chatMessages.value.map { msg ->
            if (msg.id == messageId && msg.recommendedWorkout != null) {
                chosenWorkout = msg.recommendedWorkout
                msg.copy(missionStatus = MissionStatus.ACCEPTED)
            } else msg
        }
        _chatMessages.value = updated

        if (chosenWorkout != null) {
            val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            val confirmMsg = ChatMessage(
                id = "ai_msg_" + UUID.randomUUID().toString().take(6),
                isFromAi = true,
                text = "Excelente decisão! 🟢 Missão '${chosenWorkout?.title}' aceite. Prepara o equipamento no parque, concentra-te na cadência e vamos a isso!",
                timestamp = timeNow
            )
            _chatMessages.value = _chatMessages.value + confirmMsg
        }
        return chosenWorkout
    }

    fun rejectMission(messageId: String) {
        val updated = _chatMessages.value.map { msg ->
            if (msg.id == messageId) {
                msg.copy(missionStatus = MissionStatus.REJECTED)
            } else msg
        }
        _chatMessages.value = updated

        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val alternativeWorkout = createAdaptiveMission(
            title = "Missão Alternativa IA: Express 5 min",
            subtitle = "Sessão rápida e leve para manter o hábito sem sobrecarga",
            targetDuration = 5,
            preferredLevel = DifficultyLevel.BEGINNER,
            focusArea = BodyArea.CORE
        )

        val rejectionFeedback = ChatMessage(
            id = "ai_msg_" + UUID.randomUUID().toString().take(6),
            isFromAi = true,
            text = "Sem qualquer problema! 🔴 Como atleta inteligente, respeitar a energia do dia é fundamental.\n\n" +
                "Preparei uma alternativa bem mais rápida de 5 minutos, ou podes dizer-me nos botões abaixo que tipo de foco preferes (ex: mais pernas, só abdominais ou descanso ativo)!",
            timestamp = timeNow,
            recommendedWorkout = alternativeWorkout,
            missionStatus = MissionStatus.PENDING
        )
        _chatMessages.value = _chatMessages.value + rejectionFeedback
    }

    private fun onWorkoutCompletedAiNotice(record: HistoryRecord) {
        val timeNow = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val notice = ChatMessage(
            id = "ai_msg_" + UUID.randomUUID().toString().take(6),
            isFromAi = true,
            text = "🎉 Parabéns! Acabaste de registar o treino '${record.workoutTitle}' (${record.durationMinutes} min) e conquistaste +${record.pointsEarned} pontos no ranking!\n\n" +
                "O teu corpo vai iniciar agora o processo de recuperação e supercompensação muscular. Excelente consistência!",
            timestamp = timeNow
        )
        _chatMessages.value = _chatMessages.value + notice
    }

    private fun generateAiChatResponse(prompt: String): ChatMessage {
        return CaliformAiBrain.processUserMessage(prompt, _currentUser.value, _history.value, _chatMessages.value)
    }

    private fun createAdaptiveMission(
        title: String,
        subtitle: String,
        targetDuration: Int,
        preferredLevel: DifficultyLevel,
        focusArea: BodyArea
    ): Workout {
        val allEx = CalisthenicsData.exercises
        val filtered = allEx.filter { it.bodyArea == focusArea || it.difficulty == preferredLevel }
        val pool = if (filtered.size >= 3) filtered else allEx
        val count = if (targetDuration <= 5) 3 else 4
        val chosen = pool.shuffled().take(count)

        val durationCat = when {
            targetDuration <= 5 -> DurationCategory.FIVE_MIN
            targetDuration <= 15 -> DurationCategory.FIFTEEN_MIN
            targetDuration <= 30 -> DurationCategory.THIRTY_MIN
            targetDuration <= 60 -> DurationCategory.SIXTY_MIN
            else -> DurationCategory.TWO_HOURS
        }

        val neededEquip = chosen.flatMap { it.equipmentNeeded }.distinct()

        return Workout(
            id = "mission_" + UUID.randomUUID().toString().take(6),
            title = title,
            subtitle = subtitle,
            durationCategory = durationCat,
            durationMinutes = targetDuration,
            difficulty = preferredLevel,
            bodyArea = focusArea,
            equipmentNeeded = neededEquip.ifEmpty { listOf(Equipment.SOLO) },
            description = "Missão formulada pelo CaliForm IA com base na memória do teu histórico. Foco na execução técnica e ganho de pontos no ranking.",
            exerciseIds = chosen.map { it.id },
            rounds = if (targetDuration <= 5) 2 else if (targetDuration <= 30) 3 else 5,
            workSecPerExercise = 40,
            restSecBetweenExercises = 20,
            calorieBurnEstimate = targetDuration * 9,
            isAiGenerated = true
        )
    }

    // ================= PLANIFICADOR MANUAL RÁPIDO =================

    fun generateAiWorkout(
        difficulty: DifficultyLevel,
        durationMinutes: Int,
        selectedEquipment: List<Equipment>,
        focusPrompt: String = ""
    ): Workout {
        val allEx = CalisthenicsData.exercises

        val equipmentSet = selectedEquipment.toSet()
        val matchingExercises = allEx.filter { ex ->
            ex.equipmentNeeded.any { it in equipmentSet }
        }

        val candidatePool = if (matchingExercises.size >= 2) matchingExercises else {
            allEx.filter { ex -> ex.equipmentNeeded.any { it in equipmentSet } || ex.equipmentNeeded.contains(Equipment.SOLO) }
        }

        val sortedByLevel = candidatePool.sortedBy { ex ->
            if (ex.difficulty == difficulty) 0 else 1
        }

        val countNeeded = when {
            durationMinutes <= 5 -> 3
            durationMinutes <= 15 -> 4
            else -> 6
        }

        val chosenExercises = sortedByLevel.take(countNeeded).ifEmpty {
            allEx.take(countNeeded)
        }

        val durationCat = when {
            durationMinutes <= 5 -> DurationCategory.FIVE_MIN
            durationMinutes <= 15 -> DurationCategory.FIFTEEN_MIN
            durationMinutes <= 30 -> DurationCategory.THIRTY_MIN
            durationMinutes <= 60 -> DurationCategory.SIXTY_MIN
            else -> DurationCategory.TWO_HOURS
        }

        val rounds = when {
            durationMinutes <= 5 -> 2
            durationMinutes <= 15 -> 3
            durationMinutes <= 30 -> 4
            durationMinutes <= 60 -> 5
            durationMinutes <= 90 -> 6
            durationMinutes <= 100 -> 7
            else -> 8
        }

        val equipNames = selectedEquipment.joinToString(", ") { it.shortName }
        val promptSuffix = if (focusPrompt.isNotBlank()) " • Foco: $focusPrompt" else ""

        val aiWorkout = Workout(
            id = "ai_plan_" + UUID.randomUUID().toString().take(6),
            title = "Plano IA: Treino ${durationMinutes} min ($equipNames)",
            subtitle = "Gerado por IA para os teus equipamentos selecionados$promptSuffix",
            durationCategory = durationCat,
            durationMinutes = durationMinutes,
            difficulty = difficulty,
            bodyArea = BodyArea.FULL_BODY_SKILLS,
            equipmentNeeded = selectedEquipment,
            description = "Treino personalizado criado instantaneamente pela IA para o teu nível ${difficulty.displayName}. " +
                "Utiliza especificamente: $equipNames. Executa as séries com boa forma e descansa entre intervalos.",
            exerciseIds = chosenExercises.map { it.id },
            rounds = rounds,
            workSecPerExercise = if (difficulty == DifficultyLevel.BEGINNER) 40 else 45,
            restSecBetweenExercises = if (difficulty == DifficultyLevel.BEGINNER) 25 else 20,
            calorieBurnEstimate = durationMinutes * 9,
            isAiGenerated = true
        )

        _allWorkouts.value = listOf(aiWorkout) + _allWorkouts.value
        return aiWorkout
    }

    fun getExercises(bodyArea: BodyArea?, difficulty: DifficultyLevel?, equipment: Equipment?, searchQuery: String): List<Exercise> {
        return CalisthenicsData.exercises.filter { exercise ->
            val matchesArea = bodyArea == null || exercise.bodyArea == bodyArea
            val matchesDiff = difficulty == null || exercise.difficulty == difficulty
            val matchesEquip = equipment == null || exercise.equipmentNeeded.contains(equipment)
            val matchesSearch = searchQuery.isBlank() ||
                exercise.title.contains(searchQuery, ignoreCase = true) ||
                exercise.subtitle.contains(searchQuery, ignoreCase = true) ||
                exercise.primaryMuscles.any { it.contains(searchQuery, ignoreCase = true) }
            matchesArea && matchesDiff && matchesEquip && matchesSearch
        }
    }

    fun getWorkoutsByLevel(difficulty: DifficultyLevel): List<Workout> {
        return _allWorkouts.value.filter { it.difficulty == difficulty }
    }

    fun getExerciseById(id: String): Exercise? {
        return CalisthenicsData.exercises.find { it.id == id }
    }

    fun getWorkoutById(id: String): Workout? {
        return _allWorkouts.value.find { it.id == id }
    }

    // ================= PERSISTÊNCIA =================

    private fun updateUserInAllUsers(updatedUser: UserProfile) {
        val list = _allUsers.value.map { if (it.id == updatedUser.id) updatedUser else it }
        _allUsers.value = list
        saveAllUsers(list)
    }

    private fun saveAllUsers(users: List<UserProfile>) {
        val arr = JSONArray()
        users.forEach { user ->
            val obj = JSONObject().apply {
                put("id", user.id)
                put("name", user.name)
                put("email", user.email)
                put("studentLevel", user.studentLevel)
                put("avatarInitial", user.avatarInitial)
                put("targetGoal", user.targetGoal)
                put("currentStreakDays", user.currentStreakDays)
                put("totalWorkoutsCompleted", user.totalWorkoutsCompleted)
                put("totalMinutesTrained", user.totalMinutesTrained)
                put("rankingPoints", user.rankingPoints)
                val setArray = JSONArray()
                user.masteredExerciseIds.forEach { setArray.put(it) }
                put("masteredExerciseIds", setArray)
            }
            arr.put(obj)
        }
        prefs.edit().putString("all_users_json", arr.toString()).apply()
    }

    private fun loadAllUsers(): List<UserProfile> {
        val str = prefs.getString("all_users_json", null) ?: return CalisthenicsData.sampleUsers
        return try {
            val arr = JSONArray(str)
            val list = mutableListOf<UserProfile>()
            for (i in 0 until arr.length()) {
                val json = arr.getJSONObject(i)
                val masteries = mutableSetOf<String>()
                val mArr = json.optJSONArray("masteredExerciseIds")
                if (mArr != null) {
                    for (j in 0 until mArr.length()) {
                        masteries.add(mArr.getString(j))
                    }
                }
                list.add(
                    UserProfile(
                        id = json.getString("id"),
                        name = json.getString("name"),
                        email = json.getString("email"),
                        studentLevel = json.getString("studentLevel"),
                        avatarInitial = json.getString("avatarInitial"),
                        targetGoal = json.getString("targetGoal"),
                        currentStreakDays = json.optInt("currentStreakDays", 1),
                        totalWorkoutsCompleted = json.optInt("totalWorkoutsCompleted", 0),
                        totalMinutesTrained = json.optInt("totalMinutesTrained", 0),
                        rankingPoints = json.optInt("rankingPoints", 500),
                        masteredExerciseIds = masteries
                    )
                )
            }
            list.ifEmpty { CalisthenicsData.sampleUsers }
        } catch (e: Exception) {
            CalisthenicsData.sampleUsers
        }
    }

    private fun saveUser(user: UserProfile) {
        val json = JSONObject().apply {
            put("id", user.id)
            put("name", user.name)
            put("email", user.email)
            put("studentLevel", user.studentLevel)
            put("avatarInitial", user.avatarInitial)
            put("targetGoal", user.targetGoal)
            put("currentStreakDays", user.currentStreakDays)
            put("totalWorkoutsCompleted", user.totalWorkoutsCompleted)
            put("totalMinutesTrained", user.totalMinutesTrained)
            put("rankingPoints", user.rankingPoints)
            val setArray = JSONArray()
            user.masteredExerciseIds.forEach { setArray.put(it) }
            put("masteredExerciseIds", setArray)
        }
        prefs.edit().putString("current_user_json", json.toString()).apply()
    }

    private fun loadUser(): UserProfile {
        val str = prefs.getString("current_user_json", null) ?: return CalisthenicsData.sampleUsers.first()
        return try {
            val json = JSONObject(str)
            val masteries = mutableSetOf<String>()
            val arr = json.optJSONArray("masteredExerciseIds")
            if (arr != null) {
                for (i in 0 until arr.length()) {
                    masteries.add(arr.getString(i))
                }
            }
            UserProfile(
                id = json.getString("id"),
                name = json.getString("name"),
                email = json.getString("email"),
                studentLevel = json.getString("studentLevel"),
                avatarInitial = json.getString("avatarInitial"),
                targetGoal = json.getString("targetGoal"),
                currentStreakDays = json.optInt("currentStreakDays", 1),
                totalWorkoutsCompleted = json.optInt("totalWorkoutsCompleted", 0),
                totalMinutesTrained = json.optInt("totalMinutesTrained", 0),
                rankingPoints = json.optInt("rankingPoints", 1450),
                masteredExerciseIds = masteries
            )
        } catch (e: Exception) {
            CalisthenicsData.sampleUsers.first()
        }
    }

    private fun saveHistory(historyList: List<HistoryRecord>) {
        val arr = JSONArray()
        historyList.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("workoutTitle", item.workoutTitle)
                put("dateFormatted", item.dateFormatted)
                put("durationMinutes", item.durationMinutes)
                put("difficulty", item.difficulty.name)
                put("ratingStars", item.ratingStars)
                put("notes", item.notes)
                put("pointsEarned", item.pointsEarned)
                val exArr = JSONArray()
                item.completedExerciseNames.forEach { exArr.put(it) }
                put("completedExerciseNames", exArr)
            }
            arr.put(obj)
        }
        val serialized = arr.toString()
        prefs.edit()
            .putString("history_json", serialized)
            .putString("history_json_${_currentUser.value.id}", serialized)
            .apply()
    }

    fun loadHistoryForUser(userId: String): List<HistoryRecord> {
        val str = prefs.getString("history_json_$userId", null)
            ?: prefs.getString("history_json", null)
            ?: return CalisthenicsData.getStudentInitialHistory(userId)
        return parseHistoryJson(str, userId)
    }

    private fun loadHistory(): List<HistoryRecord> {
        val currentUserId = _currentUser.value.id
        val str = prefs.getString("history_json_$currentUserId", null)
            ?: prefs.getString("history_json", null)
            ?: return CalisthenicsData.getStudentInitialHistory(currentUserId)
        return parseHistoryJson(str, currentUserId)
    }

    private fun parseHistoryJson(jsonStr: String, fallbackUserId: String): List<HistoryRecord> {
        return try {
            val arr = JSONArray(jsonStr)
            val list = mutableListOf<HistoryRecord>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val exList = mutableListOf<String>()
                val exArr = obj.optJSONArray("completedExerciseNames")
                if (exArr != null) {
                    for (j in 0 until exArr.length()) {
                        exList.add(exArr.getString(j))
                    }
                }
                list.add(
                    HistoryRecord(
                        id = obj.getString("id"),
                        workoutTitle = obj.getString("workoutTitle"),
                        dateFormatted = obj.getString("dateFormatted"),
                        durationMinutes = obj.getInt("durationMinutes"),
                        completedExerciseNames = exList,
                        difficulty = try { DifficultyLevel.valueOf(obj.getString("difficulty")) } catch (e: Exception) { DifficultyLevel.BEGINNER },
                        ratingStars = obj.optInt("ratingStars", 5),
                        notes = obj.optString("notes", ""),
                        pointsEarned = obj.optInt("pointsEarned", 150)
                    )
                )
            }
            list.ifEmpty { CalisthenicsData.getStudentInitialHistory(fallbackUserId) }
        } catch (e: Exception) {
            CalisthenicsData.getStudentInitialHistory(fallbackUserId)
        }
    }
}
