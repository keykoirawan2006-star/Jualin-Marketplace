package com.example.jualin_marketplace.ui.screens.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jualin_marketplace.data.model.User
import com.example.jualin_marketplace.ui.components.JualinTopAppBar
import com.example.jualin_marketplace.ui.screens.sell.copyUriToLocalFile
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary

@Composable
fun EditProfileScreen(
    user: User,
    onBackClick: () -> Unit,
    onSaveProfile: (String, String, String, String, String, String) -> Unit
) {
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var location by remember { mutableStateOf(user.location) }
    var bio by remember { mutableStateOf(user.bio) }
    var selectedAvatarUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Avatar Image Picker
    val avatarPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedAvatarUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        JualinTopAppBar(
            title = "Edit Profil Saya",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture with Camera Icon Overlay
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clickable { avatarPickerLauncher.launch("image/*") }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(selectedAvatarUri ?: user.avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = user.username,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(LightGreenSecondary)
                )

                // Camera Badge Button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(EmeraldPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Ubah Foto",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "+ Ubah Foto Profil",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldPrimary,
                modifier = Modifier.clickable { avatarPickerLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nama Lengkap") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = null, tint = EmeraldPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Alamat Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = null, tint = EmeraldPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Nomor Telepon / WhatsApp") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Phone, contentDescription = null, tint = EmeraldPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Location
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Kota / Lokasi") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null, tint = EmeraldPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Bio
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio / Deskripsi Singkat") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = EmeraldPrimary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    focusedLabelColor = EmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Save Button
            Button(
                onClick = {
                    if (username.isBlank()) {
                        Toast.makeText(context, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    } else {
                        val avatarPath = if (selectedAvatarUri != null) {
                            copyUriToLocalFile(context, selectedAvatarUri!!)
                        } else {
                            user.avatarUrl
                        }

                        onSaveProfile(username, email, phone, location, bio, avatarPath)
                        Toast.makeText(context, "Foto & Profil Berhasil Diperbarui!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text(
                    text = "Simpan Perubahan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
