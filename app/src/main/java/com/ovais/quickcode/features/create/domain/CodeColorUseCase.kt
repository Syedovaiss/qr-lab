package com.ovais.quickcode.features.create.domain

import androidx.compose.ui.graphics.Color
import com.ovais.quickcode.storage.db.ConfigurationDao
import com.ovais.quickcode.utils.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface CodeDefaultColorUseCase : SuspendUseCase<Pair<Color, Color>>

class DefaultCodeDefaultColorUseCase(
    private val configurationDao: ConfigurationDao,
    private val dispatcherIO: CoroutineDispatcher
) : CodeDefaultColorUseCase {

    override suspend fun invoke(): Pair<Color, Color> {
        return withContext(dispatcherIO) {
            val backgroundColor =
                configurationDao.getDefaultBackgroundColor()?.toIntOrNull()?.let {
                    Color(it)
                }
                    ?: Color.White
            val foregroundColor =
                configurationDao.getDefaultForegroundColor()?.toIntOrNull()?.let {
                    Color(it)
                }
                    ?: Color.Black
            Pair(backgroundColor, foregroundColor)
        }
    }
}