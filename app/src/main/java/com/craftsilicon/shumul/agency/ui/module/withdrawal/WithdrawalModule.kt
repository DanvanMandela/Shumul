package com.craftsilicon.shumul.agency.ui.module.withdrawal

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.ui.module.dashboard.menu.HORadioButtons
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.setComposable
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import withdrawal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithdrawalModule(data: GlobalData) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection()) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.cash_withdrawal_),
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ), navigationIcon = {
                    IconButton(onClick = {
                        data.controller.navigateUp()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HORadioButtons(
                    radioOptions = withdrawal,
                    onClick = {
                        navController.navigate(it.navigate.route)
                    }
                )
                NavHost(
                    navController = navController,
                    startDestination = Module.WithdrawalModule.Agent.route
                ) {
                    setComposable(route = Module.WithdrawalModule.Agent.route) {
                        BackHandler(enabled = true) {
                            data.controller.navigateUp()
                        }
                        AgentWithdrawalModule(function = {
                            data.controller.navigateUp()
                        })
                    }

                    setComposable(route = Module.WithdrawalModule.Customer.route) {
                        BackHandler(enabled = true) {
                            data.controller.navigateUp()
                        }
                        CustomerWithdrawalModule(function = {
                            data.controller.navigateUp()
                        })
                    }
                }
            }
        }
    }
}