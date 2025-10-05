package com.example.four_practik

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.four_practik.model.VkladUiState
import com.example.four_practik.ui.StartScreen
import com.example.four_practik.ui.VkladViewModel
import com.example.four_practik.ui.FirstPayScreen
import com.example.four_practik.ui.PeriodScreen
import com.example.four_practik.ui.SummaryScreen

enum class VkladScreen() {
    Start,
    First,
    Period,
    Summary
}

@Composable
fun IppotecaApp (
    navController: NavHostController = rememberNavController()
) {

    val viewModel: VkladViewModel = viewModel()

    Scaffold { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = VkladScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable (route = VkladScreen.Start.name) {
                StartScreen(
                    onStartOrderButtonClicked = { navController.navigate(VkladScreen.First.name)},
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable (route = VkladScreen.First.name) {
                FirstPayScreen(
                    onStartSummChange = { viewModel.uodateStartSumm(it)},
                    onProcientChange = { viewModel.updateProcient(it)},
                    onNextButtonCliced = { navController.navigate(VkladScreen.Period.name)},
                    onCancelButtonCliced = {},
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable (route = VkladScreen.Period.name) {
                PeriodScreen (
                    onEveryMonthChange = {viewModel.updateEveryMounthPay(it)},
                    onPeriodChange = { viewModel.updatePeriod(it)},
                    onNextButtonCliced = { navController.navigate(VkladScreen.Summary.name)},
                    onCencelButtonCliced = {},
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable (route = VkladScreen.Summary.name) {
                SummaryScreen(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
