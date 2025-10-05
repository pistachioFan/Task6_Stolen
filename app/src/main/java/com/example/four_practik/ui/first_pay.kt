package com.example.four_practik.ui

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.nio.file.WatchEvent


@Composable
fun FirstPayScreen (
    onStartSummChange: (String) -> Unit,
    onProcientChange: (String) -> Unit,
    onNextButtonCliced: () -> Unit,
    onCancelButtonCliced: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier.fillMaxSize().padding(top = 250.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center

    ) {
        var startSumm by remember() { mutableStateOf("") }
        OutlinedTextField(

            value = startSumm,
            onValueChange = { startSumm = it
                            onStartSummChange(it)},
            shape = RoundedCornerShape(15.dp),
            /*label = {
                Text("Стартовая сумма",
                //Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )},*/
            placeholder = {
                Text(
                    text ="Стартовая сумма",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(100.dp, 250.dp)
                )
            },
            singleLine = true,

            )
        Spacer(Modifier.height(30.dp))

        var procient by remember() { mutableStateOf("") }
        OutlinedTextField(
            value = procient,
            onValueChange = { procient = it
                            onProcientChange(it)},
            shape = RoundedCornerShape(15.dp),
            label = {
                Text(
                    "Процент",
                    //Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            singleLine = true,
        )


        Spacer(Modifier.padding(30.dp))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCancelButtonCliced,
                modifier = Modifier.size(width = 150.dp, height = 50.dp),

            ) {
                Text("Отмена", fontSize = 20.sp)
            }

            Spacer(Modifier.padding(20.dp))

            Button(
                onClick = onNextButtonCliced,
                modifier = Modifier.size(width = 150.dp, height = 50.dp),

            ) {
                Text("Далее",fontSize = 20.sp)
            }
        }
    }
}

