package com.example.caliform.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CaliElectricCyan
import com.example.ui.theme.CaliNeonLime

@Composable
fun ExerciseVisualizer(
    visualType: String,
    modifier: Modifier = Modifier.height(180.dp),
    isAnimating: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_motion")
    val motionProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "motion_progress"
    )

    val progress = if (isAnimating) motionProgress else 0.5f

    Box(
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF222B38), Color(0xFF13171F))
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background subtle grid lines
            val gridColor = Color(0xFF2A3444).copy(alpha = 0.4f)
            for (i in 1..4) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, h * i / 5),
                    end = Offset(w, h * i / 5),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val primaryColor = CaliNeonLime
            val secondaryColor = CaliElectricCyan
            val bodyColor = Color(0xFFE2E8F0)
            val strokeThick = 4.dp.toPx()

            when (visualType) {
                "pushup" -> {
                    // Floor line
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(w * 0.15f, h * 0.75f),
                        end = Offset(w * 0.85f, h * 0.75f),
                        strokeWidth = 3.dp.toPx()
                    )

                    // Push-up animation: height shifts with progress
                    // Progress 0 = top locked out, Progress 1 = chest to floor
                    val chestY = h * (0.45f + progress * 0.22f)
                    val headX = w * 0.68f
                    val feetX = w * 0.25f
                    val feetY = h * 0.73f

                    // Head
                    drawCircle(
                        color = bodyColor,
                        radius = 12.dp.toPx(),
                        center = Offset(headX + 16.dp.toPx(), chestY - 10.dp.toPx())
                    )

                    // Torso & Legs line
                    drawLine(
                        color = bodyColor,
                        start = Offset(headX, chestY),
                        end = Offset(feetX, feetY),
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )

                    // Arms (Hands at floor)
                    val handX = w * 0.60f
                    val handY = h * 0.74f
                    val elbowX = w * (0.54f + progress * 0.05f)
                    val elbowY = chestY + 12.dp.toPx()

                    drawLine(
                        color = primaryColor,
                        start = Offset(headX - 10.dp.toPx(), chestY),
                        end = Offset(elbowX, elbowY),
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = primaryColor,
                        start = Offset(elbowX, elbowY),
                        end = Offset(handX, handY),
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )

                    // Target muscle highlight glows
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.5f),
                        radius = 16.dp.toPx(),
                        center = Offset(headX - 12.dp.toPx(), chestY + 4.dp.toPx())
                    )
                }

                "pullup" -> {
                    // Overhead Bar
                    val barY = h * 0.20f
                    drawLine(
                        color = Color(0xFF94A3B8),
                        start = Offset(w * 0.20f, barY),
                        end = Offset(w * 0.80f, barY),
                        strokeWidth = 5.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Pull up animation: Progress 0 = deadhang, Progress 1 = chin over bar
                    val bodyYOffset = (1f - progress) * (h * 0.28f)
                    val headCenter = Offset(w * 0.50f, barY + 20.dp.toPx() + bodyYOffset)
                    val chestY = headCenter.y + 24.dp.toPx()
                    val hipsY = chestY + 45.dp.toPx()
                    val feetY = hipsY + 50.dp.toPx()

                    // Head
                    drawCircle(
                        color = bodyColor,
                        radius = 12.dp.toPx(),
                        center = headCenter
                    )

                    // Torso
                    drawLine(
                        color = bodyColor,
                        start = Offset(w * 0.50f, headCenter.y + 12.dp.toPx()),
                        end = Offset(w * 0.50f, hipsY),
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )

                    // Legs (slight forward hollow)
                    drawLine(
                        color = bodyColor,
                        start = Offset(w * 0.50f, hipsY),
                        end = Offset(w * 0.54f, feetY),
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )

                    // Arms to bar
                    val handLeft = Offset(w * 0.40f, barY)
                    val handRight = Offset(w * 0.60f, barY)
                    drawLine(
                        color = primaryColor,
                        start = Offset(w * 0.48f, chestY - 10.dp.toPx()),
                        end = handLeft,
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = primaryColor,
                        start = Offset(w * 0.52f, chestY - 10.dp.toPx()),
                        end = handRight,
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )

                    // Back/Lat muscle highlight
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.45f),
                        radius = 20.dp.toPx(),
                        center = Offset(w * 0.50f, chestY)
                    )
                }

                "dip" -> {
                    // Parallel bars
                    val barY = h * 0.45f
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(w * 0.30f, barY),
                        end = Offset(w * 0.70f, barY),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Dip motion: Progress 0 = lockout, Progress 1 = 90 degree bottom
                    val dipOffset = progress * (h * 0.22f)
                    val headCenter = Offset(w * 0.50f, h * 0.22f + dipOffset)
                    val shoulderY = headCenter.y + 16.dp.toPx()
                    val hipsY = shoulderY + 40.dp.toPx()
                    val kneesY = hipsY + 30.dp.toPx()

                    // Head & Body
                    drawCircle(color = bodyColor, radius = 11.dp.toPx(), center = headCenter)
                    drawLine(
                        color = bodyColor,
                        start = Offset(w * 0.50f, shoulderY),
                        end = Offset(w * 0.48f, hipsY),
                        strokeWidth = strokeThick
                    )
                    // Legs bent at knees
                    drawLine(
                        color = bodyColor,
                        start = Offset(w * 0.48f, hipsY),
                        end = Offset(w * 0.44f, kneesY),
                        strokeWidth = strokeThick
                    )
                    drawLine(
                        color = bodyColor,
                        start = Offset(w * 0.44f, kneesY),
                        end = Offset(w * 0.50f, kneesY + 15.dp.toPx()),
                        strokeWidth = strokeThick
                    )

                    // Arms on bar
                    drawLine(
                        color = primaryColor,
                        start = Offset(w * 0.47f, shoulderY),
                        end = Offset(w * 0.42f, barY),
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = primaryColor,
                        start = Offset(w * 0.53f, shoulderY),
                        end = Offset(w * 0.58f, barY),
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )
                }

                "squat" -> {
                    // Ground
                    val floorY = h * 0.85f
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(w * 0.2f, floorY),
                        end = Offset(w * 0.8f, floorY),
                        strokeWidth = 3.dp.toPx()
                    )

                    // Progress 0 = standing, Progress 1 = deep squat
                    val squatOffset = progress * (h * 0.28f)
                    val headCenter = Offset(w * 0.48f, h * 0.22f + squatOffset)
                    val hipCenter = Offset(w * (0.42f - progress * 0.08f), h * 0.45f + squatOffset)
                    val kneeCenter = Offset(w * (0.55f + progress * 0.04f), h * 0.65f + squatOffset * 0.4f)
                    val footCenter = Offset(w * 0.52f, floorY - 2.dp.toPx())

                    // Head
                    drawCircle(color = bodyColor, radius = 12.dp.toPx(), center = headCenter)
                    // Torso
                    drawLine(color = bodyColor, start = headCenter, end = hipCenter, strokeWidth = strokeThick)
                    // Thigh
                    drawLine(color = secondaryColor, start = hipCenter, end = kneeCenter, strokeWidth = strokeThick)
                    // Shin
                    drawLine(color = bodyColor, start = kneeCenter, end = footCenter, strokeWidth = strokeThick)

                    // Quad highlight
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.5f),
                        radius = 16.dp.toPx(),
                        center = Offset((hipCenter.x + kneeCenter.x) / 2, (hipCenter.y + kneeCenter.y) / 2)
                    )
                }

                "handstand" -> {
                    // Floor
                    val floorY = h * 0.82f
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(w * 0.2f, floorY),
                        end = Offset(w * 0.8f, floorY),
                        strokeWidth = 3.dp.toPx()
                    )

                    // Inverted body
                    val handsCenter = Offset(w * 0.50f, floorY - 4.dp.toPx())
                    val shouldersCenter = Offset(w * 0.50f, floorY - 45.dp.toPx())
                    val headCenter = Offset(w * 0.50f, floorY - 32.dp.toPx())
                    val hipsCenter = Offset(w * (0.50f + (progress - 0.5f) * 0.04f), h * 0.38f)
                    val feetCenter = Offset(w * (0.50f + (progress - 0.5f) * 0.06f), h * 0.16f)

                    // Head (hanging inverted)
                    drawCircle(color = bodyColor, radius = 10.dp.toPx(), center = headCenter)

                    // Arms (straight vertical pillars)
                    drawLine(
                        color = primaryColor,
                        start = handsCenter,
                        end = shouldersCenter,
                        strokeWidth = strokeThick + 1.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Torso
                    drawLine(color = bodyColor, start = shouldersCenter, end = hipsCenter, strokeWidth = strokeThick)
                    // Legs straight up
                    drawLine(color = bodyColor, start = hipsCenter, end = feetCenter, strokeWidth = strokeThick)

                    // Shoulder elevation glow
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.5f),
                        radius = 18.dp.toPx(),
                        center = shouldersCenter
                    )
                }

                "lsit" -> {
                    // Floor / Parallette
                    val floorY = h * 0.65f
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(w * 0.3f, floorY),
                        end = Offset(w * 0.55f, floorY),
                        strokeWidth = 3.dp.toPx()
                    )

                    val hands = Offset(w * 0.42f, floorY - 2.dp.toPx())
                    val shoulders = Offset(w * 0.42f, floorY - 42.dp.toPx())
                    val head = Offset(w * 0.42f, shoulders.y - 14.dp.toPx())
                    val hips = Offset(w * 0.45f, floorY - 12.dp.toPx())
                    val feet = Offset(w * 0.78f, floorY - 12.dp.toPx())

                    // Head
                    drawCircle(color = bodyColor, radius = 10.dp.toPx(), center = head)
                    // Arms pushing down
                    drawLine(color = primaryColor, start = hands, end = shoulders, strokeWidth = strokeThick)
                    // Torso
                    drawLine(color = bodyColor, start = shoulders, end = hips, strokeWidth = strokeThick)
                    // Legs straight out in sharp 'L'
                    drawLine(
                        color = secondaryColor,
                        start = hips,
                        end = feet,
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )

                    // Abdominal compression glow
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.5f),
                        radius = 16.dp.toPx(),
                        center = hips
                    )
                }

                else -> {
                    // Default Plank / Core visual
                    val floorY = h * 0.75f
                    drawLine(
                        color = Color(0xFF64748B),
                        start = Offset(w * 0.15f, floorY),
                        end = Offset(w * 0.85f, floorY),
                        strokeWidth = 3.dp.toPx()
                    )

                    val elbowCenter = Offset(w * 0.65f, floorY - 2.dp.toPx())
                    val shoulderCenter = Offset(w * 0.65f, floorY - 32.dp.toPx())
                    val headCenter = Offset(w * 0.74f, shoulderCenter.y - 4.dp.toPx())
                    val feetCenter = Offset(w * 0.25f, floorY - 4.dp.toPx())

                    drawCircle(color = bodyColor, radius = 11.dp.toPx(), center = headCenter)
                    drawLine(
                        color = primaryColor,
                        start = elbowCenter,
                        end = shoulderCenter,
                        strokeWidth = strokeThick
                    )
                    drawLine(
                        color = bodyColor,
                        start = shoulderCenter,
                        end = feetCenter,
                        strokeWidth = strokeThick,
                        cap = StrokeCap.Round
                    )

                    // Core glow
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.45f),
                        radius = 20.dp.toPx(),
                        center = Offset(w * 0.45f, shoulderCenter.y + 12.dp.toPx())
                    )
                }
            }
        }
    }
}
