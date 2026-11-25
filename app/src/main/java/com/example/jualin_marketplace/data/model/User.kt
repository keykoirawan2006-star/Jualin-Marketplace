package com.example.jualin_marketplace.data.model

data class User(
    val id: String = "u_default",
    val username: String = "Budi Santoso",
    val email: String = "budi@example.com",
    val phone: String = "081234567890",
    val location: String = "Jakarta Selatan",
    val avatarUrl: String = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200",
    val bio: String = "Penjual & Pembeli aktif di Jualin Marketplace",
    val rating: Double = 4.9,
    val joinedDate: String = "Januari 2024"
)
