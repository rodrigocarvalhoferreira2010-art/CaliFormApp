package com.example.caliform.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun HamburgerTopBar(
    currentUser: UserProfile,
    isExpanded: Boolean,
    onToggleMenu: () -> Unit,
    currentScreen: String,
    onNavigate: (String) -> Unit,
    onQuickStartWorkout: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "hamburger_rotation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CaliSurfaceDark)
    ) {
        // ================= BARRA SUPERIOR PRINCIPAL =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Menu "Hambúrguer": 3 Linhas Horizontais Melhoradas Graficamente com Efeito
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isExpanded) CaliSurfaceElevated else CaliSurfaceCard)
                    .border(
                        width = 1.5.dp,
                        color = if (isExpanded) CaliNeonLime else CaliBorder,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(onClick = onToggleMenu)
                    .testTag("hamburger_menu_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.rotate(rotation),
                    verticalArrangement = Arrangement.spacedBy(4.5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(2.5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isExpanded) CaliNeonLime else CaliTextPrimary)
                    )
                    Box(
                        modifier = Modifier
                            .width(if (isExpanded) 15.dp else 22.dp)
                            .height(2.5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isExpanded) CaliElectricCyan else CaliTextPrimary)
                    )
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(2.5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isExpanded) CaliNeonLime else CaliTextPrimary)
                    )
                }
            }

            // Título CaliForm & Distintivo do Treinador
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onNavigate("workouts") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "CALI",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliTextPrimary,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "FORM",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliNeonLime,
                        letterSpacing = 1.5.sp
                    )
                }
                Text(
                    text = "TREINADOR DE ALUNOS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliElectricCyan,
                    letterSpacing = 2.sp
                )
            }

            // Avatar do Aluno e Sequência de Treinos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CaliSurfaceCard)
                    .border(1.dp, CaliBorder, RoundedCornerShape(18.dp))
                    .clickable { onNavigate("profile") }
                    .padding(horizontal = 10.dp)
                    .testTag("topbar_user_profile_button")
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(CaliNeonLime, CaliElectricCyan)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentUser.avatarInitial,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color(0xFF0F1115)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${currentUser.currentStreakDays}d 🔥",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliTextPrimary
                )
            }
        }

        // ================= PAINEL EXPANSÍVEL PARA BAIXO COM ANIMAÇÃO DESLIZANTE =================
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioLowBouncy)
            ) + slideInVertically(
                initialOffsetY = { -it / 3 },
                animationSpec = tween(280)
            ) + fadeIn(animationSpec = tween(220)),
            exit = shrinkVertically(animationSpec = tween(220)) +
                slideOutVertically(targetOffsetY = { -it / 3 }, animationSpec = tween(200)) +
                fadeOut(animationSpec = tween(180))
        ) {
            Surface(
                color = CaliSurfaceDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("downward_expanded_menu")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Cartão do Perfil Ativo com Pontos de Ranking
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
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
                                            fontWeight = FontWeight.Black,
                                            fontSize = 18.sp,
                                            color = Color(0xFF0F1115)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = currentUser.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = CaliTextPrimary
                                        )
                                        Text(
                                            text = currentUser.studentLevel,
                                            fontSize = 11.sp,
                                            color = CaliNeonLime
                                        )
                                        Text(
                                            text = currentUser.email,
                                            fontSize = 10.sp,
                                            color = CaliTextMuted
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CaliSurfaceElevated)
                                        .border(1.dp, CaliBorder, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "🏆 ${currentUser.rankingPoints} pts",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CaliElectricCyan
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Linha de Estatísticas Rápidas
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                StatMiniItem(
                                    label = "Treinos",
                                    value = "${currentUser.totalWorkoutsCompleted}"
                                )
                                StatMiniItem(
                                    label = "Minutos",
                                    value = "${currentUser.totalMinutesTrained}m"
                                )
                                StatMiniItem(
                                    label = "Ofensiva",
                                    value = "${currentUser.currentStreakDays}d 🔥"
                                )
                                StatMiniItem(
                                    label = "Dominados",
                                    value = "${currentUser.masteredExerciseIds.size}"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "NAVEGAÇÃO RÁPIDA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliTextMuted,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExpandedMenuItem(
                            icon = Icons.Default.FitnessCenter,
                            title = "Treinos",
                            subtitle = "Iniciante a Avançado",
                            isSelected = currentScreen == "workouts",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onToggleMenu()
                                onNavigate("workouts")
                            }
                        )
                        ExpandedMenuItem(
                            icon = Icons.Default.SportsGymnastics,
                            title = "Exercícios",
                            subtitle = "Guia de Forma",
                            isSelected = currentScreen == "exercises",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onToggleMenu()
                                onNavigate("exercises")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExpandedMenuItem(
                            icon = Icons.Default.AutoAwesome,
                            title = "IA Coach",
                            subtitle = "Chat & Memória",
                            isSelected = currentScreen == "ai_coach",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onToggleMenu()
                                onNavigate("ai_coach")
                            }
                        )
                        ExpandedMenuItem(
                            icon = Icons.Default.Person,
                            title = "Perfil & Ranking",
                            subtitle = "Classificação Alunos",
                            isSelected = currentScreen == "profile",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onToggleMenu()
                                onNavigate("profile")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "LANÇAMENTO RÁPIDO NO PARQUE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = CaliTextMuted,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickLaunchPill(
                            label = "⚡ 5m Iniciante",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onToggleMenu()
                                onQuickStartWorkout("iniciante_5min")
                            }
                        )
                        QuickLaunchPill(
                            label = "⏱️ 15m Intermédio",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onToggleMenu()
                                onQuickStartWorkout("intermedio_15min")
                            }
                        )
                        QuickLaunchPill(
                            label = "🔥 1h Avançado",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onToggleMenu()
                                onQuickStartWorkout("avancado_1h")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // ================= BOTÃO DEDICADO DE LOGOUT =================
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable {
                                onToggleMenu()
                                onLogout()
                            }
                            .testTag("logout_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Sair da Conta",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Terminar Sessão (Sair da Conta)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFFCA5A5)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Botão de fechar / recolher o menu
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = onToggleMenu)
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Recolher menu",
                            tint = CaliTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Recolher Menu",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = CaliTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMiniItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            color = CaliTextPrimary
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = CaliTextSecondary
        )
    }
}

@Composable
private fun ExpandedMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CaliSurfaceElevated else CaliSurfaceCard
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isSelected) CaliNeonLime else CaliBorder,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) CaliNeonLime.copy(alpha = 0.2f) else CaliSurfaceDark),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) CaliNeonLime else CaliTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (isSelected) CaliNeonLime else CaliTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = CaliTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun QuickLaunchPill(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CaliSurfaceCard)
            .border(1.dp, CaliBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CaliTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
