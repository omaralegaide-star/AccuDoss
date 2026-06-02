package com.example.accudose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager

data class IvFluidItem(
    val nameAr: String,
    val nameEn: String,
    val compositionAr: String,
    val compositionEn: String,
    val indicationAr: String,
    val indicationEn: String
)

val ivFluidsList = listOf(
    IvFluidItem(
        nameAr = "محلول جلوكوز ملح 0.45% (Dextrose Saline)",
        nameEn = "Dextrose 5% in Half Normal Saline (0.45%)",
        compositionAr = "٥٪ جلوكوز مع ٠.٤٥٪ كلوريد الصوديوم المائي",
        compositionEn = "5% Dextrose + 0.45% NaCl solution (Ideal Maintenance)",
        indicationAr = "المحلول القياسي لصيانة وتروية تغذية الأطفال والأكثر أماناً",
        indicationEn = "Standard maintenance fluid of choice for pediatric clinical cases"
    ),
    IvFluidItem(
        nameAr = "محلول ملحي معتاد 0.9% (Normal Saline)",
        nameEn = "Normal Saline (0.9% NaCl)",
        compositionAr = "٠.٩٪ كلوريد صوديوم نقي مائي معقم",
        compositionEn = "0.9% Sodium Chloride (Isotonic fluid solution)",
        indicationAr = "محلول طوارئ وصدمات لحالات غسيل وتعديل الحجم والإنعاش التروي ومصائد الجفاف",
        indicationEn = "Emergency boluses, shock states expansion, and hydration therapy"
    ),
    IvFluidItem(
        nameAr = "محلول رينجر لاكتات (Ringer's Lactate)",
        nameEn = "Ringer's Lactate (RL)",
        compositionAr = "سائل متوازن متساوي التوتر مع الصوديوم والبوتاسيوم واللاكتات والكالسيوم",
        compositionEn = "Balanced crystalloid solution mimicking physiological serum electrolyte composition",
        indicationAr = "الإنعاش والتروية وحالات الحروق والجراحة والنزف الشديد",
        indicationEn = "Post-surgical hydration, trauma fluid resuscitation, and severe burns"
    ),
    IvFluidItem(
        nameAr = "محلول ديكستروز 5% سوى (Dextrose 5% in Water)",
        nameEn = "Dextrose 5% in Water (D5W)",
        compositionAr = "٥٪ سكر جلوكوز نقي مائي معقم خالي تماماً من الأملاح",
        compositionEn = "5g Dextrose solute per 100ml sterile aqueous solution (Salt free)",
        indicationAr = "تصحيح نقص السكر بالدم والتروية المائية الخالية من الإلكتروليتات بحذر شديد",
        indicationEn = "Correction of hypoglycemia and hydration of patient requiring minimal sodium"
    )
)

@Composable
fun IVFluidsCalculatorScreen(
    isArabic: Boolean,
    onBack: () -> Unit
) {
    var weightText by remember { mutableStateOf("10") }
    var evaluatedWeightValue by remember { mutableStateOf(10.0) }
    var isCalculated by remember { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current
    val debouncer = remember { InputDebouncer() }

    fun triggerCalculation() {
        focusManager.clearFocus()
        val cleaned = debouncer.sanitize(weightText, allowDecimal = true, isNumericOnly = true)
        val parsed = cleaned.toDoubleOrNull()
        if (parsed != null && parsed > 0) {
            evaluatedWeightValue = parsed
            isCalculated = true
        } else {
            evaluatedWeightValue = 0.0
            isCalculated = false
        }
    }

    // Holiday-Segar 100/50/20 formula calculation
    val dailyMaintenance = if (evaluatedWeightValue <= 10.0) {
        evaluatedWeightValue * 100.0
    } else if (evaluatedWeightValue <= 20.0) {
        1000.0 + ((evaluatedWeightValue - 10.0) * 50.0)
    } else {
        1500.0 + ((evaluatedWeightValue - 20.0) * 20.0)
    }

    val hourlyRate = dailyMaintenance / 24.0
    val microDripRate = hourlyRate // Since 60 microdrops/ml, microdrips/min equals ml/hr!
    val macroDripRate = hourlyRate * 20.0 / 60.0 // 20 drops/ml standard macro-infusion set

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App top header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isArabic) "سوائل ومغذيات وريدية صيانة وريد الطفل" else "IV Fluids & Infusions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = CairoFontFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isArabic) "حساب سوائل الصيانة اليومية ومعدل التقطير بناءً على معادلة Holiday-Segar العالمية" else "Maintenance fluids & rates by global Holiday-Segar method",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    color = Color.Gray
                )
            }
        }

        // Weight Input Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = if (isArabic) "وزن الطفل الحالي (كيلو غرام)" else "Child Patient WeightInKg",
                    fontFamily = CairoFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF8B5CF6)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = weightText,
                        onValueChange = {
                            weightText = it
                            val cleaned = debouncer.sanitize(it, allowDecimal = true, isNumericOnly = true)
                            val parsed = cleaned.toDoubleOrNull()
                            if (parsed != null && parsed > 0) {
                                evaluatedWeightValue = parsed
                                isCalculated = true
                            } else {
                                isCalculated = false
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { triggerCalculation() }),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.08f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                            focusedIndicatorColor = Color(0xFF8B5CF6),
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        placeholder = { Text("10.0", color = Color.White.copy(alpha = 0.3f)) },
                        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { triggerCalculation() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8B5CF6),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(54.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text(
                            text = if (isArabic) "احسب" else "Calc",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Clinical Holiday-Segar rule info footer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic)
                                "قاعدة صيانة السوائل (Holiday-Segar):\n" +
                                "• بوزن ۱-۱۰ كغ: ۱۰۰ ملل/كغ/يومياً\n" +
                                "• بوزن ۱۱-٢٠ كغ: ۱۰۰۰ ملل + ٥٠ ملل لكل كغ فوق الـ١٠\n" +
                                "• بوزن فوق ٢٠ كغ: ١٥٠٠ ملل + ٢٠ ملل لكل كغ فوق الـ٢٠"
                                else
                                "Holiday-Segar Maintenance Formula Guidelines:\n" +
                                "• weight 1-10 kg: 100 ml/kg/day\n" +
                                "• weight 11-20 kg: 1000 ml + 50 ml for each kg > 10kg\n" +
                                "• weight > 20 kg: 1500 ml + 20 ml for each kg > 20kg",
                            fontSize = 9.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            fontFamily = CairoFontFamily,
                            lineHeight = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            // Main Formula Output Cards if calculated
            Text(
                text = if (isArabic) "معدلات وقرار التغذية الوريدية المحسوبة للطفل:" else "Calculated Pediatric IV Rates & Inflow Speeds:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = CairoFontFamily,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main outputs grid
            Card(
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.12f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Item 1: Daily Total Fluid
                    CalculationInfoRow(
                        title = if (isArabic) "سوائل الصيانة الكلية (خلال ٢٤ ساعة):" else "Daily Total Fluid Volume (24Hrs):",
                        value = if (isCalculated) "${formatDouble(dailyMaintenance)} ml/day" else "???",
                        borderColor = Color(0xFF8B5CF6)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Item 2: Hourly Rate
                    CalculationInfoRow(
                        title = if (isArabic) "معدل التدفق الميكروي (خلال الساعة):" else "Hourly Flow Infusion Speed (ml/Hr):",
                        value = if (isCalculated) "${formatDouble(hourlyRate)} ml/hour" else "???",
                        borderColor = Color(0xFF0EA5E9)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Item 3: Drip Speeds
                    CalculationInfoRow(
                        title = if (isArabic) "معدل التقطير الميكروي (٦٠ قطرة/ملل):" else "Micro-drip Rate (60 gtt/ml device):",
                        value = if (isCalculated) "${formatDouble(microDripRate)} gtt/min (micro)" else "???",
                        borderColor = Color(0xFF10B981)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Item 4: Macro Drip rate
                    CalculationInfoRow(
                        title = if (isArabic) "معدل التقطير الكبير (٢٠ قطرة/ملل):" else "Macro-drip Rate (20 gtt/ml device):",
                        value = if (isCalculated) "${formatDouble(macroDripRate)} gtt/min (macro)" else "???",
                        borderColor = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isArabic) "محاليل وسوائل التروية الطبية المعتمدة:" else "Standard Fluid Composition Guidelines:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = CairoFontFamily,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Listing of common fluids
            ivFluidsList.forEach { fluid ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.08f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isArabic) fluid.nameAr else fluid.nameEn,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = CairoFontFamily,
                            color = Color(0xFF8B5CF6)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "المكونات والمحتوى: ${fluid.compositionAr}" else "Composition: ${fluid.compositionEn}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = CairoFontFamily
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isArabic) "الاستخدام الطبي: ${fluid.indicationAr}" else "Usage Indication: ${fluid.indicationEn}",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            fontFamily = CairoFontFamily
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CalculationInfoRow(
    title: String,
    value: String,
    borderColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, borderColor.copy(alpha = 0.2f)), RoundedCornerShape(10.dp))
            .background(Color.Gray.copy(alpha = 0.03f))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                fontFamily = CairoFontFamily
            )

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = borderColor,
                fontFamily = CairoFontFamily
            )
        }
    }
}
