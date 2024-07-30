package com.craftsilicon.shumul.agency.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.craftsilicon.shumul.agency.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext


private val dropdownMenuVerticalPadding = 8.dp


@Composable
fun EditDropDown(
    label: String?,
    data: MutableStateFlow<SnapshotStateList<DropDownResult>>,
    onValue: (value: DropDownResult) -> Unit = {}
) {
    var option by rememberSaveable { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    val dataState by data.collectAsState()
    val dataStateMutex = Mutex()



    if (dataState.isNotEmpty())
        LaunchedEffect(key1 = Unit) {
            delay(200L)
            withContext(Dispatchers.IO) {
                if (dataState.isEmpty()) {
                    option = String()
                    return@withContext
                }
                val display = dataStateMutex.withLock {
                    dataState.singleOrNull { it.display }
                }
                if (display != null && display.display) {
                    option = "${display.desc}"
                    onValue(
                        DropDownResult(
                            key = display.key,
                            desc = display.desc,
                            extra = display.extra
                        )
                    )
                } else option = String()

            }
        }


    val items = List(10) { it.toString() }
    val itemHeights = remember { mutableStateMapOf<Int, Int>() }
    val baseHeight = 330.dp
    val density = LocalDensity.current
    val maxHeight = remember(itemHeights.toMap()) {
        if (itemHeights.keys.toSet() != items.indices.toSet()) {
            return@remember baseHeight
        }
        val baseHeightInt = with(density) { baseHeight.toPx().toInt() }

        var sum = with(density) { dropdownMenuVerticalPadding.toPx().toInt() } * 2
        for ((_, itemSize) in itemHeights.toSortedMap()) {
            sum += itemSize
            if (sum >= baseHeightInt) {
                return@remember with(density) { (sum - itemSize / 2).toDp() }
            }
        }
        baseHeight
    }



    Box(modifier = Modifier
        .fillMaxWidth()
        .onGloballyPositioned { layoutCoordinates ->
            rowSize = layoutCoordinates.size.toSize()
        }) {

        OutlinedTextField(
            value = option,
            onValueChange = {
                option = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = "$label",
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    style = MaterialTheme.typography.labelMedium,
                )

            }, keyboardOptions = KeyboardOptions(),
            textStyle = TextStyle(
                fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            ),
            singleLine = true,
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    Icon(
                        Icons.Outlined.ArrowDropDown,
                        null,
                        modifier = Modifier.size(ButtonDefaults.IconSize.times(1.5f)),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier
                .width(with(LocalDensity.current)
                { rowSize.width.toDp() })
                .heightIn(0.dp, maxHeight)
        ) {
            val sorted = dataState.sortedBy { it.desc }
            sorted.onEachIndexed { index, entry ->

                DropdownMenuItem(text = {

                    Text(
                        text = "${entry.desc}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                    )
                }, onClick = {
                    expanded = false
                    option = "${entry.desc}"
                    onValue(
                        DropDownResult(
                            key = entry.key,
                            desc = entry.desc,
                            extra = entry.extra
                        )
                    )
                }, modifier = Modifier.onSizeChanged {
                    itemHeights[index] = it.height
                })

            }
        }
        Spacer(modifier = Modifier
            .matchParentSize()
            .background(Color.Transparent)
            .padding(10.dp)
            .clickable(
                onClick = { expanded = !expanded }
            )
        )
    }

}




data class DropDownResult(
    val key: Any? = String(),
    val desc: String? = String(),
    val extra: Any? = String(),
    var display: Boolean = false
)



@Composable
@Preview
private fun EditDropDownPreview() {
    EditDropDown(label = "", data = MutableStateFlow(SnapshotStateList()))
}