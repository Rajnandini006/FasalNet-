package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
    @Query("SELECT * FROM farmer_listings WHERE status = 'ACTIVE' ORDER BY createdAt DESC")
    fun getAllActiveListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM farmer_listings WHERE farmerId = :farmerId ORDER BY createdAt DESC")
    fun getListingsForFarmer(farmerId: String): Flow<List<ListingEntity>>

    @Query("SELECT * FROM farmer_listings WHERE id = :id LIMIT 1")
    suspend fun getListingById(id: String): ListingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListings(listings: List<ListingEntity>)

    @Update
    suspend fun updateListing(listing: ListingEntity)

    @Delete
    suspend fun deleteListing(listing: ListingEntity)

    @Query("SELECT COUNT(*) FROM farmer_listings")
    suspend fun getListingCount(): Int
}

@Dao
interface BuyerRequirementDao {
    @Query("SELECT * FROM buyer_requirements")
    fun getAllRequirements(): Flow<List<BuyerRequirementEntity>>

    @Query("SELECT * FROM buyer_requirements WHERE LOWER(cropNeeded) = LOWER(:crop)")
    suspend fun getRequirementsForCrop(crop: String): List<BuyerRequirementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequirements(requirements: List<BuyerRequirementEntity>)

    @Query("SELECT COUNT(*) FROM buyer_requirements")
    suspend fun getRequirementCount(): Int
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE farmerId = :farmerId ORDER BY createdAt DESC")
    fun getOrdersForFarmer(farmerId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE buyerId = :buyerId ORDER BY createdAt DESC")
    fun getOrdersForBuyer(buyerId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrderCount(): Int
}
