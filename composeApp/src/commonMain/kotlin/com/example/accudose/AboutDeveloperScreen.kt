package com.example.accudose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutDeveloperScreen(
    isArabic: Boolean,
    onBack: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()

    val devNameAr = AppIntegrityEngine.getDeveloperNameAr()
    val devNameEn = AppIntegrityEngine.getDeveloperNameEn()
    val instagramUrl = AppIntegrityEngine.getInstagramUrl()
    val telegramUrl = AppIntegrityEngine.getTelegramUrl()
    val channelUrl = AppIntegrityEngine.getTelegramChannelUrl()

    val cardBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
    )

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
                    text = if (isArabic) "حـول مطـوّر البرنامج" else "Developer Credentials",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = CairoFontFamily,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (isArabic) "بطاقة المبرمج والمواثيق الرسمية للتطبيق" else "Programmer profile cards & community socials",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = CairoFontFamily,
                    color = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Main Developer Bio Card
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardBrush, RoundedCornerShape(26.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                            .border(BorderStroke(2.dp, Color(0xFF38BDF8)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👨‍💻",
                            fontSize = 44.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isArabic) devNameAr else devNameEn,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isArabic) "مطور تطبيقات الموبايل والحلول الطبية الذكية" else "Mobile Application & Clinicial Solutions Developer",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        fontFamily = CairoFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (isArabic)
                                "تطبيق AccuDose مصمم لتبسيط وتأمين حسابات الجرعات الطبية السريرية لجميع العاملين في ممارسة طب وصحة الأطفال ومجموعات العناية المركزة."
                                else
                                "AccuDose is custom-crafted to facilitate double-checks for clinical pediatric infusions and everyday calculations non-invasively.",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            fontFamily = CairoFontFamily,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Social channels connection rows
            Text(
                text = if (isArabic) "القنوات الرسمية ووسائل التواصل المعتمدة:" else "Official Channels & Direct Connections:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = CairoFontFamily,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Social Card 1: Telegram Channels
            SocialLinkItemCard(
                isArabic = isArabic,
                titleAr = "قناة التلجرام الرسمية",
                titleEn = "Official Telegram Channel",
                subtitleAr = "لمتابعة شروحات التطبيق والدروس والإصدارات الجديدة",
                subtitleEn = "Follow tutorials, release notes, and latest clinical builds",
                badgeText = "@Channel",
                badgeColor = Color(0xFF0EA5E9),
                icon = { TelegramLogo(color = Color.White) },
                onClick = { uriHandler.openUri(channelUrl) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Social Card 2: Telegram Contact
            SocialLinkItemCard(
                isArabic = isArabic,
                titleAr = "التواصل المباشر عبر تلجرام",
                titleEn = "Telegram Direct Support",
                subtitleAr = "للاستفسارات، الشكاوى، والاقتراحات البرمجية الطبية",
                subtitleEn = "Clinical feedback, application requests, and design help",
                badgeText = "@Developer",
                badgeColor = Color(0xFF0284C7),
                icon = { TelegramLogo(color = Color.White) },
                onClick = { uriHandler.openUri(telegramUrl) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Social Card 3: Instagram Details
            SocialLinkItemCard(
                isArabic = isArabic,
                titleAr = "المبرمج على إنستقرام",
                titleEn = "Instagram Developer Account",
                subtitleAr = "لمتابعة المستجدات والتواصل والتغطية المهنية للمطور",
                subtitleEn = "Professional highlights, stories, and social coverage",
                badgeText = "@Instagram",
                badgeColor = Color(0xFFE1306C),
                icon = { InstagramLogo(color = Color.White) },
                onClick = { uriHandler.openUri(instagramUrl) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Copyright notice and app medical disclaimer box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Disclaimer",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic)
                                "تنبيه إخلاء مسؤولية طبي:\n" +
                                "هذا البرنامج مخصص لمساعدة ودعم الكوادر الطبية لتأكيد الحسابات السريرية المعتمدة دوماً. لا يعوض بأي شكل عيادة الطبيب المعالج والتشخيص السليم الفعلي المناسب لحالة كل مريض."
                                else
                                "Clinical Dosage & Support Disclaimer:\n" +
                                "This software acts purely as an educational companion calculation checker for practitioners. Clinical judgment and actual direct professional physical examinations strictly proceed any drug administrations.",
                            fontSize = 9.sp,
                            color = Color.Gray,
                            fontFamily = CairoFontFamily,
                            lineHeight = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "AccuDose © 2026. Made with Precision.",
                        fontSize = 9.sp,
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SocialLinkItemCard(
    isArabic: Boolean,
    titleAr: String,
    titleEn: String,
    subtitleAr: String,
    subtitleEn: String,
    badgeText: String,
    badgeColor: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // High contrast circular icon background
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isArabic) titleAr else titleEn,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = CairoFontFamily,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .background(badgeColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = if (isArabic) subtitleAr else subtitleEn,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontFamily = CairoFontFamily,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
