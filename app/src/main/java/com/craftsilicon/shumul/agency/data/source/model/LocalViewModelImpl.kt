package com.craftsilicon.shumul.agency.data.source.model

import androidx.lifecycle.ViewModel
import com.craftsilicon.shumul.agency.data.bean.AppUserStateTypeConverter
import com.craftsilicon.shumul.agency.data.bean.CurrencyBean
import com.craftsilicon.shumul.agency.data.bean.ImageHolder
import com.craftsilicon.shumul.agency.data.bean.ProductBean
import com.craftsilicon.shumul.agency.data.bean.StaticData
import com.craftsilicon.shumul.agency.data.bean.WorkSectorBean
import com.craftsilicon.shumul.agency.data.security.interaction.InteractionDataSource
import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LocalViewModelImpl @Inject constructor(
    val preferences: StorageDataSource,
    val interaction: InteractionDataSource,
    val user: AppUserStateTypeConverter
) : ViewModel() {

    val products: MutableStateFlow<MutableList<ProductBean>> = MutableStateFlow(mutableListOf())
    val currency: MutableStateFlow<MutableList<CurrencyBean>> = MutableStateFlow(mutableListOf())

    val sector: MutableStateFlow<MutableList<WorkSectorBean>> = MutableStateFlow(mutableListOf())
    val images: MutableStateFlow<ImageHolder?> = MutableStateFlow(ImageHolder())

    val staticData: MutableStateFlow<MutableList<StaticData>> = MutableStateFlow(mutableListOf())


}