package com.example.jualin_marketplace.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jualin_marketplace.ui.theme.AccentOrange
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.EmeraldPrimaryDark

data class PromoBanner(
    val title: String,
    val subtitle: String,
    val badge: String,
    val gradientColors: List<Color>,
    val icon: ImageVector
)

val defaultBanners = listOf(
    PromoBanner(
        title = "Promo Gajian Jualin!",
        subtitle = "Cek barang impian harga nego terbaik",
        badge = "PROMO",
        gradientColors = listOf(EmeraldPrimary, EmeraldPrimaryDark),
        icon = Icons.Filled.LocalOffer
    ),
    PromoBanner(
        title = "Jual Cepat Cuan Instan!",
        subtitle = "Pasang iklan gratis tanpa biaya komisi",
        badge = "GRATIS",
        gradientColors = listOf(Color(0xFF2563EB), Color(0xFF1E40AF)),
        icon = Icons.Filled.VerifiedUser
    ),
    PromoBanner(
        title = "Garansi Aman COD",
        subtitle = "Transaksi tatap muka langsung cek kondisi",
        badge = "JAMINAN",
        gradientColors = listOf(AccentOrange, Color(0xFFD97706)),
        icon = Icons.Filled.VerifiedUser
    )
)

@Composable
fun BannerCarousel(
    banners: List<PromoBanner> = defaultBanners,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(banners) { banner ->
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(130.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.horizontalGradient(banner.gradientColors))
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.CenterStart),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = banner.badge,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = banner.title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = banner.subtitle,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = banner.icon,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(top = 20.dp)
                            .fillMaxSize(0.6f)
                    )
                }
            }
        }
    }
}
