package com.craftsilicon.shumul.agency.ui.module.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.Account
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.module.dashboard.balance.AgentBalance
import com.craftsilicon.shumul.agency.ui.module.dashboard.balance.BalanceModuleResponse
import com.craftsilicon.shumul.agency.ui.module.dashboard.balance.balanceFunc
import com.craftsilicon.shumul.agency.ui.module.dashboard.menu.AgentMenus
import com.craftsilicon.shumul.agency.ui.module.dashboard.menu.CustomerMenus
import com.craftsilicon.shumul.agency.ui.module.statement.AgentFullMiniModule
import com.craftsilicon.shumul.agency.ui.module.statement.MiniStatementModuleResponse
import com.craftsilicon.shumul.agency.ui.module.statement.full.FullStatementDatePicker
import com.craftsilicon.shumul.agency.ui.module.statement.fullFunc
import com.craftsilicon.shumul.agency.ui.module.statement.miniFunc
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.navigation.NavigateDialog
import com.craftsilicon.shumul.agency.ui.navigation.NavigateModule
import com.craftsilicon.shumul.agency.ui.navigation.NavigationType
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardModule(data: GlobalData) {
    val context = LocalContext.current
    val work = hiltViewModel<WorkViewModel>()
    val owner = LocalLifecycleOwner.current
    val snackState = remember { SnackbarHostState() }
    val model = hiltViewModel<RemoteViewModelImpl>()
    val scope = rememberCoroutineScope()
    val user = model.preferences.userData.collectAsState().value
    val accountState = model.preferences.currentAccount.collectAsState().value
    val account: MutableState<Account?> = remember { mutableStateOf(accountState) }
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }
    var navType: NavigationType? by remember {
        mutableStateOf(null)
    }
    val hello = model.preferences.helloWorld.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var action: () -> Unit = {}



    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection()) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.welcome_back_),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = "${user?.firstName} ${user?.lastName}",
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                },

                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ), actions = {
                    IconButton(onClick = {
                        model.preferences.helloWorld("nothing")
                        data.controller.navigate(Module.Login.route)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                }
            )
        }) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                when (screenState) {
                    ModuleState.LOADING -> LoadingModule()
                    ModuleState.ERROR,
                    ModuleState.DISPLAY,
                    ModuleState.SUCCESS -> Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AccountHolder(model = model, account = {
                            account.value = it
                        })
                        Spacer(modifier = Modifier.size(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {

                            Text(
                                text = stringResource(id = R.string.agent_menu_),
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            AgentMenus { nav ->
                                val use = model.userState
                                when (nav.type) {
                                    is NavigateDialog -> when (nav.type) {
                                        NavigateDialog.Balance.OnRequest -> {
                                            action = {
                                                model.web(
                                                    path = "${model.deviceData?.agent}",
                                                    data = balanceFunc(
                                                        account = "${account.value?.account}",
                                                        fromAccount = "${account.value?.account}",
                                                        mobile = "${use?.mobile}",
                                                        agentId = "${account.value?.agentID}",
                                                        model = model,
                                                        pin = "$hello",
                                                        context = context
                                                    )!!,
                                                    state = { screenState = it },
                                                    onResponse = { response ->
                                                        BalanceModuleResponse(
                                                            response = response,
                                                            model = model,
                                                            onError = { error ->
                                                                screenState = ModuleState.ERROR
                                                                scope.launch {
                                                                    snackState.showSnackbar(
                                                                        message = "$error"
                                                                    )
                                                                }
                                                            },
                                                            onSuccess = { message ->
                                                                scope.launch {
                                                                    screenState =
                                                                        ModuleState.DISPLAY
                                                                    delay(200)
                                                                    navType = NavigateDialog
                                                                        .Balance
                                                                        .OnBalance(message!!)
                                                                    showDialog = true
                                                                }
                                                            }, onToken = {
                                                                work.routeData(owner, object :
                                                                    WorkStatus {
                                                                    override fun workDone(b: Boolean) {
                                                                        if (b) action.invoke()
                                                                    }

                                                                    override fun progress(p: Int) {
                                                                        AppLogger.instance.appLog(
                                                                            "DATA:Progress",
                                                                            "$p"
                                                                        )
                                                                    }
                                                                })
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                            action.invoke()
                                        }


                                        is NavigateDialog.Statement.Full -> scope.launch {
                                            screenState = ModuleState.DISPLAY
                                            delay(200)
                                            navType = NavigateDialog
                                                .Statement
                                                .Full
                                            showDialog = true
                                        }

                                        is NavigateDialog.Statement.Mini -> {
                                            action = {
                                                model.web(
                                                    path = "${model.deviceData?.agent}",
                                                    data = miniFunc(
                                                        account = "${account.value?.account}",
                                                        mobile = "${use?.mobile}",
                                                        agentId = "${account.value?.agentID}",
                                                        model = model,
                                                        pin = "$hello",
                                                        context = context
                                                    )!!,
                                                    state = { screenState = it },
                                                    onResponse = { response ->
                                                        MiniStatementModuleResponse(
                                                            response = response,
                                                            model = model,
                                                            onError = { error ->
                                                                screenState = ModuleState.ERROR
                                                                scope.launch {
                                                                    snackState.showSnackbar(
                                                                        message = "$error"
                                                                    )
                                                                }
                                                            },
                                                            onSuccess = { message ->
                                                                scope.launch {
                                                                    screenState =
                                                                        ModuleState.DISPLAY
                                                                    message?.title =
                                                                        model.resourceProvider
                                                                            .getString(R.string.mini_statement__)
                                                                    delay(200)
                                                                    navType = NavigateDialog
                                                                        .Statement
                                                                        .OnStatement(message!!)
                                                                    showDialog = true
                                                                }
                                                            }, onToken = {
                                                                work.routeData(owner, object :
                                                                    WorkStatus {
                                                                    override fun workDone(b: Boolean) {
                                                                        if (b) action.invoke()
                                                                    }

                                                                    override fun progress(p: Int) {
                                                                        AppLogger.instance.appLog(
                                                                            "DATA:Progress",
                                                                            "$p"
                                                                        )
                                                                    }
                                                                })
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                            action.invoke()
                                        }

                                        else -> throw Exception("Not implemented")

                                    }

                                    is NavigateModule -> data.controller.navigate(nav.route)

                                }
                            }
                            Spacer(modifier = Modifier.size(32.dp))
                            Text(
                                text = stringResource(id = R.string.customer_menu_),
                                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            CustomerMenus { nav ->
                                when (nav.type) {
                                    is NavigateDialog -> {
                                        //DashboardDialog(data = data, nav = nav, state = {})
                                    }

                                    is NavigateModule -> data.controller.navigate(nav.route)
                                }
                            }
                        }
                    }
                }

                SnackbarHost(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    hostState = snackState
                ) { snack: SnackbarData ->
                    CustomSnackBar(
                        snack.visuals.message,
                        isRtl = false
                    )
                }

                if (showDialog) when (navType) {
                    is NavigateDialog.Balance -> when (val s = navType) {
                        is NavigateDialog.Balance.OnBalance -> AgentBalance(
                            data = s.data.data!!,
                            action = { showDialog = false })

                        else -> throw Exception("Not implemented")
                    }

                    is NavigateDialog.Statement -> when (val s = navType) {
                        is NavigateDialog.Statement.Full -> FullStatementDatePicker(
                            action = { from, to ->
                                val use = model.userState
                                showDialog = false
                                action = {
                                    model.web(
                                        path = "${model.deviceData?.agent}",
                                        data = fullFunc(
                                            from = from,
                                            to = to,
                                            account = "${account.value?.account}",
                                            mobile = "${use?.mobile}",
                                            agentId = "${account.value?.agentID}",
                                            model = model,
                                            pin = "$hello",
                                            context = context
                                        )!!,
                                        state = { screenState = it },
                                        onResponse = { response ->
                                            MiniStatementModuleResponse(
                                                response = response,
                                                model = model,
                                                onError = { error ->
                                                    screenState = ModuleState.ERROR
                                                    scope.launch {
                                                        snackState.showSnackbar(
                                                            message = "$error"
                                                        )
                                                    }
                                                },
                                                onSuccess = { message ->
                                                    scope.launch {
                                                        screenState = ModuleState.DISPLAY
                                                        delay(200)
                                                        message?.title =
                                                            model.resourceProvider
                                                                .getString(R.string.full_statement_title_)
                                                        navType = NavigateDialog
                                                            .Statement
                                                            .OnStatement(message!!)
                                                        showDialog = true
                                                    }
                                                }, onToken = {
                                                    work.routeData(owner, object :
                                                        WorkStatus {
                                                        override fun workDone(b: Boolean) {
                                                            if (b) action.invoke()
                                                        }

                                                        override fun progress(p: Int) {
                                                            AppLogger.instance.appLog(
                                                                "DATA:Progress",
                                                                "$p"
                                                            )
                                                        }
                                                    })
                                                }
                                            )
                                        }
                                    )
                                }
                                action.invoke()
                            },
                            close = { showDialog = false })

                        is NavigateDialog.Statement.OnStatement -> AgentFullMiniModule(
                            data = s.data,
                            action = { showDialog = false })

                        else -> throw Exception("Not implemented")
                    }

                    else -> throw Exception("Not implemented")
                }
            }

        }
    }
}