package com.example.four_practik

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.four_practik.model.VkladUiState
import com.example.four_practik.ui.StartScreen
import com.example.four_practik.ui.VkladViewModel
import com.example.four_practik.ui.FirstPayScreen
import com.example.four_practik.ui.ListScreen
import com.example.four_practik.ui.PeriodScreen
import com.example.four_practik.ui.SummaryScreen
import com.example.four_practik.ui.VkladViewModelFactory

enum class VkladScreen() {
    Start,
    First,
    Period,
    Summary,

    List
}

@Composable
fun IppotecaApp (
    navController: NavHostController = rememberNavController()
) {

    //val viewModel: VkladViewModel = viewModel()
    val context = LocalContext.current.applicationContext as VkladApplication
    val viewModel: VkladViewModel = viewModel(
        factory = VkladViewModelFactory(context.repository)
    )
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
                    onListButtonClicked = {navController.navigate(VkladScreen.List.name)},
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable (route = VkladScreen.First.name) {
                FirstPayScreen(
                    onStartSummChange = { viewModel.uodateStartSumm(it)},
                    onProcientChange = { viewModel.updateProcient(it)},
                    onNextButtonCliced = { navController.navigate(VkladScreen.Period.name)},
                    onCancelButtonCliced = {onCancelButtonClicked(viewModel, navController)},
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable (route = VkladScreen.Period.name) {
                PeriodScreen (
                    onEveryMonthChange = {viewModel.updateEveryMounthPay(it)},
                    onPeriodChange = { viewModel.updatePeriod(it)},
                    onNextButtonCliced = { navController.navigate(VkladScreen.Summary.name)},
                    onCencelButtonCliced = {onCancelButtonClicked(viewModel, navController)},
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable (route = VkladScreen.Summary.name) {
                SummaryScreen(
                    viewModel = viewModel,
                    onHomeButtonCliced = {onCancelButtonClicked(viewModel, navController)},
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable (route = VkladScreen.List.name) {
                ListScreen(
                    viewModel = viewModel,
                    onBackButtonClicked = {navController.popBackStack()}
                )
            }
        }
    }
}


fun onCancelButtonClicked(
    viewModel: VkladViewModel,
    navController: NavHostController
){
    viewModel.resetVklad()
    navController.popBackStack(VkladScreen.Start.name, inclusive = false)
}