package com.craftsilicon.shumul.agency.ui.module.dashboard.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.NavigateDialog
import com.craftsilicon.shumul.agency.ui.navigation.NavigateModule
import com.craftsilicon.shumul.agency.ui.navigation.NavigateTo
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun AgentMenu(data: MenuItem, action: (route: NavigateTo) -> Unit) {
    BoxWithConstraints(modifier = Modifier
        .clickable { action(data.navigate) }
        .clip(RoundedCornerShape(8.dp))
        .fillMaxSize()
    ) {
        val size = this.maxWidth.div(2.5f)
        Column(
            modifier = Modifier.matchParentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = data.icon),
                contentDescription = null,
                modifier = Modifier.size(size)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = stringResource(id = data.title),
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp)
            )

        }
    }
}

@Composable
fun AgentMenus(
    action: (route: NavigateTo) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    BoxWithConstraints(modifier = Modifier.width(screenWidthDp)) {
        val width = this.maxWidth
        val itemSize: Dp = width / 4
//        FlowRow(
//            mainAxisSize = SizeMode.Expand,
//            mainAxisAlignment = FlowMainAxisAlignment.Start,
//            modifier = Modifier
//                .width(width)
//                .background(color = MaterialTheme.colorScheme.background)
//        ) {
//            agentMenus.forEach {
//                Box(
//                    modifier = Modifier.size(itemSize)
//                ) {
//                    AgentMenu(data = it, action = action)
//                }
//            }
//        }
        Row(
            modifier = Modifier
                .width(width)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            agentMenus.forEach {
                Box(
                    modifier = Modifier.size(itemSize)
                ) {
                    AgentMenu(data = it, action = action)
                }
            }
        }
    }


}


val agentMenus = mutableListOf(
    MenuItem(
        title = R.string.veiw_balance_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.Dashboard.route,
            type = NavigateDialog.Balance.OnRequest,
        )
    ),
    MenuItem(
        title = R.string.mini_statement_,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.Dashboard.route,
            type = NavigateDialog.Statement.Mini,
        )
    ),
    MenuItem(
        title = R.string.full_statement_,
        icon = R.drawable.box_list,
        navigate = NavigateTo(
            route = Module.Dashboard.route,
            type = NavigateDialog.Statement.Full,
        )
    ), MenuItem(
        title = R.string.change_pin_,
        icon = R.drawable.shield_security,
        navigate = NavigateTo(
            route = Module.PinChange.route,
            type = NavigateModule(),
        )
    )
)
