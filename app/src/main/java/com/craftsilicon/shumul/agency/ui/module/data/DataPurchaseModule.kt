package com.craftsilicon.shumul.agency.ui.module.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.custom.DropDownResult
import com.craftsilicon.shumul.agency.ui.custom.EditDropDown
import com.craftsilicon.shumul.agency.ui.module.SuccessDialog
import com.craftsilicon.shumul.agency.ui.module.dashboard.balance.BalanceModuleResponse
import com.craftsilicon.shumul.agency.ui.module.dashboard.balance.balanceFunc
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.navigation.NavigateDialog
import com.craftsilicon.shumul.agency.ui.navigation.NavigationType
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.countryCode
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataPurchaseModule(data: GlobalData) {
    val context = LocalContext.current
    val work = hiltViewModel<WorkViewModel>()
    val owner = LocalLifecycleOwner.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val snackState = remember { SnackbarHostState() }

    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }

    val scope = rememberCoroutineScope()

    var mno: HashMap<String, Any?> = remember {
        hashMapOf()
    }
    var mobile by rememberSaveable {
        mutableStateOf("")
    }

    val user = model.preferences.userData.collectAsState().value
    val mnoData = remember { SnapshotStateList<DropDownResult>() }

    var navType: NavigationType? by remember {
        mutableStateOf(null)
    }

    var showDialog by remember { mutableStateOf(false) }

    var action: () -> Unit = {}

    Box {

        when (screenState) {
            ModuleState.LOADING -> LoadingModule()
            ModuleState.ERROR,
            ModuleState.DISPLAY -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection()) {
                    Scaffold(topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.data_purchase_),
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
                            Spacer(modifier = Modifier.size(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                EditDropDown(
                                    label = stringResource(id = R.string.mno_),
                                    data = MutableStateFlow(mnoData)
                                ) { result ->
                                    mno = hashMapOf(
                                        "mno" to result.desc,
                                        "id" to result.key
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = mobile,
                                onValueChange = { mobile = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.mobile_number_),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                    )
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding),
                                textStyle = TextStyle(
                                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                    fontSize = MaterialTheme.typography.labelLarge.fontSize
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Phone
                                ), prefix = {
                                    Text(
                                        text = countryCode(),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold))
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.size(horizontalModulePadding))
                            Button(
                                onClick = {
                                    scope.launch {
                                        if (mno.isEmpty()) {
                                            snackState.showSnackbar(
                                                context.getString(R.string.select_service_provider_)
                                            )
                                        } else if (mobile.isEmpty()) {
                                            snackState.showSnackbar(
                                                context.getString(R.string.enter_mobile_number_)
                                            )
                                        } else {
                                            action = {
                                                model.web(
                                                    path = "${model.deviceData?.agent}",
                                                    data = balanceFunc(
                                                        account = "$",
                                                        fromAccount = "${user?.account?.firstOrNull()?.account}",
                                                        mobile = "${user?.mobile}",
                                                        agentId = "${user?.account?.firstOrNull()?.agentID}",
                                                        model = model,
                                                        context = context,
                                                        pin = "password"
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

                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.submit_),
                                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }

                        }
                    }

                }

            }

            ModuleState.SUCCESS -> {}
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
                is NavigateDialog.Balance.OnBalance -> SuccessDialog(
                    message = "${s.data.message}",
                    action = {
                        showDialog = false
                        data.controller.navigateUp()
                    })

                else -> throw Exception("Not implemented")
            }

            else -> throw Exception("Not implemented")
        }

    }

}