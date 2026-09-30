package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.FasalNetAiEngine
import com.example.data.ai.ParsedVoiceData
import com.example.data.db.BuyerRequirementEntity
import com.example.data.db.FasalNetDatabase
import com.example.data.db.ListingEntity
import com.example.data.db.OrderEntity
import com.example.data.model.AiAnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.OrderStatus
import com.example.data.model.ProduceCategory
import com.example.data.model.UserRole
import com.example.data.repository.FasalNetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class VoiceState {
    IDLE,
    LISTENING,
    PROCESSING,
    UNDERSTOOD,
    ERROR
}

data class CreateListingDraft(
    val crop: String = "Tomato",
    val cropLocal: String = "टमाटर",
    val quantityKg: Double = 500.0,
    val category: String = "Vegetables",
    val qualityGrade: String = "Grade B / Processing",
    val condition: String = "Slightly undersized, fresh pulp",
    val visibleIssues: String = "Irregular cosmetic size (35-45mm)",
    val suitableFor: String = "Sauce manufacturing, Food processing, Restaurant gravies",
    val location: String = "Udaipur, Rajasthan",
    val pricePerKg: Double = 16.0,
    val availableDate: String = "Tomorrow",
    val presetRes: String = "tomato",
    val imageBitmap: Bitmap? = null,
    val rawVoiceTranscript: String? = null,
    val aiConfidence: Double = 0.94
)

class FasalNetViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FasalNetDatabase.getDatabase(application, viewModelScope)
    private val repository = FasalNetRepository(database)

    // Current User Session
    private val _currentRole = MutableStateFlow(UserRole.FARMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.HINDI)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentScreen = MutableStateFlow("LANDING")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    val currentFarmerId = "farmer-ramesh"
    val currentFarmerName = "Ramesh Patel"
    val currentBuyerId = "buyer-shree"
    val currentBuyerName = "Shree Foods Pvt Ltd"

    // Data streams
    val allActiveListings: StateFlow<List<ListingEntity>> = repository.allActiveListings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val farmerListings: StateFlow<List<ListingEntity>> = repository.getFarmerListings(currentFarmerId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRequirements: StateFlow<List<BuyerRequirementEntity>> = repository.allRequirements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val farmerOrders: StateFlow<List<OrderEntity>> = repository.getFarmerOrders(currentFarmerId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val buyerOrders: StateFlow<List<OrderEntity>> = repository.getBuyerOrders(currentBuyerId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Voice Agent State
    private val _isVoiceDialogOpen = MutableStateFlow(false)
    val isVoiceDialogOpen: StateFlow<Boolean> = _isVoiceDialogOpen.asStateFlow()

    private val _voiceState = MutableStateFlow(VoiceState.IDLE)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private val _spokenTranscript = MutableStateFlow("")
    val spokenTranscript: StateFlow<String> = _spokenTranscript.asStateFlow()

    private val _parsedVoiceData = MutableStateFlow<ParsedVoiceData?>(null)
    val parsedVoiceData: StateFlow<ParsedVoiceData?> = _parsedVoiceData.asStateFlow()

    // Photo & AI Analysis State
    private val _isAnalyzingImage = MutableStateFlow(false)
    val isAnalyzingImage: StateFlow<Boolean> = _isAnalyzingImage.asStateFlow()

    private val _aiAnalysisResult = MutableStateFlow<com.example.data.model.AiAnalysisResult?>(null)
    val aiAnalysisResult: StateFlow<com.example.data.model.AiAnalysisResult?> = _aiAnalysisResult.asStateFlow()

    // Listing Draft
    private val _draft = MutableStateFlow(CreateListingDraft())
    val draft: StateFlow<CreateListingDraft> = _draft.asStateFlow()

    private val _submissionSuccess = MutableStateFlow(false)
    val submissionSuccess: StateFlow<Boolean> = _submissionSuccess.asStateFlow()

    private val _newlyMatchedBuyers = MutableStateFlow<List<BuyerRequirementEntity>>(emptyList())
    val newlyMatchedBuyers: StateFlow<List<BuyerRequirementEntity>> = _newlyMatchedBuyers.asStateFlow()

    // Buyer Browsing / Search / Filter State
    private val _selectedCategory = MutableStateFlow(ProduceCategory.ALL)
    val selectedCategory: StateFlow<ProduceCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGradeFilter = MutableStateFlow("All")
    val selectedGradeFilter: StateFlow<String> = _selectedGradeFilter.asStateFlow()

    private val _selectedDetailListing = MutableStateFlow<ListingEntity?>(null)
    val selectedDetailListing: StateFlow<ListingEntity?> = _selectedDetailListing.asStateFlow()

    private val _orderPlacedSuccess = MutableStateFlow<OrderEntity?>(null)
    val orderPlacedSuccess: StateFlow<OrderEntity?> = _orderPlacedSuccess.asStateFlow()

    fun switchRole(role: UserRole) {
        _currentRole.value = role
        if (role == UserRole.FARMER) {
            _currentScreen.value = "FARMER_HOME"
        } else {
            _currentScreen.value = "BUYER_HOME"
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    // Voice Dialog Controls
    fun openVoiceDialog() {
        _isVoiceDialogOpen.value = true
        _voiceState.value = VoiceState.LISTENING
        _spokenTranscript.value = ""
        _parsedVoiceData.value = null
    }

    fun closeVoiceDialog() {
        _isVoiceDialogOpen.value = false
        _voiceState.value = VoiceState.IDLE
    }

    fun onSpeechRecognized(transcript: String) {
        _spokenTranscript.value = transcript
        _voiceState.value = VoiceState.PROCESSING
        viewModelScope.launch {
            val parsed = FasalNetAiEngine.parseFarmerVoice(transcript, _currentLanguage.value.code)
            _parsedVoiceData.value = parsed
            _voiceState.value = VoiceState.UNDERSTOOD
        }
    }

    fun confirmVoiceAndCreateListing() {
        val parsed = _parsedVoiceData.value ?: return
        val preset = when (parsed.crop.lowercase()) {
            "onion" -> "onion"
            "mango" -> "mango"
            "potato" -> "potato"
            "carrot" -> "carrot"
            else -> "tomato"
        }
        _draft.value = _draft.value.copy(
            crop = parsed.crop,
            cropLocal = parsed.cropLocal,
            quantityKg = parsed.quantityKg,
            category = parsed.category,
            qualityGrade = parsed.qualityGrade,
            condition = parsed.condition,
            suitableFor = parsed.suitableFor,
            pricePerKg = parsed.estimatedPricePerKg,
            availableDate = parsed.availability,
            presetRes = preset,
            rawVoiceTranscript = _spokenTranscript.value,
            aiConfidence = parsed.confidence
        )
        closeVoiceDialog()
        _currentScreen.value = "CREATE_LISTING"
    }

    // Photo / Camera analysis
    fun onProducePhotoSelected(bitmap: Bitmap?, cropHint: String? = null) {
        _isAnalyzingImage.value = true
        _draft.value = _draft.value.copy(imageBitmap = bitmap)
        viewModelScope.launch {
            val result = FasalNetAiEngine.analyzeProduceImage(bitmap, cropHint ?: _draft.value.crop)
            _aiAnalysisResult.value = result
            val preset = when (result.detectedCrop.lowercase()) {
                "onion" -> "onion"
                "mango" -> "mango"
                "potato" -> "potato"
                "carrot" -> "carrot"
                else -> "tomato"
            }
            _draft.value = _draft.value.copy(
                crop = result.detectedCrop,
                qualityGrade = result.estimatedGrade,
                condition = result.visualCondition,
                visibleIssues = result.visibleIssues,
                suitableFor = result.recommendedBuyers.joinToString(", "),
                pricePerKg = result.suggestedPricePerKg,
                presetRes = preset,
                aiConfidence = result.confidenceScore
            )
            _isAnalyzingImage.value = false
        }
    }

    fun updateDraftField(
        crop: String? = null,
        quantityKg: Double? = null,
        qualityGrade: String? = null,
        condition: String? = null,
        location: String? = null,
        pricePerKg: Double? = null,
        availableDate: String? = null
    ) {
        _draft.value = _draft.value.copy(
            crop = crop ?: _draft.value.crop,
            quantityKg = quantityKg ?: _draft.value.quantityKg,
            qualityGrade = qualityGrade ?: _draft.value.qualityGrade,
            condition = condition ?: _draft.value.condition,
            location = location ?: _draft.value.location,
            pricePerKg = pricePerKg ?: _draft.value.pricePerKg,
            availableDate = availableDate ?: _draft.value.availableDate
        )
    }

    // FasalNet Automation Middle Layer submission
    fun submitDraftListing() {
        val current = _draft.value
        val entity = ListingEntity(
            id = "listing-" + UUID.randomUUID().toString().take(8),
            farmerId = currentFarmerId,
            farmerName = currentFarmerName,
            crop = current.crop,
            cropLocalName = current.cropLocal,
            quantityKg = current.quantityKg,
            unit = "kg",
            category = current.category,
            qualityGrade = current.qualityGrade,
            condition = current.condition,
            visibleIssues = current.visibleIssues,
            suitableFor = current.suitableFor,
            location = current.location,
            pricePerKg = current.pricePerKg,
            availableDate = current.availableDate,
            imagePresetRes = current.presetRes,
            rawVoiceTranscript = current.rawVoiceTranscript,
            aiConfidence = current.aiConfidence,
            isVerifiedByFasalNet = true,
            status = "ACTIVE",
            createdAt = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.insertListing(entity)
            val matches = repository.findMatchesForCrop(entity.crop)
            _newlyMatchedBuyers.value = matches
            _submissionSuccess.value = true
        }
    }

    fun dismissSubmissionSuccess() {
        _submissionSuccess.value = false
        _draft.value = CreateListingDraft()
        _currentScreen.value = "FARMER_HOME"
    }

    // Buyer actions
    fun setProduceCategory(category: ProduceCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setGradeFilter(grade: String) {
        _selectedGradeFilter.value = grade
    }

    fun openListingDetail(listing: ListingEntity) {
        _selectedDetailListing.value = listing
        _currentScreen.value = "BUYER_DETAIL"
    }

    fun placeBuyerOrder(quantityKg: Double, location: String) {
        val listing = _selectedDetailListing.value ?: return
        viewModelScope.launch {
            val order = repository.placeOrder(
                listing = listing,
                buyerId = currentBuyerId,
                buyerName = currentBuyerName,
                quantityKg = quantityKg,
                deliveryLocation = location
            )
            _orderPlacedSuccess.value = order
            _currentScreen.value = "ORDER_TRACKING"
        }
    }

    fun acceptFarmerOrder(orderId: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, OrderStatus.CONFIRMED)
        }
    }

    fun advanceOrderStatus(orderId: String, currentStatus: String) {
        val next = when (currentStatus) {
            OrderStatus.REQUESTED.name -> OrderStatus.CONFIRMED
            OrderStatus.CONFIRMED.name -> OrderStatus.PICKUP_SCHEDULED
            OrderStatus.PICKUP_SCHEDULED.name -> OrderStatus.IN_TRANSIT
            OrderStatus.IN_TRANSIT.name -> OrderStatus.DELIVERED
            else -> OrderStatus.DELIVERED
        }
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, next)
        }
    }
}
