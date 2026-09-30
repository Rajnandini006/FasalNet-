package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserRole
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalGreenPrimary
import com.example.ui.theme.FasalOrangeContainer
import com.example.ui.theme.FasalOrangeSecondary
import com.example.ui.viewmodel.FasalNetViewModel

@Composable
fun LandingScreen(
    viewModel: FasalNetViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // FasalNet Brand Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(FasalGreenPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = "FasalNet Logo",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "FasalNet",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = FasalGreenPrimary,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "Your surplus. Your income.",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = FasalOrangeSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Hero Banner Card
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_fasalnet_hero),
                        contentDescription = "Farmer and Marketplace Illustration",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Gradient overlay at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.Black.copy(alpha = 0.15f)
                            )
                    )
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Turn your agricultural surplus into direct income.",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 26.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "An AI-powered B2B marketplace connecting farmers who have surplus or cosmetically imperfect produce directly with verified food processors, restaurants, and wholesalers.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Select Role Section
        Text(
            text = "Choose your role to enter",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Role Option 1: FARMER
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = FasalGreenContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            onClick = { viewModel.switchRole(UserRole.FARMER) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(FasalGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌾", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "I am a Farmer",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = FasalGreenPrimary
                        )
                    )
                    Text(
                        text = "Sell surplus with voice & photo AI • Zero typing needed",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Button(
                    onClick = { viewModel.switchRole(UserRole.FARMER) },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("farmer_portal_button")
                ) {
                    Text("Enter")
                }
            }
        }

        // Role Option 2: BUYER
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = FasalOrangeContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            onClick = { viewModel.switchRole(UserRole.BUYER) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(FasalOrangeSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏭", fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "I am a Buyer",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = FasalOrangeSecondary
                        )
                    )
                    Text(
                        text = "Source fresh B2B processing surplus • Food processors & kitchens",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Button(
                    onClick = { viewModel.switchRole(UserRole.BUYER) },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalOrangeSecondary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("buyer_portal_button")
                ) {
                    Text("Browse")
                }
            }
        }

        // How FasalNet Works
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "How FasalNet Works",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))

                HowItWorksStep(
                    icon = "🌾",
                    step = "1. Farmer Lists Surplus",
                    desc = "Farmer speaks in Hindi/Gujarati/Marathi or captures a quick photo."
                )
                Spacer(modifier = Modifier.height(10.dp))
                HowItWorksStep(
                    icon = "🤖",
                    step = "2. FasalNet AI Pipeline",
                    desc = "Gemini AI validates, classifies cosmetic grade B, and normalizes data."
                )
                Spacer(modifier = Modifier.height(10.dp))
                HowItWorksStep(
                    icon = "🏭",
                    step = "3. Business Matching",
                    desc = "Matching sauce makers, processors, and restaurants discover the supply."
                )
                Spacer(modifier = Modifier.height(10.dp))
                HowItWorksStep(
                    icon = "🤝",
                    step = "4. Direct Purchase",
                    desc = "Instant transparent orders, fair pricing, and scheduled farm pickups."
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HowItWorksStep(
    icon: String,
    step: String,
    desc: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = icon, fontSize = 20.sp, modifier = Modifier.padding(top = 2.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = step,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
