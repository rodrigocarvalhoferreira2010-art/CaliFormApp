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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caliform.model.BodyArea
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.Equipment
import com.example.caliform.model.Exercise
import com.example.caliform.ui.components.BodyAreaChipsRow
import com.example.caliform.ui.components.DifficultyChipsRow
import com.example.caliform.ui.components.EquipmentChipsRow
import com.example.ui.theme.CaliBorder
import com.example.ui.theme.CaliElectricCyan
import com.example.ui.theme.CaliNeonLime
import com.example.ui.theme.CaliSurfaceCard
import com.example.ui.theme.CaliSurfaceDark
import com.example.ui.theme.CaliTextMuted
import com.example.ui.theme.CaliTextPrimary
import com.example.ui.theme.CaliTextSecondary

@Composable
fun ExercisesScreen(
    exercises: List<Exercise>,
    masteredExerciseIds: Set<String>,
    selectedArea: BodyArea?,
    onSelectArea: (BodyArea?) -> Unit,
    selectedDifficulty: DifficultyLevel?,
    onSelectDifficulty: (DifficultyLevel?) -> Unit,
    selectedEquipment: Equipment?,
    onSelectEquipment: (Equipment?) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onExerciseClick: (Exercise) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CaliSurfaceDark)
    ) {
        // Barra de Pesquisa
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = "Pesquisar exercício, músculo ou barra...",
                    fontSize = 13.sp,
                    color = CaliTextMuted
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Pesquisar",
                    tint = CaliNeonLime
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpar pesquisa",
                            tint = CaliTextSecondary
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CaliSurfaceCard,
                unfocusedContainerColor = CaliSurfaceCard,
                focusedBorderColor = CaliNeonLime,
                unfocusedBorderColor = CaliBorder,
                focusedTextColor = CaliTextPrimary,
                unfocusedTextColor = CaliTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("exercise_search_input")
        )

        // Filtro por Área Corporal
        BodyAreaChipsRow(
            selectedArea = selectedArea,
            onSelectArea = onSelectArea
        )

        // Filtro por Nível
        DifficultyChipsRow(
            selectedDifficulty = selectedDifficulty,
            onSelectDifficulty = onSelectDifficulty
        )

        // Filtro por Equipamento
        EquipmentChipsRow(
            selectedEquipment = selectedEquipment,
            onSelectEquipment = onSelectEquipment
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Contagem e Reset
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${exercises.size} EXERCÍCIOS DISPONÍVEIS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = CaliTextMuted,
                letterSpacing = 1.sp
            )

            if (selectedArea != null || selectedDifficulty != null || selectedEquipment != null || searchQuery.isNotBlank()) {
                Text(
                    text = "Limpar Filtros",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliElectricCyan,
                    modifier = Modifier.clickable {
                        onSelectArea(null)
                        onSelectDifficulty(null)
                        onSelectEquipment(null)
                        onSearchQueryChange("")
                    }
                )
            }
        }

        if (exercises.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Nenhum exercício encontrado",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CaliTextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Experimenta alterar o equipamento selecionado ou a pesquisa.",
                        fontSize = 12.sp,
                        color = CaliTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("exercises_list")
            ) {
                items(exercises, key = { it.id }) { exercise ->
                    val isMastered = masteredExerciseIds.contains(exercise.id)
                    ExerciseListItemCard(
                        exercise = exercise,
                        isMastered = isMastered,
                        onClick = { onExerciseClick(exercise) }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseListItemCard(
    exercise: Exercise,
    isMastered: Boolean,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CaliSurfaceCard),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isMastered) CaliNeonLime.copy(alpha = 0.5f) else CaliBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .testTag("exercise_card_${exercise.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Badge do Nível
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(exercise.difficulty.colorHex).copy(alpha = 0.2f))
                            .border(1.dp, Color(exercise.difficulty.colorHex), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = exercise.difficulty.displayName.uppercase(),
                            color = Color(exercise.difficulty.colorHex),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Text(
                        text = exercise.bodyArea.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaliTextSecondary
                    )

                    if (isMastered) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Dominado",
                                tint = CaliNeonLime,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Dominado",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CaliNeonLime
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = exercise.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CaliTextPrimary
                )

                Text(
                    text = exercise.subtitle,
                    fontSize = 12.sp,
                    color = CaliTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Equipamento necessário
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Handyman,
                        contentDescription = null,
                        tint = CaliElectricCyan,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = exercise.equipmentNeeded.joinToString(", ") { it.shortName },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = CaliElectricCyan
                    )
                    Text(
                        text = "• 🎯 ${exercise.targetRepsOrHold}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CaliNeonLime,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Ver técnica do exercício",
                tint = CaliTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
