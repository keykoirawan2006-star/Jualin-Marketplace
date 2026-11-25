package com.example.jualin_marketplace.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jualin_marketplace.data.model.Conversation
import com.example.jualin_marketplace.ui.components.EmptyStateView
import com.example.jualin_marketplace.ui.components.JualinTopAppBar
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray

@Composable
fun ChatListScreen(
    conversations: List<Conversation>,
    onConversationClick: (Conversation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        JualinTopAppBar(title = "Pesan & Chat")

        if (conversations.isEmpty()) {
            EmptyStateView(
                icon = Icons.Filled.ChatBubbleOutline,
                title = "Belum Ada Percakapan",
                message = "Tanyakan ketersediaan barang bekas kepada penjual dengan menekan tombol 'Chat Penjual' di detail produk."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(conversations, key = { it.id }) { conv ->
                    ConversationItem(
                        conversation = conv,
                        onClick = { onConversationClick(conv) }
                    )
                    HorizontalDivider(
                        color = SurfaceLightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationItem(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Seller Avatar
        Box(modifier = Modifier.size(54.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(conversation.sellerAvatar)
                    .crossfade(true)
                    .build(),
                contentDescription = conversation.sellerName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
            // Product Thumbnail Mini Badge
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(conversation.productImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.sellerName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = conversation.lastMessageTime,
                    fontSize = 11.sp,
                    color = TextSecondaryGray
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${conversation.productName} • ${conversation.productPrice}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = conversation.lastMessage,
                fontSize = 13.sp,
                color = if (conversation.unreadCount > 0) TextPrimaryDark else TextSecondaryGray,
                fontWeight = if (conversation.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (conversation.unreadCount > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(EmeraldPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = conversation.unreadCount.toString(),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
