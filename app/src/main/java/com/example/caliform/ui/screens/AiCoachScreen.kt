package com.example.caliform.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caliform.model.ChatMessage
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.Equipment
import com.example.caliform.model.Exercise
import com.example.caliform.model.HistoryRecord
import com.example.caliform.model.MissionStatus
import com.example.caliform.model.UserProfile
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
fun AiCoachScreen(
    currentUser: UserProfile,
    historyList: List<HistoryRecord>,
    chatMessages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onAcceptMission: (String) -> Workout?,
    onRejectMission: (String) -> Unit,
    onStartWorkout: (Workout) -> Unit,
    onGenerateCustomWorkout: (DifficultyLevel, Int, List<Equipment>) -> Workout,
    exerciseMap: Map<String, Exercise>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Chat Inteligente, 1: Gerador por Equipamentos
    var inputText by remember { mutableStateOf("") }

    // Gerador de equipamentos
    var genLevel by remember { mutableStateOf(DifficultyLevel.BEGINNER) }
    var genDuration by remember { mutableIntStateOf(15) }
    val genEquipments = remember {
        mutableStateListOf(
            Equipment.BARRA_175,
            Equipment.BARRA_225,
            Equipment.PARALELAS_1M,
            Equipment.BANCO_ABDOMINAIS,
            Equipment.SOLO
        )
    }

    val listState = rememberLazyListState()

    // Rola para a última mensagem automaticamente quando o chat atualiza
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CaliSurfaceDark)
            .testTag("ai_coach_screen")
    ) {
        // ================= CABEÇALHO DO IA COACH =================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CaliSurfaceCard)
                .border(1.dp, CaliBorder)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img_caliform_logo),
                        contentDescription = "Logo CaliForm",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, CaliNeonLime, RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "CALIFORM ",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliTextPrimary
                            )
                            Text(
                                text = "IA COACH",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliNeonLime
                            )
                        }
                        Text(
                            text = "Memória do Aluno & Histórico Ativo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliElectricCyan
                        )
                    }
                }

                // Badge de Memória da Conta
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CaliSurfaceElevated)
                        .border(1.dp, CaliBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🧠 ${historyList.size} treinos salvos",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliNeonLime
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Seletor de Abas: Chat vs Gerador
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CaliSurfaceDark)
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedTab == 0) CaliNeonLime else Color.Transparent)
                        .clickable { selectedTab = 0 },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = null,
                            tint = if (selectedTab == 0) Color(0xFF0F1115) else CaliTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "IA Chat & Missões",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 0) Color(0xFF0F1115) else CaliTextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedTab == 1) CaliNeonLime else Color.Transparent)
                        .clickable { selectedTab = 1 },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Handyman,
                            contentDescription = null,
                            tint = if (selectedTab == 1) Color(0xFF0F1115) else CaliTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Gerador Equipamentos",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTab == 1) Color(0xFF0F1115) else CaliTextSecondary
                        )
                    }
                }
            }
        }

        // ================= CONTEÚDO =================
        if (selectedTab == 0) {
            // ================= ABA 0: IA CHAT =================
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(chatMessages, key = { it.id }) { message ->
                        ChatBubble(
                            message = message,
                            currentUser = currentUser,
                            exerciseMap = exerciseMap,
                            onAccept = {
                                val workout = onAcceptMission(message.id)
                                if (workout != null) {
                                    onStartWorkout(workout)
                                }
                            },
                            onReject = { onRejectMission(message.id) }
                        )
                    }
                }

                // Sugestões Rápidas de Pergunta
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    QuickPromptChip("Como treinei ontem, o que recomendas hoje?") {
                        inputText = "Como treinei ontem, o que recomendas hoje?"
                    }
                    QuickPromptChip("Circuito rápido de 5 minutos") {
                        inputText = "Quero um circuito rápido de 5 minutos para hoje."
                    }
                    QuickPromptChip("Focar no Banco de Abdominais") {
                        inputText = "Hoje quero focar no Banco de Abdominais."
                    }
                    QuickPromptChip("Treinar nas Paralelas de 1m") {
                        inputText = "Recomenda-me um treino para as barras paralelas de 1m."
                    }
                    QuickPromptChip("Como subir no ranking?") {
                        inputText = "Como posso ganhar mais pontos no ranking hoje?"
                    }
                }

                // Barra de Envio de Mensagem
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CaliSurfaceCard)
                        .border(1.dp, CaliBorder)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = "Pergunta à IA ou pede um treino específico...",
                                fontSize = 12.sp,
                                color = CaliTextMuted
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CaliNeonLime,
                            unfocusedBorderColor = CaliBorder,
                            focusedTextColor = CaliTextPrimary,
                            unfocusedTextColor = CaliTextPrimary,
                            focusedContainerColor = CaliSurfaceDark,
                            unfocusedContainerColor = CaliSurfaceDark
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_chat_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CaliNeonLime)
                            .testTag("ai_chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = Color(0xFF0F1115),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        } else {
            // ================= ABA 1: GERADOR POR EQUIPAMENTOS DO PARQUE =================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Handyman,
                                contentDescription = null,
                                tint = CaliNeonLime,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "EQUIPAMENTOS DISPONÍVEIS NO PARQUE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliNeonLime,
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = "Seleciona os aparelhos que queres utilizar no teu treino:",
                            fontSize = 11.sp,
                            color = CaliTextSecondary,
                            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                        )

                        // 5 Equipamentos específicos do parque
                        val parkEquipmentList = listOf(
                            Equipment.BARRA_175,
                            Equipment.BARRA_225,
                            Equipment.PARALELAS_1M,
                            Equipment.BANCO_ABDOMINAIS,
                            Equipment.SOLO
                        )

                        parkEquipmentList.forEach { equip ->
                            val isChecked = genEquipments.contains(equip)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isChecked) CaliSurfaceElevated else CaliSurfaceDark)
                                    .border(
                                        1.dp,
                                        if (isChecked) CaliNeonLime.copy(alpha = 0.5f) else CaliBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        if (isChecked) {
                                            if (genEquipments.size > 1) genEquipments.remove(equip)
                                        } else {
                                            genEquipments.add(equip)
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { check ->
                                        if (check) genEquipments.add(equip) else {
                                            if (genEquipments.size > 1) genEquipments.remove(equip)
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = CaliNeonLime,
                                        checkmarkColor = Color(0xFF0F1115),
                                        uncheckedColor = CaliTextMuted
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = equip.displayName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChecked) CaliTextPrimary else CaliTextSecondary
                                    )
                                    Text(
                                        text = equip.description,
                                        fontSize = 10.sp,
                                        color = CaliTextMuted,
                                        lineHeight = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Nível do Aluno
                        Text(
                            text = "GRAU DE DIFICULDADE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliTextMuted,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            DifficultyLevel.values().forEach { level ->
                                val isSel = genLevel == level
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) Color(level.colorHex) else CaliSurfaceDark)
                                        .border(
                                            1.dp,
                                            if (isSel) Color(level.colorHex) else CaliBorder,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { genLevel = level },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = level.displayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color(0xFF0F1115) else CaliTextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Duração pretendida
                        Text(
                            text = "DURAÇÃO DO TREINO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliTextMuted,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        val durations = listOf(5, 15, 30, 60)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            durations.forEach { dur ->
                                val isSel = genDuration == dur
                                val label = if (dur == 60) "1h" else "${dur}m"
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) CaliNeonLime else CaliSurfaceDark)
                                        .border(
                                            1.dp,
                                            if (isSel) CaliNeonLime else CaliBorder,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { genDuration = dur },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color(0xFF0F1115) else CaliTextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                val generated = onGenerateCustomWorkout(genLevel, genDuration, genEquipments.toList())
                                onStartWorkout(generated)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CaliNeonLime),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("generate_and_start_ai_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color(0xFF0F1115),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "GERAR E INICIAR TREINO COM IA",
                                color = Color(0xFF0F1115),
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    currentUser: UserProfile,
    exerciseMap: Map<String, Exercise>,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    if (message.isFromAi) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(CaliNeonLime, CaliElectricCyan)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "IA",
                    tint = Color(0xFF0F1115),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp))
                        .background(CaliSurfaceCard)
                        .border(1.dp, CaliBorder, RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = message.text,
                            fontSize = 13.sp,
                            color = CaliTextPrimary,
                            lineHeight = 18.sp
                        )

                        // Cartão de Missão Recomendada pela IA
                        if (message.recommendedWorkout != null) {
                            val workout = message.recommendedWorkout
                            Spacer(modifier = Modifier.height(12.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = CaliSurfaceDark),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, CaliNeonLime, RoundedCornerShape(12.dp))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "🎯 MISSÃO RECOMENDADA",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = CaliNeonLime,
                                            letterSpacing = 1.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(CaliSurfaceElevated)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "+250 pts",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CaliElectricCyan
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = workout.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CaliTextPrimary
                                    )

                                    Text(
                                        text = workout.subtitle,
                                        fontSize = 11.sp,
                                        color = CaliTextSecondary,
                                        lineHeight = 15.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = CaliTextMuted,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "${workout.durationMinutes} min",
                                                fontSize = 11.sp,
                                                color = CaliTextSecondary
                                            )
                                        }

                                        Text(text = "•", color = CaliTextMuted)

                                        Text(
                                            text = workout.difficulty.displayName,
                                            fontSize = 11.sp,
                                            color = Color(workout.difficulty.colorHex),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Botões Interativos: Aceitar (Verde) e Recusar (Vermelho)
                                    when (message.missionStatus) {
                                        MissionStatus.PENDING -> {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                // Botão Aceitar (Verde)
                                                Button(
                                                    onClick = onAccept,
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(38.dp)
                                                        .testTag("accept_mission_button")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Aceitar Missão",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = Color.White
                                                    )
                                                }

                                                // Botão Recusar (Vermelho)
                                                Button(
                                                    onClick = onReject,
                                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(38.dp)
                                                        .testTag("reject_mission_button")
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Recusar",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                        MissionStatus.ACCEPTED -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFF22C55E).copy(alpha = 0.2f))
                                                    .border(1.dp, Color(0xFF22C55E), RoundedCornerShape(8.dp))
                                                    .padding(vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "✓ Missão Aceite pelo Aluno",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF86EFAC)
                                                )
                                            }
                                        }
                                        MissionStatus.REJECTED -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFFEF4444).copy(alpha = 0.2f))
                                                    .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(8.dp))
                                                    .padding(vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "✕ Missão Recusada • Ajustado",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFFCA5A5)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "CaliForm IA • ${message.timestamp}",
                    fontSize = 10.sp,
                    color = CaliTextMuted,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    } else {
        // Mensagem enviada pelo Aluno
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp))
                        .background(CaliNeonLime)
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = message.text,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0F1115)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${currentUser.name} • ${message.timestamp}",
                    fontSize = 10.sp,
                    color = CaliTextMuted,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(CaliSurfaceCard)
                    .border(1.dp, CaliBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentUser.avatarInitial,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = CaliNeonLime
                )
            }
        }
    }
}

@Composable
private fun QuickPromptChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CaliSurfaceElevated)
            .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = CaliTextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}
