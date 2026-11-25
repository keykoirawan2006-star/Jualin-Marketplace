package com.example.jualin_marketplace.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.jualin_marketplace.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class ProductRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("jualin_products_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FAVORITES = "favorite_ids"
        private const val KEY_USER_PRODUCTS = "user_products_json"
    }

    private val initialProducts = listOf(
        Product(
            id = "prod_1",
            name = "iPhone 13 128GB Midnight Mulus fullset",
            category = "Elektronik",
            price = 7500000.0,
            location = "Jakarta Selatan",
            condition = "Bekas - Seperti Baru",
            description = "Dijual iPhone 13 128GB warna Midnight. Pemakaian pribadi baru 6 bulan, Battery Health 92%. Garansi resmi iBox Indonesia. Fullset box original dan kabel data original. Tidak ada lecet sama sekali.",
            imageUrl = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=800",
            sellerId = "s_1",
            sellerName = "Rian Hidayat",
            sellerAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            createdAt = "2 jam yang lalu"
        ),
        Product(
            id = "prod_2",
            name = "Laptop Asus ROG Strix G15 i7 Gen 11 RTX 3060",
            category = "Elektronik",
            price = 12800000.0,
            location = "Bandung",
            condition = "Bekas - Bagus",
            description = "Laptop gaming Asus ROG Strix G15. Spesifikasi Intel Core i7-11800H, RAM 16GB, SSD 512GB NVMe, GPU NVIDIA RTX 3060 6GB. Pemakaian kerja & gaming tipis-tipis. Kondisi mulus 95%, kelengkapan laptop & charger original.",
            imageUrl = "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=800",
            sellerId = "s_2",
            sellerName = "Andi Wijaya",
            sellerAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
            createdAt = "5 jam yang lalu"
        ),
        Product(
            id = "prod_3",
            name = "Jaket Kulit Asli Leather Jacket Brown Vintage",
            category = "Fashion",
            price = 350000.0,
            location = "Yogyakarta",
            condition = "Bekas - Seperti Baru",
            description = "Jaket kulit domba asli kualitas impor. Ukuran L, warna coklat tua vintage. Jahitan rapi, bahan tebal dan hangat. Dijual karena kekecilan.",
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800",
            sellerId = "s_3",
            sellerName = "Bagus Pratama",
            sellerAvatar = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=200",
            createdAt = "1 hari yang lalu"
        ),
        Product(
            id = "prod_4",
            name = "Sepatu Nike Air Jordan 1 Low Chicago Size 42",
            category = "Fashion",
            price = 1450000.0,
            location = "Jakarta Barat",
            condition = "Bekas - Bagus",
            description = "Nike AJ 1 Low Chicago OG Original. Size US 8.5 / EUR 42. Kondisi 9/10, insole masih bersih, outsole terawat. Box original lengkap.",
            imageUrl = "https://images.unsplash.com/photo-1552346154-21d32810aba3?w=800",
            sellerId = "s_4",
            sellerName = "Kevin Sanjaya",
            sellerAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=200",
            createdAt = "1 hari yang lalu"
        ),
        Product(
            id = "prod_5",
            name = "Sofa Minimalis Scandinavian 3 Seater Grey",
            category = "Furniture",
            price = 1850000.0,
            location = "Tangerang",
            condition = "Bekas - Seperti Baru",
            description = "Sofa tamu 3 dudukan gaya Scandinavian warna abu-abu. Busa empuk tidak kempes, kain kanvas tebal mudah dibersihkan. Kaki kayu jati kokoh.",
            imageUrl = "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800",
            sellerId = "s_5",
            sellerName = "Maya Kusuma",
            sellerAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200",
            createdAt = "2 hari yang lalu"
        ),
        Product(
            id = "prod_6",
            name = "Meja Belajar Kayu Jati Minimalis + Laci",
            category = "Furniture",
            price = 650000.0,
            location = "Surakarta",
            condition = "Bekas - Bagus",
            description = "Meja belajar & meja kerja kayu jati asli. Dimensi 120cm x 60cm. Ada 2 laci penyimpanan. Finishing melamin mulus.",
            imageUrl = "https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800",
            sellerId = "s_6",
            sellerName = "Dito Raharjo",
            sellerAvatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200",
            createdAt = "3 hari yang lalu"
        ),
        Product(
            id = "prod_7",
            name = "Vespa Primavera 150 i-Get 2018 Mulus Istimewa",
            category = "Kendaraan",
            price = 34500000.0,
            location = "Jakarta Selatan",
            condition = "Bekas - Seperti Baru",
            description = "Vespa Primavera 150 i-Get tahun 2018 warna Putih. KM 12.000 berjalan. Surat-surat lengkap STNK & BPKB atas nama pribadi. Pajak hidup.",
            imageUrl = "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?w=800",
            sellerId = "s_7",
            sellerName = "Satria Dimas",
            sellerAvatar = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=200",
            createdAt = "3 jam yang lalu"
        ),
        Product(
            id = "prod_8",
            name = "Honda Vario 125 CBS ISS 2021 Hitam Doff",
            category = "Kendaraan",
            price = 14800000.0,
            location = "Bogor",
            condition = "Bekas - Bagus",
            description = "Honda Vario 125 tahun 2021. Servis rutin bengkel resmi AHASS. Bodi halus, mesin halus tarikan mantap. Siap pakai jalan jauh.",
            imageUrl = "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?w=800",
            sellerId = "s_8",
            sellerName = "Fajar Nugraha",
            sellerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
            createdAt = "4 jam yang lalu"
        ),
        Product(
            id = "prod_9",
            name = "Gitar Akustik Yamaha F310 Original + Softcase",
            category = "Hobi & Koleksi",
            price = 850000.0,
            location = "Depok",
            condition = "Bekas - Seperti Baru",
            description = "Gitar Akustik Yamaha F310 Natural Original. Suara nyaring jernih, senar d'Addario baru diganti. Bonus tas gitar tebal dan pick.",
            imageUrl = "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=800",
            sellerId = "s_9",
            sellerName = "Eko Prasetyo",
            sellerAvatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200",
            createdAt = "1 hari yang lalu"
        ),
        Product(
            id = "prod_10",
            name = "Sony PlayStation 4 Slim 1TB + 2 Stik DualShock 4",
            category = "Hobi & Koleksi",
            price = 2650000.0,
            location = "Malang",
            condition = "Bekas - Bagus",
            description = "PS4 Slim 1TB Hen/FW terbaru aman. Dapet 2 stik wireless original, kabel HDMI, kabel power dan bonus 10 game pilihan terbaik di HDD internal.",
            imageUrl = "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=800",
            sellerId = "s_10",
            sellerName = "Genta Pratama",
            sellerAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200",
            createdAt = "2 hari yang lalu"
        ),
        Product(
            id = "prod_11",
            name = "Air Fryer Philips HD9200 Low Watt 4.1L",
            category = "Peralatan Rumah",
            price = 550000.0,
            location = "Bekasi",
            condition = "Bekas - Cukup",
            description = "Philips Digital Air Fryer. Kapasitas 4.1 Liter. Kondisi fungsi normal 100%, elemen panas jos. Ada sedikit lecet pemakaian di wadah luar.",
            imageUrl = "https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=800",
            sellerId = "s_11",
            sellerName = "Larasati",
            sellerAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
            createdAt = "3 hari yang lalu"
        ),
        Product(
            id = "prod_12",
            name = "Coffee Espresso Machine DeLonghi Dedica EC685",
            category = "Peralatan Rumah",
            price = 1950000.0,
            location = "Surabaya",
            condition = "Bekas - Seperti Baru",
            description = "Mesin kopi espresso DeLonghi Dedica EC685 Merah. Pressure 15 bar, steam wand kencang untuk latte art. Bonus porta filter dan tamper stainless.",
            imageUrl = "https://images.unsplash.com/photo-1517668808822-9ebe02f2a698?w=800",
            sellerId = "s_12",
            sellerName = "Hendra Setiawan",
            sellerAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
            createdAt = "4 hari yang lalu"
        ),
        Product(
            id = "prod_13",
            name = "Set Novel Harry Potter 1-7 Bahasa Indonesia Lengkap",
            category = "Buku & Alat Tulis",
            price = 420000.0,
            location = "Bandung",
            condition = "Bekas - Bagus",
            description = "Koleksi lengkap Novel Harry Potter buku ke-1 sampai ke-7 terjemahan Gramedia. Kertas bersih tidak ada lipatan tajam, cocok untuk kolektor.",
            imageUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=800",
            sellerId = "s_13",
            sellerName = "Nadia Safira",
            sellerAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200",
            createdAt = "5 hari yang lalu"
        ),
        Product(
            id = "prod_14",
            name = "Kamera Mirrorless Sony A6400 Body Only Mulus",
            category = "Elektronik",
            price = 9200000.0,
            location = "Jakarta Pusat",
            condition = "Bekas - Seperti Baru",
            description = "Sony Alpha 6400 4K Vlogging Camera. Shutter Count rendah 3.200-an. Sensor bersih jernih. Kelengkapan box, strap, baterai ori, charger.",
            imageUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
            sellerId = "s_14",
            sellerName = "Rizal Firmansyah",
            sellerAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            createdAt = "6 jam yang lalu"
        )
    )

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        val favorites = getFavoriteIds()
        val userProducts = getUserProducts()

        val combinedList = mutableListOf<Product>()
        combinedList.addAll(userProducts)
        combinedList.addAll(initialProducts.filter { initProd -> userProducts.none { it.id == initProd.id } })

        val updatedList = combinedList.map { product ->
            product.copy(isFavorite = favorites.contains(product.id))
        }

        _products.value = updatedList
    }

    fun getFavoriteIds(): Set<String> {
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }

    fun toggleFavorite(productId: String) {
        val currentFavorites = getFavoriteIds().toMutableSet()
        if (currentFavorites.contains(productId)) {
            currentFavorites.remove(productId)
        } else {
            currentFavorites.add(productId)
        }
        prefs.edit().putStringSet(KEY_FAVORITES, currentFavorites).apply()
        loadProducts()
    }

    fun addProduct(newProduct: Product) {
        val userProducts = getUserProducts().toMutableList()
        userProducts.add(0, newProduct.copy(isUserListing = true))
        saveUserProducts(userProducts)
        loadProducts()
    }

    fun deleteProduct(productId: String) {
        val userProducts = getUserProducts().filter { it.id != productId }
        saveUserProducts(userProducts)
        loadProducts()
    }

    private fun getUserProducts(): List<Product> {
        val jsonStr = prefs.getString(KEY_USER_PRODUCTS, "[]") ?: "[]"
        val list = mutableListOf<Product>()
        try {
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    Product(
                        id = obj.optString("id", "user_prod_$i"),
                        name = obj.optString("name", "Barang Bekas"),
                        category = obj.optString("category", "Lainnya"),
                        price = obj.optDouble("price", 0.0),
                        location = obj.optString("location", "Indonesia"),
                        condition = obj.optString("condition", "Bekas - Bagus"),
                        description = obj.optString("description", "-"),
                        imageUrl = obj.optString("imageUrl", "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800"),
                        sellerId = obj.optString("sellerId", "user_me"),
                        sellerName = obj.optString("sellerName", "Budi Santoso"),
                        sellerAvatar = obj.optString("sellerAvatar", ""),
                        isUserListing = true,
                        createdAt = obj.optString("createdAt", "Baru saja")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun saveUserProducts(products: List<Product>) {
        val jsonArray = JSONArray()
        products.forEach { p ->
            val obj = JSONObject().apply {
                put("id", p.id)
                put("name", p.name)
                put("category", p.category)
                put("price", p.price)
                put("location", p.location)
                put("condition", p.condition)
                put("description", p.description)
                put("imageUrl", p.imageUrl)
                put("sellerId", p.sellerId)
                put("sellerName", p.sellerName)
                put("sellerAvatar", p.sellerAvatar)
                put("createdAt", p.createdAt)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_USER_PRODUCTS, jsonArray.toString()).apply()
    }
}
