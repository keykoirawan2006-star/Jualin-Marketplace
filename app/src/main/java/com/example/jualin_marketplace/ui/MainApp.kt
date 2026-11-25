package com.example.jualin_marketplace.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jualin_marketplace.data.repository.AuthRepository
import com.example.jualin_marketplace.data.repository.ChatRepository
import com.example.jualin_marketplace.data.repository.ProductRepository
import com.example.jualin_marketplace.data.repository.TransactionRepository
import com.example.jualin_marketplace.ui.components.JualinBottomNavigationBar
import com.example.jualin_marketplace.ui.navigation.BottomNavItem
import com.example.jualin_marketplace.ui.navigation.Screen
import com.example.jualin_marketplace.ui.screens.auth.LoginScreen
import com.example.jualin_marketplace.ui.screens.auth.RegisterScreen
import com.example.jualin_marketplace.ui.screens.chat.ChatDetailScreen
import com.example.jualin_marketplace.ui.screens.chat.ChatListScreen
import com.example.jualin_marketplace.ui.screens.checkout.CheckoutScreen
import com.example.jualin_marketplace.ui.screens.detail.ProductDetailScreen
import com.example.jualin_marketplace.ui.screens.favorite.FavoriteScreen
import com.example.jualin_marketplace.ui.screens.home.HomeScreen
import com.example.jualin_marketplace.ui.screens.profile.EditProfileScreen
import com.example.jualin_marketplace.ui.screens.profile.MyProductsScreen
import com.example.jualin_marketplace.ui.screens.profile.ProfileScreen
import com.example.jualin_marketplace.ui.screens.profile.TransactionHistoryScreen
import com.example.jualin_marketplace.ui.screens.sell.SellProductScreen
import com.example.jualin_marketplace.ui.screens.splash.SplashScreen

@Composable
fun MainApp() {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val productRepository = remember { ProductRepository(context) }
    val chatRepository = remember { ChatRepository() }
    val transactionRepository = remember { TransactionRepository(context) }

    val currentUser by authRepository.currentUser.collectAsState()
    val isLoggedIn by authRepository.isLoggedIn.collectAsState()
    val products by productRepository.products.collectAsState()
    val conversations by chatRepository.conversations.collectAsState()
    val transactions by transactionRepository.transactions.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tab routes where bottom bar should be displayed
    val bottomBarRoutes = listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Favorite.route,
        BottomNavItem.Sell.route,
        BottomNavItem.Chat.route,
        BottomNavItem.Profile.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                JualinBottomNavigationBar(
                    currentRoute = currentRoute,
                    onItemSelected = { item ->
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. Splash Screen
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashFinished = {
                        val destination = if (isLoggedIn) BottomNavItem.Home.route else Screen.Login.route
                        navController.navigate(destination) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Login Screen
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onLoginAction = { input ->
                        authRepository.login(input)
                    }
                )
            }

            // 3. Register Screen
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route)
                    },
                    onRegisterAction = { name, email ->
                        authRepository.register(name, email)
                    }
                )
            }

            // 4. Bottom Tab: Beranda (Home)
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    user = currentUser,
                    products = products,
                    onProductClick = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    },
                    onFavoriteClick = { product ->
                        productRepository.toggleFavorite(product.id)
                    }
                )
            }

            // 5. Bottom Tab: Favorit
            composable(BottomNavItem.Favorite.route) {
                val favorites = products.filter { it.isFavorite }
                FavoriteScreen(
                    favoriteProducts = favorites,
                    onProductClick = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    },
                    onFavoriteClick = { product ->
                        productRepository.toggleFavorite(product.id)
                    },
                    onExploreClick = {
                        navController.navigate(BottomNavItem.Home.route)
                    }
                )
            }

            // 6. Bottom Tab: Jual Barang
            composable(BottomNavItem.Sell.route) {
                SellProductScreen(
                    currentUser = currentUser,
                    onProductSubmitted = { newProduct ->
                        productRepository.addProduct(newProduct)
                    },
                    onNavigateHome = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(BottomNavItem.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            // 7. Bottom Tab: Chat List
            composable(BottomNavItem.Chat.route) {
                ChatListScreen(
                    conversations = conversations,
                    onConversationClick = { conv ->
                        navController.navigate(Screen.ChatDetail.createRoute(conv.id))
                    }
                )
            }

            // 8. Bottom Tab: Profil
            composable(BottomNavItem.Profile.route) {
                val userProducts = products.filter { it.isUserListing }
                val favoriteCount = products.count { it.isFavorite }

                ProfileScreen(
                    user = currentUser,
                    myProductsCount = userProducts.size,
                    favoriteCount = favoriteCount,
                    transactionCount = transactions.size,
                    onEditProfileClick = {
                        navController.navigate(Screen.EditProfile.route)
                    },
                    onMyProductsClick = {
                        navController.navigate(Screen.MyProducts.route)
                    },
                    onFavoriteClick = {
                        navController.navigate(BottomNavItem.Favorite.route)
                    },
                    onTransactionHistoryClick = {
                        navController.navigate(Screen.TransactionHistory.route)
                    },
                    onLogoutClick = {
                        authRepository.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 9. Product Detail Screen
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val product = products.find { it.id == productId }

                ProductDetailScreen(
                    product = product,
                    onBackClick = { navController.popBackStack() },
                    onFavoriteToggle = { prod ->
                        productRepository.toggleFavorite(prod.id)
                    },
                    onChatClick = { prod ->
                        val conv = chatRepository.startOrGetConversation(prod)
                        navController.navigate(Screen.ChatDetail.createRoute(conv.id))
                    },
                    onBuyNowClick = { prod ->
                        navController.navigate(Screen.Checkout.createRoute(prod.id))
                    }
                )
            }

            // 10. Chat Detail Screen
            composable(
                route = Screen.ChatDetail.route,
                arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
            ) { backStackEntry ->
                val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
                val conversation = conversations.find { it.id == conversationId }
                val allMessages by chatRepository.messagesMap.collectAsState()
                val messages = allMessages[conversationId] ?: emptyList()

                ChatDetailScreen(
                    conversation = conversation,
                    messages = messages,
                    onBackClick = { navController.popBackStack() },
                    onSendMessage = { text ->
                        chatRepository.sendMessage(conversationId, text)
                    },
                    onSimulateReply = { replyText ->
                        chatRepository.simulateSellerReply(conversationId, replyText)
                    }
                )
            }

            // 11. Edit Profile Screen
            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    user = currentUser,
                    onBackClick = { navController.popBackStack() },
                    onSaveProfile = { name, email, phone, location, bio, avatarUrl ->
                        authRepository.updateProfile(name, email, phone, location, bio, avatarUrl)
                    }
                )
            }

            // 12. My Products Screen
            composable(Screen.MyProducts.route) {
                val userProducts = products.filter { it.isUserListing }

                MyProductsScreen(
                    userProducts = userProducts,
                    onBackClick = { navController.popBackStack() },
                    onProductClick = { prod ->
                        navController.navigate(Screen.ProductDetail.createRoute(prod.id))
                    },
                    onDeleteProduct = { prodId ->
                        productRepository.deleteProduct(prodId)
                    },
                    onAddNewProductClick = {
                        navController.navigate(BottomNavItem.Sell.route)
                    }
                )
            }

            // 13. Transaction History Screen
            composable(Screen.TransactionHistory.route) {
                TransactionHistoryScreen(
                    transactions = transactions,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 14. Checkout / Payment Screen
            composable(
                route = Screen.Checkout.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: ""
                val product = products.find { it.id == productId }

                CheckoutScreen(
                    product = product,
                    onBackClick = { navController.popBackStack() },
                    onConfirmOrder = { prod, paymentMethod, userAccountNumber, totalFormatted ->
                        transactionRepository.buyProduct(prod, paymentMethod, userAccountNumber, totalFormatted)
                    },
                    onViewTransactionsClick = {
                        navController.navigate(Screen.TransactionHistory.route) {
                            popUpTo(BottomNavItem.Home.route)
                        }
                    }
                )
            }
        }
    }
}
