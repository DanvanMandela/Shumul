package com.craftsilicon.shumul.agency.ui.module.cash.cash

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.Account
import com.craftsilicon.shumul.agency.data.bean.ValidationBean
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.ActivationData
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.custom.DropDownResult
import com.craftsilicon.shumul.agency.ui.custom.EditDropDown
import com.craftsilicon.shumul.agency.ui.module.ModuleCall
import com.craftsilicon.shumul.agency.ui.module.Response
import com.craftsilicon.shumul.agency.ui.module.SuccessDialog
import com.craftsilicon.shumul.agency.ui.module.fund.FundTransferConfirmDialog
import com.craftsilicon.shumul.agency.ui.module.fund.FundTransferModuleModuleResponse
import com.craftsilicon.shumul.agency.ui.module.withdrawal.otpTransactionCompleteFunc
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.MoneyVisualTransformation
import com.craftsilicon.shumul.agency.ui.util.countryCode
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun CashToCashGenerate(function: () -> Unit) {
    val context = LocalContext.current
    val work = hiltViewModel<WorkViewModel>()
    val owner = LocalLifecycleOwner.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val snackState = remember { SnackbarHostState() }
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }



    val accountState = model.preferences.currentAccount.collectAsState().value
    val agentAccounts = remember { SnapshotStateList<DropDownResult>() }
    val agentAccount: MutableState<Account?> = remember {
        mutableStateOf(null)
    }
    val user = model.preferences.userData.collectAsState().value
    val scope = rememberCoroutineScope()
    var senderName by rememberSaveable {
        mutableStateOf("")
    }

    var senderMobile by rememberSaveable {
        mutableStateOf("")
    }

    val validationData: MutableState<ValidationBean?> = remember {
        mutableStateOf(null)
    }


    var receiverName by rememberSaveable {
        mutableStateOf("")
    }

    var receiverMobile by rememberSaveable {
        mutableStateOf("")
    }

    var amount by rememberSaveable {
        mutableStateOf("")
    }

    var narration by rememberSaveable {
        mutableStateOf("")
    }

    var moduleCall: ModuleCall by remember {
        mutableStateOf(Response.Confirm)
    }

    var showDialog by remember { mutableStateOf(false) }


    var passwordVisibility by remember { mutableStateOf(false) }

    var password by rememberSaveable {
        mutableStateOf(if (APP.ACTIVATED) ActivationData.AGENT_PIN else "")
    }
    var currency by rememberSaveable {
        mutableStateOf(context.getString(R.string.currency_symbol_))
    }

    LaunchedEffect(key1 = Unit) {
        user?.account?.forEach {
            agentAccounts.add(
                DropDownResult(
                    key = it,
                    desc = it.account,
                    display = it == accountState
                )
            )
        }
    }


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
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.size(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalModulePadding)
                        ) {
                            EditDropDown(
                                label = stringResource(id = R.string.agent_account_),
                                data = MutableStateFlow(agentAccounts)
                            ) { result ->
                                agentAccount.value = result.key as Account
                                agentAccount.value?.currency?.let {
                                    currency = it
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        OutlinedTextField(
                            value = senderName,
                            onValueChange = { senderName = it },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.sender_name_),
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
                            value = senderMobile,
                            onValueChange = { senderMobile = it },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.sender_mobile_),
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
                            value = receiverName,
                            onValueChange = { receiverName = it },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.receiver_name_),
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
                        Spacer(modifier = Modifier.size(16.dp))
                        OutlinedTextField(
                            value = receiverMobile,
                            onValueChange = { receiverMobile = it },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.receiver_mobile_),
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
                            suffix = {
                                Text(
                                    text = currency,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold))
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Phone
                            ), visualTransformation = MoneyVisualTransformation()
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
                                scope.launch {
                                    if (agentAccount.value == null) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_agent_account_number)
                                        )
                                    } else if (senderName.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_sender_name_)
                                        )
                                    } else if (senderMobile.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_sender_mobile_)
                                        )
                                    } else if (amount.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_amount_)
                                        )
                                    } else if (receiverName.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_receiver_name_)
                                        )
                                    } else if (receiverMobile.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_receiver_mobile_)
                                        )
                                    } else if (password.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_pin_)
                                        )
                                    } else if (narration.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_narration_)
                                        )
                                    } else {
                                        action = {
                                            model.web(
                                                path = "${model.deviceData?.agent}",
                                                data = CashToCashHelper.generate(
                                                    fromName = senderName,
                                                    toName = receiverName,
                                                    fromAccount = "${agentAccount.value?.account}",
                                                    amount = amount,
                                                    mobile = "${countryCode()}$senderMobile",
                                                    toMobile = "${countryCode()}$receiverMobile",
                                                    narration = narration,
                                                    agentId = "${agentAccount.value?.agentID}",
                                                    pin = password,
                                                    model = model,
                                                    context = context
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
                                                            moduleCall = Response.Success(
                                                                data = hashMapOf(
                                                                    "message" to message!!.message,
                                                                    "reference" to model.any.map(
                                                                        model.any.convert(message.data)
                                                                    )["BatchID"]
                                                                )
                                                            )
                                                            screenState = ModuleState.DISPLAY
                                                            showDialog = true
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


//                                                    CashToCashHelper.response(
//                                                        response = response,
//                                                        model = model,
//                                                        onError = { error ->
//                                                            screenState = ModuleState.ERROR
//                                                            scope.launch {
//                                                                snackState.showSnackbar(
//                                                                    message = "$error"
//                                                                )
//                                                            }
//                                                        },
//                                                        onSuccess = { validation ->
//                                                            screenState = ModuleState.DISPLAY
//                                                            moduleCall = Response.Confirm
//                                                            validation?.amount = amount
//                                                            validation?.account = senderName
//                                                            validation?.extra = hashMapOf(
//                                                                "fromName" to user?.firstName,
//                                                                "fromAccount" to "${user?.account?.firstOrNull()?.account}"
//                                                            )
//                                                            validationData.value = validation
//                                                            showDialog = true
//                                                        }, onToken = {
//                                                            work.routeData(owner, object :
//                                                                WorkStatus {
//                                                                override fun workDone(b: Boolean) {
//                                                                    if (b) action.invoke()
//                                                                }
//
//                                                                override fun progress(p: Int) {
//                                                                    AppLogger.instance.appLog(
//                                                                        "DATA:Progress",
//                                                                        "$p"
//                                                                    )
//                                                                }
//                                                            })
//                                                        }
//                                                    )
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

        if (showDialog) when (val s = moduleCall) {
            is Response.Success -> SuccessDialog(
                message = "${s.data["message"]}",
                reference = "${s.data["reference"]}",
                action = {
                    showDialog = false
                    function()
                })

            is Response.Confirm -> FundTransferConfirmDialog(
                data = validationData.value!!,
                action = { otp ->
                    showDialog = false
                    action = {
                        model.web(
                            path = "${model.deviceData?.agent}",
                            data = otpTransactionCompleteFunc(
                                toAccount = senderName,
                                fromAccount = "${user?.account?.firstOrNull()?.account}",
                                amount = amount,
                                mobile = "${countryCode()}$senderMobile",
                                narration = "${validationData.value?.traceNo}",
                                agentId = "${user?.account?.firstOrNull()?.agentID}",
                                pin = password,
                                model = model,
                                otp = otp,
                                context = context
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
                                        moduleCall = Response.Success(
                                            data = hashMapOf(
                                                "message" to message!!.message,
                                                "reference" to model.any.map(
                                                    model.any.convert(message.data)
                                                )["BatchID"]
                                            )
                                        )
                                        screenState = ModuleState.DISPLAY
                                        showDialog = true
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
        }
    }


}