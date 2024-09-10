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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.staticDataResponse
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.ActivationData
import com.craftsilicon.shumul.agency.data.source.model.LocalViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.custom.DropDownResult
import com.craftsilicon.shumul.agency.ui.custom.EditDropDown
import com.craftsilicon.shumul.agency.ui.module.ErrorDialog
import com.craftsilicon.shumul.agency.ui.module.SuccessDialog
import com.craftsilicon.shumul.agency.ui.module.airtime.airtimeValidate
import com.craftsilicon.shumul.agency.ui.module.airtime.boq
import com.craftsilicon.shumul.agency.ui.module.airtime.mnoData
import com.craftsilicon.shumul.agency.ui.module.dashboard.balance.BalanceModuleResponse
import com.craftsilicon.shumul.agency.ui.module.validation.ValidationHelper
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.navigation.NavigateDialog
import com.craftsilicon.shumul.agency.ui.navigation.NavigationType
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.MoneyVisualTransformation
import com.craftsilicon.shumul.agency.ui.util.countryCode
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun DataPurchaseCustomerModule(function: () -> Unit, data: GlobalData) {
    val context = LocalContext.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val snackState = remember { SnackbarHostState() }

    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.LOADING)
    }

    var dataStage: DataStage by remember {
        mutableStateOf(DataStage.Validation)
    }

    val local = hiltViewModel<LocalViewModelImpl>()

    val subs = remember { SnapshotStateList<DropDownResult>() }
    var sub by rememberSaveable {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    var account by rememberSaveable {
        mutableStateOf("")
    }

    var mno by rememberSaveable {
        mutableStateOf("")
    }

    var lineType by rememberSaveable {
        mutableStateOf("0")
    }

    var mobile by rememberSaveable {
        mutableStateOf("")
    }
    var narration by rememberSaveable {
        mutableStateOf("")
    }
    var amount by rememberSaveable {
        mutableStateOf("")
    }

    val staticData = local.staticData.collectAsState().value

    var static by rememberSaveable {
        mutableStateOf("")
    }

    val appUser by remember {
        mutableStateOf(model.preferences.appUserState.value)
    }

    val user = model.preferences.userData.collectAsState().value
    val mnoData = remember { SnapshotStateList<DropDownResult>() }


    var bouquet: HashMap<String, Any?> = remember {
        hashMapOf()
    }

    val bouquetData = remember { SnapshotStateList<DropDownResult>() }


    var navType: NavigationType? by remember {
        mutableStateOf(null)
    }

    var showErrorDialog by remember { mutableStateOf(false) }


    var showDialog by remember { mutableStateOf(false) }


    var passwordVisibility by remember { mutableStateOf(false) }

    var password by rememberSaveable {
        mutableStateOf(if (APP.ACTIVATED) ActivationData.AGENT_PIN else "")
    }
    var action: () -> Unit = {}


    LaunchedEffect(Unit) {
        context.resources.getStringArray(R.array.sub_type).forEach {
            subs.add(
                DropDownResult(
                    key = it,
                    desc = it,
                    display = it == sub
                )
            )
        }
    }


    if (staticData.isEmpty())
        LaunchedEffect(key1 = Unit) {
            delay(600)
            action = {
                val use = model.userState
                val stateAccount = use?.account?.first()
                model.web(
                    path = "${model.deviceData?.agent}",
                    data = mnoData(
                        account = "${stateAccount?.account}",
                        mobile = "${use?.mobile}",
                        agentId = "${stateAccount?.agentID}",
                        model = model,
                        context = context
                    )!!,
                    state = { screenState = it },
                    onResponse = { response ->
                        staticDataResponse(
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
                            onSuccess = { beans ->
                                scope.launch {
                                    local.staticData.value = beans
                                    screenState = ModuleState.DISPLAY
                                    delay(200)
                                }
                            }, onToken = {
                                showErrorDialog = true
                            }
                        )
                    }
                )
            }
            action.invoke()
        }

    LaunchedEffect(staticData) {
        staticData.forEach {
            mnoData.add(
                DropDownResult(
                    key = it.id,
                    desc = it.description,
                    extra = it.descTwo
                )
            )
        }
    }


    Box {

        when (screenState) {
            ModuleState.LOADING -> LoadingModule()
            ModuleState.ERROR,
            ModuleState.DISPLAY -> Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection()) {


                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (bouquetData.isNotEmpty()) {
                            Spacer(modifier = Modifier.size(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                EditDropDown(
                                    label = stringResource(id = R.string.bouquet),
                                    data = MutableStateFlow(bouquetData)
                                ) { result ->
                                    bouquet = hashMapOf(
                                        "name" to result.desc,
                                        "number" to result.key,
                                        "amount" to result.extra
                                    )
                                    amount = "${bouquet["amount"]}"
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))

                            OutlinedTextField(
                                value = amount,
                                onValueChange = { amount = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.amount_),
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
                                readOnly = true,
                                suffix = {
                                    Text(
                                        text = stringResource(id = R.string.currency_symbol_),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold))
                                    )
                                },
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Phone
                                ), visualTransformation = MoneyVisualTransformation()
                            )
                        } else {
                            Spacer(modifier = Modifier.size(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                EditDropDown(
                                    label = stringResource(id = R.string.subscription_),
                                    data = MutableStateFlow(subs)
                                ) { result ->
                                    sub = result.key as String
                                }
                            }
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
                                    mno = result.key as String
                                    if (result.extra != null)
                                        static = "${result.extra}"
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = account,
                                onValueChange = { account = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.account_number_),
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
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Text
                                )
                            )

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
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = narration,
                                onValueChange = { narration = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.narration_),
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
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Text
                                )
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.pin_),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalModulePadding),
                            visualTransformation = if (passwordVisibility) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            trailingIcon = {
                                IconButton(onClick = {
                                    passwordVisibility = !passwordVisibility
                                }) {
                                    if (passwordVisibility)
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    else Icon(
                                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        contentDescription = null,
                                        modifier = Modifier.size(ButtonDefaults.IconSize)
                                    )
                                }
                            }, textStyle = TextStyle(
                                fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                fontSize = MaterialTheme.typography.labelLarge.fontSize
                            )
                        )

                        Spacer(modifier = Modifier.size(horizontalModulePadding))
                        Button(
                            onClick = {
                                val use = model.userState
                                val stateAccount = use?.account?.first()
                                scope.launch {
                                    when (dataStage) {
                                        DataStage.Validation -> {
                                            if (mno.isEmpty()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.select_service_provider_)
                                                )
                                            } else if (account.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_account_number)
                                                )
                                            } else if (mobile.isEmpty()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_mobile_number_)
                                                )
                                            } else if (password.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_pin_)
                                                )
                                            } else {
                                                action = {
                                                    if (static.lowercase() == "YemenMobile".lowercase())
                                                        model.web(
                                                            path = "${model.deviceData?.agent}",
                                                            data = airtimeValidate(
                                                                account = "${stateAccount?.account}",
                                                                mno = "1",
                                                                mobile = mobile,
                                                                agentId = "${stateAccount?.agentID}",
                                                                model = model,
                                                                context = context,
                                                                pin = password,
                                                            )!!,
                                                            state = { screenState = it },
                                                            onResponse = { response ->
                                                                ValidationHelper.validateHashList(
                                                                    response = response,
                                                                    model = model,
                                                                    onError = { error ->
                                                                        screenState =
                                                                            ModuleState.ERROR
                                                                        scope.launch {
                                                                            snackState.showSnackbar(
                                                                                message = "$error"
                                                                            )
                                                                        }
                                                                    },
                                                                    onSuccess = { validateData ->
                                                                        val map =
                                                                            validateData.firstOrNull()
                                                                        lineType =
                                                                            "${map?.get("lineType")}"
                                                                        action = {
                                                                            model.web(
                                                                                path = "${model.deviceData?.agent}",
                                                                                data = boq(
                                                                                    account = account,
                                                                                    mno = static.lowercase(),
                                                                                    mobile = mobile,
                                                                                    agentId = "${stateAccount?.agentID}",
                                                                                    model = model,
                                                                                    context = context,
                                                                                    pin = password,
                                                                                    lineType = lineType
                                                                                )!!,
                                                                                state = {
                                                                                    screenState = it
                                                                                },
                                                                                onResponse = { response ->
                                                                                    ValidationHelper.validateHashList(
                                                                                        response = response,
                                                                                        model = model,
                                                                                        onError = { error ->
                                                                                            screenState =
                                                                                                ModuleState.ERROR
                                                                                            scope.launch {
                                                                                                snackState.showSnackbar(
                                                                                                    message = "$error"
                                                                                                )
                                                                                            }
                                                                                        },
                                                                                        onSuccess = { boq ->
                                                                                            CoroutineScope(
                                                                                                Dispatchers.Main
                                                                                            ).launch {
                                                                                                boq.forEach {
                                                                                                    bouquetData.add(
                                                                                                        DropDownResult(
                                                                                                            key = it["bouquetNumber"],
                                                                                                            desc = "${it["bouquetArabicName"]}",
                                                                                                            extra = it["bouquetPrice"]
                                                                                                        )
                                                                                                    )
                                                                                                }
                                                                                            }
                                                                                            screenState =
                                                                                                ModuleState.DISPLAY
                                                                                            dataStage =
                                                                                                DataStage.Pay
                                                                                        },
                                                                                        onToken = {
                                                                                            showErrorDialog =
                                                                                                true
                                                                                        }
                                                                                    )
                                                                                }
                                                                            )
                                                                        }
                                                                        action.invoke()
                                                                    }, onToken = {
                                                                        showErrorDialog = true
//
                                                                    }
                                                                )
                                                            }
                                                        ) else {
                                                        model.web(
                                                            path = "${model.deviceData?.agent}",
                                                            data = boq(
                                                                account = account,
                                                                mno = static.lowercase(),
                                                                mobile = mobile,
                                                                agentId = "${stateAccount?.agentID}",
                                                                model = model,
                                                                context = context,
                                                                pin = password,
                                                                lineType = "0"
                                                            )!!,
                                                            state = {
                                                                screenState = it
                                                            },
                                                            onResponse = { response ->
                                                                ValidationHelper.validateHashList(
                                                                    response = response,
                                                                    model = model,
                                                                    onError = { error ->
                                                                        screenState =
                                                                            ModuleState.ERROR
                                                                        scope.launch {
                                                                            snackState.showSnackbar(
                                                                                message = "$error"
                                                                            )
                                                                        }
                                                                    },
                                                                    onSuccess = { boq ->
                                                                        CoroutineScope(
                                                                            Dispatchers.Main
                                                                        ).launch {
                                                                            boq.forEach {
                                                                                bouquetData.add(
                                                                                    DropDownResult(
                                                                                        key = it["bouquetNumber"],
                                                                                        desc = "${it["bouquetArabicName"]}",
                                                                                        extra = it["bouquetPrice"]
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                        screenState =
                                                                            ModuleState.DISPLAY
                                                                        dataStage =
                                                                            DataStage.Pay
                                                                    },
                                                                    onToken = {
                                                                        showErrorDialog =
                                                                            true
                                                                    }
                                                                )
                                                            }
                                                        )

                                                    }

                                                }
                                                action.invoke()
                                            }
                                        }

                                        DataStage.Pay -> {
                                            if (mno.isEmpty()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.select_service_provider_)
                                                )
                                            } else {
                                                action = {
                                                    model.web(
                                                        path = "${model.deviceData?.agent}",
                                                        data = DataPurchaseModuleHelper.pay(
                                                            account = account,
                                                            mno = mno,
                                                            mobile = mobile,
                                                            agentId = "${stateAccount?.agentID}",
                                                            model = model,
                                                            context = context,
                                                            pin = password,
                                                            amount = amount,
                                                            narration = "",
                                                            offer = "${bouquet["number"]}",
                                                            lineType = lineType,
                                                            option = if (sub == "Renew") "renew" else "new"
                                                        )!!,
                                                        state = { screenState = it },
                                                        onResponse = { response ->
                                                            BalanceModuleResponse(
                                                                response = response,
                                                                model = model,
                                                                onError = { error ->
                                                                    screenState =
                                                                        ModuleState.ERROR
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
                                                                    showErrorDialog = true
                                                                }
                                                            )
                                                        }
                                                    )
                                                }
                                                action.invoke()
                                            }
                                        }
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
                        function()
                    })

                else -> throw Exception("Not implemented")
            }

            else -> throw Exception("Not implemented")
        }

    }

    if (showErrorDialog) ErrorDialog(message = stringResource(id = R.string.session_expired_login_)) {
        showErrorDialog = false
        data.controller.navigate(Module.Splash.route)
    }


}