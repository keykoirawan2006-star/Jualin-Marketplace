package com.example.jualin_marketplace.data.model

data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val messageText: String,
    val timestamp: String,
    val isFromMe: Boolean
)

data class Conversation(
    val id: String,
    val productId: String,
    val productName: String,
    val productImage: String,
    val productPrice: String,
    val sellerId: String,
    val sellerName: String,
    val sellerAvatar: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0
)
