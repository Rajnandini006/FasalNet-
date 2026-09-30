package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.OrderEntity
import com.example.data.model.OrderStatus
import com.example.data.model.UserRole
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalGreenPrimary
import com.example.ui.theme.FasalInfo
import com.example.ui.theme.FasalOrangeContainer
import com.example.ui.theme.FasalOrangeSecondary
import com.example.ui.theme.FasalSuccess
import com.example.ui.theme.FasalWarning
import com.example.ui.viewmodel.FasalNetViewModel

@Composable
fun OrderTrackingScreen(
    viewModel: FasalNetViewModel,
    modifier: Modifier = Modifier
) {
    val role by viewModel.currentRole.collectAsState()
    val orders by if (role == UserRole.FARMER) viewModel.farmerOrders.collectAsState()
                   else viewModel.buyerOrders.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    BackHandler {
        if (role == UserRole.FARMER) viewModel.navigateTo("FARMER_HOME")
        else viewModel.navigateTo("BUYER_HOME")
    }

    val displayOrders = if (orders.isNotEmpty()) orders else allOrders

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (role == UserRole.FARMER) viewModel.navigateTo("FARMER_HOME")
                    else viewModel.navigateTo("BUYER_HOME")
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "B2B Order Tracking",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = FasalGreenPrimary
                    )
                )
            }
        }

        item {
            Text(
                text = "Track your active orders from harvest dispatch to facility delivery.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        if (displayOrders.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📦", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No active orders found yet.", fontWeight = FontWeight.Bold)
                        Text("Orders placed in the marketplace will appear here for real-time tracking.")
                    }
                }
            }
        }

        items(displayOrders) { order ->
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("order_card_${order.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = order.id,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FasalGreenPrimary
                                )
                            )
                            Text(
                                text = "${order.quantityKg.toInt()} kg ${order.crop}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (order.status) {
                                OrderStatus.DELIVERED.name -> FasalGreenContainer
                                OrderStatus.IN_TRANSIT.name -> Color(0xFFE1F5FE)
                                OrderStatus.PICKUP_SCHEDULED.name -> FasalOrangeContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ) {
                            Text(
                                text = order.status.replace("_", " "),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = when (order.status) {
                                        OrderStatus.DELIVERED.name -> FasalSuccess
                                        OrderStatus.IN_TRANSIT.name -> FasalInfo
                                        OrderStatus.PICKUP_SCHEDULED.name -> FasalOrangeSecondary
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Farmer: ${order.farmerName} • Buyer: ${order.buyerName}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = "Delivery Destination: ${order.deliveryLocation}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Text(
                        text = "Total Value: ₹${order.totalAmount.toInt()} (₹${order.pricePerKg.toInt()}/kg)",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = FasalOrangeSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Timeline Step Tracker
                    OrderStatusTracker(currentStatus = order.status)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulation action button to advance workflow
                    if (order.status != OrderStatus.DELIVERED.name) {
                        Button(
                            onClick = { viewModel.advanceOrderStatus(order.id, order.status) },
                            colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("advance_order_${order.id}")
                        ) {
                            val nextAction = when (order.status) {
                                OrderStatus.REQUESTED.name -> "Accept & Confirm Order"
                                OrderStatus.CONFIRMED.name -> "Schedule Farm Pickup"
                                OrderStatus.PICKUP_SCHEDULED.name -> "Mark Dispatched / In Transit"
                                OrderStatus.IN_TRANSIT.name -> "Confirm Delivery at Facility"
                                else -> "Completed"
                            }
                            Text("Next Step: $nextAction")
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun OrderStatusTracker(currentStatus: String) {
    val steps = listOf(
        "Requested",
        "Confirmed",
        "Pickup",
        "In Transit",
        "Delivered"
    )

    val currentStepIndex = when (currentStatus) {
        OrderStatus.REQUESTED.name -> 0
        OrderStatus.CONFIRMED.name -> 1
        OrderStatus.PICKUP_SCHEDULED.name -> 2
        OrderStatus.IN_TRANSIT.name -> 3
        OrderStatus.DELIVERED.name -> 4
        else -> 0
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepName ->
            val isPassed = index <= currentStepIndex
            val isCurrent = index == currentStepIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> FasalOrangeSecondary
                                isPassed -> FasalGreenPrimary
                                else -> Color.LightGray.copy(alpha = 0.5f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed && !isCurrent) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stepName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isPassed) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
