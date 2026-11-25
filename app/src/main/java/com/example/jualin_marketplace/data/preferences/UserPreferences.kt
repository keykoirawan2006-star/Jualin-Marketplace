package com.example.jualin_marketplace.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.jualin_marketplace.data.model.User

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("jualin_user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_USER_LOCATION = "user_location"
        private const val KEY_USER_AVATAR = "user_avatar"
        private const val KEY_USER_BIO = "user_bio"
        private const val KEY_USER_JOINED = "user_joined"
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun getUser(): User {
        return User(
            id = prefs.getString(KEY_USER_ID, "user_me") ?: "user_me",
            username = prefs.getString(KEY_USERNAME, "Budi Santoso") ?: "Budi Santoso",
            email = prefs.getString(KEY_USER_EMAIL, "budi.santoso@gmail.com") ?: "budi.santoso@gmail.com",
            phone = prefs.getString(KEY_USER_PHONE, "0812-3456-7890") ?: "0812-3456-7890",
            location = prefs.getString(KEY_USER_LOCATION, "Jakarta Selatan") ?: "Jakarta Selatan",
            avatarUrl = prefs.getString(KEY_USER_AVATAR, "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200") ?: "",
            bio = prefs.getString(KEY_USER_BIO, "Penjual & Pembeli aktif di Jualin Marketplace") ?: "",
            joinedDate = prefs.getString(KEY_USER_JOINED, "Januari 2024") ?: "Januari 2024"
        )
    }

    fun saveUser(user: User) {
        prefs.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USERNAME, user.username)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_PHONE, user.phone)
            putString(KEY_USER_LOCATION, user.location)
            putString(KEY_USER_AVATAR, user.avatarUrl)
            putString(KEY_USER_BIO, user.bio)
            putString(KEY_USER_JOINED, user.joinedDate)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun clearSession() {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }
}
