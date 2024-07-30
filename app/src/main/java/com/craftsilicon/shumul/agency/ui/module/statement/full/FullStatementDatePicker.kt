package com.craftsilicon.shumul.agency.ui.module.statement.full

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullStatementDatePicker(action: (from: Long, to: Long) -> Unit, close: () -> Unit) {
    val dateRangePickerState = rememberDateRangePickerState(yearRange = 2024..2024)
    val snackState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var fromDate by remember {
        mutableStateOf("")
    }
    var toDate by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = dateRangePickerState.selectedEndDateMillis) {
        (if (dateRangePickerState.selectedEndDateMillis != null)
            dateRangePickerState.selectedEndDateMillis?.toDate()
        else context.getString(R.string.end_date_))?.let {
            toDate = it
        }

    }

    LaunchedEffect(key1 = dateRangePickerState.selectedStartDateMillis) {
        (if (dateRangePickerState.selectedStartDateMillis != null)
            dateRangePickerState.selectedStartDateMillis?.toDate()
        else context.getString(R.string.start_date_))?.let {
            fromDate = it
        }
    }



    Dialog(
        onDismissRequest = { close() }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    DateRangePicker(
                        state = dateRangePickerState, title = {
                            TopAppBar(title = {
                                Text(
                                    text = stringResource(id = R.string.select_range_),
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 2.dp)
                                )
                            }, colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ), navigationIcon = {
                                IconButton(onClick = {
                                    close()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        modifier = Modifier.size(ButtonDefaults.IconSize),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            })

                        }, colors = DatePickerDefaults.colors(
                            dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.2f
                            )
                        ), headline = {
                            Row(
                                modifier = Modifier
                                    .padding(bottom = ButtonDefaults.IconSpacing),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                                Text(
                                    text = fromDate,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    style = MaterialTheme.typography.titleMedium,

                                    )
                                HorizontalDivider(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .width(ButtonDefaults.IconSpacing),
                                    thickness = 2.dp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = toDate,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }, showModeToggle = false, modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Button(
                        onClick = {
                            scope.launch {
                                if (fromDate == context.getString(R.string.start_date_)) {
                                    snackState.showSnackbar(
                                        context
                                            .getString(R.string.select_start_date_)
                                    )
                                } else if (toDate == context.getString(R.string.end_date_)) {
                                    snackState.showSnackbar(
                                        context
                                            .getString(R.string.select_end_date_)
                                    )
                                } else action(
                                    dateRangePickerState.selectedStartDateMillis!!,
                                    dateRangePickerState.selectedEndDateMillis!!
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.continue_),
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                }
            }

            SnackbarHost(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(vertical = 16.dp),
                hostState = snackState
            ) { snack: SnackbarData ->
                CustomSnackBar(
                    snack.visuals.message,
                    isRtl = false
                )
            }
        }
    }
}

val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

fun Long.toDate(): String {
    return outputFormat.format(this)
}