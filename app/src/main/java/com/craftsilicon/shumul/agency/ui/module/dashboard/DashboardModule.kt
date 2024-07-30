package com.craftsilicon.shumul.agency.ui.module.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
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
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardModule(data: GlobalData) {
    val context = LocalContext.current
    val snackState = remember { SnackbarHostState() }
    val model = hiltViewModel<RemoteViewModelImpl>()
    val scope = rememberCoroutineScope()
    val user = model.preferences.userData.collectAsState().value
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
                        AccountHelper(user = user)
//                        Box(
//                            modifier = Modifier
//                                .padding(horizontal = 16.dp)
//                                .clip(RoundedCornerShape(16.dp))
//                                .aspectRatio(10 / 4.5f)
//
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.account_back),
//                                contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier.matchParentSize()
//                            )
//                            Column(
//                                modifier = Modifier
//                                    .matchParentSize()
//                            ) {
//                                Column(
//                                    modifier = Modifier
//                                        .padding(horizontal = 16.dp)
//                                        .fillMaxSize()
//                                        .weight(1f), verticalArrangement = Arrangement.Center
//                                ) {
//                                    Text(
//                                        text = "${user?.account?.firstOrNull()?.agentID}",
//                                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
//                                        style = MaterialTheme.typography.bodyLarge,
//                                        color = Color.White
//                                    )
//                                    Text(
//                                        text = stringResource(id = R.string.agent_id_),
//                                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
//                                        style = MaterialTheme.typography.labelMedium,
//                                        color = Color.White
//                                    )
//                                }
//                                Column(
//                                    modifier = Modifier
//                                        .padding(horizontal = 16.dp)
//                                        .fillMaxSize()
//                                        .weight(1f), verticalArrangement = Arrangement.Center
//                                ) {
//                                    Text(
//                                        text = "${user?.mobile}",
//                                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
//                                        style = MaterialTheme.typography.bodyLarge,
//                                        color = Color.White
//                                    )
//                                    Text(
//                                        text = "${user?.email}",
//                                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
//                                        style = MaterialTheme.typography.labelMedium,
//                                        color = Color.White
//                                    )
//                                }
//                            }
//                        }
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
                                when (nav.type) {
                                    is NavigateDialog -> when (nav.type) {
                                        NavigateDialog.Balance.OnRequest -> {
                                            action = {
                                                model.web(
                                                    path = "${model.deviceData?.agent}",
                                                    data = balanceFunc(
                                                        account = "${user?.account?.firstOrNull()?.account}",
                                                        fromAccount = "${user?.account?.firstOrNull()?.account}",
                                                        mobile = "${user?.mobile}",
                                                        agentId = "${user?.account?.firstOrNull()?.agentID}",
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
                                                            }, onToken = action
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
                                                        account = "${user?.account?.firstOrNull()?.account}",
                                                        mobile = "${user?.mobile}",
                                                        agentId = "${user?.account?.firstOrNull()?.agentID}",
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
                                                            }, onToken = action
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
                                showDialog = false
                                action = {
                                    model.web(
                                        path = "${model.deviceData?.agent}",
                                        data = fullFunc(
                                            from = from,
                                            to = to,
                                            account = "${user?.account?.firstOrNull()?.account}",
                                            mobile = "${user?.mobile}",
                                            agentId = "${user?.account?.firstOrNull()?.agentID}",
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
                                                }, onToken = action
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