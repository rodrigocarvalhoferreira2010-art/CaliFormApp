package com.example.caliform.ui.screens

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.caliform.model.Exercise
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    isMastered: Boolean,
    onToggleMastery: (String) -> Unit,
    onBack: () -> Unit,
    onNavigateToExercise: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Passos, 1: Pistas & Erros, 2: Prática
    val scrollState = rememberScrollState()

    var timerRunning by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableIntStateOf(45) }
    var practiceTargetSec by remember { mutableIntStateOf(45) }

    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            while (timerRunning && secondsRemaining > 0) {
                delay(1000)
                secondsRemaining--
            }
            if (secondsRemaining <= 0) {
                timerRunning = false
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
                        onClick = onBack,
                        modifier = Modifier.testTag("exercise_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = CaliTextPrimary
                        )
                    }
                },
                title = {
                    Text(
                        text = exercise.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .height(34.dp)
                            .clip(RoundedCornerShape(17.dp))
                            .background(if (isMastered) CaliNeonLime.copy(alpha = 0.2f) else CaliSurfaceCard)
                            .border(
                                1.dp,
                                if (isMastered) CaliNeonLime else CaliBorder,
                                RoundedCornerShape(17.dp)
                            )
                            .clickable { onToggleMastery(exercise.id) }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isMastered) Icons.Default.CheckCircle else Icons.Default.Check,
                            contentDescription = "Estado de domínio",
                            tint = if (isMastered) CaliNeonLime else CaliTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMastered) "Dominado" else "Marcar Dominado",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isMastered) CaliNeonLime else CaliTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Visualizador Animado da Mecânica
            ExerciseVisualizer(
                visualType = exercise.visualType,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Linha com Nível e Equipamento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(exercise.difficulty.colorHex).copy(alpha = 0.2f))
                        .border(1.dp, Color(exercise.difficulty.colorHex), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = exercise.difficulty.displayName.uppercase(),
                        color = Color(exercise.difficulty.colorHex),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Text(
                    text = exercise.targetRepsOrHold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliNeonLime
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = exercise.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = CaliTextPrimary
            )

            Text(
                text = exercise.subtitle,
                fontSize = 13.sp,
                color = CaliTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Equipamento Necessário
            Card(
                colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CaliBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Handyman,
                        contentDescription = null,
                        tint = CaliElectricCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "EQUIPAMENTO NECESSÁRIO NO PARQUE:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliElectricCyan,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = exercise.equipmentNeeded.joinToString(" + ") { it.displayName },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CaliTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Anatomia Trabalhada
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CaliSurfaceCard)
                    .border(1.dp, CaliBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "MÚSCULOS TRABALHADOS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliTextMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Principal: ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliNeonLime
                    )
                    Text(
                        text = exercise.primaryMuscles.joinToString(", "),
                        fontSize = 12.sp,
                        color = CaliTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row {
                    Text(
                        text = "Secundário: ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliElectricCyan
                    )
                    Text(
                        text = exercise.secondaryMuscles.joinToString(", "),
                        fontSize = 12.sp,
                        color = CaliTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Abas de Detalhe
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CaliSurfaceDark,
                contentColor = CaliNeonLime,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CaliNeonLime
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Passos da Técnica",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Pistas & Erros",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "Cronómetro",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedTab) {
                0 -> {
                    InstructionSectionPt(
                        badgeTitle = "FASE 1: POSICIONAMENTO & PREPARAÇÃO",
                        items = exercise.setupSteps,
                        accentColor = CaliElectricCyan
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    InstructionSectionPt(
                        badgeTitle = "FASE 2: EXECUÇÃO & CONTROLO MOTOR",
                        items = exercise.executionSteps,
                        accentColor = CaliNeonLime
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CaliBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🫁 RESPIRAÇÃO & PRESSÃO INTRA-ABDOMINAL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaliElectricCyan
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = exercise.breathingTip,
                                fontSize = 12.sp,
                                color = CaliTextPrimary
                            )
                        }
                    }
                }

                1 -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CaliNeonLime.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = CaliNeonLime,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "PISTAS ESSENCIAIS DO TREINADOR",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CaliNeonLime
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            exercise.formCues.forEach { cue ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "• ",
                                        color = CaliNeonLime,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = cue,
                                        color = CaliTextPrimary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ERROS COMUNS A EVITAR NO PARQUE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFEF4444)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            exercise.commonMistakes.forEach { mistake ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "✕ ",
                                        color = Color(0xFFEF4444),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = mistake,
                                        color = CaliTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "CRONÓMETRO DE PRÁTICA E SÉRIE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliElectricCyan,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Treina as repetições ou sustentação estática com cadência estrita.",
                                fontSize = 12.sp,
                                color = CaliTextSecondary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(CaliSurfaceElevated)
                                    .border(
                                        width = 3.dp,
                                        color = if (timerRunning) CaliNeonLime else CaliBorder,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val m = secondsRemaining / 60
                                    val s = secondsRemaining % 60
                                    Text(
                                        text = String.format("%02d:%02d", m, s),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (timerRunning) CaliNeonLime else CaliTextPrimary
                                    )
                                    Text(
                                        text = if (timerRunning) "EM PRÁTICA" else "PRONTO",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (timerRunning) CaliNeonLime else CaliTextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(30, 45, 60, 90).forEach { sec ->
                                    val isSel = practiceTargetSec == sec
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) CaliNeonLime else CaliSurfaceElevated)
                                            .border(
                                                1.dp,
                                                if (isSel) CaliNeonLime else CaliBorder,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                practiceTargetSec = sec
                                                secondsRemaining = sec
                                                timerRunning = false
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${sec}s",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSel) Color(0xFF0F1115) else CaliTextPrimary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { timerRunning = !timerRunning },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (timerRunning) Color(0xFFEF4444) else CaliNeonLime
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .height(44.dp)
                                        .testTag("practice_timer_start_button")
                                ) {
                                    Icon(
                                        imageVector = if (timerRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color(0xFF0F1115),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (timerRunning) "PAUSAR" else "INICIAR SÉRIE",
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF0F1115)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        timerRunning = false
                                        secondsRemaining = practiceTargetSec
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(CaliSurfaceElevated)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Reiniciar",
                                        tint = CaliTextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Árvore de Progressão
            Card(
                colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CaliBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ÁRVORE DE PROGRESSÃO DE CALISTENIA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliNeonLime,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exercise.progressionStep,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaliTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (exercise.previousProgression != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CaliSurfaceElevated)
                                    .clickable { onNavigateToExercise(exercise.previousProgression) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "← Variação Mais Fácil",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CaliElectricCyan
                                )
                            }
                        } else {
                            Text(text = "Variação Base", fontSize = 11.sp, color = CaliTextMuted)
                        }

                        if (exercise.nextProgression != null) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CaliSurfaceElevated)
                                    .clickable { onNavigateToExercise(exercise.nextProgression) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Próxima Habilidade →",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CaliNeonLime
                                )
                            }
                        } else {
                            Text(text = "Nível Supremo", fontSize = 11.sp, color = CaliNeonLime)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InstructionSectionPt(
    badgeTitle: String,
    items: List<String>,
    accentColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CaliBorder, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = badgeTitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = accentColor,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            items.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = step,
                        fontSize = 12.sp,
                        color = CaliTextPrimary
                    )
                }
            }
        }
    }
}
