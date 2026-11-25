package com.example.jualin_marketplace.ui.screens.sell

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jualin_marketplace.data.model.Product
import com.example.jualin_marketplace.data.model.User
import com.example.jualin_marketplace.data.model.defaultCategories
import com.example.jualin_marketplace.ui.components.JualinTopAppBar
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray
import java.io.File
import java.io.FileOutputStream

fun copyUriToLocalFile(context: Context, uri: Uri): String {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return uri.toString()
        val file = File(context.filesDir, "upload_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        uri.toString()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SellProductScreen(
    currentUser: User,
    onProductSubmitted: (Product) -> Unit,
    onNavigateHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var productName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Elektronik") }
    var priceText by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf("Bekas - Bagus") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf(currentUser.location) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    val conditions = listOf("Bekas - Seperti Baru", "Bekas - Bagus", "Bekas - Cukup")

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onNavigateHome()
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iklan Berhasil Dipasang!")
                }
            },
            text = {
                Text("Barang bekas Anda '$productName' sekarang sudah aktif dan tampil di Beranda.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        productName = ""
                        priceText = ""
                        description = ""
                        selectedImageUri = null
                        onNavigateHome()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Lihat di Beranda", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        JualinTopAppBar(title = "Jual Barang Bekas")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            Text(
                text = "Foto Produk",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Upload foto barang bekas Anda dari galeri HP",
                fontSize = 12.sp,
                color = TextSecondaryGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Photo Upload Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceLightGray)
                    .border(
                        width = 1.dp,
                        color = EmeraldPrimary.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Uploaded Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(LightGreenSecondary, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddAPhoto,
                                contentDescription = "Add Photo",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "+ Pilih Foto dari Galeri HP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                        Text(
                            text = "Format PNG/JPG (Maks 5MB)",
                            fontSize = 11.sp,
                            color = TextSecondaryGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Product Name
            OutlinedTextField(
                value = productName,
                onValueChange = {
                    productName = it
                    errorMessage = null
                },
                label = { Text("Nama Barang Bekas") },
                placeholder = { Text("Contoh: Laptop Asus ROG Strix / Sepatu Nike") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ShoppingBag,
                        contentDescription = null,
                        tint = EmeraldPrimary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection
            Text(
                text = "Kategori Produk",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                defaultCategories.forEach { cat ->
                    val isSelected = selectedCategory == cat.name
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat.name },
                        label = {
                            Text(
                                text = cat.name,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = SurfaceLightGray,
                            labelColor = TextPrimaryDark
                        ),
                        border = null,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price Field
            OutlinedTextField(
                value = priceText,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        priceText = it
                        errorMessage = null
                    }
                },
                label = { Text("Harga Produk (Rp)") },
                placeholder = { Text("Contoh: 1500000") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AttachMoney,
                        contentDescription = null,
                        tint = EmeraldPrimary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Condition Selection
            Text(
                text = "Kondisi Barang",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                conditions.forEach { cond ->
                    val isSelected = selectedCondition == cond
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCondition = cond },
                        label = {
                            Text(
                                text = cond,
                                fontSize = 11.sp,
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
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Lokasi Penjual") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = EmeraldPrimary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Field
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi Lengkap Barang") },
                placeholder = { Text("Jelaskan kondisi, alasan dijual, kelengkapan box, garansi dll...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Description,
                        contentDescription = null,
                        tint = EmeraldPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    val priceVal = priceText.toDoubleOrNull() ?: 0.0

                    if (productName.isBlank()) {
                        errorMessage = "Nama barang tidak boleh kosong"
                        Toast.makeText(context, "Nama barang wajib diisi!", Toast.LENGTH_SHORT).show()
                    } else if (priceText.isBlank() || priceVal <= 0) {
                        errorMessage = "Harga barang harus diisi dengan benar"
                        Toast.makeText(context, "Harga barang wajib diisi!", Toast.LENGTH_SHORT).show()
                    } else if (description.isBlank()) {
                        errorMessage = "Deskripsi barang wajib diisi"
                        Toast.makeText(context, "Deskripsi barang wajib diisi!", Toast.LENGTH_SHORT).show()
                    } else {
                        val imagePath = if (selectedImageUri != null) {
                            copyUriToLocalFile(context, selectedImageUri!!)
                        } else {
                            "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800"
                        }

                        val newProduct = Product(
                            id = "user_prod_${System.currentTimeMillis()}",
                            name = productName,
                            category = selectedCategory,
                            price = priceVal,
                            location = location.ifBlank { "Jakarta" },
                            condition = selectedCondition,
                            description = description,
                            imageUrl = imagePath,
                            sellerId = currentUser.id,
                            sellerName = currentUser.username,
                            sellerAvatar = currentUser.avatarUrl,
                            isUserListing = true,
                            createdAt = "Baru saja"
                        )

                        onProductSubmitted(newProduct)
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text(
                    text = "Pasang Iklan Sekarang",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
