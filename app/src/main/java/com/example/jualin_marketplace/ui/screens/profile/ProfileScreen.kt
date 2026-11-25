package com.example.jualin_marketplace.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jualin_marketplace.data.model.User
import com.example.jualin_marketplace.ui.components.JualinTopAppBar
import com.example.jualin_marketplace.ui.theme.AccentOrange
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray

@Composable
fun ProfileScreen(
    user: User,
    myProductsCount: Int,
    favoriteCount: Int,
    transactionCount: Int,
    onEditProfileClick: () -> Unit,
    onMyProductsClick: () -> Unit,
    onFavoriteClick: () -> Unit = {},
    onTransactionHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar dari akun Jualin Marketplace?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Keluar", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = TextSecondaryGray)
                }
            }
        )
    }

    // App Settings & Info Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = null, tint = EmeraldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pengaturan & Aplikasi")
                }
            },
            text = {
                Column {
                    Text("Aplikasi: Jualin - Marketplace", fontWeight = FontWeight.Bold)
                    Text("Versi: 1.0.0 (Release Ready)")
                    Text("Tagline: Barang Bekas, Harga Pas!")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Pengaturan Notifikasi: Aktif", fontSize = 13.sp, color = TextSecondaryGray)
                    Text("Mode Tampilan: Emerald Light Theme", fontSize = 13.sp, color = TextSecondaryGray)
                    Text("Penyimpanan: Data Local SharedPreferences", fontSize = 13.sp, color = TextSecondaryGray)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSettingsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Tutup", color = Color.White)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        JualinTopAppBar(title = "Profil Saya")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Profile Card Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLightGray)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(86.dp)) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = user.username,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(LightGreenSecondary)
                        )

                        IconButton(
                            onClick = onEditProfileClick,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(30.dp)
                                .background(EmeraldPrimary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user.username,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )

                    Text(
                        text = user.email,
                        fontSize = 13.sp,
                        color = TextSecondaryGray
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${user.location} • Bergabung ${user.joinedDate}",
                            fontSize = 12.sp,
                            color = TextSecondaryGray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rating Badge
                    Box(
                        modifier = Modifier
                            .background(LightGreenSecondary, RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = AccentOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${user.rating} (Penjual Terpercaya)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStatCard(
                    title = "Barang Saya",
                    count = myProductsCount.toString(),
                    icon = Icons.Filled.ShoppingBag,
                    onClick = onMyProductsClick,
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    title = "Favorit",
                    count = favoriteCount.toString(),
                    icon = Icons.Filled.Favorite,
                    onClick = onFavoriteClick,
                    modifier = Modifier.weight(1f)
                )
                ProfileStatCard(
                    title = "Transaksi",
                    count = transactionCount.toString(),
                    icon = Icons.Filled.History,
                    onClick = onTransactionHistoryClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Menu Options
            Text(
                text = "Menu Utama",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Filled.Edit,
                        title = "Edit Profil",
                        subtitle = "Ubah nama, email, nomor HP & lokasi",
                        onClick = onEditProfileClick
                    )
                    HorizontalDivider(color = SurfaceLightGray, thickness = 1.dp)

                    ProfileMenuItem(
                        icon = Icons.Filled.ShoppingBag,
                        title = "Barang Saya",
                        subtitle = "Daftar barang bekas yang Anda jual",
                        onClick = onMyProductsClick
                    )
                    HorizontalDivider(color = SurfaceLightGray, thickness = 1.dp)

                    ProfileMenuItem(
                        icon = Icons.Filled.History,
                        title = "Riwayat Transaksi",
                        subtitle = "Daftar pembelian & penjualan selesai",
                        onClick = onTransactionHistoryClick
                    )
                    HorizontalDivider(color = SurfaceLightGray, thickness = 1.dp)

                    ProfileMenuItem(
                        icon = Icons.Filled.Settings,
                        title = "Pengaturan Aplikasi",
                        subtitle = "Info versi, notifikasi & ketentuan",
                        onClick = { showSettingsDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Logout Button
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Keluar Akun (Logout)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    count: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = EmeraldPrimary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimaryDark
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = TextSecondaryGray
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(LightGreenSecondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = EmeraldPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextSecondaryGray
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = TextSecondaryGray,
            modifier = Modifier.size(14.dp)
        )
    }
}
