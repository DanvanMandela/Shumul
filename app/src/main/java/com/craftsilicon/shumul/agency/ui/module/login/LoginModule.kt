package com.craftsilicon.shumul.agency.ui.module.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.layout.ContentScale
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
import com.craftsilicon.shumul.agency.data.bean.app.response.GlobalTokenManager
import com.craftsilicon.shumul.agency.data.security.APP.ACTIVATED
import com.craftsilicon.shumul.agency.data.security.APP.TEST
import com.craftsilicon.shumul.agency.data.security.ActivationData
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.theme.GreenAp
import com.craftsilicon.shumul.agency.ui.theme.GreenLightApp
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import com.craftsilicon.shumul.agency.ui.util.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginModule(data: GlobalData) {
    val context = LocalContext.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val snackState = remember { SnackbarHostState() }
    var passwordVisibility by remember { mutableStateOf(false) }
    val work = hiltViewModel<WorkViewModel>()

    val user = model.preferences.userData.collectAsState().value
    val owner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }

    var password by rememberSaveable {
        mutableStateOf(if (ACTIVATED) ActivationData.AGENT_PIN else "")
    }

    var action: () -> Unit = {}

    Box {

        when (screenState) {
            ModuleState.LOADING -> LoadingModule()
            ModuleState.ERROR,
            ModuleState.DISPLAY -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(
                            shape = RoundedCornerShape(
                                bottomEnd = 32.dp,
                                bottomStart = 32.dp
                            ), color = GreenLightApp.copy(alpha = 0.2f)
                        )
                ) {

                    val size = this.maxHeight.div(1.8f)
                    Image(
                        painter = painterResource(id = R.drawable.people),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(size)
                            .align(Alignment.Center)
                    )
                }

                CompositionLocalProvider(LocalLayoutDirection provides layoutDirection()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1.3f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.size(32.dp))
                        Image(
                            painter = painterResource(id = R.drawable.shumul_logo),
                            contentDescription = null,
                            contentScale = ContentScale.Inside,
                            modifier = Modifier.width(40.dp)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = stringResource(id = R.string.login_),
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(id = R.string.enter_login_credential_),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)

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
                                .padding(horizontal = 48.dp),
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
                        Spacer(modifier = Modifier.size(24.dp))
                        Button(
                            onClick = {
                                val use = model.userState
                                val stateAccount = use?.account?.first()
                                scope.launch {
                                    if (password.isBlank()) {
                                        snackState.showSnackbar(
                                            context.getString(R.string.enter_pin_)
                                        )
                                    } else {
                                        action = {
                                            model.web(
                                                path = "${model.deviceData?.agent}",
                                                data = loginFunc(
                                                    username = "${stateAccount?.agentID}",
                                                    mobile = "${use?.mobile}",
                                                    pin = password,
                                                    model = model,
                                                    context = context
                                                )!!,
                                                state = { screenState = it },
                                                onResponse = { response ->
                                                    LoginModuleResponse(
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
                                                                model.preferences.helloWorld(
                                                                    password
                                                                )
                                                                context.toast(message)
                                                                delay(600)
                                                                data.controller.navigate(Module.Dashboard.route)
                                                            }

                                                        }, onToken = {
                                                            GlobalTokenManager(
                                                                model = work,
                                                                owner = owner,
                                                                onSuccess = action
                                                            )
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
                                .padding(horizontal = 48.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.login_),
                                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }

            }

            ModuleState.SUCCESS -> throw Exception("Not implemented")
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
    }


}