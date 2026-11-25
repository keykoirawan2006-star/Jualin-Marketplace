package com.example.jualin_marketplace.data.model

data class TransactionItem(
    val id: String,
    val invoiceNumber: String = "INV/JUALIN/88392",
    val productId: String,
    val productName: String,
    val productImage: String,
    val priceFormatted: String,
    val sellerName: String,
    val paymentMethod: String = "COD",
    val userAccountNumber: String = "",
    val date: String,
    val status: String // "Selesai", "Diproses", "Dibatalkan"
)
