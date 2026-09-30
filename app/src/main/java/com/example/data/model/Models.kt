package com.example.data.model

enum class UserRole {
    FARMER,
    BUYER
}

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String, val flag: String) {
    HINDI("hi", "Hindi", "हिन्दी", "🇮🇳"),
    GUJARATI("gu", "Gujarati", "ગુજરાતી", "🇮🇳"),
    MARATHI("mr", "Marathi", "मराठी", "🇮🇳"),
    ENGLISH("en", "English", "English", "🇬🇧")
}

enum class ProduceCategory(val displayName: String, val emoji: String) {
    ALL("All", "🌾"),
    VEGETABLES("Vegetables", "🍅"),
    FRUITS("Fruits", "🥭"),
    GRAINS("Grains", "🌾"),
    ROOTS("Roots", "🥔"),
    OTHER("Other", "🌱")
}

enum class QualityGrade(val label: String, val description: String) {
    GRADE_B("Grade B / Processing", "Imperfect cosmetic, irregular size, high processing value"),
    GRADE_A("Grade A / Premium", "Export/retail standard, uniform shape"),
    GRADE_C("Grade C / Feed & Bio", "Secondary pulp, animal feed or compost grade")
}

enum class OrderStatus(val title: String, val stepIndex: Int) {
    REQUESTED("Order Requested", 0),
    CONFIRMED("Confirmed", 1),
    PICKUP_SCHEDULED("Pickup Scheduled", 2),
    IN_TRANSIT("In Transit", 3),
    DELIVERED("Delivered", 4)
}

data class AiAnalysisResult(
    val detectedCrop: String,
    val estimatedGrade: String,
    val visualCondition: String,
    val visibleIssues: String,
    val recommendedBuyers: List<String>,
    val suggestedPricePerKg: Double,
    val confidenceScore: Double,
    val disclaimer: String = "AI assessment is an estimate. Final quality and acceptance are confirmed by the buyer."
)
