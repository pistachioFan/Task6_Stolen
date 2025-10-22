package com.example.four_practik.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.four_practik.data.Vklads

@Composable
fun ListScreen(
    viewModel: VkladViewModel = viewModel(),
    onBackButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val UiState = viewModel.uiState
    val vkladUiState by viewModel.listUiState.collectAsState()

    Column()
    {
        VkladList(
            vkladList = vkladUiState.vkladList,
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Button(
            onClick = onBackButtonClicked
        ) {
            Text("Назад")
        }
    }

}

@Composable
private fun VkladList(
    vkladList: List<Vklads>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = vkladList, key = { it.id }) { item ->
            InventoryItem(item = item,
                modifier = Modifier
                    .padding(20.dp))
        }
    }
}

@Composable
private fun InventoryItem(
    item: Vklads, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.startSumm.toString(),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.everyMounthPay.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.summary.toString(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.procient.toString(),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.period.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}