package com.craftsilicon.shumul.agency.ui.module.dashboard.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.craftsilicon.shumul.agency.R


@Composable
fun HORadioButtons(
    radioOptions: MutableList<MenuItem>,
    onClick: (selectedOption: MenuItem) -> Unit = {},
) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight(),
    ) {

        Row(Modifier.fillMaxWidth()) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                onClick(text)
                            }
                        )
                        .padding(horizontal = 4.dp), verticalAlignment = CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        modifier = Modifier.padding(all = Dp(value = 4F)),
                        onClick = {
                            onOptionSelected(text)
                            onClick(text)
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = stringResource(id = text.title),
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold))
                    )
                }
            }
        }
    }
}


@Composable
fun SelectionRadio(
    option: MenuItem,
    selected: Boolean,
    onClick: (option: MenuItem) -> Unit = {},
    modifier: Modifier
) {
    Button(
        onClick = { onClick(option) },
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f), horizontalAlignment = CenterHorizontally
        ) {
            RadioButton(
                selected = selected,
                modifier = Modifier.padding(all = Dp(value = 4F)),
                onClick = {
                    onClick(option)
                }, colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = stringResource(id = option.title),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold))
            )
        }
    }
}



