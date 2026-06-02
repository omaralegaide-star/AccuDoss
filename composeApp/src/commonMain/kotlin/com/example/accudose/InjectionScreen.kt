package com.example.accudose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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

val injectionsList = listOf(
    InjectionMedicine(
        id = "ampicillin",
        nameAr = "أمبيسيلين",
        nameEn = "Ampicillin",
        formulation = "vial 500mg+5cc N.S",
        baseCcFor10Kg = 5.0,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "amoxil",
        nameAr = "أموكسيل",
        nameEn = "Amoxil",
        formulation = "vial 500mg +5cc",
        baseCcFor10Kg = 2.5,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "ceftriaxone",
        nameAr = "سيفتریاكسون",
        nameEn = "Ceftriaxone",
        formulation = "vial 1g+10cc N.S",
        baseCcFor10Kg = 5.0,
        frequencySuffixAr = "cc × 1 or ÷ 2",
        frequencySuffixEn = "cc × 1 or ÷ 2"
    ),
    InjectionMedicine(
        id = "claforan",
        nameAr = "كلافوران",
        nameEn = "Claforan®",
        formulation = "vial 1g+10cc N.S",
        baseCcFor10Kg = 5.0,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "garamycin_80",
        nameAr = "جاراميسين",
        nameEn = "Garamycin® 80mg",
        formulation = "amp 80mg/2ml +6cc N.S",
        baseCcFor10Kg = 2.5,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "garamycin_100",
        nameAr = "جاراميسين",
        nameEn = "Garamycin® 20mg",
        formulation = "amp 20mg/2ml",
        baseCcFor10Kg = 2.5,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "ceftazidime",
        nameAr = "سيفتازيديم",
        nameEn = "Ceftazidim",
        formulation = "vial 1gm + 10cc",
        baseCcFor10Kg = 5.0,
        frequencySuffixAr = "cc × 3",
        frequencySuffixEn = "cc × 3"
    ),
    InjectionMedicine(
        id = "acyclovir",
        nameAr = "أسيكلوفير",
        nameEn = "Acyclovir (Zovirax®)",
        formulation = "vial 250mg+ 5cc (zovirax®)",
        baseCcFor10Kg = 2.0,
        frequencySuffixAr = "cc × 3",
        frequencySuffixEn = "cc × 3"
    ),
    InjectionMedicine(
        id = "amikacin_100",
        nameAr = "أميكاسين",
        nameEn = "Amikacin 100mg",
        formulation = "amp 100mg/2cc",
        baseCcFor10Kg = 1.5,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "amikacin_500",
        nameAr = "أميكاسين",
        nameEn = "Amikacin 500mg",
        formulation = "amp 500mg/2cc+3cc N.S",
        baseCcFor10Kg = 0.75,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "protec",
        nameAr = "بروتك",
        nameEn = "Protec®",
        formulation = "vial 1g + 10cc N.S",
        baseCcFor10Kg = 5.0,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "vancomycin",
        nameAr = "فانكومايسين",
        nameEn = "Vancomycin",
        formulation = "vial 500 + 5 cc N.S",
        baseCcFor10Kg = 1.33333,
        frequencySuffixAr = "cc × 3",
        frequencySuffixEn = "cc × 3",
        isSpecial = true
    ),
    InjectionMedicine(
        id = "meronem",
        nameAr = "ميرونيم",
        nameEn = "MERONEM",
        formulation = "vial 500mg + 5cc N.S",
        baseCcFor10Kg = 2.0,
        frequencySuffixAr = "× 2 or × 3",
        frequencySuffixEn = "× 2 or × 3",
        isSpecial = true
    ),
    InjectionMedicine(
        id = "paracetamol_amp",
        nameAr = "باراسيتامول أمبول",
        nameEn = "Paracetamol amp",
        formulation = "Ampoule",
        baseCcFor10Kg = 1.0,
        frequencySuffixAr = "cc × 4",
        frequencySuffixEn = "cc × 4"
    ),
    InjectionMedicine(
        id = "de_vomit",
        nameAr = "دي-فوميت",
        nameEn = "De-vomit® amp",
        formulation = "8mg/4ml",
        baseCcFor10Kg = 1.0,
        frequencySuffixAr = "cc × 3",
        frequencySuffixEn = "cc × 3"
    ),
    InjectionMedicine(
        id = "hc_vial",
        nameAr = "هيدروكورتيزون",
        nameEn = "H.C vial",
        formulation = "100mg+2ml N.S",
        baseCcFor10Kg = 1.0,
        frequencySuffixAr = "cc × 4",
        frequencySuffixEn = "cc × 4"
    ),
    InjectionMedicine(
        id = "allermine",
        nameAr = "الليرمين",
        nameEn = "Allermine® amp",
        formulation = "10mg/1ml",
        baseCcFor10Kg = 0.5,
        frequencySuffixAr = "cc × 4",
        frequencySuffixEn = "cc × 4"
    ),
    InjectionMedicine(
        id = "decadron",
        nameAr = "ديكادرون",
        nameEn = "Decadron® amp",
        formulation = "Ampoule",
        baseCcFor10Kg = 0.5,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "lasix",
        nameAr = "لازكس",
        nameEn = "Lasix® amp",
        formulation = "20mg/2ml",
        baseCcFor10Kg = 1.0,
        frequencySuffixAr = "cc × 2",
        frequencySuffixEn = "cc × 2"
    ),
    InjectionMedicine(
        id = "voltarin",
        nameAr = "فولتارين",
        nameEn = "Voltarin® amp",
        formulation = "75mg/3ml",
        baseCcFor10Kg = 0.8,
        frequencySuffixAr = "cc / IM",
        frequencySuffixEn = "cc / IM"
    )
)

@Composable
fun InjectionCalculatorScreen(
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
        val cleaned = debouncer.lastText.ifEmpty { weightText }
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
        // Bar Info Back
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
                    text = if (isArabic) "حقن (Vial & Ampoule)" else "Injections (Vial & Ampoule)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = CairoFontFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isArabic) "حساب دقيق لجرعات الحقن الوريدي والعضلي للطفل" else "Calibrated IV/IM injection schedules",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "أدخل وزن جسم الطفل الصغير (بالكجم) :" else "Enter precise pediatric weight (in kg) :",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { input ->
                            val sanitized = debouncer.sanitize(input, allowDecimal = true, isNumericOnly = true)
                            weightText = sanitized
                            debouncer.lastText = sanitized
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { triggerCalculation() }
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color(0xFF0F172A),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        ),
                        placeholder = {
                            Text(
                                text = if (isArabic) "الوزن" else "Weight",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                color = Color(0xFF475569)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF0F172A),
                            unfocusedTextColor = Color(0xFF0F172A),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(52.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { triggerCalculation() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Text(
                            text = if (isArabic) "احسب" else "Calculate",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Search Bar Filter
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = if (isArabic) "ابحث عن العلاج أو الحقنة..." else "Filter injections...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    fontFamily = CairoFontFamily
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Clear",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontFamily = CairoFontFamily
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .height(50.dp)
        )

        // Column Titles LTR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isArabic) "اسم العلاج والتركيبة اللمسية" else "Medication Formulation",
                color = Color.Gray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isArabic) "الجرعة الدقيقة المطلوبة" else "Prescription Dosage",
                color = Color.Gray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        val filteredInjections = remember(searchQuery) {
            injectionsList.filter {
                matchSearchQuery(it.nameEn, searchQuery) || matchSearchQuery(it.nameAr, searchQuery)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        ) {
            if (filteredInjections.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isArabic) "عذراً، لم نجد علاجاً بهذا الاسم." else "No injections match filters.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                filteredInjections.forEachIndexed { index, drug ->
                    val rowBgColor = if (index % 2 == 0) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    }

                    // Formula: evaluatedDose = (weight / 10) * baseDoseFor10Kg
                    val calculatedDose = (evaluatedWeightValue / 10.0) * drug.baseCcFor10Kg
                    val formattedDoseValue = formatDouble(calculatedDose)

                    InjectionRowItem(
                        isArabic = isArabic,
                        nameAr = drug.nameAr,
                        nameEn = drug.nameEn,
                        formulation = drug.formulation,
                        resultFormatted = formattedDoseValue,
                        suffixCodeAr = drug.frequencySuffixAr,
                        suffixCodeEn = drug.frequencySuffixEn,
                        backgroundColor = rowBgColor,
                        isSpecial = drug.isSpecial
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun InjectionRowItem(
    isArabic: Boolean,
    nameAr: String,
    nameEn: String,
    formulation: String,
    resultFormatted: String,
    suffixCodeAr: String,
    suffixCodeEn: String,
    backgroundColor: Color,
    isSpecial: Boolean = false
) {
    val isDarkScheme = isSystemInDarkTheme()
    val cardBg = if (isSpecial) Color(0xFF1E293B) else backgroundColor
    val cardBorder = if (isSpecial) {
        BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = cardBorder,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 13.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isArabic) nameAr else nameEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isSpecial) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = formulation,
                        fontSize = 11.sp,
                        color = if (isSpecial) Color.White.copy(alpha = 0.65f) else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                val suffixText = if (isArabic) suffixCodeAr else suffixCodeEn
                val bidiCombinedText = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color(0xFFEF4444), fontWeight = FontWeight.Black)) {
                        append(resultFormatted)
                    }
                    if (suffixText.isNotEmpty()) {
                        append(" ")
                        val suffixColor = if (isDarkScheme || isSpecial) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        withStyle(SpanStyle(color = suffixColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)) {
                            append(suffixText)
                        }
                    }
                }

                Text(
                    text = bidiCombinedText,
                    fontSize = 16.sp,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}
