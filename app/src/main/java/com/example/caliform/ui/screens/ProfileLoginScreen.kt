package com.example.caliform.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caliform.model.Exercise
import com.example.caliform.model.StudentRank
import com.example.caliform.model.UserProfile
import com.example.caliform.ui.components.StudentProgressChart
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
fun ProfileLoginScreen(
    currentUser: UserProfile,
    allUsers: List<UserProfile>,
    leaderboard: List<StudentRank>,
    onSwitchUser: (UserProfile) -> Unit,
    onLoginOrCreate: (name: String, email: String, level: String, goal: String) -> Unit,
    onLogout: () -> Unit,
    exerciseMap: Map<String, Exercise>,
    modifier: Modifier = Modifier
) {
    var showLoginForm by remember { mutableStateOf(false) }

    var loginName by remember { mutableStateOf("") }
    var loginEmail by remember { mutableStateOf("") }
    var loginLevel by remember { mutableStateOf("Nível 1 • Aluno Iniciante") }
    var loginGoal by remember { mutableStateOf("Dominar 10 flexões e remadas na barra de 1.75m") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CaliSurfaceDark)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("profile_login_screen")
    ) {
        // ================= CARTÃO DE PERFIL ATIVO =================
        Card(
            colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CaliBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar Grande
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CaliNeonLime, CaliElectricCyan)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.avatarInitial,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F1115)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Nome do Aluno
                Text(
                    text = currentUser.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = CaliTextPrimary
                )

                // Grau / Nível
                Text(
                    text = currentUser.studentLevel,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CaliNeonLime
                )

                Text(
                    text = currentUser.email,
                    fontSize = 11.sp,
                    color = CaliTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Badge de Posição no Ranking Geral
                val myRank = leaderboard.find { it.isCurrentUser }?.rank ?: 1
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CaliSurfaceElevated)
                        .border(1.dp, CaliNeonLime, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Posição #$myRank • ${currentUser.rankingPoints} Pontos",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Meta do Aluno
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CaliSurfaceElevated)
                        .border(1.dp, CaliBorder, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = CaliElectricCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Meta: ${currentUser.targetGoal}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = CaliTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Métricas Rápidas (Treinos, Minutos, Ofensiva, Dominados)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${currentUser.totalWorkoutsCompleted}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliTextPrimary
                        )
                        Text(text = "Missões", fontSize = 11.sp, color = CaliTextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${currentUser.totalMinutesTrained}m",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliNeonLime
                        )
                        Text(text = "Minutos", fontSize = 11.sp, color = CaliTextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${currentUser.currentStreakDays}d 🔥",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFF97316)
                        )
                        Text(text = "Ofensiva", fontSize = 11.sp, color = CaliTextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${currentUser.masteredExerciseIds.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliElectricCyan
                        )
                        Text(text = "Dominados", fontSize = 11.sp, color = CaliTextSecondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ================= GRÁFICO COM O PROGRESSO DO ALUNO =================
        StudentProgressChart()

        Spacer(modifier = Modifier.height(20.dp))

        // ================= 🏆 RANKING GERAL DE ALUNOS (LEADERBOARD) =================
        Card(
            colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CaliBorder, RoundedCornerShape(18.dp))
                .testTag("student_leaderboard_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RANKING GERAL DE ALUNOS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = CaliTextPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CaliSurfaceElevated)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${leaderboard.size} Alunos",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliElectricCyan
                        )
                    }
                }

                Text(
                    text = "As missões dão pontos consoante a duração, dificuldade e ofensiva. Quanto mais treinas, mais sobes no ranking!",
                    fontSize = 11.sp,
                    color = CaliTextSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp),
                    lineHeight = 15.sp
                )

                // Podium dos 3 Primeiros Alunos
                val top3 = leaderboard.take(3)
                if (top3.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // 2º Lugar
                        if (top3.size >= 2) {
                            PodiumColumn(
                                rank = top3[1],
                                medal = "🥈",
                                medalColor = Color(0xFFE2E8F0),
                                height = 120.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // 1º Lugar (Centro, mais alto)
                        if (top3.isNotEmpty()) {
                            PodiumColumn(
                                rank = top3[0],
                                medal = "🥇",
                                medalColor = Color(0xFFFBBF24),
                                height = 145.dp,
                                modifier = Modifier.weight(1.1f)
                            )
                        }

                        // 3º Lugar
                        if (top3.size >= 3) {
                            PodiumColumn(
                                rank = top3[2],
                                medal = "🥉",
                                medalColor = Color(0xFFCD7F32),
                                height = 105.dp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Lista Completa de Posições
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    leaderboard.forEach { studentRank ->
                        val isMe = studentRank.isCurrentUser
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isMe) CaliNeonLime.copy(alpha = 0.15f) else CaliSurfaceDark)
                                .border(
                                    width = if (isMe) 1.5.dp else 1.dp,
                                    color = if (isMe) CaliNeonLime else CaliBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Posição
                                Text(
                                    text = "#${studentRank.rank}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = when (studentRank.rank) {
                                        1 -> Color(0xFFFBBF24)
                                        2 -> Color(0xFFE2E8F0)
                                        3 -> Color(0xFFCD7F32)
                                        else -> CaliTextMuted
                                    },
                                    modifier = Modifier.width(28.dp)
                                )

                                // Avatar
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isMe) {
                                                Brush.linearGradient(listOf(CaliNeonLime, CaliElectricCyan))
                                            } else {
                                                Brush.linearGradient(listOf(CaliSurfaceElevated, CaliSurfaceCard))
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = studentRank.avatarInitial,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isMe) Color(0xFF0F1115) else CaliTextPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = studentRank.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isMe) CaliNeonLime else CaliTextPrimary
                                        )
                                        if (isMe) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(CaliNeonLime)
                                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = "TU",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color(0xFF0F1115)
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = "${studentRank.studentLevel} • ${studentRank.workoutsCompleted} missões",
                                        fontSize = 10.sp,
                                        color = CaliTextSecondary
                                    )
                                }
                            }

                            // Pontos
                            Text(
                                text = "${studentRank.points} pts",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isMe) CaliNeonLime else CaliElectricCyan
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Explicação dos Pontos de Missão
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CaliSurfaceElevated)
                        .border(1.dp, CaliBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CaliElectricCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SISTEMA DE PONTOS DE MISSÃO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = CaliElectricCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Treino 5 min: 100 pts  |  15 min: 250 pts  |  30 min: 450 pts  |  1h: 850 pts\n" +
                                "• Multiplicador de Nível: Iniciante (1.0x) • Intermédio (1.3x) • Avançado (1.6x)\n" +
                                "• Ofensiva diária: bónus adicional por cada dia consecutivo no parque!",
                            fontSize = 10.sp,
                            color = CaliTextSecondary,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ================= CONTAS E TROCA DE ALUNO =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TROCAR DE ALUNO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = CaliTextMuted,
                letterSpacing = 1.sp
            )

            Button(
                onClick = { showLoginForm = !showLoginForm },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (showLoginForm) CaliSurfaceElevated else CaliSurfaceCard
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier = Modifier
                    .height(34.dp)
                    .border(
                        width = 1.dp,
                        color = if (showLoginForm) CaliElectricCyan else CaliNeonLime,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .testTag("toggle_login_form_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = if (showLoginForm) CaliElectricCyan else CaliNeonLime,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (showLoginForm) "Fechar" else "+ Novo Aluno",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (showLoginForm) CaliElectricCyan else CaliNeonLime
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Formulário de Criação de Aluno
        AnimatedVisibility(visible = showLoginForm) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CaliSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CRIAR / ENTRAR COM OUTRA CONTA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliNeonLime,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = loginName,
                        onValueChange = { loginName = it },
                        placeholder = { Text("Nome do aluno", color = CaliTextMuted) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = CaliNeonLime)
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
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = loginEmail,
                        onValueChange = { loginEmail = it },
                        placeholder = { Text("email@exemplo.pt", color = CaliTextMuted) },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = CaliNeonLime)
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
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (loginName.isNotBlank() && loginEmail.isNotBlank()) {
                                onLoginOrCreate(loginName, loginEmail, loginLevel, loginGoal)
                                showLoginForm = false
                                loginName = ""
                                loginEmail = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CaliNeonLime),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(
                            text = "REGISTAR E ENTRAR",
                            color = Color(0xFF0F1115),
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Lista de Alunos Registados para Troca
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            allUsers.forEach { user ->
                val isSelected = user.id == currentUser.id
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) CaliSurfaceElevated else CaliSurfaceCard
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (isSelected) CaliNeonLime else CaliBorder,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onSwitchUser(user) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) {
                                            Brush.linearGradient(listOf(CaliNeonLime, CaliElectricCyan))
                                        } else {
                                            Brush.linearGradient(listOf(CaliSurfaceCard, CaliSurfaceElevated))
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.avatarInitial,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = if (isSelected) Color(0xFF0F1115) else CaliTextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = user.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CaliTextPrimary
                                )
                                Text(
                                    text = "${user.studentLevel} • ${user.rankingPoints} pts",
                                    fontSize = 11.sp,
                                    color = if (isSelected) CaliNeonLime else CaliTextSecondary
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Conta Ativa",
                                tint = CaliNeonLime,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = "Trocar",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CaliElectricCyan
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de Logout no Fim da Página
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.15f)),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .testTag("profile_logout_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "TERMINAR SESSÃO",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFCA5A5)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun PodiumColumn(
    rank: StudentRank,
    medal: String,
    medalColor: Color,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Medalha e Avatar
        Text(text = medal, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (rank.isCurrentUser) {
                        Brush.linearGradient(listOf(CaliNeonLime, CaliElectricCyan))
                    } else {
                        Brush.linearGradient(listOf(CaliSurfaceElevated, CaliSurfaceCard))
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.avatarInitial,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                color = if (rank.isCurrentUser) Color(0xFF0F1115) else CaliTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = rank.name.split(" ").firstOrNull() ?: rank.name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (rank.isCurrentUser) CaliNeonLime else CaliTextPrimary,
            maxLines = 1
        )
        Text(
            text = "${rank.points}p",
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = CaliElectricCyan
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Bloco do Pódio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(
                    if (rank.isCurrentUser) CaliNeonLime.copy(alpha = 0.25f) else CaliSurfaceElevated
                )
                .border(
                    width = 1.dp,
                    color = if (rank.isCurrentUser) CaliNeonLime else medalColor.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#${rank.rank}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = medalColor
            )
        }
    }
}
