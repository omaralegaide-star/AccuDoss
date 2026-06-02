package com.example.accudose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.height

// Active screens representation
sealed class AppScreen {
    object Categories : AppScreen()
    object SyrupView : AppScreen()
    object InjectionView : AppScreen()
    object IVFluidsView : AppScreen()
    object EmergencyView : AppScreen()
    object AboutView : AppScreen()
}

// Age approximation chip structure representation
data class AgeChipInfo(
    val id: String,
    val label: String,
    val weightKg: Double,
    val ageMonths: String
)

// Category structures representation
data class MedicalCategory(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val emoji: String,
    val badgeTextAr: String,
    val badgeTextEn: String,
    val colorAccent: Color
)

// Syrup/Suspension item structure representation
data class SyrupMedicine(
    val name: String,
    val baseDosePerKg: Double,
    val frequencySuffix: String,
    val isAgeBased: Boolean = false,
    val ageDoseDetailsAr: List<String> = emptyList(),
    val ageDoseDetailsEn: List<String> = emptyList(),
    val baseDosePerKgMax: Double = 0.0
)

// Injections core structures representation
data class InjectionMedicine(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val formulation: String,
    val baseCcFor10Kg: Double,
    val frequencySuffixAr: String,
    val frequencySuffixEn: String,
    val isSpecial: Boolean = false
)

// IV Fluids models representational structures
data class IvFluidMedicine(
    val id: String,
    val nameEn: String,
    val formulation: String,
    val baseCcPerKg: Double,
    val frequencySuffixEn: String,
    val isSpecial: Boolean = false,
    val specialDoseDetails: List<String> = emptyList(),
    val nameAr: String? = null,
    val frequencySuffixAr: String? = null
)

// Emergency drugs representations
data class EmergencyMedicine(
    val id: String,
    val nameEn: String,
    val formulation: String,
    val isSpecial: Boolean = false
)

// Premium system font representations for KMP
val CairoFontFamily = FontFamily.Default

// Secure Information Integrity and Anti-Tamper Engine
object AppIntegrityEngine {
    private val DATA_DEV_AR = intArrayOf(1593, 1605, 1585, 32, 1570, 1604, 32, 1581, 1605, 1575, 1583, 1610)
    private val DATA_DEV_EN = intArrayOf(79, 109, 97, 114, 32, 65, 108, 45, 65, 104, 109, 109, 97, 100, 121)
    private val DATA_INSTAGRAM = intArrayOf(104, 116, 116, 112, 115, 58, 47, 47, 119, 119, 119, 46, 105, 110, 115, 116, 97, 103, 114, 97, 109, 46, 99, 111, 109, 47, 111, 109, 101, 114, 46, 113, 122, 63, 105, 103, 115, 104, 61, 98, 51, 89, 119, 100, 50, 53, 122, 78, 122, 70, 104, 90, 87, 57, 114)
    private val DATA_TELEGRAM = intArrayOf(104, 116, 116, 112, 115, 58, 47, 47, 116, 46, 109, 101, 47, 79, 109, 52, 95, 49)
    private val DATA_TG_CHANNEL = intArrayOf(104, 116, 116, 112, 115, 58, 47, 47, 116, 46, 109, 101, 47, 43, 90, 75, 85, 109, 50, 84, 81, 105, 56, 107, 73, 52, 89, 106, 69, 121)

    private fun decode(arr: IntArray): String {
        return arr.map { it.toChar() }.joinToString("")
    }

    fun getDeveloperNameAr(): String = decode(DATA_DEV_AR)
    fun getDeveloperNameEn(): String = decode(DATA_DEV_EN)
    fun getInstagramUrl(): String = decode(DATA_INSTAGRAM)
    fun getTelegramUrl(): String = decode(DATA_TELEGRAM)
    fun getTelegramChannelUrl(): String = decode(DATA_TG_CHANNEL)
}

// Age to bodyweight estimator
fun estimateWeightByAgeInMonths(months: Double): Double {
    val m = months.coerceAtLeast(0.0)
    return when {
        m <= 1.5 -> 4.0
        m <= 2.5 -> 5.0
        m <= 3.5 -> 6.0
        m <= 5.5 -> 7.0
        m <= 8.5 -> 8.0
        m <= 11.5 -> 9.0
        m <= 15.0 -> 10.0
        m <= 21.0 -> 12.0
        m <= 30.0 -> 14.0
        m <= 42.0 -> 16.0
        m <= 54.0 -> 18.0
        m <= 66.0 -> 20.0
        m <= 78.0 -> 22.0
        m <= 90.0 -> 24.0
        m <= 102.0 -> 26.0
        m <= 114.0 -> 30.0
        m <= 126.0 -> 32.0
        m <= 138.0 -> 34.0
        else -> 36.0
    }
}

// Arabic normalization logic
fun normalizeArabic(text: String): String {
    var result = text.lowercase()
    val diacritics = listOf("\u064B", "\u064C", "\u064D", "\u064E", "\u064F", "\u0650", "\u0651", "\u0652")
    for (d in diacritics) {
        result = result.replace(d, "")
    }
    result = result.replace("[أإآ]".toRegex(), "ا")
    result = result.replace("ة", "ه")
    result = result.replace("ى", "ي")
    return result.trim().replace("\\s+".toRegex(), " ")
}

// Search text matching logic
fun matchSearchQuery(source: String, query: String): Boolean {
    if (query.isBlank()) return true
    val normalizedSource = normalizeArabic(source)
    val normalizedQuery = normalizeArabic(query)
    val queryWords = normalizedQuery.split(" ").filter { it.isNotBlank() }
    if (queryWords.isEmpty()) return true
    return queryWords.all { normalizedSource.contains(it) }
}

// Swapping eastern Arabic digits to Western counterparts
fun normalizeArabicDigits(input: String): String {
    return input.map { char ->
        when (char) {
            '٠' -> '0'
            '١' -> '1'
            '٢' -> '2'
            '٣' -> '3'
            '٤' -> '4'
            '٥' -> '5'
            '٦' -> '6'
            '٧' -> '7'
            '٨' -> '8'
            '٩' -> '9'
            else -> char
        }
    }.joinToString("")
}

fun formatDouble(value: Double): String {
    if (value == value.toLong().toDouble()) {
        return value.toLong().toString()
    }
    val rounded = ((value * 100.0) + 0.5).toLong() / 100.0
    val str = rounded.toString()
    if (str.endsWith(".0")) {
        return str.substring(0, str.length - 2)
    }
    return str
}


// Tactile key events debouncer
class InputDebouncer(initialValue: String = "") {
    var lastText: String = initialValue
    var lastTime: Long = 0L

    fun sanitize(input: String, allowDecimal: Boolean = false, isNumericOnly: Boolean = false): String {
        val normalizedInput = normalizeArabicDigits(input)
        val now = 100000L // mock multiplier or time provider placeholder
        var filtered = if (isNumericOnly) {
            if (allowDecimal) {
                normalizedInput.filter { it.isDigit() || it == '.' }
            } else {
                normalizedInput.filter { it.isDigit() }
            }
        } else {
            normalizedInput
        }

        if (allowDecimal) {
            val dotCount = filtered.count { it == '.' }
            if (dotCount > 1) {
                val firstDotIdx = filtered.indexOf('.')
                filtered = filtered.filterIndexed { index, c -> c != '.' || index == firstDotIdx }
            }
        }
        return filtered
    }
}

// Formatting clinical feedback inline annotations
fun formatClinicalText(text: String, isDarkBg: Boolean): AnnotatedString {
    val builder = AnnotatedString.Builder()
    val defaultColor = if (isDarkBg) Color.White else Color(0xFF0F172A)
    val highlightColor = Color(0xFFEF4444)
    val colonIndex = text.indexOf(':')
    val prefix: String
    val instruction: String

    if (colonIndex != -1) {
        prefix = text.substring(0, colonIndex + 1)
        instruction = text.substring(colonIndex + 1)
        builder.pushStyle(SpanStyle(color = defaultColor, fontWeight = FontWeight.Bold))
        builder.append(prefix)
        builder.pop()
    } else {
        prefix = ""
        instruction = text
    }

    val firstNumRegex = Regex("""\d+(?:\.\d+)?(?:-\d+(?:\.\d+)?)?""")
    val match = firstNumRegex.find(instruction)

    if (match != null) {
        val start = match.range.first
        val end = match.range.last + 1
        if (start > 0) {
            builder.pushStyle(SpanStyle(color = defaultColor, fontWeight = FontWeight.SemiBold))
            builder.append(instruction.substring(0, start))
            builder.pop()
        }
        builder.pushStyle(SpanStyle(color = highlightColor, fontWeight = FontWeight.Black))
        builder.append(instruction.substring(start, end))
        builder.pop()
        if (end < instruction.length) {
            builder.pushStyle(SpanStyle(color = defaultColor, fontWeight = FontWeight.SemiBold))
            builder.append(instruction.substring(end))
            builder.pop()
        }
    } else {
        builder.pushStyle(SpanStyle(color = defaultColor, fontWeight = FontWeight.SemiBold))
        builder.append(instruction)
        builder.pop()
    }
    return builder.toAnnotatedString()
}

// Instagram Vector symbol drawing
@Composable
fun InstagramLogo(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier.size(24.dp)) {
        val sizePx = size.width
        val strokeWidthPx = sizePx * 0.08f
        drawRoundRect(
            color = color,
            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
            size = androidx.compose.ui.geometry.Size(sizePx - strokeWidthPx, sizePx - strokeWidthPx),
            cornerRadius = CornerRadius(sizePx * 0.28f),
            style = Stroke(width = strokeWidthPx)
        )
        drawCircle(
            color = color,
            radius = sizePx * 0.22f,
            center = center,
            style = Stroke(width = strokeWidthPx)
        )
        drawCircle(
            color = color,
            radius = sizePx * 0.06f,
            center = Offset(sizePx * 0.72f, sizePx * 0.28f)
        )
    }
}

// Telegram Vector symbol drawing
@Composable
fun TelegramLogo(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.88f, h * 0.12f)
            lineTo(w * 0.15f, h * 0.48f)
            lineTo(w * 0.44f, h * 0.58f)
            lineTo(w * 0.54f, h * 0.88f)
            close()
        }
        val creasePath = Path().apply {
            moveTo(w * 0.44f, h * 0.58f)
            lineTo(w * 0.88f, h * 0.12f)
        }
        drawPath(path = path, color = color)
        drawPath(
            path = creasePath,
            color = color.copy(alpha = 0.6f),
            style = Stroke(width = w * 0.05f)
        )
    }
}

// Pulsing and spinning diagnostic badge symbol
@Composable
fun MedicalAnimatedLogo(
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0xFF38BDF8),
    crossColor: Color = Color.White,
    onLogoClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1600
                1.0f at 0
                1.08f at 150
                1.02f at 300
                1.14f at 440
                1.0f at 700
                1.0f at 1600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "heartbeat_scale"
    )

    val dialRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dial_spin"
    )

    val ecgSweepPathPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ecg_swim"
    )

    var clickBounceScale by remember { mutableStateOf(1.0f) }
    var clickRotationDegrees by remember { mutableStateOf(0f) }
    val targetClickBounceScale by animateFloatAsState(
        targetValue = clickBounceScale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow),
        label = "click_bounce"
    )

    Box(
        modifier = modifier
            .scale(pulseScale * targetClickBounceScale)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                clickBounceScale = 0.70f
                clickRotationDegrees += 360f
                onLogoClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(clickBounceScale) {
            if (clickBounceScale < 1.0f) {
                kotlinx.coroutines.delay(120)
                clickBounceScale = 1.0f
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val radius = canvasWidth / 2f
            val center = Offset(canvasWidth / 2f, canvasHeight / 2f)

            rotate(degrees = dialRotation + clickRotationDegrees, pivot = center) {
                drawCircle(
                    color = glowColor.copy(alpha = 0.35f),
                    radius = radius - 4.dp.toPx(),
                    style = Stroke(
                        width = 1.8.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10.dp.toPx(), 10.dp.toPx()), 0f),
                        cap = StrokeCap.Round
                    )
                )
            }

            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = radius - 8.dp.toPx(),
                style = Stroke(width = 1.dp.toPx())
            )

            val crossSize = radius * 0.72f
            val barWidth = crossSize * 0.28f
            drawRoundRect(
                color = crossColor,
                topLeft = Offset(center.x - crossSize / 2f, center.y - barWidth / 2f),
                size = androidx.compose.ui.geometry.Size(crossSize, barWidth),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            drawRoundRect(
                color = crossColor,
                topLeft = Offset(center.x - barWidth / 2f, center.y - crossSize / 2f),
                size = androidx.compose.ui.geometry.Size(barWidth, crossSize),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            val ecgPath = Path().apply {
                val left = center.x - radius * 0.9f
                val right = center.x + radius * 0.9f
                val cy = center.y

                moveTo(left, cy)
                lineTo(center.x - radius * 0.5f, cy)
                lineTo(center.x - radius * 0.4f, cy - radius * 0.32f)
                lineTo(center.x - radius * 0.25f, cy + radius * 0.52f)
                lineTo(center.x - radius * 0.1f, cy - radius * 0.72f)
                lineTo(center.x + radius * 0.05f, cy + radius * 0.68f)
                lineTo(center.x + radius * 0.2f, cy)
                lineTo(center.x + radius * 0.4f, cy)
                lineTo(center.x + radius * 0.5f, cy - radius * 0.18f)
                lineTo(center.x + radius * 0.6f, cy)
                lineTo(right, cy)
            }

            drawPath(
                path = ecgPath,
                color = glowColor,
                style = Stroke(
                    width = 2.5.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            val dotX = center.x - radius * 0.9f + (radius * 1.8f * ecgSweepPathPhase)
            val dotY = center.y

            drawCircle(
                color = Color.White,
                radius = 3.5.dp.toPx(),
                center = Offset(dotX, dotY)
            )
        }
    }
}

// Breathtaking intro animated splash loader
@Composable
fun MedicalSplashScreen(
    isArabic: Boolean,
    onDismiss: () -> Unit
) {
    var startFadeOut by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startFadeOut) 0f else 1f,
        animationSpec = tween(700),
        label = "splash_fade"
    )

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2600)
        startFadeOut = true
        kotlinx.coroutines.delay(700)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .scale(if (startFadeOut) 1.06f else 1.0f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                startFadeOut = true
            },
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "ambient_mesh")
        val gridMovementPhase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(9000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "ambient_grid_run"
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val step = 85.dp.toPx()
            val offset = step * gridMovementPhase

            var x = -step + offset
            while (x < w + step) {
                drawLine(
                    color = Color(0xFF1E293B).copy(alpha = 0.35f),
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1.dp.toPx()
                )
                x += step
            }

            var y = -step + offset
            while (y < h + step) {
                drawLine(
                    color = Color(0xFF1E293B).copy(alpha = 0.35f),
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1.dp.toPx()
                )
                y += step
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(if (startFadeOut) 0.94f else 1.0f)
        ) {
            MedicalAnimatedLogo(
                modifier = Modifier
                    .size(160.dp)
                    .padding(8.dp),
                glowColor = Color(0xFF0EA5E9),
                crossColor = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AccuDose",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 38.sp,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isArabic) "حساب جرعات الأدوية بدقة وأمان" else "Precision Dosage Solutions",
                style = TextStyle(
                    color = Color(0xFF38BDF8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = CairoFontFamily,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            val progressPercentage by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "splash_loading_status"
            )

            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(3.dp)
                    .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(1.5.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressPercentage)
                        .fillMaxHeight()
                        .background(
                            Color(0xFF38BDF8),
                            RoundedCornerShape(1.5.dp)
                        )
                )
            }
        }
    }
}
