package com.craftsilicon.shumul.agency.ui.module.remittance.pay

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.ValidationBean
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.ActivationData
import com.craftsilicon.shumul.agency.data.source.model.LocalViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.module.ConfirmDialog
import com.craftsilicon.shumul.agency.ui.module.ErrorDialog
import com.craftsilicon.shumul.agency.ui.module.ModuleCall
import com.craftsilicon.shumul.agency.ui.module.Response
import com.craftsilicon.shumul.agency.ui.module.SuccessDialog
import com.craftsilicon.shumul.agency.ui.module.fund.FundTransferModuleModuleResponse
import com.craftsilicon.shumul.agency.ui.module.remittance.RemittanceModuleHelper
import com.craftsilicon.shumul.agency.ui.module.remittance.sumOf
import com.craftsilicon.shumul.agency.ui.module.toBigNumberDisplay
import com.craftsilicon.shumul.agency.ui.module.toHashMap
import com.craftsilicon.shumul.agency.ui.module.validation.ValidationHelper
import com.craftsilicon.shumul.agency.ui.module.validation.ValidationModuleResponse
import com.craftsilicon.shumul.agency.ui.module.validation.validationFunc
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PayCustomerRemittance(function: () -> Unit,data: GlobalData) {
    val context = LocalContext.current
    val work = hiltViewModel<WorkViewModel>()
    val owner = LocalLifecycleOwner.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val snackState = remember { SnackbarHostState() }
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.LOADING)
    }
    val user = model.preferences.userData.collectAsState().value
    val scope = rememberCoroutineScope()


    var account by rememberSaveable {
        mutableStateOf("")
    }

    var totalAmount by rememberSaveable {
        mutableStateOf("")
    }



    val validationData: MutableState<ValidationBean?> = remember {
        mutableStateOf(null)
    }

    val local = hiltViewModel<LocalViewModelImpl>()


    var remittanceValue by rememberSaveable {
        mutableStateOf("")
    }

    var remittanceRead by rememberSaveable {
        mutableStateOf(false)
    }


    val currenciesData = local.currency.collectAsState().value


    var moduleCall: ModuleCall by remember {
        mutableStateOf(Response.Confirm)
    }

    var showDialog by remember { mutableStateOf(false) }


    var showErrorDialog by remember { mutableStateOf(false) }



    var passwordVisibility by remember { mutableStateOf(false) }

    var password by rememberSaveable {
        mutableStateOf(if (APP.ACTIVATED) ActivationData.AGENT_PIN else "")
    }

    val appUser by remember {
        mutableStateOf(model.preferences.appUserState.value)
    }

    var action: () -> Unit = {}

    if (currenciesData.isEmpty())
        LaunchedEffect(key1 = Unit) {
            delay(600)
            action = {
                val use = model.userState
                val stateAccount = use?.account?.first()
                model.web(
                    path = "${model.deviceData?.agent}",
                    data = RemittanceModuleHelper.currency(
                        account = "${stateAccount?.account}",
                        mobile = "${use?.mobile}",
                        agentId = "${stateAccount?.agentID}",
                        model = model,
                        context = context
                    )!!,
                    state = { screenState = it },
                    onResponse = { response ->
                        RemittanceModuleHelper.currencyResponse(
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
                                    local.currency.value = beans
                                    screenState = ModuleState.DISPLAY
                                    delay(200)
                                }
                            }, onToken = {
                                showErrorDialog=true
                            }
                        )
                    }
                )
            }
            action.invoke()
        }








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
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                            value = remittanceValue,
                            onValueChange = { remittanceValue = it },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.remittance_number_),
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
                            readOnly = remittanceRead,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Text
                            )
                        )
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
                                    if (account.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.account_number_)
                                        )
                                    } else if (remittanceValue.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_receiver_name_)
                                        )
                                    } else if (password.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_pin_)
                                        )
                                    } else {
                                        action = {
                                            model.web(
                                                path = "${model.deviceData?.agent}",
                                                data = validationFunc(
                                                    account = account,
                                                    mobile = "${use?.mobile}",
                                                    agentId = "${stateAccount?.agentID}",
                                                    model = model,
                                                    context = context
                                                )!!,
                                                state = { screenState = it },
                                                onResponse = { response ->
                                                    ValidationModuleResponse(
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
                                                        onSuccess = { accountValidation ->
                                                            action = {
                                                                model.web(
                                                                    path = "${model.deviceData?.agent}",
                                                                    data = RemittanceModuleHelper.search(
                                                                        account = remittanceValue,
                                                                        agentId = "${stateAccount?.agentID}",
                                                                        mobile = "${use?.mobile}",
                                                                        context = context,
                                                                        model = model
                                                                    )!!,
                                                                    state = { screenState = it },
                                                                    onResponse = { response ->
                                                                        ValidationHelper.validateHash(
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
                                                                            onSuccess = { validation ->
                                                                                screenState =
                                                                                    ModuleState.DISPLAY
                                                                                val mapList =
                                                                                    validation["searchData"] as MutableList<*>
                                                                                val map =
                                                                                    mapList.firstOrNull() as LinkedTreeMap<*, *>
                                                                                totalAmount =
                                                                                    hashMapOf(
                                                                                        "A" to map["agentFee"],
                                                                                        "B" to map["networkFee"],
                                                                                        "C" to map["destinationFee"],
                                                                                        "D" to map["amount"]
                                                                                    ).sumOf()
                                                                                moduleCall =
                                                                                    Response.Confirm
                                                                                val curr =
                                                                                    currenciesData.find {
                                                                                        it.id ==
                                                                                                "${map["remittanceCurrancyId"]}".toBigNumberDisplay()
                                                                                    }?.description
                                                                                validationData.value =
                                                                                    accountValidation?.apply {
                                                                                        isOtp =
                                                                                            false
                                                                                        display =
                                                                                            linkedMapOf(
                                                                                                context.getString(
                                                                                                    R.string.to_account_
                                                                                                )
                                                                                                        to account,
                                                                                                context.getString(
                                                                                                    R.string.sender_name_
                                                                                                )
                                                                                                        to map["sender"],
                                                                                                context.getString(
                                                                                                    R.string.sender_mobile_
                                                                                                )
                                                                                                        to "${map["senderMobile"] ?: 0}".toBigNumberDisplay(),
                                                                                                context.getString(
                                                                                                    R.string.receiver_name_
                                                                                                )
                                                                                                        to map["receiver"],
                                                                                                context.getString(
                                                                                                    R.string.receiver_mobile_
                                                                                                )
                                                                                                        to "${map["receiverMobile"] ?: 0}".toBigNumberDisplay(),
                                                                                                context.getString(
                                                                                                    R.string.currency_
                                                                                                )
                                                                                                        to curr,
                                                                                                context.getString(
                                                                                                    R.string.amount_
                                                                                                )
                                                                                                        to "${map["amount"] ?: 0}".toBigNumberDisplay(),

                                                                                                )
                                                                                        extra =
                                                                                            map.toHashMap()
                                                                                        this.amount =
                                                                                            totalAmount
                                                                                    }
                                                                                showDialog = true
                                                                            }, onToken = {
                                                                                showErrorDialog=true
                                                                            }
                                                                        )
                                                                    }
                                                                )
                                                            }
                                                            action.invoke()
                                                        }, onToken = {
                                                            showErrorDialog=true
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
                        Spacer(modifier = Modifier.size(horizontalModulePadding))

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


        if (showErrorDialog) ErrorDialog(message = stringResource(id = R.string.session_expired_login_)) {
            showErrorDialog = false
            data.controller.navigate(Module.Splash.route)
        }



        if (showDialog) when (val s = moduleCall) {
            is Response.Success -> SuccessDialog(
                message = "${s.data["message"]}",
                reference = "${s.data["reference"]}",
                action = {
                    showDialog = false
                    function()
                })

            is Response.Confirm -> ConfirmDialog(
                data = validationData.value!!,
                action = {
                    showDialog = false
                    action = {
                        val use = model.userState
                        val stateAccount = use?.account?.first()
                        val map = validationData.value!!.extra
                        val curr = "${map["remittanceCurrancyId"]}".toBigNumberDisplay()
                        if (currenciesData.find { it.id == curr }?.description == validationData.value?.currency)
                            model.web(
                                path = "${model.deviceData?.agent}",
                                data = RemittanceModuleHelper.payRemittance(
                                    account = account,
                                    remittance = remittanceValue,
                                    mobile = "${use?.mobile}",
                                    agentId = "${stateAccount?.agentID}",
                                    pin = password,
                                    model = model,
                                    context = context,
                                    data = hashMapOf(
                                        "senderName" to "${map["sender"]}",
                                        "senderPhone" to "${map["senderMobile"]}",
                                        "charge" to "${validationData.value?.amount}",
                                        "currencyID" to curr,
                                        "receiverName" to "${map["receiver"]}",
                                        "receiverPhone" to "${map["receiverMobile"]}",
                                        "amount" to "${map["amount"] ?: 0}".toBigNumberDisplay(),
                                        "feeID" to "${
                                            map["feeId"]
                                                .toString()
                                                .toDouble()
                                                .toInt()
                                        }"
                                    )
                                )!!,
                                state = { screenState = it },
                                onResponse = { response ->
                                    FundTransferModuleModuleResponse(
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
                                            val convertedData = model.any.map(
                                                model.any.convert(message!!.data)
                                            )
                                            val referenceValue = convertedData["BatchID"]
                                                ?: convertedData["referance"]

                                            moduleCall = Response.Success(
                                                data = hashMapOf(
                                                    "message" to message.message,
                                                    "reference" to "$referenceValue"
                                                )
                                            )
                                            screenState = ModuleState.DISPLAY
                                            showDialog = true
                                        }, onToken = {
                                            showErrorDialog=true
                                        }
                                    )
                                }
                            )
                        else scope.launch {
                            snackState.showSnackbar(
                                message = context.getString(R.string.currency_miss_match_)
                            )
                        }
                    }
                    action.invoke()
                },
                close = { showDialog = false })
        }
    }
}