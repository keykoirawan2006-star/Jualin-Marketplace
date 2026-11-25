package com.example.jualin_marketplace.data.repository

import com.example.jualin_marketplace.data.model.ChatMessage
import com.example.jualin_marketplace.data.model.Conversation
import com.example.jualin_marketplace.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRepository {

    private val initialConversations = listOf(
        Conversation(
            id = "conv_1",
            productId = "prod_1",
            productName = "iPhone 13 128GB Midnight",
            productImage = "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400",
            productPrice = "Rp 7.500.000",
            sellerId = "s_1",
            sellerName = "Rian Hidayat",
            sellerAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200",
            lastMessage = "Apakah barangnya masih ada mas?",
            lastMessageTime = "14:20",
            unreadCount = 1
        ),
        Conversation(
            id = "conv_2",
            productId = "prod_2",
            productName = "Laptop Asus ROG Strix G15",
            productImage = "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=400",
            productPrice = "Rp 12.800.000",
            sellerId = "s_2",
            sellerName = "Andi Wijaya",
            sellerAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
            lastMessage = "Bisa COD daerah Bandung mana mas?",
            lastMessageTime = "Kemarin",
            unreadCount = 0
        ),
        Conversation(
            id = "conv_3",
            productId = "prod_7",
            productName = "Vespa Primavera 150 i-Get 2018",
            productImage = "https://images.unsplash.com/photo-1568772585407-9361f9bf3a87?w=400",
            productPrice = "Rp 34.500.000",
            sellerId = "s_7",
            sellerName = "Satria Dimas",
            sellerAvatar = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=200",
            lastMessage = "Harga pasnya berapa kak?",
            lastMessageTime = "2 hari lalu",
            unreadCount = 0
        )
    )

    private val defaultMessagesMap = mapOf(
        "conv_1" to listOf(
            ChatMessage("m1", "conv_1", "s_1", "Halo kak, ada yang bisa dibantu?", "14:15", false),
            ChatMessage("m2", "conv_1", "user_me", "Apakah barangnya masih ada mas?", "14:20", true)
        ),
        "conv_2" to listOf(
            ChatMessage("m3", "conv_2", "s_2", "Siang kak, laptop Asus ROG masih mulus 100%.", "10:00", false),
            ChatMessage("m4", "conv_2", "user_me", "Bisa COD daerah Bandung mana mas?", "10:15", true),
            ChatMessage("m5", "conv_2", "s_2", "Bisa di sekitar Dago atau Dipatiukur kak.", "10:18", false)
        ),
        "conv_3" to listOf(
            ChatMessage("m6", "conv_3", "user_me", "Harga pasnya berapa kak?", "09:30", true),
            ChatMessage("m7", "conv_3", "s_7", "Net di 33.5jt gan, pajak baru diperpanjang.", "09:35", false)
        )
    )

    private val _conversations = MutableStateFlow<List<Conversation>>(initialConversations)
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private val _messagesMap = MutableStateFlow<Map<String, List<ChatMessage>>>(defaultMessagesMap)
    val messagesMap: StateFlow<Map<String, List<ChatMessage>>> = _messagesMap.asStateFlow()

    fun getMessages(conversationId: String): List<ChatMessage> {
        return _messagesMap.value[conversationId] ?: emptyList()
    }

    fun sendMessage(conversationId: String, text: String): ChatMessage {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        val newMessage = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            conversationId = conversationId,
            senderId = "user_me",
            messageText = text,
            timestamp = currentTime,
            isFromMe = true
        )

        val currentMap = _messagesMap.value.toMutableMap()
        val currentList = (currentMap[conversationId] ?: emptyList()).toMutableList()
        currentList.add(newMessage)
        currentMap[conversationId] = currentList
        _messagesMap.value = currentMap

        // Update conversation last message
        _conversations.value = _conversations.value.map { conv ->
            if (conv.id == conversationId) {
                conv.copy(lastMessage = text, lastMessageTime = currentTime, unreadCount = 0)
            } else {
                conv
            }
        }

        return newMessage
    }

    fun simulateSellerReply(conversationId: String, text: String): ChatMessage {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        val conversation = _conversations.value.find { it.id == conversationId }
        val sellerId = conversation?.sellerId ?: "seller"

        val reply = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            conversationId = conversationId,
            senderId = sellerId,
            messageText = text,
            timestamp = currentTime,
            isFromMe = false
        )

        val currentMap = _messagesMap.value.toMutableMap()
        val currentList = (currentMap[conversationId] ?: emptyList()).toMutableList()
        currentList.add(reply)
        currentMap[conversationId] = currentList
        _messagesMap.value = currentMap

        _conversations.value = _conversations.value.map { conv ->
            if (conv.id == conversationId) {
                conv.copy(lastMessage = text, lastMessageTime = currentTime)
            } else {
                conv
            }
        }

        return reply
    }

    fun startOrGetConversation(product: Product): Conversation {
        val existing = _conversations.value.find { it.productId == product.id }
        if (existing != null) {
            return existing
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())
        val newConvId = "conv_${System.currentTimeMillis()}"

        val newConv = Conversation(
            id = newConvId,
            productId = product.id,
            productName = product.name,
            productImage = product.imageUrl,
            productPrice = product.formattedPrice,
            sellerId = product.sellerId,
            sellerName = product.sellerName,
            sellerAvatar = product.sellerAvatar,
            lastMessage = "Halo ${product.sellerName}, apakah ${product.name} masih ada?",
            lastMessageTime = currentTime,
            unreadCount = 0
        )

        _conversations.value = listOf(newConv) + _conversations.value

        val currentMap = _messagesMap.value.toMutableMap()
        currentMap[newConvId] = listOf(
            ChatMessage(
                id = "m_start_${System.currentTimeMillis()}",
                conversationId = newConvId,
                senderId = "user_me",
                messageText = "Halo ${product.sellerName}, apakah ${product.name} masih ada?",
                timestamp = currentTime,
                isFromMe = true
            )
        )
        _messagesMap.value = currentMap

        return newConv
    }
}
