package com.example.jualin_marketplace.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.jualin_marketplace.data.model.Product
import com.example.jualin_marketplace.data.model.TransactionItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("jualin_transactions_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TRANSACTIONS = "transactions_json"
    }

    private val _transactions = MutableStateFlow<List<TransactionItem>>(emptyList())
    val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        val jsonStr = prefs.getString(KEY_TRANSACTIONS, null)
        if (jsonStr == null) {
            val initial = listOf(
                TransactionItem(
                    id = "tr_1",
                    invoiceNumber = "INV/JUALIN/20240115/88123",
                    productId = "prod_9",
                    productName = "Gitar Akustik Yamaha F310 Original",
                    productImage = "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=400",
                    priceFormatted = "Rp 852.000",
                    sellerName = "Eko Prasetyo",
                    paymentMethod = "Transfer Bank - BCA",
                    userAccountNumber = "0812345678",
                    date = "15 Jan 2024",
                    status = "Selesai"
                ),
                TransactionItem(
                    id = "tr_2",
                    invoiceNumber = "INV/JUALIN/20240202/99412",
                    productId = "prod_4",
                    productName = "Sepatu Nike Air Jordan 1 Low Chicago",
                    productImage = "https://images.unsplash.com/photo-1552346154-21d32810aba3?w=400",
                    priceFormatted = "Rp 1.452.000",
                    sellerName = "Kevin Sanjaya",
                    paymentMethod = "E-Wallet - DANA",
                    userAccountNumber = "08198765432",
                    date = "02 Feb 2024",
                    status = "Selesai"
                )
            )
            saveTransactions(initial)
            _transactions.value = initial
        } else {
            val list = mutableListOf<TransactionItem>()
            try {
                val array = JSONArray(jsonStr)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        TransactionItem(
                            id = obj.optString("id", "tr_$i"),
                            invoiceNumber = obj.optString("invoiceNumber", "INV/JUALIN/$i"),
                            productId = obj.optString("productId", ""),
                            productName = obj.optString("productName", "Barang Bekas"),
                            productImage = obj.optString("productImage", ""),
                            priceFormatted = obj.optString("priceFormatted", "Rp 0"),
                            sellerName = obj.optString("sellerName", "Penjual"),
                            paymentMethod = obj.optString("paymentMethod", "COD"),
                            userAccountNumber = obj.optString("userAccountNumber", "-"),
                            date = obj.optString("date", "Hari ini"),
                            status = obj.optString("status", "Selesai")
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _transactions.value = list
        }
    }

    fun buyProduct(
        product: Product,
        paymentMethod: String = "COD",
        userAccountNumber: String = "",
        formattedTotal: String = ""
    ): TransactionItem {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val randomInvNumber = "INV/JUALIN/${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}/${(10000..99999).random()}"

        val newTransaction = TransactionItem(
            id = "tr_${System.currentTimeMillis()}",
            invoiceNumber = randomInvNumber,
            productId = product.id,
            productName = product.name,
            productImage = product.imageUrl,
            priceFormatted = if (formattedTotal.isNotBlank()) formattedTotal else product.formattedPrice,
            sellerName = product.sellerName,
            paymentMethod = paymentMethod,
            userAccountNumber = userAccountNumber.ifBlank { "081234567890" },
            date = currentDate,
            status = "Selesai"
        )

        val updatedList = listOf(newTransaction) + _transactions.value
        saveTransactions(updatedList)
        _transactions.value = updatedList
        return newTransaction
    }

    private fun saveTransactions(list: List<TransactionItem>) {
        val array = JSONArray()
        list.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("invoiceNumber", item.invoiceNumber)
                put("productId", item.productId)
                put("productName", item.productName)
                put("productImage", item.productImage)
                put("priceFormatted", item.priceFormatted)
                put("sellerName", item.sellerName)
                put("paymentMethod", item.paymentMethod)
                put("userAccountNumber", item.userAccountNumber)
                put("date", item.date)
                put("status", item.status)
            }
            array.put(obj)
        }
        prefs.edit().putString(KEY_TRANSACTIONS, array.toString()).apply()
    }
}
