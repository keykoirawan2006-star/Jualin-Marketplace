package com.example.jualin_marketplace.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jualin_marketplace.data.model.Category
import com.example.jualin_marketplace.ui.theme.EmeraldPrimary
import com.example.jualin_marketplace.ui.theme.LightGreenSecondary
import com.example.jualin_marketplace.ui.theme.SurfaceLightGray
import com.example.jualin_marketplace.ui.theme.TextPrimaryDark
import com.example.jualin_marketplace.ui.theme.TextSecondaryGray

import androidx.compose.material.icons.automirrored.filled.MenuBook

fun getCategoryIcon(name: String): ImageVector {
    return when {
        name.contains("Elektronik", ignoreCase = true) -> Icons.Filled.Devices
        name.contains("Fashion", ignoreCase = true) -> Icons.Filled.Checkroom
        name.contains("Furniture", ignoreCase = true) -> Icons.Filled.Chair
        name.contains("Kendaraan", ignoreCase = true) -> Icons.Filled.DirectionsCar
        name.contains("Hobi", ignoreCase = true) -> Icons.Filled.SportsEsports
        name.contains("Rumah", ignoreCase = true) -> Icons.Filled.HomeWork
        name.contains("Buku", ignoreCase = true) -> Icons.AutoMirrored.Filled.MenuBook
        else -> Icons.Filled.Category
    }
}

@Composable
fun CategoryFilterRow(
    categories: List<Category>,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            val isSelected = selectedCategory == null
            CategoryChipItem(
                name = "Semua",
                icon = Icons.Filled.Category,
                isSelected = isSelected,
                onClick = { onSelectCategory(null) }
            )
        }

        items(categories) { category ->
            val isSelected = selectedCategory == category.name
            CategoryChipItem(
                name = category.name,
                icon = getCategoryIcon(category.name),
                isSelected = isSelected,
                onClick = { onSelectCategory(if (isSelected) null else category.name) }
            )
        }
    }
}

@Composable
fun CategoryChipItem(
    name: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) EmeraldPrimary else SurfaceLightGray
    val contentColor = if (isSelected) Color.White else TextPrimaryDark

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = contentColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = name,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun CategoryGrid(
    categories: List<Category>,
    onSelectCategory: (String) -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Can be rendered in a grid or scrollable view
    }
}
