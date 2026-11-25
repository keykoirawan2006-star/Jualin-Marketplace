package com.example.jualin_marketplace.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: String,
    val name: String,
    val iconName: String,
    val description: String = ""
)

val defaultCategories = listOf(
    Category("cat_1", "Elektronik", "Devices", "Smartphone, Laptop, TV & Aksesoris"),
    Category("cat_2", "Fashion", "Checkroom", "Pakaian, Sepatu, Jam & Aksesoris"),
    Category("cat_3", "Furniture", "Chair", "Meja, Kursi, Lemari & Perabotan"),
    Category("cat_4", "Kendaraan", "DirectionsCar", "Motor, Mobil & Aksesoris"),
    Category("cat_5", "Hobi & Koleksi", "SportsEsports", "Mainan, Musik, Olahraga & Game"),
    Category("cat_6", "Peralatan Rumah", "HomeWork", "Dapur, Kebersihan & Rumah"),
    Category("cat_7", "Buku & Alat Tulis", "MenuBook", "Buku, Novel, Alat Tulis & Kantoran"),
    Category("cat_8", "Lainnya", "Category", "Barang bekas kategori lainnya")
)
