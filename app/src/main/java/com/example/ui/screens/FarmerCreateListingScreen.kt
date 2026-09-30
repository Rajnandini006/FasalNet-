package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ProduceImage
import com.example.ui.localization.LanguageManager
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalGreenPrimary
import com.example.ui.theme.FasalOrangeContainer
import com.example.ui.theme.FasalOrangeSecondary
import com.example.ui.theme.FasalWarning
import com.example.ui.viewmodel.FasalNetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerCreateListingScreen(
    viewModel: FasalNetViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val draft by viewModel.draft.collectAsState()
    val isAnalyzing by viewModel.isAnalyzingImage.collectAsState()
    val aiResult by viewModel.aiAnalysisResult.collectAsState()
    val submissionSuccess by viewModel.submissionSuccess.collectAsState()
    val newlyMatchedBuyers by viewModel.newlyMatchedBuyers.collectAsState()

    var isManualEditOpen by remember { mutableStateOf(false) }

    // Form fields editable state
    var editCrop by remember(draft.crop) { mutableStateOf(draft.crop) }
    var editQuantity by remember(draft.quantityKg) { mutableStateOf(draft.quantityKg.toInt().toString()) }
    var editPrice by remember(draft.pricePerKg) { mutableStateOf(draft.pricePerKg.toInt().toString()) }
    var editLocation by remember(draft.location) { mutableStateOf(draft.location) }
    var editAvailability by remember(draft.availableDate) { mutableStateOf(draft.availableDate) }

    BackHandler {
        viewModel.navigateTo("FARMER_HOME")
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.onProducePhotoSelected(bitmap, draft.crop)
        }
    }

    // Gallery Photo Picker launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                viewModel.onProducePhotoSelected(bitmap, draft.crop)
            } catch (e: Exception) {
                // Fallback simulation
                viewModel.onProducePhotoSelected(null, draft.crop)
            }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo("FARMER_HOME") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "List Surplus Produce",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = FasalGreenPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // OPTION B & A: CAPTURE PHOTO OR USE VOICE
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add Produce Photo for AI Quality Check",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "FasalNet AI estimates category, visible defects, and matches buyers.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Button: Take Photo
                    OutlinedButton(
                        onClick = { cameraLauncher.launch(null) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("take_photo_button")
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = FasalGreenPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            LanguageManager.get("take_photo", currentLang),
                            color = FasalGreenPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Button: Upload Existing
                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("upload_photo_button")
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = FasalOrangeSecondary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            LanguageManager.get("upload_photo", currentLang),
                            color = FasalOrangeSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick preset crop selection for convenient emulator testing
                Text(
                    text = "Or quick select sample crop to test AI analysis:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Tomato" to "🍅", "Onion" to "🧅", "Mango" to "🥭", "Potato" to "🥔").forEach { (cropName, emoji) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (draft.crop == cropName) FasalGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.onProducePhotoSelected(null, cropName)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = emoji, fontSize = 20.sp)
                                Text(
                                    text = cropName,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (draft.crop == cropName) FasalGreenPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI ANALYSIS STATUS & RESULTS CARD
        if (isAnalyzing) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FasalGreenContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = FasalGreenPrimary,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = LanguageManager.get("ai_checking", currentLang),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = FasalGreenPrimary
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // REVIEW YOUR SURPLUS CARD
        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Review Your Surplus",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    TextButton(onClick = { isManualEditOpen = true }) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(LanguageManager.get("edit", currentLang))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProduceImage(
                        presetName = draft.presetRes,
                        bitmap = draft.imageBitmap,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "${draft.crop} (${draft.cropLocal})",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalGreenPrimary
                            )
                        )
                        Text(
                            text = "${draft.quantityKg.toInt()} kg available",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalOrangeSecondary
                            )
                        )
                        Text(
                            text = "Fair Expected Price: ₹${draft.pricePerKg.toInt()}/kg",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Structured AI Fields
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        ReviewItem(label = "AI Estimated Category", value = draft.qualityGrade, isHighlighted = true)
                        ReviewItem(label = "Visible Condition", value = draft.condition)
                        ReviewItem(label = "Visible Cosmetic Issue", value = draft.visibleIssues)
                        ReviewItem(label = "Location", value = draft.location)
                        ReviewItem(label = "Ready For Pickup", value = draft.availableDate)
                        ReviewItem(label = "Recommended Buyers", value = draft.suitableFor)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // MANDATORY DISCLAIMER
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = FasalWarning,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LanguageManager.get("disclaimer", currentLang),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF5D4037),
                                lineHeight = 16.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = { viewModel.submitDraftListing() },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("submit_to_fasalnet_button")
                ) {
                    Text(
                        text = LanguageManager.get("submit_to_fasalnet", currentLang),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // MANUAL EDIT DIALOG
    if (isManualEditOpen) {
        AlertDialog(
            onDismissRequest = { isManualEditOpen = false },
            title = { Text("Edit Produce Information") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = editCrop,
                        onValueChange = { editCrop = it },
                        label = { Text("Crop Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editQuantity,
                        onValueChange = { editQuantity = it },
                        label = { Text("Quantity (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editPrice,
                        onValueChange = { editPrice = it },
                        label = { Text("Price per kg (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editLocation,
                        onValueChange = { editLocation = it },
                        label = { Text("Approximate Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = editAvailability,
                        onValueChange = { editAvailability = it },
                        label = { Text("Available When") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateDraftField(
                            crop = editCrop,
                            quantityKg = editQuantity.toDoubleOrNull(),
                            pricePerKg = editPrice.toDoubleOrNull(),
                            location = editLocation,
                            availableDate = editAvailability
                        )
                        isManualEditOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { isManualEditOpen = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // SUBMISSION SUCCESS & BUYER MATCHES DIALOG
    if (submissionSuccess) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSubmissionSuccess() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = FasalGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submitted to FasalNet!",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = LanguageManager.get("submission_success", currentLang),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Matching Verified Buyers (${newlyMatchedBuyers.size} matches found):",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = FasalOrangeSecondary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    if (newlyMatchedBuyers.isNotEmpty()) {
                        newlyMatchedBuyers.forEach { match ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = FasalGreenContainer),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = match.buyerName,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "${match.buyerCategory} • Needs ${match.minQuantityKg.toInt()} kg ${match.cropNeeded}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "FasalNet AI is actively broadcasting your listing to verified food processors in Rajasthan.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissSubmissionSuccess() },
                    colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                    modifier = Modifier.testTag("dismiss_submission_button")
                ) {
                    Text("Go to Dashboard")
                }
            }
        )
    }
}

@Composable
private fun ReviewItem(label: String, value: String, isHighlighted: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
                color = if (isHighlighted) FasalGreenPrimary else MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
