package com.craftsilicon.shumul.agency.ui.module.account

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canhub.cropper.CropImageContract
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.permission.CameraUtil.capturedImage
import com.craftsilicon.shumul.agency.data.permission.CameraUtil.compressBitmap
import com.craftsilicon.shumul.agency.data.permission.CameraUtil.compressImage
import com.craftsilicon.shumul.agency.data.permission.CameraUtil.convert
import com.craftsilicon.shumul.agency.data.permission.ImageCallback
import com.craftsilicon.shumul.agency.data.permission.imageOption
import com.craftsilicon.shumul.agency.data.security.APP.BANK_ID
import com.craftsilicon.shumul.agency.data.security.APP.country
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.module.ModuleCall
import com.craftsilicon.shumul.agency.ui.module.Response
import com.craftsilicon.shumul.agency.ui.module.SuccessDialog
import com.craftsilicon.shumul.agency.ui.module.account.selection.MultipleSelection
import com.craftsilicon.shumul.agency.ui.module.account.selection.specialOfficer
import com.craftsilicon.shumul.agency.ui.module.statement.toElmaDate
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.buttonHeight
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOpeningDocumentModule(data: GlobalData) {


    val context = LocalContext.current
    val work = hiltViewModel<WorkViewModel>()
    val owner = LocalLifecycleOwner.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val snackState = remember { SnackbarHostState() }
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }
    val scope = rememberCoroutineScope()
    val user = model.preferences.userData.collectAsState().value
    val accountOpen = model.preferences.accountOpen.collectAsState().value


    var moduleCall: ModuleCall by remember {
        mutableStateOf(Response.Confirm)
    }

    val who: MutableState<DocumentDialog> = remember { mutableStateOf(NothingShow) }


    val passport: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }

    val signature: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }

    val idFront: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }

    var special: HashMap<String, String?> = remember {
        hashMapOf()
    }

    val idBack: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }


    var issuied by rememberSaveable {
        mutableStateOf("")
    }

    var expires by rememberSaveable {
        mutableStateOf("")
    }


    var action: () -> Unit = {}

    val sayCheese: MutableState<SayCheese> = remember {
        mutableStateOf(SayCheese.IdFront)
    }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        try {
            if (result.isSuccessful) {
                val uriContent = result.uriContent
                if (uriContent != null) {
                    val image = context.capturedImage(uriContent)
                    when (sayCheese.value) {
                        SayCheese.IdBack -> idBack.value = compressImage(image)
                        SayCheese.Selfie -> passport.value = compressImage(image)
                        SayCheese.Signature -> signature.value = compressImage(image)
                        SayCheese.IdFront -> idFront.value = compressImage(image)
                    }
                }
            } else {
                val exception = result.error
                AppLogger.instance.appLog("CROPPER:ERROR", "${exception?.localizedMessage}")
            }
        } catch (e: Exception) {
            e.localizedMessage?.let { AppLogger.instance.appLog("CROPPER:ERROR", it) }
        }

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
                    Scaffold(
                        bottomBar = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.background)
                            ) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            if (passport.value == null) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.tap_to_add_customer_passport_image
                                                    )
                                                )
                                            } else if (signature.value == null) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.tap_to_add_signature_
                                                    )
                                                )
                                            } else if (idFront.value == null) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.tap_to_add_id_front_
                                                    )
                                                )
                                            } else if (idBack.value == null) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.tap_to_add_id_back_
                                                    )
                                                )
                                            } else if (issuied.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.enter_issue_date
                                                    )
                                                )
                                            } else if (expires.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.enter_expire_date
                                                    )
                                                )
                                            } else if (special.isEmpty()) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.select_special_officer_
                                                    )
                                                )
                                            } else {
                                                moduleCall = Response.Confirm
                                                screenState = ModuleState.DISPLAY
                                                who.value = ResponseDialog
                                            }

                                        }
                                    },
                                    modifier = Modifier
                                        .padding(
                                            horizontal = horizontalModulePadding,
                                            vertical = 16.dp
                                        )
                                        .fillMaxWidth()
                                        .height(buttonHeight)

                                ) {
                                    Text(
                                        text = stringResource(id = R.string.submit_),
                                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                }
                            }
                        },
                        topBar = {
                            TopAppBar(
                                title = {
                                    Column {
                                        Text(
                                            text = stringResource(id = R.string.account_opening_),
                                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = stringResource(id = R.string.documents_),
                                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
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
                                .verticalScroll(rememberScrollState())
                                .padding(padding)
                                .background(MaterialTheme.colorScheme.background)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = issuied,
                                onValueChange = { issuied = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.date_issue_),
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
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Text
                                ), trailingIcon = {
                                    IconButton(onClick = {
                                        who.value = DateDialog(who = DialogWho.A)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.size(16.dp))

                            OutlinedTextField(
                                value = expires,
                                onValueChange = { expires = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.date_expired_),
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
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Text
                                ), trailingIcon = {
                                    IconButton(onClick = {
                                        who.value = DateDialog(who = DialogWho.B)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            if (passport.value != null)
                                Text(
                                    text = stringResource(id = R.string.passport_photo),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            Card(
                                onClick = {
                                    model.permission.imageAccess { permission ->
                                        if (permission) {
                                            sayCheese.value = SayCheese.Selfie
                                            launcher.imageOption()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = horizontalModulePadding)
                                    .fillMaxWidth(),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 7f)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.tap_to_add_customer_passport_image),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                        modifier = Modifier.align(Alignment.Center)
                                    )

                                    if (passport.value != null)
                                        Image(
                                            bitmap = passport.value!!.asImageBitmap(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .matchParentSize()
                                                .clip(RoundedCornerShape(10.dp))

                                        )

                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            if (signature.value != null)
                                Text(
                                    text = stringResource(id = R.string.signature_photo_),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            Card(
                                onClick = {
                                    model.permission.imageAccess { permission ->
                                        if (permission) {
                                            sayCheese.value = SayCheese.Signature
                                            launcher.imageOption()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = horizontalModulePadding)
                                    .fillMaxWidth(),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 7f)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.tap_to_add_signature_),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                    if (signature.value != null)
                                        Image(
                                            bitmap = signature.value!!.asImageBitmap(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .matchParentSize()
                                                .clip(RoundedCornerShape(10.dp))
                                        )

                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            if (idFront.value != null)
                                Text(
                                    text = stringResource(id = R.string.id_front_photo_),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            Card(
                                onClick = {
                                    model.permission.imageAccess { permission ->
                                        if (permission) {
                                            sayCheese.value = SayCheese.IdFront
                                            launcher.imageOption()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = horizontalModulePadding)
                                    .fillMaxWidth(),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 7f)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.tap_to_add_id_front_),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                    if (idFront.value != null)
                                        Image(
                                            bitmap = idFront.value!!.asImageBitmap(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .matchParentSize()
                                                .clip(RoundedCornerShape(10.dp))
                                        )

                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            if (idBack.value != null)
                                Text(
                                    text = stringResource(id = R.string.id_back_photo_),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            Card(
                                onClick = {
                                    model.permission.imageAccess { permission ->
                                        if (permission) {
                                            sayCheese.value = SayCheese.IdBack
                                            launcher.imageOption()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = horizontalModulePadding)
                                    .fillMaxWidth(),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(16 / 7f)
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.tap_to_add_id_back_),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                    if (idBack.value != null)
                                        Image(
                                            bitmap = idBack.value!!.asImageBitmap(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .matchParentSize()
                                                .clip(RoundedCornerShape(10.dp))
                                        )

                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                MultipleSelection(
                                    title = context.getString(R.string.special_officer),
                                    data = specialOfficer
                                ) { value ->
                                    special = value
                                }
                            }
                            Spacer(modifier = Modifier.size(horizontalModulePadding))
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


        when (val d = who.value) {
            is DateDialog -> DateOfBirthDialog(date = {
                when (d.who) {
                    DialogWho.A -> issuied = it.toElmaDate()
                    DialogWho.B -> expires = it.toElmaDate()
                }
                who.value = NothingShow
            }) { who.value = NothingShow }

            NothingShow -> {}
            ResponseDialog -> when (val s = moduleCall) {
                Response.Confirm -> AccountOpeningDialog(selfie = passport.value!!,
                    user = accountOpen, close = { who.value = NothingShow },
                    action = {
                        who.value = NothingShow
                        screenState = ModuleState.LOADING
                        action = {
                            model.web(
                                path = "${model.deviceData?.agent}",
                                data = accountOpeningFunc(
                                    date = hashMapOf(
                                        "expires" to expires,
                                        "issued" to issuied
                                    ),
                                    officer = special,
                                    branch = "${user?.account?.firstOrNull()?.branchId}",
                                    accountOpening = accountOpen!!,
                                    account = "${user?.account?.firstOrNull()?.account}",
                                    mobile = "${user?.mobile}",
                                    agentId = "${user?.account?.firstOrNull()?.agentID}",
                                    model = model,
                                    context = context
                                )!!,
                                state = { screenState = it },
                                onResponse = { response ->
                                    AccountOpeningModuleResponse(
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
                                        onSuccess = { _ ->
                                            val unique = model.preferences.uniqueID.value
                                            val dataHolder = hashMapOf(
                                                "path" to "${model.deviceData?.upload}",
                                                "unique" to unique!!,
                                                "customerId" to "${user?.account?.lastOrNull()?.agentID}",
                                                "mobileNumber" to "${user?.mobile}",
                                                "customerNumber" to "${accountOpen.validation?.mobile}",
                                                "country" to country,
                                                "bankId" to BANK_ID,
                                                "uploadTag" to "photo"
                                            )
                                            val partData = hashMapOf(
                                                "idFront" to partBase64(
                                                    base64Image = convert(idFront.value!!),
                                                    name = "IdFront"
                                                ),
                                                "passport" to partBase64(
                                                    base64Image = convert(passport.value!!),
                                                    name = "photo"
                                                ),
                                                "idBack" to partBase64(
                                                    base64Image = convert(idBack.value!!),
                                                    name = "IdBack"
                                                ),
                                                "signature" to partBase64(
                                                    base64Image = convert(signature.value!!),
                                                    name = "signature"
                                                )
                                            )
                                            action = {
                                                model.upload(
                                                    data = dataHolder,
                                                    part = partData,
                                                    state = { screenState = it },
                                                    onResponse = { response ->
                                                        AccountOpeningModuleResponseNot(
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
                                                                        "message" to message!!
                                                                    )
                                                                )
                                                                screenState = ModuleState.DISPLAY
                                                                who.value = ResponseDialog
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
                    })

                is Response.Success -> SuccessDialog(
                    message = "${s.data["message"]}",
                    action = {
                        who.value = NothingShow
                        data.controller.navigate(Module.Dashboard.route)
                    })
            }
        }
    }


}


enum class DialogWho { A, B }
sealed class DocumentDialog
data object ResponseDialog : DocumentDialog()
data class DateDialog(val who: DialogWho) : DocumentDialog()
data object NothingShow : DocumentDialog()


enum class SayCheese {
    IdBack, Selfie, Signature, IdFront
}


