package com.example.jualin_marketplace.data.model

import java.text.NumberFormat
import java.util.Locale

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val location: String,
    val condition: String, // "Bekas - Seperti Baru", "Bekas - Bagus", "Bekas - Cukup"
    val description: String,
    val imageUrl: String,
    val sellerId: String,
    val sellerName: String,
    val sellerAvatar: String,
    val isFavorite: Boolean = false,
    val isUserListing: Boolean = false,
    val createdAt: String = "Baru saja"
) {
    val formattedPrice: String
        get() {
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            format.maximumFractionDigits = 0
            return format.format(price)
        }
}
