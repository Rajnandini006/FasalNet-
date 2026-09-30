package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ListingEntity
import com.example.data.db.OrderEntity
import com.example.data.model.AppLanguage
import com.example.data.model.OrderStatus
import com.example.data.model.UserRole
import com.example.ui.components.ProduceImage
import com.example.ui.localization.LanguageManager
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalGreenPrimary
import com.example.ui.theme.FasalOrangeContainer
import com.example.ui.theme.FasalOrangeLight
import com.example.ui.theme.FasalOrangeSecondary
import com.example.ui.theme.FasalSuccess
import com.example.ui.viewmodel.FasalNetViewModel

@Composable
fun FarmerHomeScreen(
    viewModel: FasalNetViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val farmerListings by viewModel.farmerListings.collectAsState()
    val farmerOrders by viewModel.farmerOrders.collectAsState()
    val allRequirements by viewModel.allRequirements.collectAsState()

    var showLanguageMenu by remember { mutableStateOf(false) }

    val activeOrdersCount = farmerOrders.count { it.status != OrderStatus.DELIVERED.name }
    val totalEarnings = farmerOrders.filter { it.status == OrderStatus.DELIVERED.name || it.status == OrderStatus.PICKUP_SCHEDULED.name }
        .sumOf { it.totalAmount }
    val totalKgSold = farmerOrders.filter { it.status == OrderStatus.DELIVERED.name || it.status == OrderStatus.PICKUP_SCHEDULED.name }
        .sumOf { it.quantityKg }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Top Header: Greeting, Language Selector, and Role Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${LanguageManager.get("namaste", currentLang)} 👋",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalGreenPrimary
                            )
                        )
                    }
                    Text(
                        text = "${viewModel.currentFarmerName} • Udaipur, Rajasthan",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Language Switcher Button
                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = FasalGreenContainer,
                            modifier = Modifier.clickable { showLanguageMenu = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = currentLang.flag, fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = currentLang.nativeName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = FasalGreenPrimary
                                    )
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showLanguageMenu,
                            onDismissRequest = { showLanguageMenu = false }
                        ) {
                            AppLanguage.values().forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${lang.flag} ${lang.nativeName} (${lang.displayName})")
                                    },
                                    onClick = {
                                        viewModel.setLanguage(lang)
                                        showLanguageMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Switch to Buyer Mode for demonstration
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { viewModel.switchRole(UserRole.BUYER) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.SwapHoriz,
                                contentDescription = "Switch to Buyer",
                                modifier = Modifier.size(16.dp),
                                tint = FasalOrangeSecondary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Buyer",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = FasalOrangeSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // PRIMARY ACTION BUTTONS
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Button 1: Sell Your Surplus (Large Green Button)
                Button(
                    onClick = { viewModel.navigateTo("CREATE_LISTING") },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .testTag("sell_surplus_button")
                ) {
                    Text(text = "🌾", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = LanguageManager.get("sell_surplus", currentLang),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                // Button 2: Talk to FasalNet (Prominent Orange Mic Button)
                Button(
                    onClick = { viewModel.openVoiceDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalOrangeSecondary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("talk_to_fasalnet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Assistant",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = LanguageManager.get("talk_to_fasalnet", currentLang),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // METRIC STATS ROW
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    title = LanguageManager.get("my_listings", currentLang),
                    value = "${farmerListings.size}",
                    unit = "items",
                    color = FasalGreenPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
                MetricCard(
                    title = LanguageManager.get("active_orders", currentLang),
                    value = "$activeOrdersCount",
                    unit = "orders",
                    color = FasalOrangeSecondary,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
                MetricCard(
                    title = LanguageManager.get("my_earnings", currentLang),
                    value = "₹${totalEarnings.toInt()}",
                    unit = "earned",
                    color = FasalSuccess,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo("FARMER_EARNINGS") }
                )
            }
        }

        // SECTION: INCOMING BUYER REQUESTS & OFFERS
        val pendingOrders = farmerOrders.filter { it.status == OrderStatus.REQUESTED.name }
        if (pendingOrders.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ " + LanguageManager.get("buyer_requests", currentLang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalOrangeSecondary
                            )
                        )
                        Text(
                            text = "${pendingOrders.size} new",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = FasalOrangeSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    pendingOrders.forEach { order ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = FasalOrangeContainer.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = order.buyerName,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "Requested ${order.quantityKg.toInt()} kg of ${order.crop} @ ₹${order.pricePerKg.toInt()}/kg",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Text(
                                        text = "₹${order.totalAmount.toInt()}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = FasalOrangeSecondary
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { viewModel.acceptFarmerOrder(order.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("accept_order_${order.id}")
                                    ) {
                                        Text(LanguageManager.get("accept_offer", currentLang))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // SECTION: YOUR RECENT PRODUCE LISTINGS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = LanguageManager.get("recent_produce", currentLang),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        items(farmerListings) { listing ->
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProduceImage(
                        presetName = listing.imagePresetRes,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${listing.crop} (${listing.cropLocalName})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "${listing.quantityKg.toInt()} kg • ₹${listing.pricePerKg.toInt()}/kg",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = FasalOrangeSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = FasalGreenContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = FasalGreenPrimary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "FasalNet Verified: ${listing.qualityGrade}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = FasalGreenPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                        Text(
                            text = "Condition: ${listing.condition}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // SECTION: FARMER SUSTAINABILITY & REVENUE IMPACT
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FasalGreenContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌱 FasalNet Impact Summary",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = FasalGreenPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ImpactItem(emoji = "🌱", label = "Surplus Sold", value = "${totalKgSold.toInt()} kg")
                        ImpactItem(emoji = "♻️", label = "Waste Diverted", value = "${totalKgSold.toInt()} kg")
                        ImpactItem(emoji = "💰", label = "Direct Income", value = "₹${totalEarnings.toInt()}")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun ImpactItem(emoji: String, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 20.sp)
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = FasalGreenPrimary
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}
