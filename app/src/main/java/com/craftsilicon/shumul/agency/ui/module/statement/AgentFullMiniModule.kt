package com.craftsilicon.shumul.agency.ui.module.statement

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.craftsilicon.shumul.agency.R
import com.craftsilicon.shumul.agency.data.bean.MiniData
import com.craftsilicon.shumul.agency.ui.navigation.ModuleState
import com.craftsilicon.shumul.agency.ui.util.LoadingModule
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentFullMiniModule(data: MiniData, action: () -> Unit) {
    var screenState: ModuleState by remember {
        mutableStateOf(ModuleState.LOADING)
    }

    LaunchedEffect(key1 = Unit) {
        delay(400)
        screenState = if (data.data.isNullOrEmpty()) ModuleState.ERROR
        else ModuleState.DISPLAY
    }

    Dialog(
        onDismissRequest = { action() },
        properties = DialogProperties(
            decorFitsSystemWindows = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = data.title,
                            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ), navigationIcon = {
                        IconButton(onClick = {
                            action()
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
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {

                when (screenState) {
                    ModuleState.LOADING -> LoadingModule()
                    ModuleState.ERROR -> Image(
                        painter = painterResource(id = R.drawable.paper),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.Center)
                    )

                    ModuleState.DISPLAY,
                    ModuleState.SUCCESS -> LazyColumn {
                        items(data.data!!) { item ->
                            MiniDataItem(data = item)
                        }
                    }
                }

            }

        }
    }
}