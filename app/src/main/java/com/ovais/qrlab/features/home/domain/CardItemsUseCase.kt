package com.ovais.qrlab.features.home.domain

import com.ovais.qrlab.R
import com.ovais.qrlab.core.ui.theme.HomeCardFour
import com.ovais.qrlab.core.ui.theme.HomeCardOne
import com.ovais.qrlab.core.ui.theme.HomeCardThree
import com.ovais.qrlab.core.ui.theme.HomeCardTwo
import com.ovais.qrlab.features.home.data.CardItemType
import com.ovais.qrlab.features.home.data.HomeCardItem
import com.ovais.qrlab.utils.usecase.UseCase

interface CardItemsUseCase : UseCase<List<HomeCardItem>>

class DefaultCardItemsUseCase : CardItemsUseCase {
    override fun invoke(): List<HomeCardItem> {
        return mutableListOf(
            HomeCardItem(
                iconRes = R.drawable.ic_create_qr,
                title = R.string.create_qr,
                type = CardItemType.Create,
                gradientColors = HomeCardOne
            ),
            HomeCardItem(
                iconRes = R.drawable.ic_scan_qr,
                title = R.string.scan_qr,
                type = CardItemType.Scan,
                gradientColors = HomeCardTwo
            ),
            HomeCardItem(
                iconRes = R.drawable.ic_history,
                title = R.string.history,
                type = CardItemType.History,
                gradientColors = HomeCardThree
            ),
            HomeCardItem(
                iconRes = R.drawable.ic_settings,
                title = R.string.settings,
                type = CardItemType.Settings,
                gradientColors = HomeCardFour
            )

        )
    }
}