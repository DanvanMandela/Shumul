package com.craftsilicon.shumul.agency.ui.module.dashboard.menu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.craftsilicon.shumul.agency.ui.navigation.NavigateTo

data class MenuItem(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val navigate: NavigateTo
)


