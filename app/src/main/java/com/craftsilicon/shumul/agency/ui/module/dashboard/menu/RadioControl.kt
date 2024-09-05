import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.ui.module.dashboard.menu.MenuItem
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.NavigateModule
import com.craftsilicon.shumul.agency.ui.navigation.NavigateTo


val accountStatement = mutableListOf(
    MenuItem(
        title = R.string.full_statement_title_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.AccountStatement.Full.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.mini_statement__,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.AccountStatement.Mini.route,
            type = NavigateModule(),
        )
    )
)


val withdrawal = mutableListOf(
    MenuItem(
        title = R.string.agent_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.WithdrawalModule.Agent.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.customer_,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.WithdrawalModule.Customer.route,
            type = NavigateModule(),
        )
    )
)


val remittance = mutableListOf(
    MenuItem(
        title = R.string.agent_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.Remittance.Agent.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.customer_,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.Remittance.Customer.route,
            type = NavigateModule(),
        )
    )
)


val accountCash = mutableListOf(
    MenuItem(
        title = R.string.generate_token_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.AccountToCash.Generate.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.redeem_token_,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.AccountToCash.Redeem.route,
            type = NavigateModule(),
        )
    )
)

val cashToCash = mutableListOf(
    MenuItem(
        title = R.string.generate_token_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.CashToCash.Generate.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.redeem_token_,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.CashToCash.Redeem.route,
            type = NavigateModule(),
        )
    )
)


val remittancePay = mutableListOf(
    MenuItem(
        title = R.string.agent_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.Remittance.Agent.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.customer_,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.Remittance.Customer.route,
            type = NavigateModule(),
        )
    )
)


val dataPurchase = mutableListOf(
    MenuItem(
        title = R.string.agent_,
        icon = R.drawable.bank_icon,
        navigate = NavigateTo(
            route = Module.DataPurchase.Agent.route,
            type = NavigateModule(),
        )
    ),
    MenuItem(
        title = R.string.customer_,
        icon = R.drawable.bank_note,
        navigate = NavigateTo(
            route = Module.DataPurchase.Customer.route,
            type = NavigateModule(),
        )
    )
)


