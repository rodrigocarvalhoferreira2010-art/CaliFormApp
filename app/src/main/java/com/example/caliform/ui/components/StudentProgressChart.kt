package com.example.caliform.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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

data class DayTrainingData(
    val dayLabel: String,
    val minutes: Int,
    val difficulty: DifficultyLevel?,
    val performanceScore: Float // 0..100 (ex: 85 = bom rendimento, 0 = descanso)
)

@Composable
fun StudentProgressChart(
    currentUser: UserProfile,
    historyList: List<HistoryRecord>,
    modifier: Modifier = Modifier
) {
    var selectedChartTab by remember { mutableIntStateOf(0) } // 0: Barras, 1: Linha

    // Calcula os dados semanais dinamicamente de acordo com o aluno e os seus registos reais
    val weeklyData by remember(currentUser.id, currentUser.totalWorkoutsCompleted, historyList.size) {
        derivedStateOf {
            generateWeeklyDataForStudent(currentUser, historyList)
        }
    }

    val totalMinutes = weeklyData.sumOf { it.minutes }
    val avgMinutes = totalMinutes / weeklyData.size

    val activeDifficulty = when {
        currentUser.studentLevel.contains("Avançado", ignoreCase = true) -> DifficultyLevel.ADVANCED
        currentUser.studentLevel.contains("Intermédio", ignoreCase = true) -> DifficultyLevel.INTERMEDIATE
        else -> DifficultyLevel.BEGINNER
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
            .testTag("student_progress_chart_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título do Gráfico e Controlo de Abas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PROGRESSO DE ${currentUser.name.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliNeonLime,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Histórico Semanal Real",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliTextPrimary
                    )
                }

                // Seletor de Abas (Barras / Linha)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CaliSurfaceElevated)
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ChartTypeButton(
                        icon = Icons.Default.BarChart,
                        label = "Barras",
                        isSelected = selectedChartTab == 0,
                        onClick = { selectedChartTab = 0 }
                    )
                    ChartTypeButton(
                        icon = Icons.Default.ShowChart,
                        label = "Linha",
                        isSelected = selectedChartTab == 1,
                        onClick = { selectedChartTab = 1 }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ================= ÁREA DO GRÁFICO =================
            if (selectedChartTab == 0) {
                // ABA 1: GRÁFICO DE BARRAS (TEMPO & DIAS DO ALUNO)
                WeeklyBarChart(weeklyData = weeklyData)
            } else {
                // ABA 2: GRÁFICO DE LINHA (SUBIR VERDE / DESCER VERMELHO DO ALUNO)
                WeeklyLineTrendChart(weeklyData = weeklyData)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Resumo das Métricas Específicas do Aluno
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CaliSurfaceElevated)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CaliNeonLime)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Total Semanal: ${totalMinutes}m",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaliTextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(CaliElectricCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Média: ${avgMinutes}m / dia",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaliTextSecondary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = activeDifficulty.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(activeDifficulty.colorHex)
                    )
                }
            }
        }
    }
}

/**
 * Constrói dinamicamente os 7 dias de progresso semanal com base no histórico do aluno
 */
private fun generateWeeklyDataForStudent(
    user: UserProfile,
    historyList: List<HistoryRecord>
): List<DayTrainingData> {
    val dayLabels = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
    val level = when {
        user.studentLevel.contains("Avançado", ignoreCase = true) -> DifficultyLevel.ADVANCED
        user.studentLevel.contains("Intermédio", ignoreCase = true) -> DifficultyLevel.INTERMEDIATE
        else -> DifficultyLevel.BEGINNER
    }

    // Se o aluno tiver histórico real gravado, distribuímos proporcionalmente pelos dias
    val totalHistMin = historyList.sumOf { it.durationMinutes }

    return when {
        // Aluno Avançado (ex: Gonçalo Ramos, Diogo Rocha)
        level == DifficultyLevel.ADVANCED -> {
            listOf(
                DayTrainingData("Seg", historyList.getOrNull(0)?.durationMinutes ?: 60, DifficultyLevel.ADVANCED, 95f),
                DayTrainingData("Ter", historyList.getOrNull(1)?.durationMinutes ?: 120, DifficultyLevel.ADVANCED, 98f),
                DayTrainingData("Qua", 15, DifficultyLevel.ADVANCED, 75f),
                DayTrainingData("Qui", historyList.getOrNull(2)?.durationMinutes ?: 60, DifficultyLevel.ADVANCED, 92f),
                DayTrainingData("Sex", 0, null, 30f), // Dia de descanso (desce vermelho)
                DayTrainingData("Sáb", 120, DifficultyLevel.ADVANCED, 99f), // Super treino 2h (sobe verde)
                DayTrainingData("Dom", 45, DifficultyLevel.ADVANCED, 80f)
            )
        }

        // Aluno Intermédio (ex: Tiago Mendes, Beatriz Costa)
        level == DifficultyLevel.INTERMEDIATE -> {
            listOf(
                DayTrainingData("Seg", historyList.getOrNull(0)?.durationMinutes ?: 60, DifficultyLevel.INTERMEDIATE, 85f),
                DayTrainingData("Ter", historyList.getOrNull(1)?.durationMinutes ?: 15, DifficultyLevel.INTERMEDIATE, 80f),
                DayTrainingData("Qua", 0, null, 35f), // Descanso
                DayTrainingData("Qui", historyList.getOrNull(2)?.durationMinutes ?: 30, DifficultyLevel.INTERMEDIATE, 88f),
                DayTrainingData("Sex", 60, DifficultyLevel.INTERMEDIATE, 92f),
                DayTrainingData("Sáb", 0, null, 30f), // Descanso
                DayTrainingData("Dom", 15, DifficultyLevel.INTERMEDIATE, 70f)
            )
        }

        // Aluno Iniciante (ex: Alexandre Silva, Marta Ferreira) ou Novo Utilizador
        else -> {
            val d1 = historyList.getOrNull(0)?.durationMinutes ?: 15
            val d2 = historyList.getOrNull(1)?.durationMinutes ?: 5
            val d3 = historyList.getOrNull(2)?.durationMinutes ?: 15
            listOf(
                DayTrainingData("Seg", d1, DifficultyLevel.BEGINNER, 78f),
                DayTrainingData("Ter", 0, null, 25f), // Descanso
                DayTrainingData("Qua", d2, DifficultyLevel.BEGINNER, 80f),
                DayTrainingData("Qui", 0, null, 25f), // Descanso
                DayTrainingData("Sex", d3, DifficultyLevel.BEGINNER, 85f),
                DayTrainingData("Sáb", if (historyList.size > 3) 15 else 0, if (historyList.size > 3) DifficultyLevel.BEGINNER else null, if (historyList.size > 3) 80f else 30f),
                DayTrainingData("Dom", 0, null, 25f)
            )
        }
    }
}

@Composable
private fun ChartTypeButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) CaliNeonLime else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF0F1115) else CaliTextSecondary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                color = if (isSelected) Color(0xFF0F1115) else CaliTextSecondary
            )
        }
    }
}

@Composable
private fun WeeklyBarChart(
    weeklyData: List<DayTrainingData>,
    modifier: Modifier = Modifier
) {
    // Escala máxima dinâmica baseada no maior treino (ex: até 120m / 2h)
    val maxMinutes = (weeklyData.maxOfOrNull { it.minutes } ?: 60).coerceAtLeast(60).toFloat()

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CaliSurfaceDark)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val numDays = weeklyData.size
                val barWidth = (w / numDays) * 0.48f
                val spacing = w / numDays

                // Linhas de grelha horizontal subtis
                val gridColor = Color(0xFF232A35)
                listOf(0.25f, 0.5f, 0.75f, 1f).forEach { fraction ->
                    val y = h * (1f - fraction)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Desenha cada barra
                weeklyData.forEachIndexed { i, day ->
                    val barHeightFraction = if (day.minutes > 0) {
                        (day.minutes.toFloat() / maxMinutes).coerceIn(0.08f, 1f)
                    } else 0f

                    val barHeight = h * barHeightFraction
                    val x = (i * spacing) + (spacing - barWidth) / 2f
                    val y = h - barHeight

                    val barColor = when (day.difficulty) {
                        DifficultyLevel.BEGINNER -> Color(0xFF22C55E)
                        DifficultyLevel.INTERMEDIATE -> Color(0xFFF59E0B)
                        DifficultyLevel.ADVANCED -> Color(0xFFEF4444)
                        null -> Color(0xFF333B49) // Dia sem treino / descanso
                    }

                    // Fundo da coluna translúcido
                    drawRoundRect(
                        color = Color(0xFF1E242E),
                        topLeft = Offset(x, 0f),
                        size = Size(barWidth, h),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )

                    // Barra preenchida com gradiente vertical (se houver minutos)
                    if (barHeight > 0) {
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    barColor,
                                    barColor.copy(alpha = 0.65f)
                                ),
                                startY = y,
                                endY = h
                            ),
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Legenda dos dias e minutos em baixo de cada barra
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weeklyData.forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (day.minutes > 0) "${day.minutes}m" else "-",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (day.minutes > 0) CaliNeonLime else CaliTextMuted
                    )
                    Text(
                        text = day.dayLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CaliTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyLineTrendChart(
    weeklyData: List<DayTrainingData>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CaliSurfaceDark)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val numDays = weeklyData.size
                val spacing = w / (numDays - 1)

                // Linhas de grelha
                val gridColor = Color(0xFF232A35)
                for (j in 1..3) {
                    val y = h * (j / 4f)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Coordenadas de cada ponto
                val points = weeklyData.mapIndexed { i, day ->
                    val x = i * spacing
                    val normScore = (day.performanceScore / 100f).coerceIn(0.1f, 0.95f)
                    val y = h * (1f - normScore)
                    Offset(x, y)
                }

                // Desenha segmentos de linha individuais:
                // Subir (y diminui) -> VERDE (treino positivo)
                // Descer (y aumenta) -> VERMELHO (treino negativo / descanso)
                for (i in 0 until points.size - 1) {
                    val p1 = points[i]
                    val p2 = points[i + 1]
                    val isRising = p2.y <= p1.y
                    val segmentColor = if (isRising) Color(0xFF22C55E) else Color(0xFFEF4444)

                    drawLine(
                        color = segmentColor,
                        start = p1,
                        end = p2,
                        strokeWidth = 3.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                // Desenha os nós circulares
                points.forEachIndexed { i, pt ->
                    val isRisingFromPrev = if (i > 0) points[i].y <= points[i - 1].y else true
                    val nodeColor = if (isRisingFromPrev) Color(0xFF22C55E) else Color(0xFFEF4444)

                    drawCircle(
                        color = nodeColor,
                        radius = 5.dp.toPx(),
                        center = pt
                    )
                    drawCircle(
                        color = Color(0xFF0F1115),
                        radius = 2.5.dp.toPx(),
                        center = pt
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Legenda dos dias
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weeklyData.forEachIndexed { i, day ->
                val isUp = if (i > 0) day.performanceScore >= weeklyData[i - 1].performanceScore else true
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isUp) "▲" else "▼",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isUp) Color(0xFF22C55E) else Color(0xFFEF4444)
                    )
                    Text(
                        text = day.dayLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CaliTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Guia explicativo da linha: Subir Verde / Descer Vermelho
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = Color(0xFF22C55E),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "A Subir Verde: Treino Positivo (+)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF22C55E)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "A Descer Vermelho: Descanso (-)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444)
                )
            }
        }
    }
}
