package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer_listings")
data class ListingEntity(
    @PrimaryKey
    val id: String,
    val farmerId: String,
    val farmerName: String,
    val crop: String,
    val cropLocalName: String,
    val quantityKg: Double,
    val unit: String = "kg",
    val category: String, // Vegetables, Fruits, Grains, Roots, Other
    val qualityGrade: String, // Grade B / Processing, Grade A, etc.
    val condition: String,
    val visibleIssues: String,
    val suitableFor: String,
    val location: String,
    val pricePerKg: Double,
    val availableDate: String,
    val imagePresetRes: String = "tomato", // Identifier for produce icon/asset
    val imageUri: String? = null,
    val rawVoiceTranscript: String? = null,
    val aiConfidence: Double = 0.92,
    val isVerifiedByFasalNet: Boolean = true,
    val status: String = "ACTIVE", // ACTIVE, SOLD
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "buyer_requirements")
data class BuyerRequirementEntity(
    @PrimaryKey
    val id: String,
    val buyerId: String,
    val buyerName: String,
    val buyerCategory: String, // e.g., "Food Processing", "Sauce Manufacturer", "Restaurant Chain"
    val cropNeeded: String,
    val minQuantityKg: Double,
    val maxPricePerKg: Double,
    val location: String,
    val contactPerson: String
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val listingId: String,
    val farmerId: String,
    val farmerName: String,
    val buyerId: String,
    val buyerName: String,
    val crop: String,
    val quantityKg: Double,
    val pricePerKg: Double,
    val totalAmount: Double,
    val status: String, // REQUESTED, CONFIRMED, PICKUP_SCHEDULED, IN_TRANSIT, DELIVERED
    val pickupDate: String,
    val deliveryLocation: String,
    val createdAt: Long = System.currentTimeMillis()
)
