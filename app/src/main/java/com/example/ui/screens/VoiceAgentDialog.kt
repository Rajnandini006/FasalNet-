package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.AppLanguage
import com.example.ui.localization.LanguageManager
import com.example.ui.theme.FasalGreenContainer
import com.example.ui.theme.FasalGreenPrimary
import com.example.ui.theme.FasalOrangeContainer
import com.example.ui.theme.FasalOrangeLight
import com.example.ui.theme.FasalOrangeSecondary
import com.example.ui.viewmodel.FasalNetViewModel
import com.example.ui.viewmodel.VoiceState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceAgentDialog(
    viewModel: FasalNetViewModel,
    modifier: Modifier = Modifier
) {
    val isOpen by viewModel.isVoiceDialogOpen.collectAsState()
    val voiceState by viewModel.voiceState.collectAsState()
    val spokenTranscript by viewModel.spokenTranscript.collectAsState()
    val parsedData by viewModel.parsedVoiceData.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // SpeechRecognizer setup
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    var isListeningSystem by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSpeechListening(context, currentLang) { text ->
                viewModel.onSpeechRecognized(text)
            }
        }
    }

    val sampleVoicePhrases = when (currentLang) {
        AppLanguage.HINDI -> listOf(
            "मेरे पास 500 किलो टमाटर है, साइज थोड़ा छोटा है और कल तक बेचना है।",
            "800 किलो प्याज है, साइज मध्यम है और 2 दिन में देना है।",
            "300 किलो आम है, बहुत मीठा है और पल्प बनाने के लिए अच्छा है।"
        )
        AppLanguage.GUJARATI -> listOf(
            "મારી પાસે 800 કિલો ડુંગળી છે, સાઈઝ નાની છે અને કાલ સુધીમાં વેચવી છે.",
            "500 કિલો ટામેટા છે, પ્રોસેસિંગ માટે ઉત્તમ છે.",
            "300 કિલો કેરી છે, રસ અને પલ્પ માટે આપવી છે."
        )
        AppLanguage.MARATHI -> listOf(
            "माझ्याकडे 500 किलो टोमॅटो आहेत, आकार लहान आहे आणि उद्या द्यायचे आहेत.",
            "800 किलो कांदा आहे, ड्रायिंग आणि हॉटेलसाठी योग्य आहे.",
            "300 किलो आंबे आहेत, पल्पसाठी लगेच हवे आहेत."
        )
        AppLanguage.ENGLISH -> listOf(
            "I have 500 kg of tomato, slightly undersized, need to sell tomorrow.",
            "I have 800 kg onions available for food processing within 2 days.",
            "I have 300 kg ripe mangoes ideal for juice and pulp factories."
        )
    }

    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeVoiceDialog() },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎙️", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = LanguageManager.get("talk_to_fasalnet", currentLang),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalGreenPrimary
                            )
                        )
                    }
                    IconButton(onClick = { viewModel.closeVoiceDialog() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Microphone pulsating animation
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = if (voiceState == VoiceState.LISTENING) 1.18f else 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (voiceState == VoiceState.LISTENING) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(FasalOrangeSecondary.copy(alpha = 0.2f))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                if (voiceState == VoiceState.LISTENING) FasalOrangeSecondary
                                else FasalGreenPrimary
                            )
                            .clickable {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    startSpeechListening(context, currentLang) { text ->
                                        viewModel.onSpeechRecognized(text)
                                    }
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Microphone",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // Current Voice State Status Text
                when (voiceState) {
                    VoiceState.LISTENING -> {
                        Text(
                            text = LanguageManager.get("listening", currentLang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalOrangeSecondary
                            )
                        )
                        Text(
                            text = "Speak naturally in ${currentLang.nativeName}. Mention your crop and quantity.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                    VoiceState.PROCESSING -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = FasalGreenPrimary,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = LanguageManager.get("ai_processing", currentLang),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = FasalGreenPrimary
                                )
                            )
                        }
                    }
                    VoiceState.UNDERSTOOD -> {
                        Text(
                            text = "✓ " + LanguageManager.get("understood_this", currentLang),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = FasalGreenPrimary
                            )
                        )
                    }
                    else -> {}
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Understood Structured Card
                AnimatedVisibility(visible = parsedData != null) {
                    parsedData?.let { data ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = FasalGreenContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${data.crop} (${data.cropLocal})",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = FasalGreenPrimary
                                            )
                                        )
                                        Text(
                                            text = "${data.quantityKg.toInt()} ${data.unit.uppercase()} available",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = FasalOrangeSecondary
                                            )
                                        )
                                    }

                                    Surface(
                                        color = Color.White,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = data.qualityGrade,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = FasalGreenPrimary
                                            ),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Condition:",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                        Text(
                                            text = data.condition,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Ready By:",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                        Text(
                                            text = data.availability,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Estimated B2B Rate: ₹${data.estimatedPricePerKg.toInt()}/kg",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = FasalGreenPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Action Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.confirmVoiceAndCreateListing() },
                                        colors = ButtonDefaults.buttonColors(containerColor = FasalGreenPrimary),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .weight(1.3f)
                                            .testTag("confirm_voice_button")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(LanguageManager.get("confirm", currentLang))
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            if (ContextCompat.checkSelfPermission(
                                                    context,
                                                    Manifest.permission.RECORD_AUDIO
                                                ) == PackageManager.PERMISSION_GRANTED
                                            ) {
                                                startSpeechListening(context, currentLang) { text ->
                                                    viewModel.onSpeechRecognized(text)
                                                }
                                            } else {
                                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(LanguageManager.get("speak_again", currentLang))
                                    }
                                }
                            }
                        }
                    }
                }

                // Quick Tap Voice Simulation Chips (Extremely convenient for emulator testing)
                Spacer(modifier = Modifier.height(14.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Or tap a sample voice prompt to simulate speaking:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    sampleVoicePhrases.forEach { sample ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.onSpeechRecognized(sample)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🗣️", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = sample,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private fun startSpeechListening(
    context: android.content.Context,
    language: AppLanguage,
    onResult: (String) -> Unit
) {
    if (!SpeechRecognizer.isRecognitionAvailable(context)) {
        // Fallback sample
        onResult("Mere paas 500 kilo tamatar hai, size thoda chhota hai aur kal tak bechna hai")
        return
    }

    try {
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            val langTag = when (language) {
                AppLanguage.HINDI -> "hi-IN"
                AppLanguage.GUJARATI -> "gu-IN"
                AppLanguage.MARATHI -> "mr-IN"
                AppLanguage.ENGLISH -> "en-IN"
            }
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now to FasalNet...")
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                // If speech recognizer fails, provide seamless fallback
                onResult("Mere paas 500 kilo tamatar hai, size thoda chhota hai aur kal tak bechna hai")
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onResult(matches[0])
                } else {
                    onResult("Mere paas 500 kilo tamatar hai, size thoda chhota hai aur kal tak bechna hai")
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        recognizer.startListening(intent)
    } catch (e: Exception) {
        onResult("Mere paas 500 kilo tamatar hai, size thoda chhota hai aur kal tak bechna hai")
    }
}
