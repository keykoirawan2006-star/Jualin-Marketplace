package com.example.jualin_marketplace.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jualin_marketplace.data.model.Product
import com.example.jualin_marketplace.data.model.User
import com.example.jualin_marketplace.data.model.defaultCategories
import com.example.jualin_marketplace.ui.components.BannerCarousel
import com.example.jualin_marketplace.ui.components.CategoryFilterRow
import com.example.jualin_marketplace.ui.components.EmptyStateView
import com.example.jualin_marketplace.ui.components.ProductCard
import com.example.jualin_marketplace.ui.components.SectionHeader
import com.example.jualin_marketplace.ui.components.TopSearchBar
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf("Semua") } // "Semua", "Terbaru", "Rekomendasi"
    var showNotificationDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Filter products
    val filteredProducts = products.filter { product ->
        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true) ||
                product.location.contains(searchQuery, ignoreCase = true)

        val matchesCategory = selectedCategory == null || product.category.equals(selectedCategory, ignoreCase = true)

        val matchesTab = when (selectedTab) {
            "Terbaru" -> product.createdAt.contains("jam") || product.createdAt.contains("Baru") || product.isUserListing
            "Rekomendasi" -> product.condition.contains("Seperti Baru") || product.price > 1000000
            else -> true
        }

        matchesSearch && matchesCategory && matchesTab
    }

    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = EmeraldPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Notifikasi Jualin")
                }
            },
            text = {
                Column {
                    Text(
                        text = "• Promo Gajian: Diskon hingga 30% untuk barang bekas pilihan!",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Iklan Anda 'Laptop Asus ROG' mendapat 5 calon pembeli baru.",
                        color = TextSecondaryGray,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text("Tutup", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(LightGreenSecondary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingBag,
                        contentDescription = "Jualin Logo",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Jualin",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = " Marketplace",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimaryDark
                        )
                    }
                    Text(
                        text = "Halo, Selamat Datang, ${user.username.split(" ").firstOrNull() ?: "Pengguna"}! 👋",
                        fontSize = 12.sp,
                        color = TextSecondaryGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(onClick = { showNotificationDialog = true }) {
                BadgedBox(
                    badge = {
                        Badge(containerColor = Color(0xFFEF4444)) {
                            Text("2", color = Color.White)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        tint = TextPrimaryDark,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Main List Content
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar & Promo Banners
            item(span = { GridItemSpan(2) }) {
                Column {
                    TopSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    BannerCarousel(modifier = Modifier.padding(bottom = 16.dp))

                    SectionHeader(
                        title = "Kategori Produk",
                        subtitle = "Cari berdasarkan kategori barang"
                    )

                    CategoryFilterRow(
                        categories = defaultCategories,
                        selectedCategory = selectedCategory,
                        onSelectCategory = { selectedCategory = it },
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Tab Selection (Semua, Terbaru, Rekomendasi)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Semua", "Terbaru", "Rekomendasi").forEach { tab ->
                            val isSelected = selectedTab == tab
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedTab = tab },
                                label = {
                                    Text(
                                        text = tab,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LightGreenSecondary,
                                    selectedLabelColor = EmeraldPrimary,
                                    containerColor = SurfaceLightGray,
                                    labelColor = TextPrimaryDark
                                ),
                                border = null,
                                shape = RoundedCornerShape(20.dp)
                            )
                        }
                    }

                    SectionHeader(
                        title = when {
                            selectedCategory != null -> "Kategori: $selectedCategory"
                            searchQuery.isNotEmpty() -> "Hasil Pencarian"
                            else -> "Daftar Produk Bekas"
                        },
                        subtitle = "Menampilkan ${filteredProducts.size} barang"
                    )
                }
            }

            // Empty state if filtered products list is empty
            if (filteredProducts.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    EmptyStateView(
                        icon = Icons.Filled.SearchOff,
                        title = "Produk Tidak Ditemukan",
                        message = "Coba gunakan kata kunci pencarian lain atau ganti filter kategori.",
                        buttonText = "Reset Filter",
                        onButtonClick = {
                            searchQuery = ""
                            selectedCategory = null
                            selectedTab = "Semua"
                        },
                        modifier = Modifier.padding(vertical = 40.dp)
                    )
                }
            } else {
                items(filteredProducts, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        onProductClick = onProductClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }
        }
    }
}
