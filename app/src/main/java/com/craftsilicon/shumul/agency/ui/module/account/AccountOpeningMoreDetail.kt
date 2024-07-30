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
import com.craftsilicon.shumul.agency.data.bean.MoreDetailsBean
import com.craftsilicon.shumul.agency.data.source.model.LocalViewModelImpl
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
import com.craftsilicon.shumul.agency.ui.util.countryCode
import com.craftsilicon.shumul.agency.ui.util.horizontalModulePadding
import com.craftsilicon.shumul.agency.ui.util.layoutDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOpeningMoreDetail(data: GlobalData) {

    val context = LocalContext.current
    val model: RemoteViewModelImpl = hiltViewModel()
    val local = hiltViewModel<LocalViewModelImpl>()
    val user = model.preferences.userData.collectAsState().value
    val snackState = remember { SnackbarHostState() }
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.DISPLAY)
    }
    val scope = rememberCoroutineScope()

    val accountOpen = model.preferences.accountOpen.collectAsState().value

    val workSectorData = local.sector.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }


    var education by rememberSaveable {
        mutableStateOf("")
    }

    val countries = remember { SnapshotStateList<DropDownResult>() }
    var country by rememberSaveable {
        mutableStateOf("")
    }

    var address by rememberSaveable {
        mutableStateOf("")
    }


    var job by rememberSaveable {
        mutableStateOf("")
    }

    var name by rememberSaveable {
        mutableStateOf("")
    }

    var occupation by rememberSaveable {
        mutableStateOf("")
    }

    var city by rememberSaveable {
        mutableStateOf("")
    }

    var monthly by rememberSaveable {
        mutableStateOf("")
    }

    var deposit by rememberSaveable {
        mutableStateOf("")
    }

    var withdraw by rememberSaveable {
        mutableStateOf("")
    }

    var total by rememberSaveable {
        mutableStateOf("")
    }

    var source by rememberSaveable {
        mutableStateOf("")
    }

    var residence by rememberSaveable {
        mutableStateOf("")
    }


    val workSector = remember { SnapshotStateList<DropDownResult>() }
    var sector by rememberSaveable { mutableStateOf("") }


    var emergency by rememberSaveable {
        mutableStateOf("")
    }


    var action: () -> Unit = {}

    if (workSectorData.isNotEmpty())
        LaunchedEffect(key1 = Unit) {
            workSectorData.forEach {
                workSector.apply {
                    removeIf { p -> p.key == it.id }
                    add(DropDownResult(key = it.id, desc = it.name))
                }
            }

        }

    if (workSectorData.isEmpty())
        LaunchedEffect(key1 = Unit) {
            delay(600)
            action = {
                model.web(
                    path = "${model.deviceData?.agent}",
                    data = workSectorFunc(
                        account = "${user?.account?.lastOrNull()?.account}",
                        mobile = "${user?.mobile}",
                        agentId = "${user?.account?.firstOrNull()?.agentID}",
                        model = model,
                        context = context
                    )!!,
                    state = { screenState = it },
                    onResponse = { response ->
                        AccountOpeningModuleWorkSectorResponse(
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
                            onSuccess = { sectorBeans ->
                                scope.launch {
                                    local.sector.value = sectorBeans
                                    screenState = ModuleState.DISPLAY
                                    delay(200)
                                }
                            }, onToken = action
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
                                            if (education.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_education_level)
                                                )
                                            } else if (sector.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.select_work_sector)
                                                )
                                            } else if (job.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_job_quality_)
                                                )
                                            } else if (name.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_employer_name_)
                                                )
                                            } else if (monthly.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_monthly_income_)
                                                )
                                            } else if (source.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_source_income_)
                                                )
                                            } else if (residence.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_place_residence_)
                                                )
                                            } else if (emergency.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_emergency_person_contact_)
                                                )
                                            } else if (occupation.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_your_occupation_)
                                                )
                                            } else if (city.isBlank()) {
                                                snackState.showSnackbar(
                                                    context.getString(R.string.enter_your_city_)
                                                )
                                            } else {

                                                accountOpen?.more = MoreDetailsBean(
                                                    sector = sector,
                                                    education = education,
                                                    job = job,
                                                    employer = name,
                                                    income = source,
                                                    deposit = deposit,
                                                    withdraw = withdraw,
                                                    residence = residence,
                                                    emergency = emergency,
                                                    monthly = monthly,
                                                    city = city,
                                                    country = country,
                                                    address = address,
                                                    occupation = occupation

                                                )
                                                if (accountOpen != null) {
                                                    model.preferences.accountOpen(accountOpen)
                                                }
                                                delay(200)
                                                data.controller.navigate(
                                                    Module.AccountOpening.Documents.route
                                                )

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
                                            text = stringResource(id = R.string.more_details_),
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
                                value = education,
                                onValueChange = { education = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.education_level_),
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
                                value = job,
                                onValueChange = { job = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.job_title_),
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
                                value = occupation,
                                onValueChange = { occupation = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.occupation_),
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
                                value = name,
                                onValueChange = { name = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.employer_name_),
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
                                    label = stringResource(id = R.string.work_sector_),
                                    data = MutableStateFlow(workSector)
                                ) { result ->
                                    sector = result.key as String
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = monthly,
                                onValueChange = { monthly = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.monthly_income_),
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
                                )
                            )


                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = deposit,
                                onValueChange = { deposit = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.max_deposit_),
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
                                )
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = withdraw,
                                onValueChange = { withdraw = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.max_withdraw_),
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
                                )
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = total,
                                onValueChange = { total = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.total_income_),
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
                                )
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = source,
                                onValueChange = { source = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.source_of_income_),
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
                                value = residence,
                                onValueChange = { residence = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.place_residence),
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
                                    label = stringResource(id = R.string.country_),
                                    data = MutableStateFlow(countries)
                                ) { result ->
                                    country = result.key as String
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            OutlinedTextField(
                                value = city,
                                onValueChange = { city = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.city_),
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
                                value = address,
                                onValueChange = { address = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.address_),
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
                                value = emergency,
                                onValueChange = { emergency = it },
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.emergency_number_),
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
            monthly = it.toElmaDate()
            showDialog = false
        }) {
            showDialog = false
        }
    }

}