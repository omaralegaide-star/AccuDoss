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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager

data class EmergencyDrug(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val concentration: String,
    val baseDoseFormulaAr: String,
    val baseDoseFormulaEn: String,
    val calculateDose: (weight: Double) -> String,
    val unitAr: String,
    val unitEn: String,
    val alertAr: String,
    val alertEn: String
)

val emergencyDrugsDatabase = listOf(
    EmergencyDrug(
        id = "adrenaline",
        nameAr = "أدرينالين (١:١٠,٠٠٠)",
        nameEn = "Adrenaline (Epinephrine 1:10,000)",
        concentration = "0.1 mg / ml (vial or pre-diluted)",
        baseDoseFormulaAr = "0.01 mg / kg (0.1 ml / kg) وريدي أو داخل العظم",
        baseDoseFormulaEn = "0.01 mg / kg (0.1 ml / kg) IV / IO",
        calculateDose = { weight -> formatDouble(weight * 0.1) },
        unitAr = "ملل (ml)",
        unitEn = "ml IV/IO",
        alertAr = "للإنعاش القلبي والرئوي وحالات الصدمة التحسسية الحادة. تكرر كل ٣-٥ دقائق حسب الاستجابة.",
        alertEn = "For cardiac arrest, severe anaphylaxis. Repeat every 3-5 minutes as clinically indicated."
    ),
    EmergencyDrug(
        id = "atropine",
        nameAr = "أتروبين (Atropine)",
        nameEn = "Atropine Sulfate",
        concentration = "1 mg / 1 ml",
        baseDoseFormulaAr = "0.02 mg / kg (الحد الأدنى 0.1 مغ، الحد الأقصى للجعة الواحدة 0.5 مغ)",
        baseDoseFormulaEn = "0.02 mg / kg (Min dose 0.1mg, Max single dose 0.5mg) IV/IO",
        calculateDose = { weight -> 
            val calculated = weight * 0.02 // mg
            // Convert to volume (ml) using 1mg/ml
            val finalMl = calculated.coerceIn(0.1, 0.5)
            formatDouble(finalMl)
        },
        unitAr = "ملل (ml)",
        unitEn = "ml IV/IO",
        alertAr = "لعلاج تباطؤ ضربات القلب الحاد المصحوب بعلامات صدمة تروية.",
        alertEn = "For severe bradycardia with cardiorespiratory compromise."
    ),
    EmergencyDrug(
        id = "amiodarone",
        nameAr = "أميو دارون (مضاد اضطراب قلب)",
        nameEn = "Amiodarone",
        concentration = "150 mg / 3 ml (50 mg / ml)",
        baseDoseFormulaAr = "5 mg / kg وريدي سريع (صدم المقاومة)",
        baseDoseFormulaEn = "5 mg / kg IV bolus for refractory VF/pulseless VT",
        calculateDose = { weight ->
            val totalMg = weight * 5.0
            val volumeMl = totalMg / 50.0 // 50mg/ml
            formatDouble(volumeMl)
        },
        unitAr = "ملل (ml)",
        unitEn = "ml",
        alertAr = "لحالات الرجفان البطيني المقاوم للصعق وعودة التسرع فوق البطيني الحاد.",
        alertEn = "Use for refractory ventricular fibrillation or pulseless ventricular tachycardia."
    ),
    EmergencyDrug(
        id = "bicarb",
        nameAr = "بيكربونات الصوديوم 8.4%",
        nameEn = "Sodium Bicarbonate (8.4%)",
        concentration = "1 mEq / ml (8.4% aqueous solute)",
        baseDoseFormulaAr = "1 mEq / kg (1 ml / kg) وريدي بطيء حذر جداً",
        baseDoseFormulaEn = "1 mEq / kg (1 ml / kg) slow IV infusion",
        calculateDose = { weight -> formatDouble(weight * 1.0) },
        unitAr = "ملل (ml) وريدي ببطء",
        unitEn = "ml (slow IV)",
        alertAr = "يستخدم لحالات الحماض الاستقلابي الحاد أو فرط البوتاسيوم المستمر. تجنب الخلط المباشر مع الكالسيوم.",
        alertEn = "For persistent metabolic acidosis or hyperkalemia. Flush lines completely - incompatible with Calcium."
    ),
    EmergencyDrug(
        id = "ns_bolus",
        nameAr = "دفعة محلول ملحي صدمية (Bolus)",
        nameEn = "Normal Saline Bolus (0.9% NaCl)",
        concentration = "0.9% Isotonic Sterile Solution",
        baseDoseFormulaAr = "20 ml / kg دفعة واحدة سريعة لإنعاش الضغط والتغذية الدموية",
        baseDoseFormulaEn = "20 ml / kg rapid IV bolus fluid therapy",
        calculateDose = { weight -> formatDouble(weight * 20.0) },
        unitAr = "ملل (ml) تروية وريدية سريعة",
        unitEn = "ml normal saline",
        alertAr = "لحالات الصدمة ونقص الحجم الشديد. تسرب خلال ٥-٢٠ دقيقة وتعاد التقييمات التروية للطفل.",
        alertEn = "Indications include clinical shock state or hypovolemia. Infuse over 5-20 min."
    )
)

@Composable
fun EmergencyCalculatorScreen(
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
        // App top header with red/critical alarm design accents
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(Color(0xFFEF4444).copy(alpha = 0.15f), CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFEF4444)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isArabic) "طوارئ وإنعاش طبي حرج" else "Critical Emergency & Resus",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily,
                        color = Color(0xFFEF4444)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFEF4444), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isArabic) "حالة حرجة" else "CRITICAL",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = CairoFontFamily,
                            color = Color.White
                        )
                    }
                }
                Text(
                    text = if (isArabic) "حساب فوري للأدوية الإنعاشية وجرعات الصدمات وإنقاذ الحياة للأطفال" else "Emergency life-saving pediatric drug protocols",
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
                    text = if (isArabic) "وزن الطفل الحالي للإنعاش السريع:" else "Child Patient Resus WeightInKg:",
                    fontFamily = CairoFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFEF4444)
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
                            focusedIndicatorColor = Color(0xFFEF4444),
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
                            containerColor = Color(0xFFEF4444),
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

                // Fast estimating chips for code red state
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "تقدير عاجل بالوزن التقريبي للطفل:" else "Quick emergency weight estimations:",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        fontFamily = CairoFontFamily
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                val codeChips = listOf(
                    AgeChipInfo("Nsn", if (isArabic) "حديث ولادة (4ك)" else "Neonate (4kg)", 4.0, "1"),
                    AgeChipInfo("6m", if (isArabic) "٦ أشهر (7ك)" else "6 Months (7kg)", 7.0, "6"),
                    AgeChipInfo("12m", if (isArabic) "سنة (10ك)" else "1 Year (10kg)", 10.0, "12"),
                    AgeChipInfo("5y", if (isArabic) "٥ سنوات (18ك)" else "5 Years (18kg)", 18.0, "60")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    codeChips.forEach { chip ->
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (evaluatedWeightValue == chip.weightKg) Color(0xFFEF4444) else Color.Transparent
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
                                color = if (evaluatedWeightValue == chip.weightKg) Color(0xFFEF4444) else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = CairoFontFamily
                            )
                        }
                    }
                }
            }
        }

        // Filter search input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = if (isArabic) "بحث عاجل في أدوية الإنعاش والطوارئ..." else "Filter emergency medicines database...",
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
                focusedBorderColor = Color(0xFFEF4444),
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

        // Fluid and dry drug list calculations
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            val filteredEmergency = emergencyDrugsDatabase.filter {
                matchSearchQuery(it.nameAr, searchQuery) || matchSearchQuery(it.nameEn, searchQuery)
            }

            if (filteredEmergency.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("🚨", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isArabic) "عفواً، لا توجد نتائج مطابقة لاسم هذا الدواء العاجل" else "No matching cardiopulmonary pediatric drugs found",
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
                    filteredEmergency.forEachIndexed { index, drug ->
                        val rowBgColor = if (index % 2 == 0) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        }

                        // Evaluate result
                        val calculatedValue = if (isCalculated) drug.calculateDose(evaluatedWeightValue) else "???"
                        val resolvedUnit = if (isArabic) {
                            if (drug.id == "amiodarone") "ملل (يحتوي على ${formatDouble(evaluatedWeightValue * 5.0)} مغ)" else drug.unitAr
                        } else {
                            if (drug.id == "amiodarone") "ml (gives ${formatDouble(evaluatedWeightValue * 5.0)} mg)" else drug.unitEn
                        }

                        EmergencyRowItem(
                            isArabic = isArabic,
                            nameAr = drug.nameAr,
                            nameEn = drug.nameEn,
                            concentration = drug.concentration,
                            formula = if (isArabic) drug.baseDoseFormulaAr else drug.baseDoseFormulaEn,
                            resultValue = calculatedValue,
                            unitText = resolvedUnit,
                            alertText = if (isArabic) drug.alertAr else drug.alertEn,
                            backgroundColor = rowBgColor
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmergencyRowItem(
    isArabic: Boolean,
    nameAr: String,
    nameEn: String,
    concentration: String,
    formula: String,
    resultValue: String,
    unitText: String,
    alertText: String,
    backgroundColor: Color
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, Color(0xFFEF4444).copy(alpha = 0.15f)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isArabic) nameAr else nameEn,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily,
                        color = Color(0xFFEF4444)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Vial Concentration: $concentration",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFFEF4444), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "إنقاذ حياة" else "Emergency",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Formula
            Text(
                text = "${if (isArabic) "المعادلة الارشادية:" else "Guideline Formula:"} $formula",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontFamily = CairoFontFamily
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Calculated Output Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) "الجرعة العاجلة فوراً:" else "Urgent Dose Volume:",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = resultValue,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFEF4444)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = unitText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontFamily = CairoFontFamily
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Emergency alert notice box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEF4444).copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Alert",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = alertText,
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold,
                        fontFamily = CairoFontFamily,
                        lineHeight = 13.sp
                    )
                }
            }
        }
    }
}
