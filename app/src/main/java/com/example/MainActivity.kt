package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.screens.BuyerDetailScreen
import com.example.ui.screens.BuyerHomeScreen
import com.example.ui.screens.FarmerCreateListingScreen
import com.example.ui.screens.FarmerEarningsScreen
import com.example.ui.screens.FarmerHomeScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.VoiceAgentDialog
import com.example.ui.theme.FasalNetTheme
import com.example.ui.viewmodel.FasalNetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FasalNetTheme {
                val viewModel: FasalNetViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()
                val currentRole by viewModel.currentRole.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        when (currentScreen) {
                            "LANDING" -> LandingScreen(viewModel = viewModel)
                            "FARMER_HOME" -> FarmerHomeScreen(viewModel = viewModel)
                            "CREATE_LISTING" -> FarmerCreateListingScreen(viewModel = viewModel)
                            "FARMER_EARNINGS" -> FarmerEarningsScreen(viewModel = viewModel)
                            "BUYER_HOME" -> BuyerHomeScreen(viewModel = viewModel)
                            "BUYER_DETAIL" -> BuyerDetailScreen(viewModel = viewModel)
                            "ORDER_TRACKING" -> OrderTrackingScreen(viewModel = viewModel)
                            else -> LandingScreen(viewModel = viewModel)
                        }

                        // Global Voice Assistant BottomSheet/Dialog
                        VoiceAgentDialog(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
