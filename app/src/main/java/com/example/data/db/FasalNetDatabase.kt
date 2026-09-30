package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ListingEntity::class,
        BuyerRequirementEntity::class,
        OrderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FasalNetDatabase : RoomDatabase() {

    abstract fun listingDao(): ListingDao
    abstract fun buyerRequirementDao(): BuyerRequirementDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: FasalNetDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FasalNetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FasalNetDatabase::class.java,
                    "fasalnet_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDemoData(database)
                    }
                }
            }
        }

        suspend fun populateInitialDemoData(db: FasalNetDatabase) {
            val listingDao = db.listingDao()
            val buyerDao = db.buyerRequirementDao()
            val orderDao = db.orderDao()

            if (listingDao.getListingCount() == 0) {
                val initialListings = listOf(
                    ListingEntity(
                        id = "listing-001",
                        farmerId = "farmer-ramesh",
                        farmerName = "Ramesh Patel",
                        crop = "Tomato",
                        cropLocalName = "टमाटर",
                        quantityKg = 500.0,
                        category = "Vegetables",
                        qualityGrade = "Grade B / Processing",
                        condition = "Freshly harvested, high pulp content, slightly undersized (35-45mm)",
                        visibleIssues = "Irregular cosmetic size, uniform red ripeness",
                        suitableFor = "Sauce manufacturing, Ketchup, Puree, Restaurant gravies",
                        location = "Udaipur, Rajasthan",
                        pricePerKg = 16.0,
                        availableDate = "Tomorrow",
                        imagePresetRes = "tomato",
                        aiConfidence = 0.94,
                        isVerifiedByFasalNet = true,
                        status = "ACTIVE",
                        createdAt = System.currentTimeMillis() - 3600000 * 2
                    ),
                    ListingEntity(
                        id = "listing-002",
                        farmerId = "farmer-shanti",
                        farmerName = "Shantilal Gurjar",
                        crop = "Onion",
                        cropLocalName = "प्याज",
                        quantityKg = 800.0,
                        category = "Vegetables",
                        qualityGrade = "Grade B / Processing",
                        condition = "Dry cured, firm texture, mixed bulb sizes",
                        visibleIssues = "Cosmetic outer peel peeling, 100% sound core",
                        suitableFor = "Dehydrated flakes, Onion powder, Bulk commercial frying",
                        location = "Rajsamand, Rajasthan",
                        pricePerKg = 22.0,
                        availableDate = "In 2 days",
                        imagePresetRes = "onion",
                        aiConfidence = 0.91,
                        isVerifiedByFasalNet = true,
                        status = "ACTIVE",
                        createdAt = System.currentTimeMillis() - 3600000 * 5
                    ),
                    ListingEntity(
                        id = "listing-003",
                        farmerId = "farmer-mukesh",
                        farmerName = "Mukesh Choudhary",
                        crop = "Mango",
                        cropLocalName = "आम",
                        quantityKg = 300.0,
                        category = "Fruits",
                        qualityGrade = "Grade B / Processing",
                        condition = "High natural sugar brix (18°), very juicy, spot ripened",
                        visibleIssues = "Surface sap blemishes, odd curvature",
                        suitableFor = "Mango pulp, Aamras processing, Fruit leather, Bakery",
                        location = "Nathdwara, Rajasthan",
                        pricePerKg = 38.0,
                        availableDate = "Tomorrow",
                        imagePresetRes = "mango",
                        aiConfidence = 0.96,
                        isVerifiedByFasalNet = true,
                        status = "ACTIVE",
                        createdAt = System.currentTimeMillis() - 3600000 * 12
                    ),
                    ListingEntity(
                        id = "listing-004",
                        farmerId = "farmer-devendra",
                        farmerName = "Devendra Singh",
                        crop = "Potato",
                        cropLocalName = "आलू",
                        quantityKg = 1200.0,
                        category = "Roots",
                        qualityGrade = "Grade B / Processing",
                        condition = "Firm skin, medium starch, baby to medium grade",
                        visibleIssues = "Minor field dirt, irregular roundness",
                        suitableFor = "Starch extraction, Samosa stuffing, Canteen catering",
                        location = "Kota, Rajasthan",
                        pricePerKg = 14.0,
                        availableDate = "Available today",
                        imagePresetRes = "potato",
                        aiConfidence = 0.89,
                        isVerifiedByFasalNet = true,
                        status = "ACTIVE",
                        createdAt = System.currentTimeMillis() - 3600000 * 24
                    )
                )
                listingDao.insertListings(initialListings)
            }

            if (buyerDao.getRequirementCount() == 0) {
                val initialRequirements = listOf(
                    BuyerRequirementEntity(
                        id = "req-001",
                        buyerId = "buyer-shree",
                        buyerName = "Shree Foods Pvt Ltd",
                        buyerCategory = "Sauce & Puree Manufacturer",
                        cropNeeded = "Tomato",
                        minQuantityKg = 300.0,
                        maxPricePerKg = 18.0,
                        location = "Sukher Industrial Area, Udaipur",
                        contactPerson = "Anil Mehta (Procurement)"
                    ),
                    BuyerRequirementEntity(
                        id = "req-002",
                        buyerId = "buyer-agro",
                        buyerName = "Rajasthan Agro Industries",
                        buyerCategory = "Food Dehydration Unit",
                        cropNeeded = "Onion",
                        minQuantityKg = 500.0,
                        maxPricePerKg = 24.0,
                        location = "RIICO Industrial Area, Rajsamand",
                        contactPerson = "Vikram Sharma (Head Buyer)"
                    ),
                    BuyerRequirementEntity(
                        id = "req-003",
                        buyerId = "buyer-freshserve",
                        buyerName = "FreshServe Commercial Kitchens",
                        buyerCategory = "Hotel & Restaurant Supply",
                        cropNeeded = "Tomato",
                        minQuantityKg = 200.0,
                        maxPricePerKg = 19.0,
                        location = "City Palace Road, Udaipur",
                        contactPerson = "Chef Rajesh Verma"
                    ),
                    BuyerRequirementEntity(
                        id = "req-004",
                        buyerId = "buyer-mewar-pulp",
                        buyerName = "Mewar Pulp & Beverage Corp",
                        buyerCategory = "Juice & Pulp Processor",
                        cropNeeded = "Mango",
                        minQuantityKg = 250.0,
                        maxPricePerKg = 42.0,
                        location = "Mavli Junction, Udaipur",
                        contactPerson = "Pooja Joshi (QC Head)"
                    )
                )
                buyerDao.insertRequirements(initialRequirements)
            }

            if (orderDao.getOrderCount() == 0) {
                val initialOrders = listOf(
                    OrderEntity(
                        id = "ORD-7821",
                        listingId = "listing-001",
                        farmerId = "farmer-ramesh",
                        farmerName = "Ramesh Patel",
                        buyerId = "buyer-shree",
                        buyerName = "Shree Foods Pvt Ltd",
                        crop = "Tomato",
                        quantityKg = 250.0,
                        pricePerKg = 16.0,
                        totalAmount = 4000.0,
                        status = "PICKUP_SCHEDULED",
                        pickupDate = "Tomorrow 10:00 AM",
                        deliveryLocation = "Sukher Unit 2, Udaipur",
                        createdAt = System.currentTimeMillis() - 3600000 * 8
                    ),
                    OrderEntity(
                        id = "ORD-7650",
                        listingId = "listing-002",
                        farmerId = "farmer-ramesh",
                        farmerName = "Ramesh Patel",
                        buyerId = "buyer-freshserve",
                        buyerName = "FreshServe Foods",
                        crop = "Tomato",
                        quantityKg = 150.0,
                        pricePerKg = 16.0,
                        totalAmount = 2400.0,
                        status = "DELIVERED",
                        pickupDate = "Yesterday",
                        deliveryLocation = "Bapu Bazaar Kitchen, Udaipur",
                        createdAt = System.currentTimeMillis() - 3600000 * 48
                    )
                )
                orderDao.insertOrders(initialOrders)
            }
        }
    }
}
