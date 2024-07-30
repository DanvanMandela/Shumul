package com.craftsilicon.shumul.agency.ui.module.account

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.PersonalDetail
import com.craftsilicon.shumul.agency.data.bean.identificationList
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.ui.custom.CustomSnackBar
import com.craftsilicon.shumul.agency.ui.custom.DropDownResult
import com.craftsilicon.shumul.agency.ui.custom.EditDropDown
import com.craftsilicon.shumul.agency.ui.module.statement.toElmaDate
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.buttonHeight
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.isValidEmail
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOpeningPersonalDetail(data: GlobalData) {

    val context = LocalContext.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val snackState = remember { SnackbarHostState() }
    val screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }
    val scope = rememberCoroutineScope()

    val accountOpen = model.preferences.accountOpen.collectAsState().value


    var showDialog by remember { mutableStateOf(false) }


    var firstName by rememberSaveable {
        mutableStateOf("")
    }

    var middleName by rememberSaveable {
        mutableStateOf("")
    }

    var thirdName by rememberSaveable {
        mutableStateOf("")
    }
    var lastName by rememberSaveable {
        mutableStateOf("")
    }

    var dob by rememberSaveable {
        mutableStateOf("")
    }

    var idNumber by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }


    val idTypes = remember { SnapshotStateList<DropDownResult>() }
    var idType: HashMap<String, Any?> = remember {
        hashMapOf()
    }

    val genders = remember { SnapshotStateList<DropDownResult>() }
    var gender by rememberSaveable {
        mutableStateOf("")
    }

    val marital = remember { SnapshotStateList<DropDownResult>() }
    var maritalStatus by rememberSaveable {
        mutableStateOf("")
    }

    val nationalities = remember { SnapshotStateList<DropDownResult>() }
    var nationality by rememberSaveable {
        mutableStateOf("")
    }


    LaunchedEffect(key1 = Unit) {
        context.resources.getStringArray(R.array.gender).forEach {
            genders.add(
                DropDownResult(
                    key = it,
                    desc = it
                )
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        identificationList.forEach {
            idTypes.add(
                DropDownResult(
                    key = it.id,
                    desc = it.type
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
                                            if (firstName.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_first_name_)
                                                )
                                            } else if (middleName.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_middle_name_)
                                                )
                                            } else if (gender.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.select_gender_)
                                                )
                                            } else if (maritalStatus.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(
                                                        R.string.select_marital_status_
                                                    )
                                                )
                                            } else if (thirdName.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_third_name_)
                                                )
                                            } else if (dob.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_date_birth_)
                                                )
                                            } else if (idType.isEmpty()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_identification_type)
                                                )
                                            } else if (idNumber.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_id_number_)
                                                )
                                            } else {
                                                if (email.isNotBlank() && !email.isValidEmail())
                                                    snackState.showSnackbar(
                                                        context.getString(R.string.enter_valid_email_address_)
                                                    )
                                                else {
                                                    accountOpen?.personal = PersonalDetail(
                                                        firstName = firstName,
                                                        secondName = middleName,
                                                        thirdName = thirdName,
                                                        lastName = lastName,
                                                        dob = dob,
                                                        id = idNumber,
                                                        idType = idType,
                                                        email = email,
                                                        gender = gender
                                                    )
                                                    if (accountOpen != null) {
                                                        model.preferences.accountOpen(accountOpen)
                                                    }
                                                    delay(200)
                                                    data.controller.navigate(
                                                        Module.AccountOpening.MoreDetail.route
                                                    )
                                                }

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
                                            text = stringResource(id = R.string.personal_details_),
                                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
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
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.first_name_),
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
                                value = middleName,
                                onValueChange = { middleName = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.middle_name_),
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
                                value = thirdName,
                                onValueChange = { thirdName = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.third_name_),
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
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.last_name_),
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
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                EditDropDown(
                                    label = stringResource(id = R.string.gender_),
                                    data = MutableStateFlow(genders)
                                ) { result ->
                                    gender = result.key as String
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                EditDropDown(
                                    label = stringResource(id = R.string.marital_status_),
                                    data = MutableStateFlow(marital)
                                ) { result ->
                                    maritalStatus = result.key as String
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = dob,
                                onValueChange = { dob = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.date_of_birth_),
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
                                    IconButton(onClick = { showDialog = true }) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                EditDropDown(
                                    label = stringResource(id = R.string.identification_document_),
                                    data = MutableStateFlow(idTypes)
                                ) { result ->
                                    idType = hashMapOf(
                                        "id" to result.key,
                                        "type" to result.desc
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = idNumber,
                                onValueChange = { idNumber = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.id_number_),
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
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = horizontalModulePadding)
                            ) {
                                EditDropDown(
                                    label = stringResource(id = R.string.nationality_),
                                    data = MutableStateFlow(nationalities)
                                ) { result ->
                                    nationality = result.key as String
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.email_address_),
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
                                    keyboardType = KeyboardType.Email
                                )
                            )
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

        if (showDialog) DateOfBirthDialog(date = {
            dob = it.toElmaDate()
            showDialog = false
        }) {
            showDialog = false
        }
    }

}