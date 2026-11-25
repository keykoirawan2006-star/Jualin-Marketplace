package com.example.jualin_marketplace.ui.screens.detail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jualin_marketplace.data.model.Product
import com.example.jualin_marketplace.ui.theme.AccentOrange
import com.example.jualin_marketplace.ui.theme.ConditionFairColor
import com.example.jualin_marketplace.ui.theme.ConditionGoodColor
import com.example.jualin_marketplace.ui.theme.ConditionNewColor
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray

@Composable
fun ProductDetailScreen(
    product: Product?,
    onBackClick: () -> Unit,
    onFavoriteToggle: (Product) -> Unit,
    onChatClick: (Product) -> Unit,
    onBuyNowClick: (Product) -> Unit
) {
    val context = LocalContext.current
    var showBuyDialog by remember { mutableStateOf(false) }
    var buySuccessDialog by remember { mutableStateOf(false) }

    if (product == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Produk tidak ditemukan")
        }
        return
    }

    val scrollState = rememberScrollState()

    // Confirmation Purchase Dialog
    if (showBuyDialog) {
        AlertDialog(
            onDismissRequest = { showBuyDialog = false },
            title = {
                Text("Konfirmasi Pembelian", fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Apakah Anda yakin ingin membeli barang bekas ini?")
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceLightGray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(product.formattedPrice, fontWeight = FontWeight.Bold, color = EmeraldPrimary, fontSize = 16.sp)
                            Text("Penjual: ${product.sellerName}", fontSize = 12.sp, color = TextSecondaryGray)
                            Text("Metode Pembayaran: COD / Transfer", fontSize = 12.sp, color = TextSecondaryGray)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBuyDialog = false
                        onBuyNowClick(product)
                        buySuccessDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Beli Sekarang", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBuyDialog = false }) {
                    Text("Batal", color = TextSecondaryGray)
                }
            }
        )
    }

    // Success Purchase Dialog
    if (buySuccessDialog) {
        AlertDialog(
            onDismissRequest = { buySuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Transaksi Berhasil!")
                }
            },
            text = {
                Text("Selamat! Pesanan Anda telah dibuat. Riwayat transaksi telah disimpan di menu Profil Anda.")
            },
            confirmButton = {
                Button(
                    onClick = { buySuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Selesai", color = Color.White)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(bottom = 90.dp) // Space for bottom action bar
        ) {
            // Product Image Header with Back & Share Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Floating Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimaryDark
                        )
                    }

                    Row {
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Link barang disalin ke clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Share",
                                tint = TextPrimaryDark
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { onFavoriteToggle(product) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (product.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (product.isFavorite) Color(0xFFEF4444) else TextSecondaryGray
                            )
                        }
                    }
                }
            }

            // Product Details Content
            Column(modifier = Modifier.padding(20.dp)) {
                // Price & Condition Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.formattedPrice,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldPrimary
                    )

                    val (badgeBg, badgeText) = when (product.condition) {
                        "Bekas - Seperti Baru" -> ConditionNewColor to "Seperti Baru"
                        "Bekas - Bagus" -> ConditionGoodColor to "Kondisi Bagus"
                        else -> ConditionFairColor to "Kondisi Cukup"
                    }

                    Box(
                        modifier = Modifier
                            .background(badgeBg.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeBg,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = TextSecondaryGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.location} • Diunggah ${product.createdAt}",
                        fontSize = 13.sp,
                        color = TextSecondaryGray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = SurfaceLightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(20.dp))

                // Seller Info Section
                Text(
                    text = "Informasi Penjual",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(product.sellerAvatar)
                                .crossfade(true)
                                .build(),
                            contentDescription = product.sellerName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = product.sellerName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Rating",
                                    tint = AccentOrange,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "4.9 Penjual Terpercaya • ${product.location}",
                                    fontSize = 12.sp,
                                    color = TextSecondaryGray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = SurfaceLightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(20.dp))

                // Description Section
                Text(
                    text = "Deskripsi Produk",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = TextPrimaryDark,
                    lineHeight = 22.sp
                )
            }
        }

        // Bottom Fixed Action Bar (Chat & Beli Sekarang)
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chat Button
                OutlinedButton(
                    onClick = { onChatClick(product) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, EmeraldPrimary)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "Chat",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Chat Penjual",
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        fontSize = 14.sp
                    )
                }

                // Buy Now Button
                Button(
                    onClick = { onBuyNowClick(product) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingBag,
                        contentDescription = "Buy",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Beli Sekarang",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
