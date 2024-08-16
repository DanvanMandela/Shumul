package com.craftsilicon.shumul.agency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.canhub.cropper.CropImageContract
import com.craftsilicon.shumul.agency.data.AppCallback
import com.craftsilicon.shumul.agency.data.permission.CameraUtil.capturedImage
import com.craftsilicon.shumul.agency.data.permission.ImageCallback
import com.craftsilicon.shumul.agency.data.permission.imageOption
import com.craftsilicon.shumul.agency.data.source.model.LocalViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.module.SplashModule
import com.craftsilicon.shumul.agency.ui.module.account.AccountOpeningDocumentModule
import com.craftsilicon.shumul.agency.ui.module.account.AccountOpeningModule
import com.craftsilicon.shumul.agency.ui.module.account.AccountOpeningMoreDetail
import com.craftsilicon.shumul.agency.ui.module.account.AccountOpeningPersonalDetail
import com.craftsilicon.shumul.agency.ui.module.account.statement.AccountStatement
import com.craftsilicon.shumul.agency.ui.module.airtime.AirtimeModule
import com.craftsilicon.shumul.agency.ui.module.balance.AccountBalance
import com.craftsilicon.shumul.agency.ui.module.cash.account.AccountToCashModule
import com.craftsilicon.shumul.agency.ui.module.cash.cash.CashToCashModule
import com.craftsilicon.shumul.agency.ui.module.dashboard.DashboardModule
import com.craftsilicon.shumul.agency.ui.module.data.DataPurchaseModule
import com.craftsilicon.shumul.agency.ui.module.data.DataPurchaseSubscribeModule
import com.craftsilicon.shumul.agency.ui.module.deposit.DepositModule
import com.craftsilicon.shumul.agency.ui.module.fund.FundTransferModule
import com.craftsilicon.shumul.agency.ui.module.login.ActivationModule
import com.craftsilicon.shumul.agency.ui.module.login.LoginModule
import com.craftsilicon.shumul.agency.ui.module.pin.ChangePin
import com.craftsilicon.shumul.agency.ui.module.remittance.RemittanceModule
import com.craftsilicon.shumul.agency.ui.module.withdrawal.WithdrawalModule
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.setComposable
import com.craftsilicon.shumul.agency.ui.theme.ShumulTheme
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), AppCallback {
    private val local by viewModels<LocalViewModelImpl>()
    private val remote by viewModels<RemoteViewModelImpl>()
    private var imageCallback: ImageCallback? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        try {
            if (result.isSuccessful) {
                val uriContent = result.uriContent
                if (uriContent != null) {
                    val image = this@MainActivity.capturedImage(uriContent)
                    imageCallback?.onImage(image, uriContent.toString())
                }
            } else {
                val exception = result.error
                AppLogger.instance.appLog("CROPPER:ERROR", "${exception?.localizedMessage}")
            }
        } catch (e: Exception) {
            e.localizedMessage?.let { AppLogger.instance.appLog("CROPPER:ERROR", it) }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShumulTheme {
                val systemUiController = rememberSystemUiController()
                val statusBarColor = MaterialTheme.colorScheme.primary
                SideEffect {
                    systemUiController.setStatusBarColor(statusBarColor)
                }
                val navController = rememberNavController()
                val global = GlobalData(
                    callback = this,
                    controller = navController
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Module.Splash.route
                    ) {
                        setComposable(route = Module.Splash.route) {
                            BackHandler(enabled = true) {
                                finish()
                            }
                            SplashModule(data = global)
                        }

                        setComposable(route = Module.Login.route) {
                            BackHandler(enabled = true) {
                                finish()
                            }
                            LoginModule(data = global)
                        }

                        setComposable(route = Module.Dashboard.route) {
                            BackHandler(enabled = true) {
                                finish()
                            }
                            DashboardModule(data = global)
                        }

                        setComposable(route = Module.Deposit.route) {
                            DepositModule(data = global)
                        }

                        setComposable(route = Module.PinChange.route) {
                            ChangePin(data = global)
                        }

                        setComposable(route = Module.AccountBalance.route) {
                            AccountBalance(data = global)
                        }

                        setComposable(route = Module.WithdrawalModule().route) {
                            WithdrawalModule(data = global)
                        }

                        setComposable(route = Module.AccountToCash().route) {
                            AccountToCashModule(data = global)
                        }

                        setComposable(route = Module.CashToCash().route) {
                            CashToCashModule(data = global)
                        }


                        setComposable(route = Module.FundTransferModule.route) {
                            FundTransferModule(data = global)
                        }

                        setComposable(route = Module.Activation.route) {
                            BackHandler(enabled = true) {
                                finish()
                            }
                            ActivationModule(data = global)
                        }

                        setComposable(route = Module.AccountOpening.Validation.route) {
                            AccountOpeningModule(data = global)
                        }

                        setComposable(route = Module.AccountOpening.PersonalDetail.route) {
                            AccountOpeningPersonalDetail(data = global)
                        }

                        setComposable(route = Module.AccountOpening.Documents.route) {
                            AccountOpeningDocumentModule(data = global)
                        }

                        setComposable(route = Module.AccountOpening.MoreDetail.route) {
                            AccountOpeningMoreDetail(data = global)
                        }

                        setComposable(route = Module.AccountStatement().route) {
                            AccountStatement(data = global)
                        }

                        setComposable(route = Module.Airtime.route) {
                            AirtimeModule(data = global)
                        }

                        setComposable(route = Module.DataPurchase.Validate.route) {
                            DataPurchaseModule(data = global)
                        }
                        setComposable(route = Module.DataPurchase.Subscribe.route) {
                            DataPurchaseSubscribeModule(data = global)
                        }

                        setComposable(route = Module.Remittance().route) {
                            RemittanceModule(data = global)
                        }

                    }
                }
                LaunchedEffect(key1 = Unit) {
                    onUserInteraction()
                    onInActivity(controller = navController)
                    remote.permission.generalAccess()
                }
            }


        }
    }

    override fun onImage(call: ImageCallback) {
        imageCallback = call
        cropImage.imageOption()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        local.interaction.onUserInteracted()
    }

    private fun onInActivity(controller: NavController) {
        val state = local.preferences.inActivity.asLiveData()
        state.observe(this) {
            if (it != null) {
                val current = controller.currentDestination?.route
                if (it && current != Module.Splash.route
                    && current != Module.AccountOpening.Documents.route
                ) controller.navigate(Module.Splash.route)
            }
        }
    }

}

