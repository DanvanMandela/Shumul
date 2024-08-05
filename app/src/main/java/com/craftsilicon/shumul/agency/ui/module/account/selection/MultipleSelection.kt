package com.craftsilicon.shumul.agency.ui.module.account.selection

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.craftsilicon.shumul.agency.R
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun MultipleSelection(
    title: String,
    data: MutableList<SelectionItemData>,
    click: (value: HashMap<String, String?>) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val holder: HashMap<String, String?> = remember {
        hashMapOf()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)

        )
        Spacer(modifier = Modifier.size(16.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            val itemSize: Dp = screenWidthDp / 2
            FlowRow(
                mainAxisSize = SizeMode.Expand,
                mainAxisAlignment = FlowMainAxisAlignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                data.forEach {
                    Box(
                        modifier = Modifier
                            .width(itemSize)
                            .height(itemSize.div(2.5f))
                    ) {
                        SelectionItem(data = it,
                            select = { s, b ->
                                if (holder.containsKey(s) && !b)
                                    holder.remove(s)
                                else holder[s] = s
                                click(holder)
                            }
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun SelectionItem(
    data: SelectionItemData,
    select: (value: String, selected: Boolean) -> Unit
) {
    var selected by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checkbox(checked = selected,
            onCheckedChange = {
                selected = !selected
                select(
                    context.getString(data.title),
                    selected
                )
            })
        Text(
            text = stringResource(id = data.title),
            style = MaterialTheme.typography.labelMedium,
            maxLines = 3,
            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 8.dp)

        )
    }
}

data class SelectionItemData(
    @StringRes val title: Int
)

val specialOfficer = mutableListOf(
    SelectionItemData(title = R.string.can_send_greeting_),
    SelectionItemData(title = R.string.mobile_alert_),
    SelectionItemData(title = R.string.statement_online_),
    SelectionItemData(title = R.string.can_send_special_officer_),
    SelectionItemData(title = R.string.can_special_associate_officer_)
)


@Composable
@Preview(showBackground = true)
fun MultipleSelectionPreview() {
    MultipleSelection("Special Officer", specialOfficer) {}
}

