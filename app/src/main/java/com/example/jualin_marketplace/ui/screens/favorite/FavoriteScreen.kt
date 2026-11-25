package com.example.jualin_marketplace.ui.screens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jualin_marketplace.data.model.Product
import com.example.jualin_marketplace.ui.components.EmptyStateView
import com.example.jualin_marketplace.ui.components.JualinTopAppBar
import com.example.jualin_marketplace.ui.components.ProductCard
import com.example.jualin_marketplace.ui.components.SectionHeader

@Composable
fun FavoriteScreen(
    favoriteProducts: List<Product>,
    onProductClick: (Product) -> Unit,
    onFavoriteClick: (Product) -> Unit,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        JualinTopAppBar(title = "Favorit Saya")

        if (favoriteProducts.isEmpty()) {
            EmptyStateView(
                icon = Icons.Filled.FavoriteBorder,
                title = "Belum Ada Favorit",
                message = "Simpan barang bekas yang Anda sukai dengan menekan ikon hati di produk.",
                buttonText = "Cari Barang Bekas",
                onButtonClick = onExploreClick
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                SectionHeader(
                    title = "Barang Disukai (${favoriteProducts.size})",
                    subtitle = "Daftar barang bekas impian Anda"
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteProducts, key = { it.id }) { product ->
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
}
