package com.example.jualin_marketplace.ui.screens.profile

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jualin_marketplace.data.model.TransactionItem
import com.example.jualin_marketplace.ui.components.EmptyStateView
import com.example.jualin_marketplace.ui.components.JualinTopAppBar
import com.example.jualin_marketplace.ui.theme.BorderGray
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray

@Composable
fun TransactionHistoryScreen(
    transactions: List<TransactionItem>,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        JualinTopAppBar(
            title = "Riwayat Transaksi",
            onBackClick = onBackClick
        )

        if (transactions.isEmpty()) {
            EmptyStateView(
                icon = Icons.Filled.History,
                title = "Belum Ada Transaksi",
                message = "Seluruh pembelian barang bekas Anda yang berhasil akan dicatat dan tampil di sini."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transactions, key = { it.id }) { tx ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLightGray)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = tx.invoiceNumber.ifBlank { "INV/JUALIN/HASIL" },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark
                                    )
                                    Text(
                                        text = tx.date,
                                        fontSize = 11.sp,
                                        color = TextSecondaryGray
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(LightGreenSecondary, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = tx.status,
                                        color = EmeraldPrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(tx.productImage)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = tx.productName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.productName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryDark,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = tx.priceFormatted,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Penjual: ${tx.sellerName}",
                                        fontSize = 11.sp,
                                        color = TextSecondaryGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = BorderGray)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Metode: ${tx.paymentMethod}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimaryDark
                                )

                                if (tx.userAccountNumber.isNotBlank()) {
                                    Text(
                                        text = "Pengirim: ${tx.userAccountNumber}",
                                        fontSize = 11.sp,
                                        color = TextSecondaryGray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
