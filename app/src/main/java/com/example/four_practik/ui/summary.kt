package com.example.four_practik.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.four_practik.ui.theme.White

@Composable
fun SummaryScreen(
    viewModel: VkladViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Стартовая сумма: ${state.collectAsState().value.startSumm}",
            modifier = Modifier.padding(top = 5.dp, end = 5.dp).size(width = 250.dp, height = 20.dp),
            textAlign = TextAlign.Center,)
        Text(text = "Процент: ${state.collectAsState().value.procient}")
        Text(text = "Период: ${state.collectAsState().value.period} месяцев")
        Text(text = "Итоговая сумма: ${state.collectAsState().value.summary}")
    }
}

@Preview (
    showBackground = true
)
@Composable
fun SummaryScreenPreview(
) {
    SummaryScreen(
    )
}
