package com.craftsilicon.shumul.agency.ui.module.dashboard.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.NavigateModule
import com.craftsilicon.shumul.agency.ui.navigation.NavigateTo
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun CustomerMenu(data: MenuItem, action: (route: NavigateTo) -> Unit) {
    Card(
        onClick = { action(data.navigate) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val size = this.maxHeight.div(2.5f)
            Column(
                modifier = Modifier
                    .matchParentSize(),
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
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                )
            }
        }
    }


}

@Composable
fun CustomerMenus(
    action: (route: NavigateTo) -> Unit
) {
    BoxWithConstraints {
        val width = this.maxWidth
        val itemSize: Dp = width / 2
        val height = itemSize.div(1.5f)
        FlowRow(
            mainAxisSize = SizeMode.Expand,
            mainAxisAlignment = FlowMainAxisAlignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            customerMenus.forEach {
                Box(
                    modifier = Modifier
                        .width(itemSize)
                        .height(height)
                ) {
                    CustomerMenu(data = it, action = action)
                }
            }
        }
    }

}

val customerMenus = mutableListOf(
    MenuItem(
        title = R.string.cash_deposit_,
        icon = R.drawable.deposit,
        navigate = NavigateTo(
            route = Module.Deposit.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.cash_withdrawal_,
        icon = R.drawable.withdraw,
        navigate = NavigateTo(
            route = Module.WithdrawalModule().route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.fund_transfer_,
        icon = R.drawable.round_transfer,
        navigate = NavigateTo(
            route = Module.FundTransferModule.route,
            type = NavigateModule(),
        )
    ), MenuItem(
        title = R.string.account_statement_,
        icon = R.drawable.box_list,
        navigate = NavigateTo(
            route = Module.AccountStatement().route,
            type = NavigateModule(),
        )
    ), MenuItem(
        title = R.string.account_balance_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.AccountBalance.route,
            type = NavigateModule(),
        )
    ), MenuItem(
        title = R.string.account_opening_,
        icon = R.drawable.account_open,
        navigate = NavigateTo(
            route = Module.AccountOpening.Validation.route,
            type = NavigateModule(),
        )
    ),
//    MenuItem(
//        title = R.string.account_to_cash_,
//        icon = R.drawable.account_to_cash,
//        navigate = NavigateTo(
//            route = Module.AccountToCash().route,
//            type = NavigateModule(),
//        )
//    ),
//    MenuItem(
//        title = R.string.cash_to_cash_,
//        icon = R.drawable.cash_to_cash,
//        navigate = NavigateTo(
//            route = Module.CashToCash().route,
//            type = NavigateModule(),
//        )
//    ),
    MenuItem(
        title = R.string.airtime_,
        icon = R.drawable.airtime,
        navigate = NavigateTo(
            route = Module.Airtime.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.data_purchase_,
        icon = R.drawable.airtime,
        navigate = NavigateTo(
            route = Module.DataPurchase().route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.send_remittance_,
        icon = R.drawable.remittance,
        navigate = NavigateTo(
            route = Module.Remittance().route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.pay_remittance_,
        icon = R.drawable.remittance,
        navigate = NavigateTo(
            route = Module.PayRemittance().route,
            type = NavigateModule(),
        )
    )
)
