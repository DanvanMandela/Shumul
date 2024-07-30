package com.craftsilicon.shumul.agency.ui.module.statement

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.MiniBean

@Composable
fun MiniDataItem(data: MiniBean) {


    Card(
        onClick = { },
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (!data.amount.isNullOrBlank())
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.amount_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                    Text(
                        text = "${data.amount}",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.transaction_),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                )
                Text(
                    text = "${data.particulars}",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                )
            }
            if (!data.toName.isNullOrBlank())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.to_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                    Text(
                        text = "${data.toName}",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                }
            if (!data.toAccount.isNullOrBlank())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.to_account_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                    Text(
                        text = "${data.toAccount}",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                }
            if (!data.fromName.isNullOrBlank())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.from_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                    Text(
                        text = "${data.fromName}",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                }
            if (!data.fromAccount.isNullOrBlank())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.from_account_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                    Text(
                        text = "${data.fromAccount}",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                }


            if (!data.close.isNullOrBlank())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.closing_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                    Text(
                        text = "${data.close}",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                }

            if (!data.currency.isNullOrBlank())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.currency_),
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                    Text(
                        text = "${data.currency}",
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)

                    )
                }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.when_),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                )
                Text(
                    text = "${data.date}",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                )
            }
        }
    }
}

