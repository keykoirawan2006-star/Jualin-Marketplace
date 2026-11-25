package com.example.jualin_marketplace.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")

    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    object Checkout : Screen("checkout/{productId}") {
        fun createRoute(productId: String) = "checkout/$productId"
    }

    object ChatDetail : Screen("chat_detail/{conversationId}") {
        fun createRoute(conversationId: String) = "chat_detail/$conversationId"
    }

    object EditProfile : Screen("edit_profile")
    object MyProducts : Screen("my_products")
    object TransactionHistory : Screen("transaction_history")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(
        route = "tab_home",
        title = "Beranda",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Favorite : BottomNavItem(
        route = "tab_favorite",
        title = "Favorit",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    object Sell : BottomNavItem(
        route = "tab_sell",
        title = "Jual",
        selectedIcon = Icons.Filled.AddCircle,
        unselectedIcon = Icons.Outlined.AddCircleOutline
    )

    object Chat : BottomNavItem(
        route = "tab_chat",
        title = "Chat",
        selectedIcon = Icons.AutoMirrored.Filled.Chat,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline
    )

    object Profile : BottomNavItem(
        route = "tab_profile",
        title = "Profil",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.PersonOutline
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Favorite,
    BottomNavItem.Sell,
    BottomNavItem.Chat,
    BottomNavItem.Profile
)
