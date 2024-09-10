package com.craftsilicon.shumul.agency.ui.module.pin

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
import com.craftsilicon.shumul.agency.data.security.APP
import com.craftsilicon.shumul.agency.data.security.ActivationData
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.module.ModuleCall
import com.craftsilicon.shumul.agency.ui.module.Response
import com.craftsilicon.shumul.agency.ui.module.SuccessDialog
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePin(data: GlobalData) {
    val context = LocalContext.current
    val work = hiltViewModel<WorkViewModel>()
    val owner = LocalLifecycleOwner.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val user = model.preferences.userData.collectAsState().value
    val snackState = remember { SnackbarHostState() }
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }
    val scope = rememberCoroutineScope()
    var passwordVisibilityNew by remember { mutableStateOf(false) }

    var passwordNew by rememberSaveable {
        mutableStateOf(if (APP.ACTIVATED) ActivationData.AGENT_PIN else "")
    }
    var moduleCall: ModuleCall by remember {
        mutableStateOf(Response.Confirm)
    }
    var passwordVisibilityOld by remember { mutableStateOf(false) }

    var passwordOld by rememberSaveable {
        mutableStateOf(if (APP.ACTIVATED) ActivationData.AGENT_PIN else "")
    }

    var passwordVisibilityConfirm by remember { mutableStateOf(false) }

    var passwordConfirm by rememberSaveable {
        mutableStateOf(if (APP.ACTIVATED) ActivationData.AGENT_PIN else "")
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
                                    text = stringResource(id = R.string.change_pin_module_),
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

                            OutlinedTextField(
                                value = passwordOld,
                                onValueChange = { passwordOld = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.old_pin_),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding),
                                visualTransformation = if (passwordVisibilityOld) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.NumberPassword
                                ),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        passwordVisibilityOld = !passwordVisibilityOld
                                    }) {
                                        if (passwordVisibilityOld)
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

                            Spacer(modifier = Modifier.size(16.dp))

                            OutlinedTextField(
                                value = passwordNew,
                                onValueChange = { passwordNew = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.new_pin_),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding),
                                visualTransformation = if (passwordVisibilityNew) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.NumberPassword
                                ),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        passwordVisibilityNew = !passwordVisibilityNew
                                    }) {
                                        if (passwordVisibilityNew)
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

                            Spacer(modifier = Modifier.size(16.dp))

                            OutlinedTextField(
                                value = passwordConfirm,
                                onValueChange = { passwordConfirm = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.confirm_pin_),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium))
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding),
                                visualTransformation = if (passwordVisibilityConfirm) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.NumberPassword
                                ),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        passwordVisibilityConfirm = !passwordVisibilityConfirm
                                    }) {
                                        if (passwordVisibilityConfirm)
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
                                        if (passwordOld.isBlank()) {
                                            snackState.showSnackbar(
                                                context.getString(R.string.enter_old_pin_)
                                            )
                                        } else if (passwordNew.isBlank()) {
                                            snackState.showSnackbar(
                                                context.getString(R.string.enter_new_pin_)
                                            )
                                        } else if (passwordConfirm.isBlank()) {
                                            snackState.showSnackbar(
                                                context.getString(R.string.confirm_new_pin_)
                                            )
                                        } else if (passwordNew == passwordOld) {
                                            snackState.showSnackbar(
                                                context.getString(R.string.new_pass_not_same_)
                                            )
                                        } else if (passwordNew != passwordConfirm) {
                                            snackState.showSnackbar(
                                                context.getString(R.string.password_not_matching_)
                                            )
                                        } else {
                                            action = {

                                                model.web(
                                                    path = "${model.deviceData?.agent}",
                                                    data = changePin(
                                                        pin = hashMapOf(
                                                            "new" to passwordNew,
                                                            "old" to passwordOld
                                                        ),
                                                        account = "${stateAccount?.account}",
                                                        mobile = "${use?.mobile}",
                                                        agentId = "${stateAccount?.agentID}",
                                                        model = model,
                                                        context = context
                                                    )!!,
                                                    state = { screenState = it },
                                                    onResponse = { response ->
                                                        ChangePinModuleResponse(
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
                                                                moduleCall =
                                                                    Response.Success(
                                                                        data = hashMapOf(
                                                                            "message" to message!!.message
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

        if (showDialog) when (val s = moduleCall) {
            is Response.Success -> SuccessDialog(
                message = "${s.data["message"]}",
                action = {
                    showDialog = false
                    data.controller.navigate(Module.Login.route)
                })

            else -> throw Exception("Not implemented")
        }


    }
}