package com.craftsilicon.shumul.agency.ui.module

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModel
import com.craftsilicon.shumul.agency.data.source.model.RemoteViewModelImpl
import com.craftsilicon.shumul.agency.data.source.model.WorkViewModel
import com.craftsilicon.shumul.agency.data.source.work.WorkStatus
import com.craftsilicon.shumul.agency.ui.navigation.GlobalData
import com.craftsilicon.shumul.agency.ui.navigation.Module
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.theme.GreenAp
import com.craftsilicon.shumul.agency.ui.theme.GreenLightApp
import com.craftsilicon.shumul.agency.ui.util.AppLogger
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import com.craftsilicon.shumul.agency.ui.util.LoadingModuleMain
import com.craftsilicon.shumul.agency.ui.util.horizontalPadding

@Composable
fun SplashModule(data: GlobalData) {
    val model = hiltViewModel<WorkViewModel>()
    val remote = hiltViewModel<RemoteViewModelImpl>()
    val login = remote.preferences.login.collectAsState().value
    val owner = LocalLifecycleOwner.current
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.LOADING)
    }
    LaunchedEffect(key1 = Unit) {
        model.routeData(owner, object : WorkStatus {
            override fun workDone(b: Boolean) {
                if (b) screenState = ModuleState.DISPLAY
            }

            override fun progress(p: Int) {
                AppLogger.instance.appLog("DATA:Progress", "$p")
            }
        })
    }


    when (screenState) {
        ModuleState.LOADING -> LoadingModuleMain()
        ModuleState.ERROR -> {}
        ModuleState.DISPLAY,
        ModuleState.SUCCESS -> Column(
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1.2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(56.dp))
                Image(
                    painter = painterResource(id = R.drawable.shumul_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.width(40.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = stringResource(id = R.string.welcome_to_shumul_),
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(id = R.string.to_login_slogan_),
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)

                )
                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = {
                        data.controller.navigate(
                            if (login) Module.Login.route
                            else Module.Activation.route
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                ) {
                    Text(
                        text = stringResource(id = R.string.proceed_to_login),
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }

        }
    }

}