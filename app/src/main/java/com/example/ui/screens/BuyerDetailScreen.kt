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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ProduceImage
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalGreenPrimary
import com.example.ui.theme.FasalOrangeContainer
import com.example.ui.theme.FasalOrangeSecondary
import com.example.ui.theme.FasalWarning
import com.example.ui.viewmodel.FasalNetViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerDetailScreen(
    viewModel: FasalNetViewModel,
    modifier: Modifier = Modifier
) {
    val listing = viewModel.selectedDetailListing.collectAsState().value

    BackHandler {
        viewModel.navigateTo("BUYER_HOME")
    }

    if (listing == null) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Listing not found")
            Button(onClick = { viewModel.navigateTo("BUYER_HOME") }) {
                Text("Back to Market")
            }
        }
        return
    }

    var requestedQuantityKg by remember { mutableFloatStateOf(listing.quantityKg.toFloat().coerceAtMost(300f)) }
    var deliveryLocation by remember { mutableStateOf("Sukher Industrial Area, Unit 2, Udaipur") }

    val calculatedTotal = (requestedQuantityKg * listing.pricePerKg).roundToInt()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Nav
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo("BUYER_HOME") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Surplus Lot Details",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = FasalGreenPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Hero Produce Image & Details
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProduceImage(
                        presetName = listing.imagePresetRes,
                        modifier = Modifier.size(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = listing.crop,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "${listing.quantityKg.toInt()} kg total available",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = FasalGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹${listing.pricePerKg.toInt()}/kg",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalOrangeSecondary
                            )
                        )
                        Text(
                            text = "B2B Wholesale Rate",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // FasalNet AI Verification Badge
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = FasalGreenContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = "Verified",
                            tint = FasalGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "✓ Listing Processed by FasalNet",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = FasalGreenPrimary
                                )
                            )
                            Text(
                                text = "Automated pipeline validated origin, normalized units, and verified quality parameters (AI Confidence: ${(listing.aiConfidence * 100).toInt()}%).",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quality & Characteristics
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        DetailRow(label = "Quality Grade", value = listing.qualityGrade, isBold = true)
                        DetailRow(label = "Visible Condition", value = listing.condition)
                        DetailRow(label = "Cosmetic Variance", value = listing.visibleIssues)
                        DetailRow(label = "Approx. Location", value = listing.location)
                        DetailRow(label = "Ready For Dispatch", value = listing.availableDate)
                        DetailRow(label = "Recommended Usage", value = listing.suitableFor)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mandatory disclaimer
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = FasalWarning, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI assessment is an estimate. Final quality and acceptance are confirmed by the buyer upon farm dispatch.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF5D4037))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ORDER / BUY REQUEST CARD
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Request / Place B2B Order",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Quantity selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity to Procure:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = "${requestedQuantityKg.roundToInt()} kg",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = FasalOrangeSecondary
                        )
                    )
                }

                Slider(
                    value = requestedQuantityKg,
                    onValueChange = { requestedQuantityKg = it },
                    valueRange = 50f..listing.quantityKg.toFloat(),
                    steps = ((listing.quantityKg - 50) / 50).toInt().coerceAtLeast(0),
                    colors = SliderDefaults.colors(
                        thumbColor = FasalGreenPrimary,
                        activeTrackColor = FasalGreenPrimary
                    ),
                    modifier = Modifier.testTag("quantity_slider")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = deliveryLocation,
                    onValueChange = { deliveryLocation = it },
                    label = { Text("Processing Facility / Delivery Address") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = FasalGreenPrimary) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("delivery_location_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Total Calculation Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(FasalOrangeContainer.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Estimated Order Total",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "${requestedQuantityKg.roundToInt()} kg @ ₹${listing.pricePerKg.toInt()}/kg",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(
                        text = "₹$calculatedTotal",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = FasalOrangeSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Buy / Request Button
                Button(
                    onClick = {
                        viewModel.placeBuyerOrder(
                            quantityKg = requestedQuantityKg.toDouble(),
                            location = deliveryLocation
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("request_buy_button")
                ) {
                    Text(
                        text = "Submit Purchase Request",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun DetailRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
                color = if (isBold) FasalGreenPrimary else MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.weight(1.4f)
        )
    }
}
