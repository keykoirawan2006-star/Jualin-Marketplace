package com.example.jualin_marketplace.data.repository

import android.content.Context
import com.example.jualin_marketplace.data.model.User
import com.example.jualin_marketplace.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(context: Context) {
    private val prefs = UserPreferences(context)

    private val _currentUser = MutableStateFlow(prefs.getUser())
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(prefs.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login(emailOrUsername: String): Boolean {
        val current = _currentUser.value
        val name = if (emailOrUsername.contains("@")) emailOrUsername.substringBefore("@").replaceFirstChar { it.uppercase() } else emailOrUsername
        val updatedUser = current.copy(
            email = if (emailOrUsername.contains("@")) emailOrUsername else "$emailOrUsername@example.com",
            username = name.ifBlank { "Pengguna Jualin" }
        )
        prefs.saveUser(updatedUser)
        _currentUser.value = updatedUser
        _isLoggedIn.value = true
        return true
    }

    fun register(fullName: String, email: String): Boolean {
        val updatedUser = User(
            id = "u_${System.currentTimeMillis()}",
            username = fullName,
            email = email,
            phone = "0812-3456-7890",
            location = "Jakarta Selatan",
            bio = "Penjual & Pembeli aktif di Jualin Marketplace",
            joinedDate = "Baru Bergabung"
        )
        prefs.saveUser(updatedUser)
        _currentUser.value = updatedUser
        _isLoggedIn.value = true
        return true
    }

    fun updateProfile(name: String, email: String, phone: String, location: String, bio: String, avatarUrl: String) {
        val current = _currentUser.value
        val updatedUser = current.copy(
            username = name,
            email = email,
            phone = phone,
            location = location,
            bio = bio,
            avatarUrl = avatarUrl.ifBlank { current.avatarUrl }
        )
        prefs.saveUser(updatedUser)
        _currentUser.value = updatedUser
    }

    fun logout() {
        prefs.clearSession()
        _isLoggedIn.value = false
    }
}
