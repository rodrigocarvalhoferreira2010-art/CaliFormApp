package com.example.caliform.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.caliform.model.HistoryRecord
import com.example.caliform.model.UserProfile
import com.example.ui.theme.CaliBorder
import com.example.ui.theme.CaliElectricCyan
import com.example.ui.theme.CaliNeonLime
import com.example.ui.theme.CaliSurfaceCard
import com.example.ui.theme.CaliSurfaceDark
import com.example.ui.theme.CaliSurfaceElevated
import com.example.ui.theme.CaliTextMuted
import com.example.ui.theme.CaliTextPrimary
import com.example.ui.theme.CaliTextSecondary

@Composable
fun HistoryScreen(
    currentUser: UserProfile,
    historyList: List<HistoryRecord>,
    onClearHistory: () -> Unit,
    onManualLog: (title: String, duration: Int, diff: DifficultyLevel, rating: Int, notes: String) -> Unit,
    onRepeatWorkout: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showManualDialog by remember { mutableStateOf(false) }
    var showClearConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CaliSurfaceDark)
    ) {
        // Estatísticas do Aluno
        Card(
            colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "HISTÓRICO DE ${currentUser.name.uppercase()}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliElectricCyan,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Registo de Treinos no Parque",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliTextPrimary
                        )
                    }

                    Button(
                        onClick = { showManualDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CaliSurfaceElevated),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .border(1.dp, CaliNeonLime, RoundedCornerShape(10.dp))
                            .testTag("log_manual_workout_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Registar treino",
                                tint = CaliNeonLime,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Registo Manual",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaliNeonLime
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    HistoryStat(label = "Concluídos", value = "${currentUser.totalWorkoutsCompleted}")
                    HistoryStat(label = "Minutos", value = "${currentUser.totalMinutesTrained}m")
                    HistoryStat(label = "Sequência", value = "${currentUser.currentStreakDays}d 🔥")
                    HistoryStat(label = "Dominados", value = "${currentUser.masteredExerciseIds.size}")
                }
            }
        }

        // Título da Seção e Ação Limpar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${historyList.size} SESSÕES REGISTADAS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = CaliTextMuted,
                letterSpacing = 1.sp
            )

            if (historyList.isNotEmpty()) {
                Text(
                    text = "Limpar Tudo",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444),
                    modifier = Modifier
                        .clickable { showClearConfirm = true }
                        .testTag("clear_history_button")
                )
            }
        }

        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = CaliTextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ainda não tens treinos registados",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CaliTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Inicia qualquer treino de 5 min, 15 min ou 1h para gravar o teu primeiro registo!",
                        fontSize = 12.sp,
                        color = CaliTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("history_list")
            ) {
                items(historyList, key = { it.id }) { record ->
                    HistoryCardPt(
                        record = record,
                        onRepeat = { onRepeatWorkout(record.workoutTitle) }
                    )
                }
            }
        }
    }

    // Modal de Registo Manual
    if (showManualDialog) {
        var manualTitle by remember { mutableStateOf("Treino Livre no Parque") }
        var manualDuration by remember { mutableIntStateOf(20) }
        var manualNotes by remember { mutableStateOf("Trabalhei na barra de 1.75m e fiz fundos nas barras de 1m.") }
        var manualDifficulty by remember { mutableStateOf(DifficultyLevel.INTERMEDIATE) }

        AlertDialog(
            onDismissRequest = { showManualDialog = false },
            containerColor = CaliSurfaceDark,
            title = {
                Text(
                    text = "Registar Treino de Calistenia",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = CaliTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Adiciona treinos livres realizados nas barras ou em casa:",
                        fontSize = 12.sp,
                        color = CaliTextSecondary
                    )

                    OutlinedTextField(
                        value = manualTitle,
                        onValueChange = { manualTitle = it },
                        label = { Text("Nome da Sessão", fontSize = 11.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CaliTextPrimary,
                            unfocusedTextColor = CaliTextPrimary,
                            focusedBorderColor = CaliNeonLime,
                            unfocusedBorderColor = CaliBorder
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 15, 30, 60).forEach { dur ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (manualDuration == dur) CaliNeonLime else CaliSurfaceElevated)
                                    .clickable { manualDuration = dur }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (dur == 60) "1h" else "${dur}m",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (manualDuration == dur) Color(0xFF0F1115) else CaliTextPrimary
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = manualNotes,
                        onValueChange = { manualNotes = it },
                        label = { Text("Notas do Aluno / Sensações", fontSize = 11.sp) },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = CaliTextPrimary,
                            unfocusedTextColor = CaliTextPrimary,
                            focusedBorderColor = CaliNeonLime,
                            unfocusedBorderColor = CaliBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onManualLog(
                            manualTitle,
                            manualDuration,
                            manualDifficulty,
                            5,
                            manualNotes
                        )
                        showManualDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CaliNeonLime)
                ) {
                    Text("Gravar", color = Color(0xFF0F1115), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualDialog = false }) {
                    Text("Cancelar", color = CaliTextSecondary)
                }
            }
        )
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            containerColor = CaliSurfaceDark,
            title = {
                Text("Limpar Todo o Histórico?", color = CaliTextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Esta ação irá remover todas as sessões guardadas do teu perfil de aluno.", color = CaliTextSecondary)
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearHistory()
                        showClearConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Limpar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("Cancelar", color = CaliTextSecondary)
                }
            }
        )
    }
}

@Composable
fun HistoryCardPt(
    record: HistoryRecord,
    onRepeat: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CaliBorder, RoundedCornerShape(14.dp))
            .testTag("history_card_${record.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.dateFormatted,
                    fontSize = 11.sp,
                    color = CaliTextMuted,
                    fontWeight = FontWeight.Medium
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CaliNeonLime.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${record.durationMinutes} MIN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliNeonLime
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = record.workoutTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = CaliTextPrimary
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (star <= record.ratingStars) CaliNeonLime else CaliBorder,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (record.completedExerciseNames.isNotEmpty()) {
                Text(
                    text = "Exercícios Realizados: " + record.completedExerciseNames.joinToString(", "),
                    fontSize = 12.sp,
                    color = CaliTextSecondary,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (record.notes.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CaliSurfaceElevated)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "\"${record.notes}\"",
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = CaliTextPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            color = CaliTextPrimary
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = CaliTextSecondary
        )
    }
}
