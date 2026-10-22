package com.example.caliform.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caliform.data.CalisthenicsData
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
fun AuthScreen(
    onLoginSuccess: (email: String, pass: String) -> Unit,
    onRegisterSuccess: (name: String, email: String, pass: String, level: String, goal: String) -> Unit,
    onOAuthLogin: (provider: String, demoUser: UserProfile?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRegisterMode by remember { mutableStateOf(false) }

    // Campos de Login / Registo
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf("Nível 1 • Aluno Iniciante") }
    var targetGoal by remember { mutableStateOf("Dominar 10 flexões e remadas na barra 1.75m") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CaliSurfaceDark)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Logótipo e Identidade CaliForm
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(CaliNeonLime, CaliElectricCyan)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Color(0xFF0F1115),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "CALI",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = CaliTextPrimary,
                letterSpacing = 2.sp
            )
            Text(
                text = "FORM",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = CaliNeonLime,
                letterSpacing = 2.sp
            )
        }

        Text(
            text = "TREINADOR DE CALISTENIA & PARQUE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = CaliElectricCyan,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isRegisterMode) {
                "Cria a tua conta de aluno para entrar no ranking e salvar o teu progresso!"
            } else {
                "Inicia sessão para aceder às missões de treino, IA e pontuações do parque."
            },
            fontSize = 13.sp,
            color = CaliTextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp),
            lineHeight = 18.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Seletor de Modo: Iniciar Sessão vs Criar Conta
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(CaliSurfaceCard)
                .border(1.dp, CaliBorder, RoundedCornerShape(14.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (!isRegisterMode) CaliNeonLime else Color.Transparent)
                    .clickable {
                        isRegisterMode = false
                        errorMessage = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Iniciar Sessão",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (!isRegisterMode) Color(0xFF0F1115) else CaliTextSecondary
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isRegisterMode) CaliNeonLime else Color.Transparent)
                    .clickable {
                        isRegisterMode = true
                        errorMessage = null
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Criar Conta",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isRegisterMode) Color(0xFF0F1115) else CaliTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mensagem de Erro (se houver)
        AnimatedVisibility(visible = errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEF4444).copy(alpha = 0.15f))
                    .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = errorMessage ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFCA5A5)
                )
            }
        }

        // ================= FORMULÁRIO =================
        Card(
            colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CaliBorder, RoundedCornerShape(18.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                if (isRegisterMode) {
                    // Nome Completo
                    Text(
                        text = "Nome do Aluno",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Ex: Alexandre Silva", color = CaliTextMuted) },
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
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_input_name")
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Email
                Text(
                    text = "Email do Aluno",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("aluno@caliform.pt", color = CaliTextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = CaliNeonLime)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                        .fillMaxWidth()
                        .testTag("auth_input_email")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Palavra-passe
                Text(
                    text = "Palavra-passe",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••", color = CaliTextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = CaliNeonLime)
                    },
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isPasswordVisible) "Ocultar palavra-passe" else "Mostrar palavra-passe",
                                tint = CaliTextSecondary
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                        .fillMaxWidth()
                        .testTag("auth_input_password")
                )

                if (isRegisterMode) {
                    Spacer(modifier = Modifier.height(14.dp))

                    // Nível do Aluno
                    Text(
                        text = "Grau de Experiência Inicial",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LevelSelectionChip(
                            label = "Iniciante",
                            isSelected = selectedLevel.contains("Iniciante"),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedLevel = "Nível 1 • Aluno Iniciante" }
                        )
                        LevelSelectionChip(
                            label = "Intermédio",
                            isSelected = selectedLevel.contains("Intermédio"),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedLevel = "Nível 2 • Praticante Intermédio" }
                        )
                        LevelSelectionChip(
                            label = "Avançado",
                            isSelected = selectedLevel.contains("Avançado"),
                            modifier = Modifier.weight(1f),
                            onClick = { selectedLevel = "Nível 3 • Atleta Avançado" }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Meta / Objetivo
                    Text(
                        text = "Meta no Parque de Calistenia",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CaliTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = targetGoal,
                        onValueChange = { targetGoal = it },
                        leadingIcon = {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = CaliElectricCyan)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CaliElectricCyan,
                            unfocusedBorderColor = CaliBorder,
                            focusedTextColor = CaliTextPrimary,
                            unfocusedTextColor = CaliTextPrimary,
                            focusedContainerColor = CaliSurfaceDark,
                            unfocusedContainerColor = CaliSurfaceDark
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎁 Bónus de Registo:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CaliNeonLime)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "+150 pts diretos no ranking!", fontSize = 11.sp, color = CaliTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Botão de Ação Primário
                Button(
                    onClick = {
                        if (isRegisterMode) {
                            if (name.isBlank() || email.isBlank()) {
                                errorMessage = "Por favor preenche o teu nome e email."
                            } else {
                                onRegisterSuccess(name, email, password, selectedLevel, targetGoal)
                            }
                        } else {
                            if (email.isBlank()) {
                                errorMessage = "Por favor introduz o teu email para entrar."
                            } else {
                                onLoginSuccess(email, password)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CaliNeonLime),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag(if (isRegisterMode) "auth_register_button" else "auth_login_button")
                ) {
                    Text(
                        text = if (isRegisterMode) "CRIAR CONTA E COMEÇAR" else "ENTRAR NA CONTA",
                        color = Color(0xFF0F1115),
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Separador "OU ENTRA COM"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(CaliBorder)
            )
            Text(
                text = "OU CONTINUA COM",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = CaliTextMuted,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(CaliBorder)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Botões Sociais Google & Microsoft
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SocialAuthButton(
                iconText = "G",
                label = "Google",
                modifier = Modifier.weight(1f),
                badgeColor = Color(0xFF4285F4),
                onClick = { onOAuthLogin("Google", null) },
                testTag = "auth_google_button"
            )

            SocialAuthButton(
                iconText = "MS",
                label = "Microsoft",
                modifier = Modifier.weight(1f),
                badgeColor = Color(0xFF00A4EF),
                onClick = { onOAuthLogin("Microsoft", null) },
                testTag = "auth_microsoft_button"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Acesso Rápido de Demonstração (Alunos Existentes do Parque)
        Card(
            colors = CardDefaults.cardColors(containerColor = CaliSurfaceElevated),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CaliBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ACESSO RÁPIDO PARA TESTES (ALUNOS EXEMPLO)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = CaliElectricCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                CalisthenicsData.sampleUsers.take(3).forEach { sampleUser ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CaliSurfaceCard)
                            .border(1.dp, CaliBorder, RoundedCornerShape(10.dp))
                            .clickable { onOAuthLogin("Demo", sampleUser) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                Text(
                                    text = sampleUser.avatarInitial,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F1115)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = sampleUser.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CaliTextPrimary
                                )
                                Text(
                                    text = sampleUser.studentLevel,
                                    fontSize = 11.sp,
                                    color = CaliTextSecondary
                                )
                            }
                        }

                        Text(
                            text = "Entrar →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CaliNeonLime
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelSelectionChip(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) CaliNeonLime else CaliSurfaceDark)
            .border(
                1.dp,
                if (isSelected) CaliNeonLime else CaliBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color(0xFF0F1115) else CaliTextPrimary
        )
    }
}

@Composable
private fun SocialAuthButton(
    iconText: String,
    label: String,
    modifier: Modifier = Modifier,
    badgeColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CaliSurfaceCard)
            .border(1.dp, CaliBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CaliTextPrimary
            )
        }
    }
}
