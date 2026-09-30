package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalOrangeContainer

@Composable
fun ProduceImage(
    presetName: String,
    bitmap: Bitmap? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = presetName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            val (bgColor, emoji, iconColor) = when (presetName.lowercase()) {
                "tomato" -> Triple(Color(0xFFFFEBEE), "🍅", Color(0xFFC62828))
                "onion" -> Triple(Color(0xFFF3E5F5), "🧅", Color(0xFF6A1B9A))
                "mango" -> Triple(Color(0xFFFFF8E1), "🥭", Color(0xFFF57F17))
                "potato" -> Triple(Color(0xFFEFEBE9), "🥔", Color(0xFF4E342E))
                "carrot" -> Triple(Color(0xFFFFF3E0), "🥕", Color(0xFFE65100))
                "grains" -> Triple(Color(0xFFFFFDE7), "🌾", Color(0xFFF57F17))
                else -> Triple(FasalGreenContainer, "🌱", Color(0xFF134E27))
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 38.sp
                )
            }
        }
    }
}
