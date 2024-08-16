package com.craftsilicon.shumul.agency.ui.navigation

import androidx.navigation.NavController
import com.craftsilicon.shumul.agency.data.AppCallback
import com.craftsilicon.shumul.agency.data.bean.BalanceBean
import com.craftsilicon.shumul.agency.data.bean.BalanceResponse
import com.craftsilicon.shumul.agency.data.bean.MiniData


enum class ModuleState {
    LOADING, ERROR, DISPLAY, SUCCESS
}

enum class ModuleAction {
    Validate, Proceed
}

sealed class Module(open val route: String, open val name: String = String()) {
    data object Splash : Module(route = "splash")
    data object Dashboard : Module(route = "dashboard?key={key}", name = "dashboard")
    data object Login : Module(route = "login")
    data object Activation : Module(route = "activation")
    data object Deposit : Module(route = "deposit")
    data object PinChange : Module(route = "pinChange")
    data object Airtime : Module(route = "airtime")

    data object AccountBalance : Module(route = "accountBalance")
    open class WithdrawalModule(override val route: String = "withdrawal") : Module(route) {
        data object Agent : WithdrawalModule(route = "agent")
        data object Customer : WithdrawalModule(route = "customer")
    }


    open class DataPurchase(
        override val route: String = "dataPurchase",
        override val name: String = route
    ) : Module(route) {
        data object Validate : DataPurchase(route = "validate")
        data object Subscribe : DataPurchase(
            route = "subscribe?key={key}",
            name = "subscribe"
        )
    }

    data object FundTransferModule : Module(route = "fundTransfer")

    open class AccountOpening : Module(route = "accountOpening") {
        data object Validation : Module(route = "customerValidation")
        data object PersonalDetail : Module(route = "personalDetail")
        data object MoreDetail : Module(route = "moreDetail")
        data object Documents : Module(route = "document")

    }

    open class Remittance : Module(route = "remittance") {
        data object Agent : Module(route = "agentRemittance")
        data object Customer : Module(route = "customerRemittance")
    }

    open class AccountStatement(override val route: String = "statement") : Module(route) {
        data object Mini : AccountStatement(route = "mini")
        data object Full : AccountStatement(route = "full")
    }

    open class AccountToCash(override val route: String = "accountToCash") : Module(route) {
        data object Generate : AccountToCash(route = "generate")
        data object Redeem : AccountToCash(route = "redeem")
    }

    open class CashToCash(override val route: String = "cashToCash") : Module(route) {
        data object Generate : CashToCash(route = "generate")
        data object Redeem : CashToCash(route = "redeem")
    }
}

object ModuleArguments {
    const val ARG_KEY = "key"
}

data class GlobalData(
    var callback: AppCallback? = null,
    var controller: NavController
)


sealed class NavigationType
sealed class NavigateDialog : NavigationType() {
    sealed class Balance : NavigateDialog() {
        data class OnBalance(val data: BalanceResponse) : Balance()

        data object OnRequest : Balance()
    }

    sealed class Statement : NavigateDialog() {
        data object Mini : Statement()

        data class OnStatement(val data: MiniData) : Statement()

        data object Full : Statement()
    }
}


open class NavigateModule : NavigationType() {
    sealed class Mini : NavigateModule() {
        data class OnBalance(val data: BalanceBean) : Mini()

        data object OnRequest : Mini()
    }
}


data class NavigateTo(
    val route: String,
    val type: NavigationType
)
