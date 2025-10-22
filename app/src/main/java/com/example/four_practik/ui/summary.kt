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
import androidx.compose.material3.ButtonColors
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.four_practik.ui.theme.Black
import com.example.four_practik.ui.theme.White
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SummaryScreen(
    viewModel: VkladViewModel = viewModel(),
    onHomeButtonCliced:() -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Стартовая сумма: ${state.collectAsState().value.startSumm}",
            modifier = Modifier.padding(bottom = 10.dp),
            fontSize = 20.sp,)
        Text(text = "Процент: ${state.collectAsState().value.procient}",
            modifier = Modifier.padding(bottom = 10.dp),
            fontSize = 20.sp)
        Text(text = "Период: ${state.collectAsState().value.period} месяцев",
            modifier = Modifier.padding(bottom = 10.dp),
            fontSize = 20.sp)
        Text(text = "Итоговая сумма: ${state.collectAsState().value.summary}",
            modifier = Modifier.padding(bottom = 10.dp),
            fontSize = 20.sp)

        Spacer(Modifier.padding(top = 30.dp))

        Button(
            onClick = onHomeButtonCliced,
            modifier = Modifier.size(height = 50.dp, width = 150.dp),
            colors = ButtonColors(Black, contentColor = White, disabledContentColor = Black, disabledContainerColor = White)

        ) {
            Text("Домой",
                fontSize = 30.sp)
        }

        Spacer(Modifier.padding(top = 30.dp))

        Button(
            onClick = { coroutineScope.launch {
                viewModel.saveVklad()
                onHomeButtonCliced} },
        ) {
            Text("Сохранить")
        }
    }
}

@Preview (
    showBackground = true
)
@Composable
fun SummaryScreenPreview(
) {
    SummaryScreen(
        onHomeButtonCliced = {}
    )
}
