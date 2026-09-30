package com.example.data.repository

import com.example.data.db.BuyerRequirementEntity
import com.example.data.db.FasalNetDatabase
import com.example.data.db.ListingEntity
import com.example.data.db.OrderEntity
import com.example.data.model.OrderStatus
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FasalNetRepository(private val database: FasalNetDatabase) {

    private val listingDao = database.listingDao()
    private val buyerRequirementDao = database.buyerRequirementDao()
    private val orderDao = database.orderDao()

    val allActiveListings: Flow<List<ListingEntity>> = listingDao.getAllActiveListings()
    val allRequirements: Flow<List<BuyerRequirementEntity>> = buyerRequirementDao.getAllRequirements()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()

    fun getFarmerListings(farmerId: String): Flow<List<ListingEntity>> =
        listingDao.getListingsForFarmer(farmerId)

    fun getFarmerOrders(farmerId: String): Flow<List<OrderEntity>> =
        orderDao.getOrdersForFarmer(farmerId)

    fun getBuyerOrders(buyerId: String): Flow<List<OrderEntity>> =
        orderDao.getOrdersForBuyer(buyerId)

    suspend fun getListingById(id: String): ListingEntity? =
        listingDao.getListingById(id)

    suspend fun insertListing(listing: ListingEntity) {
        listingDao.insertListing(listing)
    }

    suspend fun findMatchesForCrop(crop: String): List<BuyerRequirementEntity> {
        return buyerRequirementDao.getRequirementsForCrop(crop)
    }

    suspend fun placeOrder(
        listing: ListingEntity,
        buyerId: String,
        buyerName: String,
        quantityKg: Double,
        deliveryLocation: String
    ): OrderEntity {
        val totalAmount = quantityKg * listing.pricePerKg
        val order = OrderEntity(
            id = "ORD-" + UUID.randomUUID().toString().take(6).uppercase(),
            listingId = listing.id,
            farmerId = listing.farmerId,
            farmerName = listing.farmerName,
            buyerId = buyerId,
            buyerName = buyerName,
            crop = listing.crop,
            quantityKg = quantityKg,
            pricePerKg = listing.pricePerKg,
            totalAmount = totalAmount,
            status = OrderStatus.REQUESTED.name,
            pickupDate = listing.availableDate,
            deliveryLocation = deliveryLocation,
            createdAt = System.currentTimeMillis()
        )
        orderDao.insertOrder(order)
        return order
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        orderDao.updateOrderStatus(orderId, newStatus.name)
    }
}
