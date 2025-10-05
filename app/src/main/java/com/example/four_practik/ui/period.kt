package com.example.four_practik.ui

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

@Composable
fun PeriodScreen(
    onEveryMonthChange: (String) -> Unit,
    onPeriodChange: (String) -> Unit,
    onNextButtonCliced: () -> Unit,
    onCencelButtonCliced:() -> Unit,
    modifier: Modifier = Modifier
) {

    Column(

        modifier = Modifier.fillMaxSize().padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var everymounthdodep by remember() { mutableStateOf("") }
        OutlinedTextField(
            value = everymounthdodep,
            onValueChange = {everymounthdodep = it
                            onEveryMonthChange(it)},
            label = { Text("Введите ежемесечный платёж") }
        )

        Spacer (Modifier.padding(30.dp))

        var period by remember { mutableStateOf("") }
        OutlinedTextField(
            value = period,
            onValueChange = { period = it
                            onPeriodChange(it)},
            label = { Text("Ведите период")}
        )

        Spacer(modifier = Modifier.padding(30.dp))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                modifier = Modifier.size(width = 150.dp, height = 50.dp),
                onClick = onCencelButtonCliced
            ) {
                Text("Отмена", fontSize = 20.sp)
            }

            Spacer(Modifier.padding(20.dp))

            Button(
                modifier = Modifier.size(width = 150.dp, height = 50.dp),
                onClick = onNextButtonCliced
            ) {
                Text("Далее",fontSize = 20.sp)
            }
        }
    }




}