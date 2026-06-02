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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager

data class SyrupItem(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val concentrationMg: Double,
    val concentrationMl: Double,
    val baseDoseMgPerKg: Double,
    val frequencyAr: String,
    val frequencyEn: String,
    val isSpecial: Boolean = false
)

val syrupMedicines = listOf(
    SyrupItem(
        id = "paracetamol_120",
        nameAr = "باراسيتامول (أدول/باندول ١٢٠مغ)",
        nameEn = "Paracetamol (Adol/Panadol 120mg)",
        descriptionAr = "مسكن خافض حرارة (concentration: 120mg/5ml)",
        descriptionEn = "Analgesic & Antipyretic (120mg / 5ml)",
        concentrationMg = 120.0,
        concentrationMl = 5.0,
        baseDoseMgPerKg = 15.0,
        frequencyAr = "ملل × ٣-٤ مرات يومياً (كل ٦ ساعات عند الحاجة)",
        frequencyEn = "ml × 3-4 daily (Every 6 hours as needed)"
    ),
    SyrupItem(
        id = "paracetamol_250",
        nameAr = "باراسيتامول (أدول/باندول ٢٥٠مغ)",
        nameEn = "Paracetamol (Adol/Panadol 250mg)",
        descriptionAr = "مسكن خافض حرارة (concentration: 250mg/5ml)",
        descriptionEn = "Analgesic & Antipyretic (250mg / 5ml)",
        concentrationMg = 250.0,
        concentrationMl = 5.0,
        baseDoseMgPerKg = 15.0,
        frequencyAr = "ملل × ٣-٤ مرات يومياً (كل ٦ ساعات عند الحاجة)",
        frequencyEn = "ml × 3-4 daily (Every 6 hours as needed)"
    ),
    SyrupItem(
        id = "ibuprofen_100",
        nameAr = "إيبوبروفين (بروفين ١٠٠مغ)",
        nameEn = "Ibuprofen (Brufen 100mg)",
        descriptionAr = "مضاد التهاب خافض مسكن (concentration: 100mg/5ml)",
        descriptionEn = "NSAID Antipyretic (100mg / 5ml)",
        concentrationMg = 100.0,
        concentrationMl = 5.0,
        baseDoseMgPerKg = 10.0,
        frequencyAr = "ملل × ٣ مرات يومياً (كل ٨ ساعات بحذر بعد الأكل)",
        frequencyEn = "ml × 3 daily (Every 8 hours with food)"
    ),
    SyrupItem(
        id = "amoxicillin_125",
        nameAr = "أموكسيسيلين (أموكسيل ١٢٥مغ)",
        nameEn = "Amoxicillin (Amoxil 125mg)",
        descriptionAr = "مضاد حيوي بكتيري واسع الطيف (concentration: 125mg/5ml)",
        descriptionEn = "Broad Spectrum Antibiotic (125mg / 5ml)",
        concentrationMg = 125.0,
        concentrationMl = 5.0,
        baseDoseMgPerKg = 15.0, // standard 45mg/kg/day divided by 3 doses
        frequencyAr = "ملل × ٣ مرات يومياً (كل ٨ ساعات بدقة بالتناوب)",
        frequencyEn = "ml × 3 daily (Every 8 hours precisely)"
    ),
    SyrupItem(
        id = "amoxicillin_250",
        nameAr = "أموكسيسيلين (أموكسيل ٢٥٠مغ)",
        nameEn = "Amoxicillin (Amoxil 250mg)",
        descriptionAr = "مضاد حيوي بكتيري واسع الطيف (concentration: 250mg/5ml)",
        descriptionEn = "Broad Spectrum Antibiotic (250mg / 5ml)",
        concentrationMg = 250.0,
        concentrationMl = 5.0,
        baseDoseMgPerKg = 15.0,
        frequencyAr = "ملل × ٣ مرات يومياً (كل ٨ ساعات بدقة بالتناوب)",
        frequencyEn = "ml × 3 daily (Every 8 hours precisely)"
    ),
    SyrupItem(
        id = "cefixime_100",
        nameAr = "سيفيكسيم (سوبراكس ١٠٠مغ)",
        nameEn = "Cefixime (Suprax 100mg)",
        descriptionAr = "مضاد حيوي جيل ثالث للأطفال (concentration: 100mg/5ml)",
        descriptionEn = "3rd Gen Cephalosporin Antibiotic (100mg / 5ml)",
        concentrationMg = 100.0,
        concentrationMl = 5.0,
        baseDoseMgPerKg = 8.0, // 8mg/kg once daily
        frequencyAr = "ملل × مرة واحدة يومياً (كل ٢٤ ساعة بدقة مواعدية)",
        frequencyEn = "ml × 1 daily (Once daily every 24 hours)"
    ),
    SyrupItem(
        id = "zyrtec",
        nameAr = "زيرتك شراب (٥مغ)",
        nameEn = "Cetirizine (Zyrtec 5mg)",
        descriptionAr = "مضاد حساسية ورشح آمن (concentration: 5mg/5ml)",
        descriptionEn = "Antihistamine Syrup (5mg / 5ml)",
        concentrationMg = 5.0,
        concentrationMl = 5.0,
        baseDoseMgPerKg = 0.25,
        frequencyAr = "ملل × مرة واحدة أو مقسمة على جرعتين بحذر",
        frequencyEn = "ml × 1 once daily or split into twice daily"
    )
)

@Composable
fun SyrupCalculatorScreen(
    isArabic: Boolean,
    onBack: () -> Unit
) {
    var weightText by remember { mutableStateOf("10") }
    var searchQuery by remember { mutableStateOf("") }
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
                    text = if (isArabic) "شـراب معـلّق والمسكنات" else "Liquid Suspensions & Syrups",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = CairoFontFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isArabic) "حساب سريع لمقدار جرعة الدواء السائل بالتناسب الدقيق لوزن الطفل" else "Calibrated volume (ml) for liquid pediatrics",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    color = Color.Gray
                )
            }
        }

        // Weight Entry Card
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
                    color = Color(0xFF38BDF8)
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
                            focusedIndicatorColor = Color(0xFF38BDF8),
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
                            containerColor = Color(0xFF0EA5E9),
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

                Spacer(modifier = Modifier.height(14.dp))

                // Fast Age Weight Estimating Chips
                Text(
                    text = if (isArabic) "تقدير الوزن السريع بالتناسب مع العمر التقريبي:" else "Fast Weight Estimator by approximate ages:",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.6f),
                    fontFamily = CairoFontFamily
                )
                Spacer(modifier = Modifier.height(8.dp))

                val estimationChips = listOf(
                    AgeChipInfo("3m", if (isArabic) "٣ أشهر (6ك)" else "3 Months (6kg)", 6.0, "3"),
                    AgeChipInfo("1y", if (isArabic) "سنة (10ك)" else "1 Year (10kg)", 10.0, "12"),
                    AgeChipInfo("2y", if (isArabic) "سنتين (12ك)" else "2 Years (12kg)", 12.0, "24"),
                    AgeChipInfo("3y", if (isArabic) "٣ سنوات (14ك)" else "3 Years (14kg)", 14.0, "36")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    estimationChips.forEach { chip ->
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (evaluatedWeightValue == chip.weightKg) Color(0xFF38BDF8) else Color.Transparent
                                    ),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    weightText = chip.weightKg.toInt().toString()
                                    evaluatedWeightValue = chip.weightKg
                                    isCalculated = true
                                    focusManager.clearFocus()
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = chip.label,
                                color = if (evaluatedWeightValue == chip.weightKg) Color(0xFF38BDF8) else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }
                    }
                }
            }
        }

        // Live Medical Search Filters
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = if (isArabic) "ابدأ بكتابة اسم الدواء للبحث العاجل..." else "Start typing drug name to filter...",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = CairoFontFamily
                )
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Outlined.Close, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            shape = RoundedCornerShape(14.dp)
        )

        // Calculated Table Items List
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            val filteredSyrups = syrupMedicines.filter {
                matchSearchQuery(it.nameAr, searchQuery) || matchSearchQuery(it.nameEn, searchQuery)
            }

            if (filteredSyrups.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    Text("⚠️", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isArabic) "عفواً، لا توجد نتائج مطابقة لاسم هذا الدواء المعلق" else "No matching suspention child syrup drugs found",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily,
                        color = Color.Gray
                    )
                }
            } else {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    filteredSyrups.forEachIndexed { index, drug ->
                        val rowBgColor = if (index % 2 == 0) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        }

                        // Formula: Dose (ml) = (WeightInKg * baseDoseMgPerKg) / (concentrationMg / concentrationMl)
                        val oneMlMg = drug.concentrationMg / drug.concentrationMl
                        val calculatedDoseMl = (evaluatedWeightValue * drug.baseDoseMgPerKg) / oneMlMg
                        val formattedDoseValue = formatDouble(calculatedDoseMl)

                        SyrupRowItem(
                            isArabic = isArabic,
                            nameAr = drug.nameAr,
                            nameEn = drug.nameEn,
                            description = if (isArabic) drug.descriptionAr else drug.descriptionEn,
                            resultFormatted = if (isCalculated) formattedDoseValue else "???",
                            suffixAr = drug.frequencyAr,
                            suffixEn = drug.frequencyEn,
                            backgroundColor = rowBgColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SyrupRowItem(
    isArabic: Boolean,
    nameAr: String,
    nameEn: String,
    description: String,
    resultFormatted: String,
    suffixAr: String,
    suffixEn: String,
    backgroundColor: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.12f)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isArabic) nameAr else nameEn,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontFamily = CairoFontFamily
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .background(Color(0xFF38BDF8).copy(alpha = 0.13f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "دواء شراب سائل" else "Oral Syrup",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily,
                        color = Color(0xFF0EA5E9)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Result presentation row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B), RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isArabic) "الجرعة المقدرة:" else "Calibrated Dose:",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = resultFormatted,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFEF4444)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isArabic) suffixAr else suffixEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = CairoFontFamily
                        )
                    }
                }
            }
        }
    }
}
