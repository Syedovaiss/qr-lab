package com.ovais.quickcode.features.home.presentation

import com.ovais.quickcode.features.home.data.HomeCardItem
import com.ovais.quickcode.features.home.data.UserInfo

data class HomeUiData(
    val userInfo: UserInfo,
    val cardItem: List<HomeCardItem>
)
