package com.example.caliform.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caliform.data.CaliformRepository
import com.example.caliform.data.CalisthenicsData
import com.example.caliform.model.BodyArea
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.Equipment
import com.example.caliform.model.Exercise
import com.example.caliform.model.Workout
import com.example.caliform.ui.components.HamburgerTopBar
import com.example.caliform.ui.screens.AiCoachScreen
import com.example.caliform.ui.screens.AuthScreen
import com.example.caliform.ui.screens.ExerciseDetailScreen
import com.example.caliform.ui.screens.ExercisesScreen
import com.example.caliform.ui.screens.HistoryScreen
import com.example.caliform.ui.screens.ProfileLoginScreen
import com.example.caliform.ui.screens.WorkoutPlayerScreen
import com.example.caliform.ui.screens.WorkoutsScreen
import com.example.ui.theme.CaliBorder
import com.example.ui.theme.CaliNeonLime
import com.example.ui.theme.CaliSurfaceDark
import com.example.ui.theme.CaliTextPrimary
import com.example.ui.theme.CaliTextSecondary

@Composable
fun CaliformApp() {
    val context = LocalContext.current
    val repository = remember { CaliformRepository(context) }

    val isLoggedIn by repository.isLoggedIn.collectAsState()
    val currentUser by repository.currentUser.collectAsState()
    val allUsers by repository.allUsers.collectAsState()
    val historyList by repository.history.collectAsState()
    val masteredExerciseIds by repository.masteredExercises.collectAsState()
    val allWorkouts by repository.allWorkouts.collectAsState()
    val chatMessages by repository.chatMessages.collectAsState()

    var currentScreen by remember { mutableStateOf("workouts") } // workouts, exercises, ai_coach, history, profile
    var isHamburgerExpanded by remember { mutableStateOf(false) }

    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var activeWorkout by remember { mutableStateOf<Workout?>(null) }

    // Filtros de Exercícios
    var selectedArea by remember { mutableStateOf<BodyArea?>(null) }
    var selectedExerciseDifficulty by remember { mutableStateOf<DifficultyLevel?>(null) }
    var selectedExerciseEquipment by remember { mutableStateOf<Equipment?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val exerciseMap = remember { CalisthenicsData.exercises.associateBy { it.id } }

    // ================= ECRÃ DE AUTENTICAÇÃO OBRIGATÓRIA =================
    // Quando deslogado ou na primeira abertura, bloqueia o acesso à app e pede Login ou Criar Conta
    if (!isLoggedIn) {
        AuthScreen(
            onLoginSuccess = { email, pass ->
                repository.loginWithEmailPassword(email, pass)
            },
            onRegisterSuccess = { name, email, pass, level, goal ->
                repository.register(name, email, pass, level, goal)
            },
            onOAuthLogin = { provider, demoUser ->
                repository.loginWithOAuth(provider, demoUser)
            }
        )
        return
    }

    // Gerenciador do Botão Físico "Retroceder"
    BackHandler(enabled = isHamburgerExpanded || selectedExercise != null || activeWorkout != null || currentScreen != "workouts") {
        if (isHamburgerExpanded) {
            isHamburgerExpanded = false
        } else if (selectedExercise != null) {
            selectedExercise = null
        } else if (activeWorkout != null) {
            activeWorkout = null
        } else if (currentScreen != "workouts") {
            currentScreen = "workouts"
        }
    }

    // Reprodutor Ativo do Treino
    if (activeWorkout != null) {
        WorkoutPlayerScreen(
            workout = activeWorkout!!,
            exerciseMap = exerciseMap,
            onFinishAndLog = { title, durationMin, exNames, diff, rating, notes ->
                repository.recordCompletedWorkout(
                    workoutTitle = title,
                    durationMinutes = durationMin,
                    exerciseNames = exNames,
                    difficulty = diff,
                    ratingStars = rating,
                    notes = notes
                )
                activeWorkout = null
                currentScreen = "history"
            },
            onClose = { activeWorkout = null }
        )
        return
    }

    // Detalhe do Exercício
    if (selectedExercise != null) {
        val ex = selectedExercise!!
        ExerciseDetailScreen(
            exercise = ex,
            isMastered = masteredExerciseIds.contains(ex.id),
            onToggleMastery = { id -> repository.toggleMastery(id) },
            onBack = { selectedExercise = null },
            onNavigateToExercise = { nextId ->
                val next = repository.getExerciseById(nextId)
                if (next != null) {
                    selectedExercise = next
                }
            }
        )
        return
    }

    Scaffold(
        containerColor = CaliSurfaceDark,
        topBar = {
            HamburgerTopBar(
                currentUser = currentUser,
                isExpanded = isHamburgerExpanded,
                onToggleMenu = { isHamburgerExpanded = !isHamburgerExpanded },
                currentScreen = currentScreen,
                onNavigate = { screen ->
                    currentScreen = screen
                    isHamburgerExpanded = false
                },
                onQuickStartWorkout = { workoutId ->
                    val wo = repository.getWorkoutById(workoutId)
                    if (wo != null) {
                        activeWorkout = wo
                        isHamburgerExpanded = false
                    }
                },
                onLogout = {
                    repository.logout()
                    isHamburgerExpanded = false
                }
            )
        },
        bottomBar = {
            // Barra inferior com 5 categorias: Treinos, Exercícios, [⭐ IA Coach no meio], Histórico, Perfil
            NavigationBar(
                containerColor = CaliSurfaceDark,
                contentColor = CaliTextPrimary,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav_bar")
            ) {
                // 1. Treinos
                NavigationBarItem(
                    selected = currentScreen == "workouts",
                    onClick = {
                        currentScreen = "workouts"
                        isHamburgerExpanded = false
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Treinos"
                        )
                    },
                    label = {
                        Text(
                            text = "Treinos",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == "workouts") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CaliSurfaceDark,
                        selectedTextColor = CaliNeonLime,
                        indicatorColor = CaliNeonLime,
                        unselectedIconColor = CaliTextSecondary,
                        unselectedTextColor = CaliTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_workouts")
                )

                // 2. Exercícios
                NavigationBarItem(
                    selected = currentScreen == "exercises",
                    onClick = {
                        currentScreen = "exercises"
                        isHamburgerExpanded = false
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SportsGymnastics,
                            contentDescription = "Exercícios"
                        )
                    },
                    label = {
                        Text(
                            text = "Exercícios",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == "exercises") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CaliSurfaceDark,
                        selectedTextColor = CaliNeonLime,
                        indicatorColor = CaliNeonLime,
                        unselectedIconColor = CaliTextSecondary,
                        unselectedTextColor = CaliTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_exercises")
                )

                // 3. ⭐ BOTÃO IA COACH NO MEIO: Caixa esverdeada preenchida com o símbolo de brilho IA na cor do fundo da app
                NavigationBarItem(
                    selected = currentScreen == "ai_coach",
                    onClick = {
                        currentScreen = "ai_coach"
                        isHamburgerExpanded = false
                    },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CaliNeonLime)
                                .border(1.5.dp, CaliNeonLime, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "IA Coach",
                                tint = CaliSurfaceDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "IA Coach",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliNeonLime
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CaliSurfaceDark,
                        selectedTextColor = CaliNeonLime,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = CaliSurfaceDark,
                        unselectedTextColor = CaliNeonLime
                    ),
                    modifier = Modifier.testTag("nav_item_ai_coach")
                )

                // 4. Histórico
                NavigationBarItem(
                    selected = currentScreen == "history",
                    onClick = {
                        currentScreen = "history"
                        isHamburgerExpanded = false
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Histórico"
                        )
                    },
                    label = {
                        Text(
                            text = "Histórico",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == "history") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CaliSurfaceDark,
                        selectedTextColor = CaliNeonLime,
                        indicatorColor = CaliNeonLime,
                        unselectedIconColor = CaliTextSecondary,
                        unselectedTextColor = CaliTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_history")
                )

                // 5. Perfil
                NavigationBarItem(
                    selected = currentScreen == "profile",
                    onClick = {
                        currentScreen = "profile"
                        isHamburgerExpanded = false
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil"
                        )
                    },
                    label = {
                        Text(
                            text = "Perfil",
                            fontSize = 11.sp,
                            fontWeight = if (currentScreen == "profile") FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CaliSurfaceDark,
                        selectedTextColor = CaliNeonLime,
                        indicatorColor = CaliNeonLime,
                        unselectedIconColor = CaliTextSecondary,
                        unselectedTextColor = CaliTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_profile")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "workouts" -> {
                    WorkoutsScreen(
                        workouts = allWorkouts,
                        onStartWorkout = { workout -> activeWorkout = workout },
                        onViewExercise = { exId ->
                            val ex = repository.getExerciseById(exId)
                            if (ex != null) {
                                selectedExercise = ex
                            }
                        },
                        onNavigateToAiCoach = { currentScreen = "ai_coach" },
                        exerciseMap = exerciseMap
                    )
                }

                "exercises" -> {
                    val filteredExercises = repository.getExercises(
                        bodyArea = selectedArea,
                        difficulty = selectedExerciseDifficulty,
                        equipment = selectedExerciseEquipment,
                        searchQuery = searchQuery
                    )
                    ExercisesScreen(
                        exercises = filteredExercises,
                        masteredExerciseIds = masteredExerciseIds,
                        selectedArea = selectedArea,
                        onSelectArea = { selectedArea = it },
                        selectedDifficulty = selectedExerciseDifficulty,
                        onSelectDifficulty = { selectedExerciseDifficulty = it },
                        selectedEquipment = selectedExerciseEquipment,
                        onSelectEquipment = { selectedExerciseEquipment = it },
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onExerciseClick = { ex -> selectedExercise = ex }
                    )
                }

                "ai_coach" -> {
                    AiCoachScreen(
                        currentUser = currentUser,
                        historyList = historyList,
                        chatMessages = chatMessages,
                        onSendMessage = { prompt -> repository.sendUserChatMessage(prompt) },
                        onAcceptMission = { messageId -> repository.acceptMission(messageId) },
                        onRejectMission = { messageId -> repository.rejectMission(messageId) },
                        onStartWorkout = { workout -> activeWorkout = workout },
                        onGenerateCustomWorkout = { level, durationMin, equipList ->
                            repository.generateAiWorkout(level, durationMin, equipList)
                        },
                        exerciseMap = exerciseMap
                    )
                }

                "history" -> {
                    HistoryScreen(
                        currentUser = currentUser,
                        historyList = historyList,
                        onClearHistory = { repository.clearHistory() },
                        onManualLog = { title, duration, diff, rating, notes ->
                            repository.recordCompletedWorkout(
                                workoutTitle = title,
                                durationMinutes = duration,
                                exerciseNames = listOf("Treino Livre no Parque"),
                                difficulty = diff,
                                ratingStars = rating,
                                notes = notes
                            )
                        },
                        onRepeatWorkout = { title ->
                            val match = allWorkouts.find { it.title == title }
                            if (match != null) {
                                activeWorkout = match
                            }
                        }
                    )
                }

                "profile" -> {
                    ProfileLoginScreen(
                        currentUser = currentUser,
                        allUsers = allUsers,
                        leaderboard = repository.getStudentLeaderboard(),
                        onSwitchUser = { user -> repository.switchUser(user) },
                        onLoginOrCreate = { name, email, level, goal ->
                            repository.loginOrCreateUser(name, email, level, goal)
                        },
                        onLogout = { repository.logout() },
                        exerciseMap = exerciseMap
                    )
                }
            }
        }
    }
}
