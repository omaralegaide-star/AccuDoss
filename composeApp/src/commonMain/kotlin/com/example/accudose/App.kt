package com.example.accudose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme

// Central Modern Slate Palette
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0F172A),
    secondary = Color(0xFF0EA5E9),
    tertiary = Color(0xFF10B981),
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    secondary = Color(0xFF0EA5E9),
    tertiary = Color(0xFF34D399),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onPrimary = Color(0xFF0F172A),
    onSecondary = Color.White,
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC)
)

@Composable
fun AccuDoseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

val categoriesList = listOf(
    MedicalCategory(
        id = "syrup",
        titleAr = "شـراب معـلّق",
        titleEn = "Liquid Suspension",
        subtitleAr = "جرعة المضادات الحيوية، خافضات الحرارة والمسكنات",
        subtitleEn = "Paracetamol, ibuprofen, antihistamine & antibiotic syrups",
        emoji = "🧪",
        badgeTextAr = "دواء شراب معلق",
        badgeTextEn = "Suspension Doses",
        colorAccent = Color(0xFF38BDF8)
    ),
    MedicalCategory(
        id = "injection",
        titleAr = "حُـقَن (Vial & Ampoule)",
        titleEn = "Injections (Vial & Amp)",
        subtitleAr = "حساب جرعات ومعدل تخفيف حقن الأطفال الوريدية والعضلية",
        subtitleEn = "Calibrated IV/IM injections, cephalosporins & vials",
        emoji = "💉",
        badgeTextAr = "أمبول وفيال وريدي وعضلي",
        badgeTextEn = "Injection Doses",
        colorAccent = Color(0xFF10B981)
    ),
    MedicalCategory(
        id = "iv_fluids",
        titleAr = "سوائل ومغذيات وريدية",
        titleEn = "IV Fluids & Infusions",
        subtitleAr = "حجم ومعدل تقطير محاليل الجفاف والتروية الوريدية",
        subtitleEn = "Continuous normal saline, glucose infusion & maintenance rates",
        emoji = "💧",
        badgeTextAr = "محاليل التروية والجفاف وريدية",
        badgeTextEn = "IV Fluids & Infusions",
        colorAccent = Color(0xFF8B5CF6)
    ),
    MedicalCategory(
        id = "emergency",
        titleAr = "طوارئ وإنعاش",
        titleEn = "Critical Emergency & Resus",
        subtitleAr = "جرعات حالات الإنعاش، العناية المركزة والظروف الطارئة الحرجة",
        subtitleEn = "Emergency adrenaline, cardiac resus, shock & ICU treatments",
        emoji = "🚨",
        badgeTextAr = "حالات طارئة وإنعاش",
        badgeTextEn = "Emergency & Shock",
        colorAccent = Color(0xFFEF4444)
    )
)

@Composable
fun App() {
    var isArabic by remember { mutableStateOf(true) }
    var showSplashScreen by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Categories) }

    AccuDoseTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (showSplashScreen) {
                MedicalSplashScreen(
                    isArabic = isArabic,
                    onDismiss = { showSplashScreen = false }
                )
            } else {
                // Platform Back Interceptor
                PlatformBackHandler(enabled = currentScreen != AppScreen.Categories) {
                    currentScreen = AppScreen.Categories
                }

                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        if (targetState is AppScreen.Categories) {
                            (slideInHorizontally { -it } + fadeIn()).togetherWith(
                                slideOutHorizontally { it } + fadeOut()
                            )
                        } else {
                            (slideInHorizontally { it } + fadeIn()).togetherWith(
                                slideOutHorizontally { -it } + fadeOut()
                            )
                        }
                    },
                    label = "navigator_engine"
                ) { screen ->
                    when (screen) {
                        is AppScreen.Categories -> {
                            CategoriesGridScreen(
                                isArabic = isArabic,
                                onLanguageToggle = { isArabic = !isArabic },
                                onCategorySelected = { categoryId ->
                                    currentScreen = when (categoryId) {
                                        "syrup" -> AppScreen.SyrupView
                                        "injection" -> AppScreen.InjectionView
                                        "iv_fluids" -> AppScreen.IVFluidsView
                                        "emergency" -> AppScreen.EmergencyView
                                        else -> AppScreen.Categories
                                    }
                                },
                                onAboutSelected = {
                                    currentScreen = AppScreen.AboutView
                                }
                            )
                        }
                        is AppScreen.SyrupView -> {
                            SyrupCalculatorScreen(
                                isArabic = isArabic,
                                onBack = { currentScreen = AppScreen.Categories }
                            )
                        }
                        is AppScreen.InjectionView -> {
                            InjectionCalculatorScreen(
                                isArabic = isArabic,
                                onBack = { currentScreen = AppScreen.Categories }
                            )
                        }
                        is AppScreen.IVFluidsView -> {
                            IVFluidsCalculatorScreen(
                                isArabic = isArabic,
                                onBack = { currentScreen = AppScreen.Categories }
                            )
                        }
                        is AppScreen.EmergencyView -> {
                            EmergencyCalculatorScreen(
                                isArabic = isArabic,
                                onBack = { currentScreen = AppScreen.Categories }
                            )
                        }
                        is AppScreen.AboutView -> {
                            AboutDeveloperScreen(
                                isArabic = isArabic,
                                onBack = { currentScreen = AppScreen.Categories }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesGridScreen(
    isArabic: Boolean,
    onLanguageToggle: () -> Unit,
    onCategorySelected: (String) -> Unit,
    onAboutSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // App header banner with language switch
        HeaderBanner(isArabic = isArabic, onLanguageToggle = onLanguageToggle)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            categoriesList.forEach { category ->
                CategoryCardItem(
                    isArabic = isArabic,
                    category = category,
                    onClick = { onCategorySelected(category.id) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Developer Info card
            AboutDeveloperBannerCard(
                isArabic = isArabic,
                onClick = onAboutSelected
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HeaderBanner(
    isArabic: Boolean,
    onLanguageToggle: () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradientBrush)
            .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Heartbeat dynamic emblem
                MedicalAnimatedLogo(
                    modifier = Modifier.size(56.dp),
                    glowColor = Color(0xFF0EA5E9),
                    crossColor = Color.White
                )

                // High-contrast, clean pill language toggler
                Button(
                    onClick = onLanguageToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.12f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(
                        text = if (isArabic) "English EN" else "العربية AR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AccuDose",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Text(
                text = if (isArabic) "حسـاب جـرعـات أدوية الأطفال بدقة سلفية متناهية" else "Accurate clinical calibration for pediatric practitioners",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF38BDF8),
                fontFamily = CairoFontFamily
            )
        }
    }
}

@Composable
fun CategoryCardItem(
    isArabic: Boolean,
    category: MedicalCategory,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicator Badge details
                Box(
                    modifier = Modifier
                        .background(category.colorAccent.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isArabic) category.badgeTextAr else category.badgeTextEn,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily,
                        color = category.colorAccent
                    )
                }

                Text(
                    text = category.emoji,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (isArabic) category.titleAr else category.titleEn,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                fontFamily = CairoFontFamily,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isArabic) category.subtitleAr else category.subtitleEn,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CairoFontFamily,
                color = Color.Gray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun AboutDeveloperBannerCard(
    isArabic: Boolean,
    onClick: () -> Unit
) {
    val cardBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
    )

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBrush)
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isArabic) "بطاقة المطور" else "Developer Profile",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = CairoFontFamily,
                            color = Color(0xFF38BDF8)
                        )
                    }

                    Text(
                        text = "👨‍💻",
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isArabic) "حـول مطـوّر البرنامج" else "About the Application Developer",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontFamily = CairoFontFamily
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isArabic) 
                        "اضغط هنا للانتقال إلى معلومات مبرمج التطبيق وقنوات التواصل الرسمية" 
                        else "Click to view programmer cards, code credentials, and direct socials",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
