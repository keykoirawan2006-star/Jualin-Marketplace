package com.example.jualin_marketplace.ui.screens.checkout

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jualin_marketplace.data.model.Product
import com.example.jualin_marketplace.data.model.TransactionItem
import com.example.jualin_marketplace.ui.components.JualinTopAppBar
import com.example.jualin_marketplace.ui.theme.BorderGray
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

data class BankOption(
    val id: String,
    val name: String,
    val vaNumber: String
)

val bankList = listOf(
    BankOption("bca", "BCA", "8830 8123 4567 8901"),
    BankOption("mandiri", "Mandiri", "8800 8123 4567 8901"),
    BankOption("bni", "BNI", "8811 8123 4567 8901"),
    BankOption("bri", "BRI", "8822 8123 4567 8901"),
    BankOption("seabank", "SeaBank", "8855 8123 4567 8901"),
    BankOption("jago", "Bank Jago", "8877 8123 4567 8901"),
    BankOption("danamon", "Danamon", "8899 8123 4567 8901")
)

data class EWalletOption(
    val id: String,
    val name: String,
    val number: String
)

val eWalletList = listOf(
    EWalletOption("gopay", "GoPay", "0812-3456-7890"),
    EWalletOption("ovo", "OVO", "0812-3456-7890"),
    EWalletOption("dana", "DANA", "0812-3456-7890"),
    EWalletOption("shopeepay", "ShopeePay", "0812-3456-7890")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckoutScreen(
    product: Product?,
    onBackClick: () -> Unit,
    onConfirmOrder: (Product, String, String, String) -> TransactionItem,
    onViewTransactionsClick: () -> Unit
) {
    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Produk tidak ditemukan")
        }
        return
    }

    var paymentCategory by remember { mutableStateOf("bank") }
    var selectedBank by remember { mutableStateOf(bankList.first()) }
    var selectedEWallet by remember { mutableStateOf(eWalletList.first()) }
    var selectedShippingMethod by remember { mutableStateOf("COD / Ketemuan") }
    var userAccountNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var createdInvoice by remember { mutableStateOf<TransactionItem?>(null) }

    // 30-Minute Live Countdown Timer (1800 seconds)
    var remainingSeconds by remember { mutableStateOf(1800) }

    LaunchedEffect(key1 = remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val formattedTimer = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val serviceFee = 2000.0
    val shippingFee = if (selectedShippingMethod == "Kurir Reguler (J&T/JNE)") 15000.0 else 0.0
    val totalAmount = product.price + serviceFee + shippingFee

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).apply {
        maximumFractionDigits = 0
    }

    val formattedTotal = currencyFormat.format(totalAmount)
    val formattedServiceFee = currencyFormat.format(serviceFee)
    val formattedShippingFee = currencyFormat.format(shippingFee)

    // Full Official Invoice Dialog Screen
    if (createdInvoice != null) {
        val invoice = createdInvoice!!

        AlertDialog(
            onDismissRequest = {
                createdInvoice = null
                onViewTransactionsClick()
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
                    Column {
                        Text("INVOICE TRANSAKSI", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(invoice.invoiceNumber, fontSize = 11.sp, color = TextSecondaryGray)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(LightGreenSecondary, RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("STATUS: HASIL TRANSAKSI LUNAS", color = EmeraldPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Waktu: ${invoice.date}", fontSize = 12.sp, color = TextSecondaryGray)

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderGray)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Product Details
                    Text("Rincian Barang Bekas:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(invoice.productImage)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(invoice.productName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Penjual: ${invoice.sellerName}", fontSize = 11.sp, color = TextSecondaryGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderGray)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Payment Method Details
                    Text("Rincian Metode Pembayaran:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Metode: ${invoice.paymentMethod}", fontSize = 12.sp)
                    Text("• No. Rekening/HP Pengirim: ${invoice.userAccountNumber}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderGray)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Financial Breakdown
                    Text("Rincian Pembayaran:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Harga Barang", fontSize = 12.sp, color = TextSecondaryGray)
                        Text(product.formattedPrice, fontSize = 12.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Biaya Layanan Jualin", fontSize = 12.sp, color = TextSecondaryGray)
                        Text(formattedServiceFee, fontSize = 12.sp)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Ongkos Kirim", fontSize = 12.sp, color = TextSecondaryGray)
                        Text(formattedShippingFee, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Dibayar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(invoice.priceFormatted, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
                    }
                }
            },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            Toast.makeText(context, "Invoice ${invoice.invoiceNumber} berhasil disimpan ke HP!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Filled.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cetak / Simpan Invoice PDF", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedButton(
                        onClick = {
                            createdInvoice = null
                            onViewTransactionsClick()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, EmeraldPrimary)
                    ) {
                        Icon(Icons.Filled.History, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lihat Riwayat Transaksi", color = EmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            JualinTopAppBar(title = "Pembayaran Transaksi", onBackClick = onBackClick)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .padding(bottom = 90.dp)
            ) {
                // Live 30-Minute Timer Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Timer, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Batas Waktu Pembayaran:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFD97706), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(formattedTimer, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Product Summary
                Text("Barang yang Dibeli", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimaryDark)
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(product.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimaryDark)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(product.formattedPrice, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = EmeraldPrimary)
                            Text("Penjual: ${product.sellerName} (${product.location})", fontSize = 11.sp, color = TextSecondaryGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Delivery Options
                Text("Metode Pengiriman / Serah Terima", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimaryDark)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("COD / Ketemuan", "Kurir Reguler (J&T/JNE)").forEach { method ->
                        val isSelected = selectedShippingMethod == method
                        val borderCol = if (isSelected) EmeraldPrimary else SurfaceLightGray
                        val bgCol = if (isSelected) LightGreenSecondary else SurfaceLightGray

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                                .clickable { selectedShippingMethod = method },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = bgCol)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Icon(
                                    imageVector = if (method.contains("COD")) Icons.Filled.Handshake else Icons.Filled.LocalShipping,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(method, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimaryDark)
                                Text(if (method.contains("COD")) "Gratis (Meetup)" else "Rp 15.000", fontSize = 11.sp, color = TextSecondaryGray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Structured Payment Categories Selector
                Text("Kategori Pembayaran", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimaryDark)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mapOf(
                        "bank" to "Transfer Bank",
                        "ewallet" to "E-Wallet",
                        "qris" to "QRIS Instan",
                        "cod" to "COD Tunai"
                    ).forEach { (id, label) ->
                        val isSelected = paymentCategory == id
                        FilterChip(
                            selected = isSelected,
                            onClick = { paymentCategory = id },
                            label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
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

                Spacer(modifier = Modifier.height(14.dp))

                // Detail Options Based on Selected Payment Category
                when (paymentCategory) {
                    "bank" -> {
                        Text("Pilih Bank Tujuan:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            bankList.forEach { bank ->
                                val isSelected = selectedBank.id == bank.id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedBank = bank },
                                    label = { Text(bank.name, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = LightGreenSecondary,
                                        selectedLabelColor = EmeraldPrimary,
                                        containerColor = SurfaceLightGray,
                                        labelColor = TextPrimaryDark
                                    ),
                                    border = if (isSelected) BorderStroke(1.dp, EmeraldPrimary) else null,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Bank VA Details Box
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = LightGreenSecondary.copy(alpha = 0.5f)),
                            border = BorderStroke(1.dp, EmeraldPrimary)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Virtual Account / No. Rekening ${selectedBank.name}:", fontSize = 12.sp, color = TextSecondaryGray)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedBank.vaNumber, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimaryDark, letterSpacing = 1.sp)
                                    OutlinedButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("VA", selectedBank.vaNumber.replace(" ", ""))
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Nomor VA ${selectedBank.name} berhasil disalin!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, EmeraldPrimary)
                                    ) {
                                        Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldPrimary)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Salin", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                    }
                                }
                                Text("a.n. PT Jualin Marketplace Indonesia", fontSize = 11.sp, color = TextSecondaryGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // MANDATORY Account Number Input
                        Text("Nomor Rekening Anda yang Mengirim (Wajib diisi):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = userAccountNumber,
                            onValueChange = {
                                userAccountNumber = it
                                errorMessage = null
                            },
                            placeholder = { Text("Contoh: 1234567890 (No. Rekening ${selectedBank.name} Anda)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = BorderGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }

                    "ewallet" -> {
                        Text("Pilih Provider E-Wallet:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            eWalletList.forEach { wallet ->
                                val isSelected = selectedEWallet.id == wallet.id
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedEWallet = wallet },
                                    label = { Text(wallet.name, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = LightGreenSecondary,
                                        selectedLabelColor = EmeraldPrimary,
                                        containerColor = SurfaceLightGray,
                                        labelColor = TextPrimaryDark
                                    ),
                                    border = if (isSelected) BorderStroke(1.dp, EmeraldPrimary) else null,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = LightGreenSecondary.copy(alpha = 0.5f)),
                            border = BorderStroke(1.dp, EmeraldPrimary)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Nomor HP E-Wallet Jualin (${selectedEWallet.name}):", fontSize = 12.sp, color = TextSecondaryGray)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedEWallet.number, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimaryDark)
                                    OutlinedButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("EWallet", selectedEWallet.number)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Nomor E-Wallet ${selectedEWallet.name} disalin!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, EmeraldPrimary)
                                    ) {
                                        Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldPrimary)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Salin", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                                    }
                                }
                                Text("a.n. Jualin Official Store", fontSize = 11.sp, color = TextSecondaryGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // MANDATORY E-Wallet Phone Number Input
                        Text("Nomor HP E-Wallet Anda (Wajib diisi):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = userAccountNumber,
                            onValueChange = {
                                userAccountNumber = it
                                errorMessage = null
                            },
                            placeholder = { Text("Contoh: 081234567890 (No. HP ${selectedEWallet.name} Anda)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = BorderGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }

                    "qris" -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, EmeraldPrimary)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Kode Barcode QRIS Nasional", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimaryDark)
                                Text("Bisa di-scan via GoPay, OVO, DANA, ShopeePay, BCA, Mandiri dll.", fontSize = 11.sp, color = TextSecondaryGray)

                                Spacer(modifier = Modifier.height(10.dp))

                                Box(
                                    modifier = Modifier
                                        .size(190.dp)
                                        .background(Color.White)
                                        .border(2.dp, EmeraldPrimary, RoundedCornerShape(16.dp))
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.QrCode2, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("QRIS NATIONAL", fontWeight = FontWeight.ExtraBold, fontSize = 10.sp, color = EmeraldPrimary)
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Icon(
                                            imageVector = Icons.Filled.QrCode2,
                                            contentDescription = "QR Code",
                                            tint = TextPrimaryDark,
                                            modifier = Modifier.size(100.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Merchant: Jualin Official", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondaryGray)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Kode Barcode QRIS berhasil disimpan ke Galeri HP!", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Filled.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Simpan Barcode QRIS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Nomor HP / ID Akun Pembayar (Wajib diisi):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = userAccountNumber,
                            onValueChange = {
                                userAccountNumber = it
                                errorMessage = null
                            },
                            placeholder = { Text("Contoh: 081234567890 (No. HP akun Anda saat scan QRIS)", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = BorderGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }

                    "cod" -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = LightGreenSecondary.copy(alpha = 0.5f))
                        ) {
                            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Handshake, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Ketemuan & Bayar di Tempat (COD)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                                    Text("Pembayaran tunai langsung dilakukan ke penjual ${product.sellerName} saat bertemu dan memeriksa barang.", fontSize = 11.sp, color = TextSecondaryGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Nomor HP Anda yang Bisa Dihubungi untuk COD (Wajib diisi):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimaryDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = userAccountNumber,
                            onValueChange = {
                                userAccountNumber = it
                                errorMessage = null
                            },
                            placeholder = { Text("Contoh: 081234567890", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = BorderGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(errorMessage!!, color = Color(0xFFDC2626), fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Payment Breakdown Summary
                Text("Rincian Pembayaran", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimaryDark)
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLightGray)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Harga Barang Bekas", fontSize = 13.sp, color = TextSecondaryGray)
                            Text(product.formattedPrice, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Biaya Layanan Jualin", fontSize = 13.sp, color = TextSecondaryGray)
                            Text(formattedServiceFee, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ongkos Kirim / Penanganan", fontSize = 13.sp, color = TextSecondaryGray)
                            Text(formattedShippingFee, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFE2E8F0))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Pembayaran", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                            Text(formattedTotal, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
                        }
                    }
                }
            }
        }

        // Bottom Bar Confirmation
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Pembayaran", fontSize = 11.sp, color = TextSecondaryGray)
                    Text(formattedTotal, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
                }

                Button(
                    onClick = {
                        if (userAccountNumber.isBlank()) {
                            errorMessage = "Nomor Rekening / No. HP Pengirim wajib diisi untuk verifikasi transaksi!"
                            Toast.makeText(context, "Nomor Rekening/HP Pengirim wajib diisi!", Toast.LENGTH_SHORT).show()
                        } else {
                            val methodFull = when (paymentCategory) {
                                "bank" -> "Transfer Bank - ${selectedBank.name}"
                                "ewallet" -> "E-Wallet - ${selectedEWallet.name}"
                                "qris" -> "QRIS Instan"
                                else -> "COD Tunai"
                            }

                            val newInvoice = onConfirmOrder(product, methodFull, userAccountNumber, formattedTotal)
                            createdInvoice = newInvoice
                        }
                    },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Konfirmasi & Bayar", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}
