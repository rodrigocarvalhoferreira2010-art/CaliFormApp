package com.example.caliform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caliform.model.BodyArea
import com.example.caliform.model.DifficultyLevel
import com.example.caliform.model.DurationCategory
import com.example.caliform.model.Equipment
import com.example.ui.theme.CaliBorder
import com.example.ui.theme.CaliElectricCyan
import com.example.ui.theme.CaliNeonLime
import com.example.ui.theme.CaliSurfaceCard
import com.example.ui.theme.CaliSurfaceElevated
import com.example.ui.theme.CaliTextSecondary

@Composable
fun BodyAreaChipsRow(
    selectedArea: BodyArea?,
    onSelectArea: (BodyArea?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipPill(
            label = "Todas as Áreas",
            isSelected = selectedArea == null,
            onClick = { onSelectArea(null) }
        )
        BodyArea.values().forEach { area ->
            FilterChipPill(
                label = when (area) {
                    BodyArea.UPPER_BODY -> "Superiores"
                    BodyArea.CORE -> "Core & Abdominais"
                    BodyArea.LOWER_BODY -> "Inferiores"
                    BodyArea.FULL_BODY_SKILLS -> "Habilidades"
                },
                isSelected = selectedArea == area,
                onClick = { onSelectArea(area) }
            )
        }
    }
}

@Composable
fun DifficultyChipsRow(
    selectedDifficulty: DifficultyLevel?,
    onSelectDifficulty: (DifficultyLevel?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipPill(
            label = "Todos os Níveis",
            isSelected = selectedDifficulty == null,
            onClick = { onSelectDifficulty(null) }
        )
        DifficultyLevel.values().forEach { diff ->
            FilterChipPill(
                label = diff.displayName,
                isSelected = selectedDifficulty == diff,
                accentColor = Color(diff.colorHex),
                onClick = { onSelectDifficulty(diff) }
            )
        }
    }
}

@Composable
fun EquipmentChipsRow(
    selectedEquipment: Equipment?,
    onSelectEquipment: (Equipment?) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChipPill(
            label = "Qualquer Equipamento",
            isSelected = selectedEquipment == null,
            onClick = { onSelectEquipment(null) }
        )
        Equipment.values().forEach { eq ->
            FilterChipPill(
                label = eq.shortName,
                isSelected = selectedEquipment == eq,
                accentColor = CaliElectricCyan,
                onClick = { onSelectEquipment(eq) }
            )
        }
    }
}

@Composable
fun FilterChipPill(
    label: String,
    isSelected: Boolean,
    accentColor: Color = CaliNeonLime,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(if (isSelected) CaliSurfaceElevated else CaliSurfaceCard)
            .border(
                width = 1.dp,
                color = if (isSelected) accentColor else CaliBorder,
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
            color = if (isSelected) accentColor else CaliTextSecondary
        )
    }
}
