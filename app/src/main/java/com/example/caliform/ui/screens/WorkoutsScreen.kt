package com.example.caliform.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.Equipment
import com.example.caliform.model.Exercise
import com.example.caliform.model.Workout
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
fun WorkoutsScreen(
    workouts: List<Workout>,
    onStartWorkout: (Workout) -> Unit,
    onViewExercise: (String) -> Unit,
    onNavigateToAiCoach: () -> Unit,
    exerciseMap: Map<String, Exercise>,
    modifier: Modifier = Modifier
) {
    var selectedEquipFilter by remember { mutableStateOf<Equipment?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 80.dp),
        modifier = modifier
            .fillMaxSize()
            .background(CaliSurfaceDark)
            .testTag("workouts_screen")
    ) {
        // ================= HEADER DA HUB =================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "HUB DE TREINOS",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliTextPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Toca nos níveis para expandir ou usa o IA Coach",
                            fontSize = 12.sp,
                            color = CaliTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botão para navegar para a aba do IA Coach
                    Box(
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CaliNeonLime.copy(alpha = 0.15f))
                            .border(1.dp, CaliNeonLime, RoundedCornerShape(10.dp))
                            .clickable(onClick = onNavigateToAiCoach)
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = CaliNeonLime,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "IA Coach →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaliNeonLime
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))


                // FILTRO RÁPIDO POR EQUIPAMENTO NO PARQUE COM BOTÕES UNIFORMES
                Text(
                    text = "FILTRAR POR EQUIPAMENTO NO PARQUE:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliTextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                val scroll = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scroll),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EquipmentFilterPill(
                        label = "Todos Equipamentos",
                        isSelected = selectedEquipFilter == null,
                        onClick = { selectedEquipFilter = null }
                    )
                    Equipment.values().forEach { eq ->
                        EquipmentFilterPill(
                            label = eq.shortName,
                            isSelected = selectedEquipFilter == eq,
                            onClick = { selectedEquipFilter = if (selectedEquipFilter == eq) null else eq }
                        )
                    }
                }
            }
        }

        // ================= SEÇÃO 1: INICIANTE (RECOLHIDA POR DEFEITO) =================
        item {
            val beginnerWorkouts = workouts.filter {
                it.difficulty == DifficultyLevel.BEGINNER &&
                    (selectedEquipFilter == null || it.equipmentNeeded.contains(selectedEquipFilter))
            }
            LevelHubSection(
                level = DifficultyLevel.BEGINNER,
                title = "Iniciante",
                subtitle = "Bases, postura na barra baixa (1.75m), solo e cama de abdominais",
                imageRes = R.drawable.img_beginner_training,
                workouts = beginnerWorkouts,
                exerciseMap = exerciseMap,
                onStartWorkout = onStartWorkout,
                onViewExercise = onViewExercise
            )
        }

        // ================= SEÇÃO 2: INTERMÉDIO (RECOLHIDA POR DEFEITO) =================
        item {
            val intermediateWorkouts = workouts.filter {
                it.difficulty == DifficultyLevel.INTERMEDIATE &&
                    (selectedEquipFilter == null || it.equipmentNeeded.contains(selectedEquipFilter))
            }
            LevelHubSection(
                level = DifficultyLevel.INTERMEDIATE,
                title = "Intermédio",
                subtitle = "Elevações na barra alta (2.25m), fundos nas paralelas (1m) e banco",
                imageRes = R.drawable.img_intermediate_training,
                workouts = intermediateWorkouts,
                exerciseMap = exerciseMap,
                onStartWorkout = onStartWorkout,
                onViewExercise = onViewExercise
            )
        }

        // ================= SEÇÃO 3: AVANÇADO (RECOLHIDA POR DEFEITO) =================
        item {
            val advancedWorkouts = workouts.filter {
                it.difficulty == DifficultyLevel.ADVANCED &&
                    (selectedEquipFilter == null || it.equipmentNeeded.contains(selectedEquipFilter))
            }
            LevelHubSection(
                level = DifficultyLevel.ADVANCED,
                title = "Avançado",
                subtitle = "Muscle-Ups, Dragon Flag no banco, L-Sit nas paralelas e Pistols",
                imageRes = R.drawable.img_advanced_training,
                workouts = advancedWorkouts,
                exerciseMap = exerciseMap,
                onStartWorkout = onStartWorkout,
                onViewExercise = onViewExercise
            )
        }
    }
}

@Composable
fun LevelHubSection(
    level: DifficultyLevel,
    title: String,
    subtitle: String,
    imageRes: Int,
    workouts: List<Workout>,
    exerciseMap: Map<String, Exercise>,
    onStartWorkout: (Workout) -> Unit,
    onViewExercise: (String) -> Unit
) {
    // 1- POR NORMA DIMINUÍDAS (RECOLHIDAS) NO INÍCIO, SÓ ABRE QUANDO CLICAR
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // HEADER COM IMAGEM ESCURECIDA DO PRATICANTE
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
                .clickable { isExpanded = !isExpanded }
                .border(
                    width = 1.dp,
                    color = if (isExpanded) Color(level.colorHex) else Color(level.colorHex).copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("level_header_${level.name}")
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Imagem de fundo
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "$title Calistenia",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Camada escura com gradiente para contraste e legibilidade
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF0D0F12).copy(alpha = 0.94f),
                                    Color(0xFF0D0F12).copy(alpha = 0.75f),
                                    Color(0xFF0D0F12).copy(alpha = 0.45f)
                                )
                            )
                        )
                )

                // Conteúdo do cabeçalho
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Color(level.colorHex))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = title.uppercase(),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliTextPrimary,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = CaliTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isExpanded) "▲ Tocar para recolher" else "▼ Tocar para ver treinos (${workouts.size})",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(level.colorHex)
                        )
                    }

                    // Botão Indicador Uniforme de Expansão
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CaliSurfaceCard.copy(alpha = 0.85f))
                            .border(1.dp, Color(level.colorHex).copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Recolher" else "Expandir",
                            tint = Color(level.colorHex),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // LISTA DE TREINOS DO NÍVEL (Treino 5 min, Treino 15 min, Treino 1h)
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(tween(250)) + fadeIn(tween(200)),
            exit = shrinkVertically(tween(200)) + fadeOut(tween(150))
        ) {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (workouts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CaliSurfaceCard)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum treino com o equipamento selecionado. Limpa o filtro acima.",
                            fontSize = 12.sp,
                            color = CaliTextSecondary
                        )
                    }
                } else {
                    workouts.forEach { workout ->
                        WorkoutHubRowItem(
                            workout = workout,
                            levelColor = Color(level.colorHex),
                            onStart = { onStartWorkout(workout) },
                            onViewExercise = onViewExercise,
                            exerciseMap = exerciseMap
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutHubRowItem(
    workout: Workout,
    levelColor: Color,
    onStart: () -> Unit,
    onViewExercise: (String) -> Unit,
    exerciseMap: Map<String, Exercise>
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CaliBorder, RoundedCornerShape(12.dp))
            .testTag("workout_item_${workout.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Linha superior: Duração, Título e Botão Uniforme Iniciar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(26.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CaliNeonLime.copy(alpha = 0.18f))
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (workout.durationMinutes <= 15) Icons.Default.ElectricBolt else Icons.Default.Schedule,
                                contentDescription = null,
                                tint = CaliNeonLime,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${workout.durationMinutes} MIN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliNeonLime
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = workout.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botão "Iniciar" Uniforme e Direito (Material 3 Button)
                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = CaliNeonLime),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("start_workout_${workout.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Começar treino",
                            tint = Color(0xFF0F1115),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Iniciar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F1115)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Subtítulo
            Text(
                text = workout.subtitle,
                fontSize = 11.sp,
                color = CaliTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Equipamentos Usados Neste Treino
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Handyman,
                    contentDescription = null,
                    tint = CaliElectricCyan,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Equipamento: ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliElectricCyan
                )
                Text(
                    text = workout.equipmentNeeded.joinToString(", ") { it.shortName },
                    fontSize = 10.sp,
                    color = CaliTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Exercícios Incluídos (Pills Uniformes)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                workout.exerciseIds.take(4).forEachIndexed { i, exId ->
                    val ex = exerciseMap[exId]
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(CaliSurfaceElevated)
                            .border(0.5.dp, CaliBorder, RoundedCornerShape(6.dp))
                            .clickable { onViewExercise(exId) }
                            .padding(horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ex?.title ?: exId,
                            fontSize = 9.sp,
                            color = CaliTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (workout.exerciseIds.size > 4) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${workout.exerciseIds.size - 4}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EquipmentFilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) CaliSurfaceElevated else CaliSurfaceCard)
            .border(
                width = 1.dp,
                color = if (isSelected) CaliNeonLime else CaliBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) CaliNeonLime else CaliTextSecondary
        )
    }
}
