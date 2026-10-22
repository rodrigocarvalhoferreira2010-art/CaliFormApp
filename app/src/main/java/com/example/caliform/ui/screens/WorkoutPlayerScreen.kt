package com.example.caliform.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.Exercise
import com.example.caliform.model.Workout
import com.example.caliform.ui.components.ExerciseVisualizer
import com.example.ui.theme.CaliBorder
import com.example.ui.theme.CaliElectricCyan
import com.example.ui.theme.CaliNeonLime
import com.example.ui.theme.CaliSurfaceCard
import com.example.ui.theme.CaliSurfaceDark
import com.example.ui.theme.CaliSurfaceElevated
import com.example.ui.theme.CaliTextMuted
import com.example.ui.theme.CaliTextPrimary
import com.example.ui.theme.CaliTextSecondary
import kotlinx.coroutines.delay

enum class SessionPhase {
    WORK, REST, COMPLETED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    workout: Workout,
    exerciseMap: Map<String, Exercise>,
    onFinishAndLog: (title: String, durationMin: Int, exNames: List<String>, diff: DifficultyLevel, rating: Int, notes: String) -> Unit,
    onClose: () -> Unit
) {
    var currentRound by remember { mutableIntStateOf(1) }
    var currentExerciseIndex by remember { mutableIntStateOf(0) }
    var phase by remember { mutableStateOf(SessionPhase.WORK) }
    var isPaused by remember { mutableStateOf(false) }

    val currentExerciseId = workout.exerciseIds.getOrElse(currentExerciseIndex) { "" }
    val currentExercise = exerciseMap[currentExerciseId]

    val workDuration = workout.workSecPerExercise
    val restDuration = workout.restSecBetweenExercises

    var secondsLeft by remember { mutableIntStateOf(workDuration) }
    var totalSecondsElapsed by remember { mutableIntStateOf(0) }

    var starRating by remember { mutableIntStateOf(5) }
    var sessionNotes by remember { mutableStateOf("") }

    LaunchedEffect(isPaused, phase, secondsLeft) {
        if (!isPaused && phase != SessionPhase.COMPLETED) {
            delay(1000)
            totalSecondsElapsed++
            if (secondsLeft > 1) {
                secondsLeft--
            } else {
                if (phase == SessionPhase.WORK) {
                    val isLastExercise = currentExerciseIndex == workout.exerciseIds.size - 1
                    val isLastRound = currentRound == workout.rounds

                    if (isLastExercise && isLastRound) {
                        phase = SessionPhase.COMPLETED
                    } else {
                        phase = SessionPhase.REST
                        secondsLeft = restDuration
                    }
                } else if (phase == SessionPhase.REST) {
                    if (currentExerciseIndex < workout.exerciseIds.size - 1) {
                        currentExerciseIndex++
                    } else {
                        currentExerciseIndex = 0
                        currentRound++
                    }
                    phase = SessionPhase.WORK
                    secondsLeft = workDuration
                }
            }
        }
    }

    Scaffold(
        containerColor = CaliSurfaceDark,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CaliSurfaceDark,
                    titleContentColor = CaliTextPrimary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("workout_player_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Sair do treino",
                            tint = CaliTextPrimary
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = workout.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = if (phase == SessionPhase.COMPLETED) "Sessão Concluída!" else "Ronda $currentRound de ${workout.rounds}",
                            fontSize = 11.sp,
                            color = CaliNeonLime
                        )
                    }
                },
                actions = {
                    if (phase != SessionPhase.COMPLETED) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CaliSurfaceCard)
                                .clickable { phase = SessionPhase.COMPLETED }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("skip_to_finish_button")
                        ) {
                            Text(
                                text = "Terminar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaliElectricCyan
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            )
        }
    ) { innerPadding ->
        if (phase == SessionPhase.COMPLETED) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(CaliNeonLime.copy(alpha = 0.2f))
                        .border(2.dp, CaliNeonLime, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Concluído",
                        tint = CaliNeonLime,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "TREINO CONQUISTADO!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = CaliNeonLime,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "Excelente disciplina no parque. Técnica e postura aprimoradas.",
                    fontSize = 13.sp,
                    color = CaliTextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val minutesRecorded = maxOf(1, totalSecondsElapsed / 60)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${minutesRecorded}m",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliTextPrimary
                            )
                            Text(text = "Tempo Real", fontSize = 11.sp, color = CaliTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${workout.exerciseIds.size * workout.rounds}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliNeonLime
                            )
                            Text(text = "Séries Totais", fontSize = 11.sp, color = CaliTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "~${workout.calorieBurnEstimate}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFF97316)
                            )
                            Text(text = "Calorias Est.", fontSize = 11.sp, color = CaliTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AVALIA O TEU ESFORÇO E TÉCNICA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliElectricCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (1..5).forEach { star ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Estrela $star",
                                    tint = if (star <= starRating) CaliNeonLime else CaliBorder,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clickable { starRating = star }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "NOTAS DO ALUNO (OPCIONAL)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliTextMuted
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = sessionNotes,
                            onValueChange = { sessionNotes = it },
                            placeholder = {
                                Text(
                                    "Ex: Boa estabilidade nas barras de 1m, senti bem as costas na barra 1.75m.",
                                    fontSize = 12.sp,
                                    color = CaliTextMuted
                                )
                            },
                            maxLines = 3,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CaliSurfaceElevated,
                                unfocusedContainerColor = CaliSurfaceElevated,
                                focusedBorderColor = CaliNeonLime,
                                unfocusedBorderColor = CaliBorder,
                                focusedTextColor = CaliTextPrimary,
                                unfocusedTextColor = CaliTextPrimary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("workout_player_notes_input")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val minutesRecorded = maxOf(workout.durationMinutes, totalSecondsElapsed / 60)
                        val exNames = workout.exerciseIds.mapNotNull { exerciseMap[it]?.title }
                        onFinishAndLog(
                            workout.title,
                            minutesRecorded,
                            exNames,
                            workout.difficulty,
                            starRating,
                            sessionNotes
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CaliNeonLime),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("save_to_history_button")
                ) {
                    Text(
                        text = "GUARDAR NO HISTÓRICO DE TREINOS",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = Color(0xFF0F1115),
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        } else {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val totalSteps = workout.rounds * workout.exerciseIds.size
                val currentStep = ((currentRound - 1) * workout.exerciseIds.size) + currentExerciseIndex + 1
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps.toFloat() },
                    color = CaliNeonLime,
                    trackColor = CaliSurfaceElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (phase == SessionPhase.WORK) CaliNeonLime.copy(alpha = 0.2f)
                                else CaliElectricCyan.copy(alpha = 0.2f)
                            )
                            .border(
                                1.dp,
                                if (phase == SessionPhase.WORK) CaliNeonLime else CaliElectricCyan,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (phase == SessionPhase.WORK) "🔥 SÉRIE DE TRABALHO" else "💤 DESCANSO & RESPIRAÇÃO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (phase == SessionPhase.WORK) CaliNeonLime else CaliElectricCyan
                        )
                    }

                    Text(
                        text = "Exercício ${currentExerciseIndex + 1} de ${workout.exerciseIds.size} • Ronda $currentRound/${workout.rounds}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaliTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentExercise?.title ?: "Exercício",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = CaliTextPrimary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (currentExercise != null) {
                        Text(
                            text = "🛠️ " + currentExercise.equipmentNeeded.joinToString(", ") { it.shortName },
                            fontSize = 11.sp,
                            color = CaliElectricCyan,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = currentExercise?.targetRepsOrHold ?: "",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliNeonLime
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (currentExercise != null) {
                    ExerciseVisualizer(
                        visualType = currentExercise.visualType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        isAnimating = !isPaused && phase == SessionPhase.WORK
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Contador Circular Central
                Box(
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape)
                        .background(CaliSurfaceCard)
                        .border(
                            width = 4.dp,
                            color = if (phase == SessionPhase.WORK) CaliNeonLime else CaliElectricCyan,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$secondsLeft",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Black,
                            color = if (phase == SessionPhase.WORK) CaliNeonLime else CaliElectricCyan
                        )
                        Text(
                            text = if (isPaused) "PAUSADO" else if (phase == SessionPhase.WORK) "SEGUNDOS" else "DESCANSO",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Controlos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { secondsLeft = maxOf(1, secondsLeft - 10) },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CaliSurfaceElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "-10 segundos",
                            tint = CaliTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { isPaused = !isPaused },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPaused) CaliNeonLime else CaliSurfaceElevated
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(56.dp)
                            .testTag("workout_player_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPaused) "Continuar" else "Pausar",
                            tint = if (isPaused) Color(0xFF0F1115) else CaliTextPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = { secondsLeft += 10 },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CaliSurfaceElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "+10 segundos",
                            tint = CaliTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = {
                            if (currentExerciseIndex < workout.exerciseIds.size - 1) {
                                currentExerciseIndex++
                            } else if (currentRound < workout.rounds) {
                                currentExerciseIndex = 0
                                currentRound++
                            } else {
                                phase = SessionPhase.COMPLETED
                            }
                            phase = SessionPhase.WORK
                            secondsLeft = workDuration
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CaliSurfaceElevated)
                            .testTag("workout_player_skip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Saltar exercício",
                            tint = CaliTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Pistas do Treinador para o Exercício em Curso
                if (currentExercise != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CaliBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "PISTAS DO TREINADOR PARA ESTE EXERCÍCIO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliNeonLime
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            currentExercise.formCues.take(2).forEach { cue ->
                                Text(
                                    text = "• $cue",
                                    fontSize = 12.sp,
                                    color = CaliTextPrimary,
                                    modifier = Modifier.padding(vertical = 1.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
