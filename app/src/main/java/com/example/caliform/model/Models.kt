package com.example.caliform.model

enum class DifficultyLevel(val displayName: String, val colorHex: Long) {
    BEGINNER("Iniciante", 0xFF22C55E),       // Verde
    INTERMEDIATE("Intermédio", 0xFFF59E0B),  // Âmbar
    ADVANCED("Avançado", 0xFFEF4444)         // Vermelho
}

enum class BodyArea(val displayName: String, val iconDescription: String) {
    UPPER_BODY("Superiores", "Peito, Costas, Braços e Ombros"),
    CORE("Core e Abdominais", "Zona Média, Oblíquos e Lombar"),
    LOWER_BODY("Inferiores", "Quadríceps, Glúteos e Gémeos"),
    FULL_BODY_SKILLS("Habilidades", "Muscle-up, Parada de Mãos e Levers")
}

enum class DurationCategory(val label: String, val approxMinutes: Int) {
    FIVE_MIN("Treino 5 min", 5),
    FIFTEEN_MIN("Treino 15 min", 15),
    THIRTY_MIN("Treino 30 min", 30),
    SIXTY_MIN("Treino 1h", 60),
    TWO_HOURS("Treino 2h", 120)
}

enum class Equipment(val displayName: String, val shortName: String, val icon: String, val description: String) {
    BARRA_175(
        displayName = "Barra Baixa (1.75m)",
        shortName = "Barra 1.75m",
        icon = "⎯",
        description = "Ideal para remadas australianas, elevações com salto e apoio"
    ),
    BARRA_225(
        displayName = "Barra Alta (2.25m)",
        shortName = "Barra 2.25m",
        icon = "⎺",
        description = "Barra para elevações completas, suspensão ativa e muscle-ups"
    ),
    PARALELAS_1M(
        displayName = "Barras Paralelas (1m)",
        shortName = "Paralelas 1m",
        icon = "║║",
        description = "Duas barras paralelas laterais para fundos (dips), L-sit e apoios"
    ),
    BANCO_ABDOMINAIS(
        displayName = "Banco de Abdominais",
        shortName = "Banco Abdominais",
        icon = "◿",
        description = "Estrutura tipo cama/banco inclinado para elevar o tronco e pernas"
    ),
    SOLO(
        displayName = "Solo / Peso Corporal",
        shortName = "Solo",
        icon = "▭",
        description = "Sem equipamento: flexões, pranchas, agachamentos e saltos"
    )
}

data class Exercise(
    val id: String,
    val title: String,
    val subtitle: String,
    val difficulty: DifficultyLevel,
    val bodyArea: BodyArea,
    val targetRepsOrHold: String,
    val equipmentNeeded: List<Equipment>,
    val primaryMuscles: List<String>,
    val secondaryMuscles: List<String>,
    val setupSteps: List<String>,
    val executionSteps: List<String>,
    val formCues: List<String>,
    val commonMistakes: List<String>,
    val breathingTip: String,
    val progressionStep: String,
    val previousProgression: String? = null,
    val nextProgression: String? = null,
    val visualType: String // e.g. "pushup", "pullup", "dip", "squat", "plank", "lsit", "handstand", "muscleup"
)

data class Workout(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationCategory: DurationCategory,
    val durationMinutes: Int,
    val difficulty: DifficultyLevel,
    val bodyArea: BodyArea,
    val equipmentNeeded: List<Equipment>,
    val description: String,
    val exerciseIds: List<String>,
    val rounds: Int = 3,
    val workSecPerExercise: Int = 40,
    val restSecBetweenExercises: Int = 20,
    val calorieBurnEstimate: Int = 120,
    val isAiGenerated: Boolean = false
)

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val studentLevel: String,
    val avatarInitial: String,
    val targetGoal: String,
    val currentStreakDays: Int = 3,
    val totalWorkoutsCompleted: Int = 14,
    val totalMinutesTrained: Int = 210,
    val rankingPoints: Int = 920,
    val masteredExerciseIds: Set<String> = setOf("pushup_solo", "plank_solo", "squat_solo")
)

data class StudentRank(
    val rank: Int,
    val name: String,
    val avatarInitial: String,
    val studentLevel: String,
    val points: Int,
    val workoutsCompleted: Int,
    val isCurrentUser: Boolean = false
)

enum class MissionStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

data class ChatMessage(
    val id: String,
    val isFromAi: Boolean,
    val text: String,
    val timestamp: String,
    val recommendedWorkout: Workout? = null,
    val missionStatus: MissionStatus = MissionStatus.PENDING
)

data class HistoryRecord(
    val id: String,
    val workoutTitle: String,
    val dateFormatted: String,
    val durationMinutes: Int,
    val completedExerciseNames: List<String>,
    val difficulty: DifficultyLevel,
    val ratingStars: Int = 5,
    val notes: String = "",
    val pointsEarned: Int = 150
)

