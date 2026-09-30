package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.data.model.ProduceCategory
import com.example.data.model.UserRole
import com.example.ui.components.ProduceImage
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalGreenPrimary
import com.example.ui.theme.FasalOrangeContainer
import com.example.ui.theme.FasalOrangeSecondary
import com.example.ui.viewmodel.FasalNetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerHomeScreen(
    viewModel: FasalNetViewModel,
    modifier: Modifier = Modifier
) {
    val listings by viewModel.allActiveListings.collectAsState()
    val buyerOrders by viewModel.buyerOrders.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGrade by viewModel.selectedGradeFilter.collectAsState()

    // Filter logic
    val filteredListings = listings.filter { listing ->
        val matchesCategory = when (selectedCategory) {
            ProduceCategory.ALL -> true
            ProduceCategory.VEGETABLES -> listing.category.equals("Vegetables", ignoreCase = true)
            ProduceCategory.FRUITS -> listing.category.equals("Fruits", ignoreCase = true)
            ProduceCategory.GRAINS -> listing.category.equals("Grains", ignoreCase = true)
            ProduceCategory.ROOTS -> listing.category.equals("Roots", ignoreCase = true)
            ProduceCategory.OTHER -> listing.category.equals("Other", ignoreCase = true)
        }
        val matchesSearch = if (searchQuery.isBlank()) true else {
            listing.crop.contains(searchQuery, ignoreCase = true) ||
            listing.cropLocalName.contains(searchQuery, ignoreCase = true) ||
            listing.location.contains(searchQuery, ignoreCase = true)
        }
        val matchesGrade = if (selectedGrade == "All") true else {
            listing.qualityGrade.contains(selectedGrade, ignoreCase = true)
        }
        matchesCategory && matchesSearch && matchesGrade
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Buyer Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(FasalOrangeContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Business,
                            contentDescription = null,
                            tint = FasalOrangeSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = viewModel.currentBuyerName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Verified Buyer • Food Processing",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = FasalGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Orders button
                    IconButton(onClick = { viewModel.navigateTo("ORDER_TRACKING") }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = "My Orders",
                            tint = FasalGreenPrimary
                        )
                    }

                    // Switch to Farmer Mode
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = FasalGreenContainer,
                        modifier = Modifier.clickable { viewModel.switchRole(UserRole.FARMER) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.SwapHoriz,
                                contentDescription = "Switch to Farmer",
                                modifier = Modifier.size(16.dp),
                                tint = FasalGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Farmer",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = FasalGreenPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Title and tagline
        item {
            Column {
                Text(
                    text = "Find fresh surplus directly from farms",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = FasalGreenPrimary,
                        lineHeight = 28.sp
                    )
                )
                Text(
                    text = "FasalNet AI-inspected surplus & processing-grade produce at B2B wholesale rates",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Search Bar (Blinkit inspired minimal search)
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search tomato, onion, mango...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = FasalGreenPrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = FasalGreenPrimary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buyer_search_input")
            )
        }

        // Category Filter Pills
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(ProduceCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setProduceCategory(category) },
                        label = { Text("${category.emoji} ${category.displayName}") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = FasalGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Quality Grade Filter Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Grade:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                listOf("All", "Grade B", "Grade A").forEach { grade ->
                    val isSelected = selectedGrade == grade
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) FasalOrangeSecondary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { viewModel.setGradeFilter(grade) }
                            .padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = if (grade == "Grade B") "Grade B (Processing)" else grade,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Available Surplus Count
        item {
            Text(
                text = "${filteredListings.size} verified surplus lots available",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // PRODUCE LISTING CARDS
        items(filteredListings) { listing ->
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openListingDetail(listing) }
                    .testTag("listing_card_${listing.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProduceImage(
                            presetName = listing.imagePresetRes,
                            modifier = Modifier.size(84.dp)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = listing.crop,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "₹${listing.pricePerKg.toInt()}/kg",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = FasalOrangeSecondary
                                    )
                                )
                            }

                            Text(
                                text = "${listing.quantityKg.toInt()} kg available",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = FasalGreenPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = FasalGreenContainer
                            ) {
                                Text(
                                    text = listing.qualityGrade,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = FasalGreenPrimary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = listing.location,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pickup: ${listing.availableDate}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Button(
                            onClick = { viewModel.openListingDetail(listing) },
                            colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("view_details_${listing.id}")
                        ) {
                            Text("View Details")
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
